package com.hh.urm.notify.service.notify;

import com.hh.urm.notify.enmus.NotifyServiceEnums;
import com.hh.urm.notify.service.notify.handler.IConvertHandler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    private IConvertHandler smsConvert;

    /**
     * 策略组
     */
    protected static Map<String, IConvertHandler> convertHandlerMaps = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        convertHandlerMaps.put(NotifyServiceEnums.SMS.getCode(), smsConvert);
    }
}
