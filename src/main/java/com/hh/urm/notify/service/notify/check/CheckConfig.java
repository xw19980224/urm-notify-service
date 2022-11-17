package com.hh.urm.notify.service.notify.check;

import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.service.notify.check.factory.ICheck;
import com.hh.urm.notify.service.notify.check.factory.impl.AppCheck;
import com.hh.urm.notify.service.notify.check.factory.impl.SmsCheck;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: CheckConfig
 * @Author: MaxWell
 * @Description: 校验配置类
 * @Date: 2022/11/17 13:59
 * @Version: 1.0
 */
public class CheckConfig {

    /**
     * 校验策略组
     */
    protected static Map<String, ICheck> checkMap = new ConcurrentHashMap<>();

    /**
     * 短信参数校验服务
     */
    @Resource
    private SmsCheck smsCheck;

    /**
     * OneApp参数校验服务
     */
    @Resource
    private AppCheck appCheck;

    @PostConstruct
    public void init() {
        checkMap.put(NotifyServiceEnums.SMS.getCode(), smsCheck);
        checkMap.put(NotifyServiceEnums.ONE_APP.getCode(), appCheck);
    }
}
