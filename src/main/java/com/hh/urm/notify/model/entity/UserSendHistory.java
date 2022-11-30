package com.hh.urm.notify.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Table(name = "t_user_send_history", schema = "notify")
public class UserSendHistory {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "code", nullable = false, length = 25)
    private String id;

    @Column(name = "trace_id", nullable = false, length = 25)
    private String traceId;

    @Column(name = "request_id", nullable = false, length = 25)
    private String requestId;

    @Column(name = "super_id", nullable = false, length = 25)
    private String superId;

    @Column(name = "sign", length = 25)
    private String sign;

    @Column(name = "type", nullable = false, length = 1)
    private String type;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "result", length = 1)
    private String result;

    @Column(name = "extend_type", length = 1)
    private String extendType;

    @Column(name = "receiver", nullable = false, length = 25)
    private String receiver;

    @Column(name = "params", nullable = false)
    private String params;

    @Column(name = "notify_status", length = 1)
    private String notifyStatus;

    @Column(name = "send_time", length = 25)
    private String sendTime;

    @Column(name = "notify_time", length = 25)
    private String notifyTime;

    @Lob
    @Column(name = "extend")
    private String extend;

    @Lob
    @Column(name = "send_content")
    private String sendContent;

    @Column(name = "_create_by", nullable = false, length = 25)
    private String createBy;

    @Column(name = "_create_time", nullable = false)
    private Timestamp createTime;

    @Column(name = "_update_by", nullable = false, length = 25)
    private String updateBy;

    @Column(name = "_update_time", nullable = false)
    private Timestamp updateTime;

    @Column(name = "template_name", length = 50)
    private String templateName;

    @Column(name = "template_code", length = 50)
    private String templateCode;

    @Lob
    @Column(name = "t_desc")
    private String tDesc;

    public String getTDesc() {
        return tDesc;
    }

    public void setTDesc(String tDesc) {
        this.tDesc = tDesc;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getExtendType() {
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}