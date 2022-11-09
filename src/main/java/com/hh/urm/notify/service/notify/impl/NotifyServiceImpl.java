package com.hh.urm.notify.service.notify.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enmus.NotifyServiceEnums;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.dto.NotifyDataDTO;
import com.hh.urm.notify.model.dto.message.SmsMessageDTO;
import com.hh.urm.notify.repository.SmsMetadataRepository;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.utils.TimeUtil;
import com.hh.urm.notify.utils.base.ServiceResponse;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.Pair;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.*;
import static com.hh.urm.notify.consts.DbTableConst.T_USER_SEND_INFO_HISTORY;

/**
 * @ClassName: NotifyServiceImpl
 * @Author: MaxWell
 * @Description: 通知业务层
 * @Date: 2022/10/25 15:36
 * @Version: 1.0
 */
@Slf4j
@Service
public class NotifyServiceImpl implements INotifyService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Resource
    private MongoTemplate mongoTemplate;

    @Value("${spring.kafka.urm_topics.notify_topic}")
    private String[] topics;

    @Override
    public ServiceResponse<Object> sendMessage(NotifyBo notifyBo) {
        JSONObject result = new JSONObject();

        String traceId = notifyBo.getTraceId();

        List<NotifyDataDTO> data = notifyBo.getData();

        List<String> notifyType = Arrays.stream(notifyBo.getNotifyType()).collect(Collectors.toList());

        ArrayList<String> configTopicsList = Lists.newArrayList(topics);

        // 1、获取通知topic
        List<String> topicList;
        if ((notifyType).contains(NotifyServiceEnums.ALL.getCode())) {
            topicList = Arrays.stream(topics).filter(item -> !item.equals(NotifyServiceEnums.ALL.getServiceName())).collect(Collectors.toList());
        } else {
            topicList = notifyType.stream().map(item -> topics[Integer.parseInt(item)]).collect(Collectors.toList());
        }

        // 2、更具不同topic发送消息
        for (String topic : topicList) {
            // 获取对应通知方式模板Id
            Pair<String, String> template = getTemplateCodeAndName(topic, notifyBo);
            data.forEach(item -> {
                        String superId = item.getSuperId();

                        // 2.1将NotifyDataDTO对象转换为JsonObject，并封装相关通知参数
                        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(item));
                        jsonObject.put(TRACE_ID, traceId);
                        jsonObject.putAll(getNotifyParams(topic, notifyBo,item));
                        String currJsonStr = jsonObject.toJSONString();

                        // 2.2发送kafka
                        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, superId, currJsonStr);
                        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                log.info(topic + " - 生产者 发送消息失败：" + throwable.getMessage());
                                recordHistory(
                                        traceId,
                                        String.valueOf(configTopicsList.indexOf(topic)),
                                        NotifyConst.KafkaStateEnums.EXCEPTION.getCode(),
                                        template.getFirst(), template.getSecond(),
                                        item, throwable.getMessage());
                            }

                            @Override
                            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                                // 记录成功历史
                                log.info(topic + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                                recordHistory(
                                        traceId,
                                        String.valueOf(configTopicsList.indexOf(topic)),
                                        NotifyConst.KafkaStateEnums.READY.getCode(),
                                        template.getFirst(), template.getSecond(),
                                        item, null);
                            }
                        });
                    }
            );
        }


        return ServiceResponse.createSuccessResponse(traceId, result);
    }

    /**
     * 活动通知类型参数
     *
     * @param topic
     * @param notifyBo
     * @param item
     * @return
     */
    private JSONObject getNotifyParams(String topic, NotifyBo notifyBo, NotifyDataDTO item) {
        JSONObject jsonObject = new JSONObject();
        String type = String.valueOf(Lists.newArrayList(topics).indexOf(topic));
        // 通知类型为sms为短信时，封装短信参数
        if (NotifyServiceEnums.SMS.getCode().equals(type)) {
            JSONObject smsMetadata = JSONObject.parseObject(JSONObject.toJSONString(notifyBo.getSmsMetadata()));
            JSONObject smsContent = JSONObject.parseObject(JSONObject.toJSONString(item.getSmsMessage()));
            jsonObject.putAll(smsMetadata);
            jsonObject.putAll(smsContent);
        }
        return jsonObject;
    }

    /**
     * 通过topic，获取模板Code和Name
     *
     * @param topic
     * @param notifyBo
     * @return
     */
    private Pair<String, String> getTemplateCodeAndName(String topic, NotifyBo notifyBo) {
        String type = String.valueOf(Lists.newArrayList(topics).indexOf(topic));
        if (NotifyServiceEnums.SMS.getCode().equals(type)) {
            return Pair.of(notifyBo.getSmsMetadata().getCode(), notifyBo.getSmsMetadata().getName());
        }
        return Pair.of("", "");
    }

    /**
     * 记录发送历史
     *
     * @param traceId      链路Id
     * @param type         消息类型 {@link NotifyServiceEnums}
     * @param status       消息状态 {@link NotifyConst.KafkaStateEnums}
     * @param templateCode
     * @param templateName
     * @param notifyData   通知内容
     * @param msg          描述
     */
    public void recordHistory(String traceId, String type, int status, String templateCode, String templateName, NotifyDataDTO notifyData, String msg) {
        String currDate = TimeUtil.formatYYYYMMDDHHMMSS(new Date());
        BasicDBObject dataInfo = new BasicDBObject();

        dataInfo.put(TRACE_ID, traceId);
        dataInfo.put(TYPE, type);
        dataInfo.put(STATUS, status);
        dataInfo.put(CREATE_TIME, currDate);
        dataInfo.put(UPDATE_TIME, currDate);
        if (!Strings.isNullOrEmpty(msg)) {
            dataInfo.put(EXCEPTION, msg);
        }
        dataInfo.put(NotifyConst.Sms.TEMPLATE_CODE, templateCode);
        dataInfo.put(NotifyConst.Sms.TEMPLATE_NAME, templateCode);
        JSONObject data = objectToJsonObject(type, notifyData);
        dataInfo.putAll(data);

        mongoTemplate.save(dataInfo, T_USER_SEND_INFO_HISTORY);
    }

    /**
     * notifyData转JSONObject
     *
     * @param notifyData 通知数据对象
     * @return JSONObject
     */
    private JSONObject objectToJsonObject(String type, NotifyDataDTO notifyData) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SUPER_ID, notifyData.getSuperId());

        if (!Objects.isNull(notifyData.getSmsMessage()) && Objects.equals(type, NotifyServiceEnums.SMS.getCode())) {
            jsonObject.putAll(JSONObject.parseObject(JSONObject.toJSONString(notifyData.getSmsMessage())));
        }

        if (!Objects.isNull(notifyData.getMailMessage()) && Objects.equals(type, NotifyServiceEnums.MAIL.getCode())) {
            jsonObject.putAll(JSONObject.parseObject(JSONObject.toJSONString(notifyData.getMailMessage())));
        }
        if (!Objects.isNull(notifyData.getAppMessage()) && Objects.equals(type, NotifyServiceEnums.ONE_APP.getCode())) {
            jsonObject.putAll(JSONObject.parseObject(JSONObject.toJSONString(notifyData.getAppMessage())));
        }
        return jsonObject;
    }
}
