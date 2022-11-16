package com.hh.urm.notify.model.req.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: MailMessageDTO
 * Description: 发邮件信息
 */
public class MailMessageReq implements Serializable {
    /***
     * 收件人列表
     */
    private List<String> toMails;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;

    public MailMessageReq() {
    }

    public MailMessageReq(List<String> toMails, String title, String content) {
        this.toMails = toMails;
        this.title = title;
        this.content = content;
    }

    public List<String> getToMails() {
        return toMails;
    }

    public void setToMails(List<String> toMails) {
        this.toMails = toMails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
