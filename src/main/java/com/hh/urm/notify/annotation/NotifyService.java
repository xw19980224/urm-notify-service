package com.hh.urm.notify.annotation;

import com.hh.urm.notify.enums.NotifyServiceEnums;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @ClassName: NotifyService
 * @Author: MaxWell
 * @Description: 通知注解
 * @Date: 2022/10/26 10:30
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface NotifyService {
    NotifyServiceEnums notifyService();
}
