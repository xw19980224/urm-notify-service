package com.hh.urm.notify.enmus;

/**
 * @ClassName: NotifyServiceEnums
 * @Author: MaxWell
 * @Description: 通知服务枚举类
 * @Date: 2022/10/26 10:32
 * @Version: 1.0
 */
public enum NotifyServiceEnums {
    /**
     * 全部
     */
    ALL("0", "all"),
    /**
     * 短信
     */
    SMS("1", "smsService"),
    /**
     * 邮箱
     */
    MAIL("2", "mailService"),
    /**
     * OneApp
     */
    ONE_APP("3", "oneAppService"),
    /**
     * 飞书
     */
    FEI_SHU("4", "feiShuService"),
    /**
     * 企微
     */
    QI_WEI("5", "qiWeiService"),

    /**
     * 其他
     */
    OTHER("6", "other");

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
     * @return serviceName
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

    /**
     * 检查code数组中，是否包含未知值
     *
     * @param codes
     * @return 是否包含未知值
     */
    public static Boolean checkCodeIsExist(String[] codes) {
        boolean exist = true;
        for (String code : codes) {
            if (getServiceNameByCode(code) == null) {
                exist = false;
            }
        }
        return exist;
    }
}
