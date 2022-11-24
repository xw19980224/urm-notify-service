package com.hh.urm.notify.enums;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: NotifyServiceEnums
 * @Author: MaxWell
 * @Description: 通知服务枚举类
 * @Date: 2022/10/26 10:32
 * @Version: 1.0
 */
public enum NotifyServiceEnums {
    /**
     * 短信
     */
    SMS("1", "sms", "smsService", "urm_sms_notify_topic"),
    /**
     * 邮箱
     */
    MAIL("2", "mail", "mailService", "urm_mail_notify_topic"),
    /**
     * OneApp
     */
    ONE_APP("3", "oneApp", "oneAppService", "urm_one_notify_topic"),
    /**
     * 飞书
     */
    FEI_SHU("4", "feiShu", "feiShuService", "urm_feiShu_notify_topic"),
    /**
     * 企微
     */
    QI_WEI("5", "qiWei", "qiWeiService", "urm_qiWei_notify_topic"),

    /**
     * 其他
     */
    OTHER("6", "other", "other", "urm_other_notify_topic");

    /**
     * 业务编码
     */
    private final String code;
    /**
     * 通知名称
     */
    private final String name;
    /**
     * 业务标识
     */
    private final String serviceName;
    /**
     * 通知主题名称
     */
    private final String topicName;

    NotifyServiceEnums(String code, String name, String serviceName, String topicName) {
        this.code = code;
        this.name = name;
        this.serviceName = serviceName;
        this.topicName = topicName;
    }

    public String getCode() {
        return code;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getName() {
        return name;
    }

    public String getTopicName() {
        return topicName;
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
     * @param code
     * @return 是否包含未知值
     */
    public static Boolean checkCodeIsExist(String code) {
        return getServiceNameByCode(code) != null;
    }

    /**
     * 通过通知类型获取通知主题
     *
     * @param notifyType
     * @return
     */
    public static List<String> getTopicList(List<String> notifyType) {
        return notifyType.stream().filter(item -> !Strings.isNullOrEmpty(getTopicNameByCode(item))).map(NotifyServiceEnums::getTopicNameByCode).collect(Collectors.toList());
    }

    /**
     * 通过code获取通知主题
     *
     * @param code 业务编码
     * @return serviceName
     */
    public static String getTopicNameByCode(String code) {
        NotifyServiceEnums[] notifyServiceEnums = values();
        for (NotifyServiceEnums notifyServiceEnum : notifyServiceEnums) {
            if (notifyServiceEnum.getCode().equals(code)) {
                return notifyServiceEnum.getTopicName();
            }
        }
        return null;
    }

    /**
     * 通过topicName 获取 code
     *
     * @param topicName
     * @return
     */
    public static String getCodeByTopicName(String topicName) {
        NotifyServiceEnums[] notifyServiceEnums = values();
        for (NotifyServiceEnums notifyServiceEnum : notifyServiceEnums) {
            if (notifyServiceEnum.getTopicName().equals(topicName)) {
                return notifyServiceEnum.getCode();
            }
        }
        return null;
    }
}
