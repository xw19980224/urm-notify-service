package com.hh.urm.notify.model.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: AppMessageDTO
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 17:57
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppMessageDTO {

    /**
     * OneApp的用户Id
     */
    private String idmId;
    /**
     * 业务类型
     */
    private Integer businessType;
    /**
     * 推送类型 0:极光推送 1:app站内信推送 2:弹屏 -1:all
     */
    private Integer[] pushTypes;
    /**
     * 极光推送类型 0:notification通知 1:message自定义消息 2:all
     */
    private Integer jpushType;
    /**
     * 操作时间
     */
    private String actionTime;
    /**
     * 车辆vin码,当车控通知时必传
     */
    private String vin;
    /**
     * 图片链接url
     */
    private String imageUrl;
    /**
     * 通知内容
     */
    private String content;
    /**
     * 通知标题
     */
    private String title;
    /**
     * 提醒跳转url
     */
    private String linkUrl;
    /**
     * 是否全屏 0:非全屏 1:全屏 default:0
     */
    private Integer fullType;
    /**
     * 动态扩展字段
     */
    private String businessExt;
    /**
     * 弹框扩展字段
     */
    private String popWindowExt;

}
