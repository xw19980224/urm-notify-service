package com.hh.urm.notify.model.notify.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: NotifyDTO
 * @Author: MaxWell
 * @Description: 通知请求参数
 * @Date: 2022/10/24 15:31
 * @Version: 1.0
 */
@ApiModel("通知DTO")
public class NotifyReq implements Serializable {

    private static final long serialVersionUID = 1160620297336378641L;

    @NotBlank(message = "traceId必填")
    @ApiModelProperty(value = "链路id")
    private String traceId;


    @NotBlank(message = "记录标识")
    @ApiModelProperty(value = "记录标识")
    private String sign;

    /**
     * 通知类型：{@link com.hh.urm.notify.enums.NotifyServiceEnums}通知类型：1、短信 2、邮箱 3、APP 4、飞书 5、企微 6、其他
     */
    @NotEmpty(message = "通知类型必填")
    @ApiModelProperty(value = "通知类型：1、短信 2、邮箱 3、APP 4、飞书 5、企微 6、其他")
    private String notifyType;

    /**
     * 模板code
     */
    @NotBlank(message = "模板code")
    @ApiModelProperty(value = "模板code，必填")
    private String templateCode;

    @ApiModelProperty(value = "通知参数")
    private List<NotifyDataReq> data;

    public NotifyReq() {
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public List<NotifyDataReq> getData() {
        return data;
    }

    public void setData(List<NotifyDataReq> data) {
        this.data = data;
    }
}
