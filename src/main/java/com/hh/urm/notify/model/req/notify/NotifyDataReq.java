package com.hh.urm.notify.model.req.notify;


/**
 * @ClassName: NotifyDataDTO
 * @Author: MaxWell
 * @Description: 通知参数
 * @Date: 2022/11/4 14:38
 * @Version: 1.0
 */
public class NotifyDataReq {

    private String superId;

    private SmsReq sms;

    private AppReq app;

    private MailReq mail;

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public SmsReq getSms() {
        return sms;
    }

    public void setSms(SmsReq sms) {
        this.sms = sms;
    }

    public AppReq getApp() {
        return app;
    }

    public void setApp(AppReq app) {
        this.app = app;
    }

    public MailReq getMail() {
        return mail;
    }

    public void setMail(MailReq mail) {
        this.mail = mail;
    }
}
