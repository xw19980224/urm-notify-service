package com.hh.urm.notify.service.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: NotifyProducer
 * @Author: MaxWell
 * @Description: 通知消息生成者
 * @Date: 2022/11/11 10:34
 * @Version: 1.0
 */
@Slf4j
@Component
public class NotifyProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;



}
