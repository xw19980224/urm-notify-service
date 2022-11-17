package com.hh.urm.notify.test;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.UrmNotifyApplication;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.repository.AppTemplateRepository;
import com.hh.urm.notify.repository.SmsTemplateRepository;
import com.hh.urm.notify.service.notify.handler.INotifyHandler;
import com.hh.urm.notify.utils.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @ClassName: BeanTest
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 11:02
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrmNotifyApplication.class)
public class BeanTest {

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    @Autowired
    private INotifyHandler iNotifyHandler;

    @Test
    public void test() {
        SmsTemplate smsTemplate = new SmsTemplate();
        Timestamp nowTime = TimeUtil.nowTimestamp();
        smsTemplate.setName("短信测试");
        smsTemplate.setCreateTime(nowTime);
        smsTemplate.setUpdateTime(nowTime);
        smsTemplate.setCreateBy("xw");
        smsTemplate.setSender("8820100935957");
        smsTemplate.setAppKey("9e4VrLQd7BX3VZhq6x1g951q4aCr");
        smsTemplate.setAppSecret("r83k2Q9AQD31FrNO904okwTJ1ZBJ");
        smsTemplate.setTemplateId("4e34d053e21f448c933a069a636a4226");
        smsTemplate.setTemplateContent("您的好友 ${1}（尾号${2}）帮您预约了高合汽车试驾，后续将会有出行顾问与您确认时间及行程，感谢对高合汽车的支持！");
        smsTemplateRepository.save(smsTemplate);
    }

    @Test
    public void test1() {
        AppTemplate appTemplate = new AppTemplate();
        appTemplate.setBusinessType(10000005);
        appTemplate.setTitle("20221027");
        appTemplate.setContent("2022102701");
        appTemplate.setjPushType(0);
        appTemplate.setPushTypes("0,1");
        appTemplateRepository.save(appTemplate);
    }

    public static void main(String[] args) {
        String[] split = "".split(",");
        System.out.println(JSONObject.toJSONString(Arrays.stream(split).collect(Collectors.toList())));
    }

}
