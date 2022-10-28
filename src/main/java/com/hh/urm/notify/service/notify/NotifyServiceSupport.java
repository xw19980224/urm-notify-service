package com.hh.urm.notify.service.notify;

import com.hh.urm.notify.annotation.NotifyService;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: NotifyServiceConfig
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 10:23
 * @Version: 1.0
 */
public class NotifyServiceSupport {

    @Resource
    private List<IMessage> messageList;

    /**
     * 策略组
     */
    protected static Map<String, IMessage> notifyServiceConfig = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        messageList.forEach(item -> {
            NotifyService notifyService = AnnotationUtils.findAnnotation(item.getClass(), NotifyService.class);
            if (null != notifyService) {
                notifyServiceConfig.put(notifyService.notifyService().getCode(), item);
            }
        });
    }
}
