package com.hh.urm.notify.utils;

/**
 * @ClassName: NotifyException
 * @Author: MaxWell
 * @Description: 通知异常
 * @Date: 2022/12/1 15:39
 * @Version: 1.0
 */
public class NotifyException extends RuntimeException {

    public NotifyException(String message) {
        super(message);
    }

    public NotifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
