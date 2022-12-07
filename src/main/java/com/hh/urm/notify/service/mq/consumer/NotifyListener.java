package com.hh.urm.notify.service.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.notify.req.NotifyDataReq;
import com.hh.urm.notify.service.notify.handler.INotifyHandler;
import com.hh.urm.notify.service.notify.handler.MessageHandOutFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.hh.urm.notify.consts.CommonConst.*;

/**
 * @ClassName: NotifyListener
 * @Author: MaxWell
 * @Description: 消息监听消息
 * @Date: 2022/11/1 17:03
 * @Version: 1.0
 */
@Slf4j
@Component
public class NotifyListener {

    @Resource
    private MessageHandOutFactory messageHandOutFactory;

    @KafkaListener(id = "notify_topic", topics = "#{'${spring.kafka.urm_topics.notify_topic}'.split(',')}")
    public void consumer(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        Optional<?> message = Optional.ofNullable(record.value());

        // 1、判断消息是否存在
        if (!message.isPresent()) {
            return;
        }

        // 2、处理消息
        try {
            String jsonStr = (String) message.get();

            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            String traceId = ((String) jsonObject.getOrDefault(TRACE_ID, ""));
            String type = ((String) jsonObject.getOrDefault(TYPE, ""));
            String config = (String) jsonObject.getOrDefault(CONFIG, "");

            List<NotifyDataReq> data = jsonObject.getJSONArray(DATA).toJavaList(NotifyDataReq.class);

            // 3、获取处理器并执行
            INotifyHandler notifyHandler = messageHandOutFactory.getNotifyHandler(type);
            if (Objects.isNull(notifyHandler)) {
                log.error("traceId:{},topic:{},kafka consumer breakOff, result:{}", traceId, topic, "暂无消息处理器");
                return;
            }
            notifyHandler.handler(traceId, data, config);
        } catch (Exception e) {
            log.error("消费MQ消息，失败 topic：{} message：{}", topic, message.get());
            throw e;
        } finally {
            // 4. 消息消费完成
            ack.acknowledge();
        }
    }

}
