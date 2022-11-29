package com.hh.urm.notify.model.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: MailMessageDTO
 * @Author: MaxWell
 * @Description: 发邮件信息
 * @Date: 2022/11/24 10:16
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailMessageDTO implements Serializable {
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
}
