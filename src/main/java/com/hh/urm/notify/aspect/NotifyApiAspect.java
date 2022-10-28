package com.hh.urm.notify.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hh.urm.notify.utils.IPUtil;
import com.hh.urm.notify.utils.base.ServiceResponse;
import com.hh.urm.notify.utils.log.AliLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.hh.urm.notify.consts.CommonConst.BODY;
import static com.hh.urm.notify.consts.CommonConst.TRACE_ID;

/**
 * @ClassName: NotifyApiAspect
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/25 14:08
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class NotifyApiAspect {

    public static final ThreadLocal<AliLog> PAIR_THREAD_LOCAL = new ThreadLocal<>();

    @Pointcut("execution(public * com.hh.urm.notify.controller..*.*(..))")
    public void invokeAspect() {
        //do something
    }

    /**
     * @param joinPoint
     * @throws Throwable
     */
    @Before("invokeAspect()")
    public void doBefore(JoinPoint joinPoint) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String traceId = ServletRequestUtils.getStringParameter(request, TRACE_ID, String.valueOf(System.currentTimeMillis()));
        AliLog aliLog = new AliLog();
        aliLog.setTraceId(traceId);
        aliLog.setBasePath(request.getRequestURL().toString());
        aliLog.setUri(request.getPathInfo());
        aliLog.setIp(IPUtil.getIP(request));
        aliLog.setMethod(request.getMethod());
        aliLog.setUrl(request.getQueryString());
        Map<String, Object[]> requestMap = Maps.newHashMap();
        requestMap.putAll(request.getParameterMap());
        requestMap.put(BODY, joinPoint.getArgs());
        aliLog.setRequest(JSON.toJSONString(requestMap));
        log.info("NotifyApiAspect doBefore：{}", JSON.toJSONString(aliLog));
        PAIR_THREAD_LOCAL.set(aliLog);
    }

    /**
     * @param serviceResponse
     * @throws Throwable
     */
    @AfterReturning(value = "invokeAspect()", returning = "serviceResponse")
    public void doAfter(ServiceResponse<Object> serviceResponse) {
        AliLog neeLog = PAIR_THREAD_LOCAL.get();
        neeLog.setResponse(JSON.toJSONString(serviceResponse));
        log.info("NotifyApiAspect doAfter：{}", JSON.toJSONString(neeLog));
        PAIR_THREAD_LOCAL.remove();
    }
}
