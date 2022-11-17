package com.hh.urm.notify.service.notify.handler;

import com.hh.urm.notify.annotation.NotifyService;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: HandlerConfig
 * @Author: MaxWell
 * @Description: 消息处理器配置类
 * @Date: 2022/11/17 17:03
 * @Version: 1.0
 */
public class HandlerConfig {

    @Resource
    private List<INotifyHandler> notifyHandlers;

    /**
     * 策略组
     */
    protected static Map<String, INotifyHandler> notifyHandlerMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        notifyHandlers.forEach(item -> {
            NotifyService notifyService = AnnotationUtils.findAnnotation(item.getClass(), NotifyService.class);
            if (null != notifyService) {
                notifyHandlerMap.put(notifyService.notifyService().getCode(), item);
            }
        });
    }
}
