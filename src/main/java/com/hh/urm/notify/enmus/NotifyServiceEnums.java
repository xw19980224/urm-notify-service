package com.hh.urm.notify.enmus;

/**
 * @ClassName: NotifyServiceEnums
 * @Author: MaxWell
 * @Description: 通知服务枚举类
 * @Date: 2022/10/26 10:32
 * @Version: 1.0
 */
public enum NotifyServiceEnums {
    SMS("1", "smsService");

    /**
     * 业务编码
     */
    private final String code;
    /**
     * 业务标识
     */
    private final String serviceName;

    NotifyServiceEnums(String code, String serviceName) {
        this.code = code;
        this.serviceName = serviceName;
    }

    public String getCode() {
        return code;
    }

    public String getServiceName() {
        return serviceName;
    }

    /**
     * 通过code获取serviceName
     *
     * @param code 业务编码
     * @return
     */
    public static String getServiceNameByCode(String code) {
        NotifyServiceEnums[] notifyServiceEnums = values();
        for (NotifyServiceEnums notifyServiceEnum : notifyServiceEnums) {
            if (notifyServiceEnum.getCode().equals(code)) {
                return notifyServiceEnum.getServiceName();
            }
        }
        return null;
    }
}
