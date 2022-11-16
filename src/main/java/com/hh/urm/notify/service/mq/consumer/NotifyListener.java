package com.hh.urm.notify.service.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.service.notify.adapter.IMessage;
import com.hh.urm.notify.service.notify.NotifyServiceSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

import static com.hh.urm.notify.consts.CommonConst.CONFIG;

/**
 * @ClassName: NotifyListener
 * @Author: MaxWell
 * @Description: 消息监听消息
 * @Date: 2022/11/1 17:03
 * @Version: 1.0
 */
@Slf4j
@Component
public class NotifyListener extends NotifyServiceSupport {

    @Resource
    private IMessage smsService;

    @KafkaListener(id = "sms_topic", topics = "#{'${spring.kafka.urm_topics.sms_topic}'}")
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        Optional<?> message = Optional.ofNullable(record.value());

        // 1、判断消息是否存在
        if (!message.isPresent()) {
            return;
        }

        // 2、处理消息
        String jsonStr = (String) message.get();

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONObject config = jsonObject.getJSONObject(CONFIG);

        JSONObject result = smsService.sendMessage(jsonObject, config);

        // 4. 消息消费完成
        ack.acknowledge();
    }
}
