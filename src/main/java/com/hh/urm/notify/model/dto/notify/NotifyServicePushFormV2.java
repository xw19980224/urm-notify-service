package com.hh.urm.notify.model.dto.notify;

import lombok.Data;

/**
 * @ClassName: NotifyServicePushFormV2
 * @Author: MaxWell
 * @Description: 通知服务推送表
 * @Date: 2022/11/18 10:04
 * @Version: 1.0
 */
@Data
public class NotifyServicePushFormV2 {
    /**
     * 推送类型（0：极光推送；1：app站内推送；2：弹框；-1：all；）
     */
    private Integer[] pushTypes;
    /**
     * 极光推送类型（0：notification通知；1：message自定义消息；2：all；）
     */
    private Integer jpushType;
    /**
     * 极光推送类型（0：notification通知；1：message自定义消息；2：all；）
     */
    private Integer fullType = 1;
    /**
     * 用户superId
     */
    private String superId;

    /**
     * 业务类型 充电业务：1000
     */
    private Integer businessType;

    /**
     * 客户端上报时间
     */
    private String actionTime;
    /**
     * 动态扩展字段
     */
    private String businessExt;
    /**
     * 弹框扩展字段
     */
    private String popWindowExt;
    /**
     * 图片链接url
     */
    private String imageUrl;

    /**
     * 车辆vin码
     */
    private String vin;

    /**
     * 通知内容
     */
    private String content;
    /**
     * 通知标题
     */
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提醒跳转URL
     */
    private String linkUrl;
}
