package com.hh.urm.notify.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName: NotifyDTO
 * @Author: MaxWell
 * @Description: 通知参数
 * @Date: 2022/10/24 15:31
 * @Version: 1.0
 */
@ApiModel("通知DTO")
public class NotifyDTO implements Serializable {

    private static final long serialVersionUID = 1160620297336378641L;
    @NotBlank(message = "traceId必填")
    @ApiModelProperty(value = "链路id")
    private String traceId;

    /**
     * 通知类型：{@link com.hh.urm.notify.enmus.NotifyServiceEnums} 1、短信 2、邮箱 3、APP 4、飞书、企微 5、其他
     *
     */
    @NotBlank(message = "通知类型必填")
    @ApiModelProperty(value = "通知类型：1、短信 2、邮箱 3、APP 4、飞书、企微 5、其他")
    private String notifyType;

    public NotifyDTO() {
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }
}
