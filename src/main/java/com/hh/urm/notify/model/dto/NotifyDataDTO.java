package com.hh.urm.notify.model.dto;

import com.hh.urm.notify.model.dto.message.AppMessageDTO;
import com.hh.urm.notify.model.dto.message.MailMessageDTO;
import com.hh.urm.notify.model.dto.message.SmsMessageDTO;

/**
 * @ClassName: NotifyDataDTO
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/4 14:38
 * @Version: 1.0
 */
public class NotifyDataDTO {

    private String superId;

    private SmsMessageDTO smsMessage;

    private AppMessageDTO appMessage;

    private MailMessageDTO mailMessage;

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public SmsMessageDTO getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(SmsMessageDTO smsMessage) {
        this.smsMessage = smsMessage;
    }

    public AppMessageDTO getAppMessage() {
        return appMessage;
    }

    public void setAppMessage(AppMessageDTO appMessage) {
        this.appMessage = appMessage;
    }

    public MailMessageDTO getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(MailMessageDTO mailMessage) {
        this.mailMessage = mailMessage;
    }

}
