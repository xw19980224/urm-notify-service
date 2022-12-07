package com.hh.urm.notify.service.request.notify;

import com.alibaba.fastjson.JSONObject;
import com.hh.framework.ms.client.MailClient;
import com.hh.framework.ms.exchange.client.ExchangeClient;
import com.hh.framework.ms.exchange.mail.ExchangeMail;
import com.hh.framework.ms.mail.Mail;
import com.hh.framework.ms.pojo.MailBody;
import com.hh.framework.ms.pojo.MailInformation;
import com.hh.framework.ms.pojo.MailUser;
import com.hh.urm.notify.model.notify.dto.MailMessageDTO;
import com.hh.urm.notify.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.*;

/**
 * @ClassName: MailService
 * @Author: MaxWell
 * @Description: 短信服务
 * @Date: 2022/11/24 10:05
 * @Version: 1.0
 */
@Slf4j
@Service
public class MailService extends BaseService {

    public String sendMail(MailMessageDTO mailMessage) {
        JSONObject result = new JSONObject();
        try {
            //收件人
            List<MailUser> mailUsers = mailMessage.getToMails().stream().map(x -> {
                MailUser mailUser = new MailUser();
                mailUser.setAddress(x);
                return mailUser;
            }).collect(Collectors.toList());

            MailClient mailClient = ExchangeClient.builder().env("mail").build();

            MailInformation mailInformation = new MailInformation();
            mailInformation.setSubject(mailMessage.getTitle());
            mailInformation.setTo(mailUsers);

            MailBody mailBody = new MailBody();
            mailBody.setContent(mailMessage.getContent());

            Mail mail = ExchangeMail.builder().body(mailBody).information(mailInformation).build();

            mailClient.send(mail);
        } catch (Exception e) {
            log.error("MailService 发生异常,message:{},err_msg:{}", JSONObject.toJSONString(mailMessage), e.getStackTrace());
            result.put(SUCCESS, false);
            result.put(STATUS, EXCEPTION);
            result.put(MESSAGE, e.getMessage());
            result.put(EXCEPTION, e.getStackTrace());
            return result.toJSONString();
        }
        getResultMsg(result, "通知成功", SUCCESS, true);
        return result.toJSONString();
    }
}
