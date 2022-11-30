package com.hh.urm.notify.service.notify.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.entity.UserSendHistory;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.repository.UserSendHistoryRepository;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.utils.TimeUtil;
import com.hh.urm.notify.utils.base.ServiceResponse;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.*;
import static com.hh.urm.notify.consts.DbTableConst.UserSendHistory.NAME;
import static com.hh.urm.notify.consts.NotifyConst.SYSTEM;


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

    @Resource
    UserSendHistoryRepository userSendHistoryRepository;

    @Override
    public ServiceResponse<Object> notify(NotifyBo notifyBo) {
        JSONObject result = new JSONObject();

        String traceId = notifyBo.getTraceId();

        List<NotifyDataReq> data = notifyBo.getData();

        String notifyType = notifyBo.getNotifyType();

        String topic = NotifyServiceEnums.getTopicNameByCode(notifyType);

        // 构建消息通知参数
        JSONObject jsonObject = buildKafkaParamsByTopic(notifyType, notifyBo, data);
        // 发送MQ
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, jsonObject.toJSONString());
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info(topic + " - 生产者 发送消息失败：" + throwable.getMessage());
                recordHistory(
                        traceId,
                        notifyType,
                        NotifyConst.KafkaStateEnums.EXCEPTION.getCode(),
                        notifyBo, throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                // 记录成功历史
                recordHistory(
                        traceId,
                        notifyType,
                        NotifyConst.KafkaStateEnums.READY.getCode(),
                        notifyBo, null);
                log.info(topic + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });

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

        jsonObject.put(TRACE_ID, notifyBo.getTraceId());
        jsonObject.put(TYPE, code);
        jsonObject.put(CONFIG, notifyBo.getTemplate().toJSONString());
        jsonObject.put(DATA, data);
        return jsonObject;
    }

    /**
     * 通过topic，获取模板Code和Name
     *
     * @param notifyBo 通知业务数据
     * @return Pair 1、模板code 2、模板名称
     */
    private Pair<String, String> getTemplateCodeAndName(NotifyBo notifyBo) {
        JSONObject template = notifyBo.getTemplate();
        String code = (String) template.getOrDefault(CODE, "");
        String name = (String) template.getOrDefault(NAME, "");
        return Pair.of(code, name);
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
        Timestamp timestamp = TimeUtil.nowTimestamp();
        JSONObject dataInfo = new JSONObject();
        String sign = notifyBo.getSign();

        List<UserSendHistory> list = notifyBo.getData().stream().map(item -> {
            UserSendHistory userSendHistory = new UserSendHistory();
            userSendHistory.setTraceId(traceId);
            userSendHistory.setType(type);
            userSendHistory.setStatus(status);
            userSendHistory.setCreateBy(SYSTEM);
            userSendHistory.setCreateTime(timestamp);
            userSendHistory.setUpdateTime(timestamp);
            userSendHistory.setUpdateBy(SYSTEM);
            userSendHistory.setSign(sign);
            if (!Strings.isNullOrEmpty(msg)) {
                userSendHistory.setTDesc(msg);
            }
            // 获取模板名称，模板code
            Pair<String, String> template = getTemplateCodeAndName(notifyBo);
            userSendHistory.setTemplateCode(template.getFirst());
            userSendHistory.setTemplateName(template.getSecond());

            String params = item.getParams();
            String receiver = item.getReceiver();
            String superId = item.getSuperId();
            String requestId = item.getRequestId();
            JSONObject extend = item.getExtend();
            userSendHistory.setParams(params);
            userSendHistory.setReceiver(receiver);
            userSendHistory.setSuperId(superId);
            userSendHistory.setRequestId(requestId);
            userSendHistory.setExtend(extend.toJSONString());

            return userSendHistory;
        }).collect(Collectors.toList());

        userSendHistoryRepository.saveAll(list);
    }
}
