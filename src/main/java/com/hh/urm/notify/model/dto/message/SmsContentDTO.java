package com.hh.urm.notify.model.dto.message;

/**
 * @ClassName: SmsContentDTO
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/31 16:18
 * @Version: 1.0
 */
public class SmsContentDTO {
    /**
     * 群发短信接收方的号码
     */
    private String[] to;
    /**
     * 模板Id
     */
    private String templateId;
    /**
     * 短信模板的变量值列表
     */
    private String[] templateParas;
    /**
     * 签名名称
     */
    private String signature;

    public SmsContentDTO() {
    }

    public SmsContentDTO(String[] to, String templateId, String[] templateParas, String signature) {
        this.to = to;
        this.templateId = templateId;
        this.templateParas = templateParas;
        this.signature = signature;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String[] getTemplateParas() {
        return templateParas;
    }

    public void setTemplateParas(String[] templateParas) {
        this.templateParas = templateParas;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
