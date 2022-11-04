package com.hh.urm.notify.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @ClassName: SmsMetadata
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/2 9:45
 * @Version: 1.0
 */
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Table(name = "t_sms_metadata", schema = "notify", catalog = "notify")
public class SmsMetadata {
    /**
     * id
     */
    private String code;
    /**
     * 短信名称
     */
    private String name;
    /**
     * 短信描述
     */
    private String description;
    /**
     * 通道号
     */
    private String sender;
    /**
     * APP_Key
     */
    private String appKey;
    /**
     * APP_Secret
     */
    private String appSecret;
    /**
     * 签明名称
     */
    private String signature;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 短信模板Id
     */
    private String templateId;
    /**
     * 短信模板内容
     */
    private String templateContent;

    @Id
    @Column(name = "code", nullable = false, length = 255)
    @GeneratedValue(generator = "jpa-uuid")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 25)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = description;
    }

    @Basic
    @Column(name = "sender", nullable = false, length = 25)
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Basic
    @Column(name = "app_key", nullable = false, length = 50)
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Basic
    @Column(name = "app_secret", nullable = false, length = 50)
    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Basic
    @Column(name = "signature", nullable = true, length = 50)
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time", nullable = false)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "create_by", nullable = false, length = 25)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "template_id", nullable = false, length = 50)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Basic
    @Column(name = "template_content", nullable = false, length = 255)
    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsMetadata that = (SmsMetadata) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(sender, that.sender) && Objects.equals(appKey, that.appKey) && Objects.equals(appSecret, that.appSecret) && Objects.equals(signature, that.signature) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(createBy, that.createBy) && Objects.equals(templateId, that.templateId) && Objects.equals(templateContent, that.templateContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, description, sender, appKey, appSecret, signature, createTime, updateTime, createBy, templateId, templateContent);
    }
}
