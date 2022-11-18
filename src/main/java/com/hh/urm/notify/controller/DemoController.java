package com.hh.urm.notify.controller;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.repository.AppTemplateRepository;
import com.hh.urm.notify.repository.SmsTemplateRepository;
import com.hh.urm.notify.utils.TimeUtil;
import com.hh.urm.notify.utils.base.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * @ClassName: DemoController
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/4 10:24
 * @Version: 1.0
 */
@Slf4j
@RestController
public class DemoController {

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    @Resource
    private SmsTemplateRepository smsTemplateRepository;

    @PostMapping("/demo")
    public String demo(@RequestBody JSONObject jsonObject) {
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
        return "ok";
    }

    @PostMapping("/saveApp")
    public ServiceResponse<Object> saveApp() {
        AppTemplate appTemplate = new AppTemplate();
        appTemplate.setBusinessType(10000005);
        appTemplate.setTitle("您有一笔即将过期积分");
        appTemplate.setContent("【高合HiPhi】尊敬的用户，您的高合HiPhiApp的{points}贝还有{day}天即将到期，请尽快点击URLq前往高合之选商城进行兑换使用");
        appTemplate.setjPushType(2);
        appTemplate.setPushTypes("0,1");
        appTemplate.setLinkUrl("https://eeh5.hiphi.com/appv2/#/task");
        AppTemplate save = appTemplateRepository.save(appTemplate);
        return ServiceResponse.createSuccessResponse("", save);
    }
}
