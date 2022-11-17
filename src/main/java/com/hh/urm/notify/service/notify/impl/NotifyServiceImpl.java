package com.hh.urm.notify.service.notify.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.service.notify.NotifyServiceSupport;
import com.hh.urm.notify.service.notify.convert.IConvertHandler;
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
public class NotifyServiceImpl extends NotifyServiceSupport implements INotifyService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public ServiceResponse<Object> sendMessage(NotifyBo notifyBo) {
        JSONObject result = new JSONObject();

        String traceId = notifyBo.getTraceId();

        List<NotifyDataReq> data = notifyBo.getData();

        List<String> notifyType = notifyBo.getNotifyType();

        List<String> topicList = NotifyServiceEnums.getTopicList(notifyType);

        // 2、更具不同topic发送消息
        for (String topic : topicList) {
            // 获取通知类型
            String code = NotifyServiceEnums.getCodeByTopicName(topic);
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
                            notifyBo, throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                    // 记录成功历史
                    recordHistory(
                            traceId,
                            code,
                            NotifyConst.KafkaStateEnums.READY.getCode(),
                            notifyBo, null);
                    log.info(topic + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                }
            });
        }

        return ServiceResponse.createSuccessResponse(traceId, result);
    }

    /**
     * 活动通知类型参数
     *
     * @param code     通知类型
     * @param notifyBo 通知业务数据
     * @param data     消息内容
     * @return JSONObject对象
     */
    private JSONObject buildKafkaParamsByTopic(String code, NotifyBo notifyBo, List<NotifyDataReq> data) {
        JSONObject jsonObject = new JSONObject();

        // 获取指定类型的配置和数据
        IConvertHandler iConvertHandler = convertHandlerMaps.get(code);
        Pair<JSONObject, String> convert = iConvertHandler.convert(notifyBo, data);

        jsonObject.put(TRACE_ID, notifyBo.getTraceId());
        jsonObject.put(TYPE, code);
        jsonObject.put(CONFIG, convert.getFirst());
        jsonObject.put(DATA, convert.getSecond());
        return jsonObject;
    }

    /**
     * 通过topic，获取模板Code和Name
     *
     * @param code     通知类型
     * @param notifyBo 通知业务数据
     * @return Pair 1、模板code 2、模板名称
     */
    private Pair<String, String> getTemplateCodeAndName(String code, NotifyBo notifyBo) {
        if (NotifyServiceEnums.SMS.getCode().equals(code)) {
            return Pair.of(notifyBo.getSmsTemplate().getCode(), notifyBo.getSmsTemplate().getName());
        }
        return Pair.of("", "");
    }

    /**
     * 记录发送历史
     *
     * @param traceId  链路Id
     * @param type     消息类型 {@link NotifyServiceEnums}
     * @param status   消息状态 {@link NotifyConst.KafkaStateEnums}
     * @param notifyBo 通知内容
     * @param msg      描述
     */
    public void recordHistory(String traceId, String type, int status, NotifyBo notifyBo, String msg) {
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
        // 获取模板名称，模板code
        Pair<String, String> template = getTemplateCodeAndName(type, notifyBo);
        dataInfo.put(NotifyConst.Sms.TEMPLATE_CODE, template.getFirst());
        dataInfo.put(NotifyConst.Sms.TEMPLATE_NAME, template.getSecond());
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
