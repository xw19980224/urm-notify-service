package com.hh.urm.notify.model.req.notify;

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
    private List<String> notifyType;

    /**
     * 短信模板code据code
     */
    @ApiModelProperty(value = "短信源数据code，当notifyType为0或1时，必填")
    private String smsCode;

    /**
     * oneApp模板code
     */
    @ApiModelProperty(value = "oneApp模板code，当notifyType为0或3时，必填")
    private String appCode;

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

    public List<String> getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(List<String> notifyType) {
        this.notifyType = notifyType;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public List<NotifyDataReq> getData() {
        return data;
    }

    public void setData(List<NotifyDataReq> data) {
        this.data = data;
    }
}
