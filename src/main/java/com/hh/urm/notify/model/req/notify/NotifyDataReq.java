package com.hh.urm.notify.model.req.notify;


/**
 * @ClassName: NotifyDataDTO
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/4 14:38
 * @Version: 1.0
 */
public class NotifyDataReq {

    private String superId;

    private SmsMessageReq smsMessage;

    private AppMessageReq appMessage;

    private MailMessageReq mailMessage;

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public SmsMessageReq getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(SmsMessageReq smsMessage) {
        this.smsMessage = smsMessage;
    }

    public AppMessageReq getAppMessage() {
        return appMessage;
    }

    public void setAppMessage(AppMessageReq appMessage) {
        this.appMessage = appMessage;
    }

    public MailMessageReq getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(MailMessageReq mailMessage) {
        this.mailMessage = mailMessage;
    }

}
