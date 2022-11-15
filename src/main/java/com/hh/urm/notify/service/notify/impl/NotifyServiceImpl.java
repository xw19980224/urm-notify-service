package com.hh.urm.notify.service.notify.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enmus.NotifyServiceEnums;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.dto.NotifyDataDTO;
import com.hh.urm.notify.model.dto.message.SmsMessageDTO;
import com.hh.urm.notify.model.entity.SmsMetadata;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.utils.TimeUtil;
import com.hh.urm.notify.utils.base.ServiceResponse;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.Pair;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hh.opengateway.constant.Constant.SIGN;
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

    @Override
    public ServiceResponse<Object> sendMessage(NotifyBo notifyBo) {
        JSONObject result = new JSONObject();

        String traceId = notifyBo.getTraceId();

        List<NotifyDataDTO> data = notifyBo.getData();

        List<String> notifyType = Arrays.stream(notifyBo.getNotifyType()).collect(Collectors.toList());

        List<String> topicList = NotifyServiceEnums.getTopicList(notifyType);

        // 2、更具不同topic发送消息
        for (String topic : topicList) {
            // 获取通知类型
            String code = NotifyServiceEnums.getCodeByTopicName(topic);
            // 获取模板名称，模板code
            Pair<String, String> template = getTemplateCodeAndName(code, notifyBo);
            // 构建消息通知参数
            JSONObject jsonObject = buildKafkaParamsByTopic(code, notifyBo, data);
            // 发送MQ
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, jsonObject.toJSONString());
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.info(topic + " - 生产者 发送消息失败：" + throwable.getMessage());
                    recordHistory(
                            traceId,
                            code,
                            NotifyConst.KafkaStateEnums.EXCEPTION.getCode(),
                            template.getFirst(), template.getSecond(),
                            notifyBo, throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                    // 记录成功历史
                    recordHistory(
                            traceId,
                            code,
                            NotifyConst.KafkaStateEnums.READY.getCode(),
                            template.getFirst(), template.getSecond(),
                            notifyBo, null);
                    log.info(topic + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                }
            });
//            data.forEach(item -> {
//                        String superId = item.getSuperId();
//
//                        // 2.1将NotifyDataDTO对象转换为JsonObject，并封装相关通知参数
//                        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(item));
//                        jsonObject.put(TRACE_ID, traceId);
//                        jsonObject.putAll(getNotifyParams(topic, notifyBo, item));
//                        String currJsonStr = jsonObject.toJSONString();
//
//                        // 2.2发送kafka
//                        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, superId, currJsonStr);
//                        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//                            @Override
//                            public void onFailure(Throwable throwable) {
//                                log.info(topic + " - 生产者 发送消息失败：" + throwable.getMessage());
//                                recordHistory(
//                                        traceId,
//                                        String.valueOf(configTopicsList.indexOf(topic)),
//                                        NotifyConst.KafkaStateEnums.EXCEPTION.getCode(),
//                                        template.getFirst(), template.getSecond(),
//                                        item, throwable.getMessage());
//                            }
//
//                            @Override
//                            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
//                                // 记录成功历史
//                                log.info(topic + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
//                                recordHistory(
//                                        traceId,
//                                        String.valueOf(configTopicsList.indexOf(topic)),
//                                        NotifyConst.KafkaStateEnums.READY.getCode(),
//                                        template.getFirst(), template.getSecond(),
//                                        item, null);
//                            }
//                        });
//                    }
//            );
        }

        return ServiceResponse.createSuccessResponse(traceId, result);
    }

    /**
     * 活动通知类型参数
     *
     * @param code     通知类型
     * @param notifyBo
     * @param data     消息内容
     * @return
     */
    private JSONObject buildKafkaParamsByTopic(String code, NotifyBo notifyBo, List<NotifyDataDTO> data) {
        JSONObject jsonObject = new JSONObject();

        String dataStr = "";
        JSONObject config = new JSONObject();

        // 通知类型为sms为短信时，封装短信参数
        if (NotifyServiceEnums.SMS.getCode().equals(code)) {
            List<JSONObject> list = data.stream().map(item -> {
                SmsMessageDTO smsMessage = item.getSmsMessage();
                JSONObject tempJsonObject = JSONObject.parseObject(JSONObject.toJSONString(smsMessage));
                tempJsonObject.put(SUPER_ID, item.getSuperId());
                return tempJsonObject;
            }).collect(Collectors.toList());

            dataStr = JSONObject.toJSONString(list);
            SmsMetadata smsMetadata = notifyBo.getSmsMetadata();
            config = JSONObject.parseObject(JSONObject.toJSONString(smsMetadata));
        }
        jsonObject.put(TRACE_ID, notifyBo.getTraceId());
        jsonObject.put(TYPE, code);
        jsonObject.put(DATA, dataStr);
        jsonObject.put(CONFIG, config);
        return jsonObject;
    }

    /**
     * 通过topic，获取模板Code和Name
     *
     * @param code
     * @param notifyBo
     * @return
     */
    private Pair<String, String> getTemplateCodeAndName(String code, NotifyBo notifyBo) {
        if (NotifyServiceEnums.SMS.getCode().equals(code)) {
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
     * @param notifyBo     通知内容
     * @param msg          描述
     */
    public void recordHistory(String traceId, String type, int status, String templateCode, String templateName, NotifyBo notifyBo, String msg) {
        String currDate = TimeUtil.formatYYYYMMDDHHMMSS(new Date());
        JSONObject dataInfo = new JSONObject();

        dataInfo.put(TRACE_ID, traceId);
        dataInfo.put(TYPE, type);
        dataInfo.put(STATUS, status);
        dataInfo.put(CREATE_TIME, currDate);
        dataInfo.put(UPDATE_TIME, currDate);
        dataInfo.put(SIGN, notifyBo.getSign());
        if (!Strings.isNullOrEmpty(msg)) {
            dataInfo.put(EXCEPTION, msg);
        }
        dataInfo.put(NotifyConst.Sms.TEMPLATE_CODE, templateCode);
        dataInfo.put(NotifyConst.Sms.TEMPLATE_NAME, templateName);
        List<BasicDBObject> data = objectToJsonObject(type, dataInfo, notifyBo);

        mongoTemplate.insert(data, T_USER_SEND_INFO_HISTORY);
    }

    /**
     * notifyData转JSONObject
     *
     * @param type     通知类型
     * @param dataInfo 通用参数
     * @param notifyBo 通知数据
     * @return JSONObject
     */
    private List<BasicDBObject> objectToJsonObject(String type, JSONObject dataInfo, NotifyBo notifyBo) {
        return notifyBo.getData().stream().map(notifyData -> {
            BasicDBObject jsonObject = new BasicDBObject();
            jsonObject.putAll(dataInfo);
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
        }).collect(Collectors.toList());

    }
}
