package com.hh.urm.notify.model.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

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


    @NotBlank(message = "记录标识")
    @ApiModelProperty(value = "记录标识")
    private String sign;

    /**
     * 通知类型：{@link com.hh.urm.notify.enmus.NotifyServiceEnums}通知类型：1、短信 2、邮箱 3、APP 4、飞书 5、企微 6、其他
     */
    @NotEmpty(message = "通知类型必填")
    @ApiModelProperty(value = "通知类型：1、短信 2、邮箱 3、APP 4、飞书 5、企微 6、其他")
    private String[] notifyType;

    /**
     * 短信源数据code
     */
    @ApiModelProperty(value = "短信源数据code，当notifyType为0或1时，必填")
    private String smsCode;

    @ApiModelProperty(value = "通知参数")
    private List<NotifyDataDTO> data;

    public NotifyDTO() {
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

    public String[] getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String[] notifyType) {
        this.notifyType = notifyType;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public List<NotifyDataDTO> getData() {
        return data;
    }

    public void setData(List<NotifyDataDTO> data) {
        this.data = data;
    }
}
