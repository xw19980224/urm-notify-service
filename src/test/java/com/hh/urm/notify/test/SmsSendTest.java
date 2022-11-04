package com.hh.urm.notify.test;

import com.hh.framework.sms.Sms;
import com.hh.framework.sms.client.Client;
import com.hh.framework.sms.client.SmsClient;
import com.hh.framework.sms.pojo.SmsMessage;
import com.hh.urm.notify.UrmNotifyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: Test
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/24 18:00
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrmNotifyApplication.class)
public class SmsSendTest  {

    @Test
    public void sendMail() {
        Client mailClient = SmsClient.builder().env("dev").build();

        Sms sms = Sms.builder().body(mainSmsMessage()).build();

        mailClient.send(sms);
    }


    private SmsMessage mainSmsMessage() {
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setParas(new String[]{"xw","123"});
        smsMessage.setReceivers(new String[]{"13203115616","15151377166"});
        smsMessage.setTemplateName("td");
        return smsMessage;
    }

    private SmsMessage childSmsMessage() {
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setParas(new String[]{"world2"});
        smsMessage.setReceivers(new String[]{"13203115616"});
        smsMessage.setTemplateName("td");
        return smsMessage;
    }

    @Test
    public void sendBatchMail() {
        Client mailClient = SmsClient.builder().env("dev").build();

        Sms sms = Sms.builder().body(mainSmsMessage()).add(childSmsMessage()).build(); // 这里发送的Message必须是模板ID不重复

        mailClient.send(sms);
    }
}
