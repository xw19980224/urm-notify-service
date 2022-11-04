package com.hh.urm.notify.test;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.UrmNotifyApplication;
import com.hh.urm.notify.model.entity.SmsMetadata;
import com.hh.urm.notify.repository.SmsMetadataRepository;
import com.hh.urm.notify.service.notify.IMessage;
import com.hh.urm.notify.utils.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    private SmsMetadataRepository smsMetadataRepository;

    @Autowired
    private IMessage iMessage;

    @Test
    public void test() {
        SmsMetadata smsMetadata = new SmsMetadata();
        Timestamp nowTime = TimeUtil.nowTimestamp();
        smsMetadata.setName("短信测试");
        smsMetadata.setCreateTime(nowTime);
        smsMetadata.setUpdateTime(nowTime);
        smsMetadata.setCreateBy("xw");
        smsMetadata.setSender("8820100935957");
        smsMetadata.setAppKey("9e4VrLQd7BX3VZhq6x1g951q4aCr");
        smsMetadata.setAppSecret("r83k2Q9AQD31FrNO904okwTJ1ZBJ");
        smsMetadata.setTemplateId("4e34d053e21f448c933a069a636a4226");
        smsMetadata.setTemplateContent("您的好友 ${1}（尾号${2}）帮您预约了高合汽车试驾，后续将会有出行顾问与您确认时间及行程，感谢对高合汽车的支持！");
        smsMetadataRepository.save(smsMetadata);
    }

    public static void main(String[] args) {
        String[] split = "".split(",");
        System.out.println(JSONObject.toJSONString(Arrays.stream(split).collect(Collectors.toList())));
    }

}
