package com.hh.urm.notify.test;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.hh.urm.notify.UrmNotifyApplication;
import com.hh.urm.notify.model.dto.notify.SmsContentDTO;
import com.hh.urm.notify.model.dto.notify.SmsDTO;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.repository.SmsTemplateRepository;
import com.hh.urm.notify.service.request.notify.SmsService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: Test
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/24 18:00
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrmNotifyApplication.class)
public class SmsSendTest {

    @Resource
    private SmsService smsService;

    @Resource
    private SmsTemplateRepository smsTemplateRepository;

    @Test
    public void batchSendSms() throws UnsupportedEncodingException {

        SmsTemplate smsTemplate = smsTemplateRepository.findOneByCode("8a93e2b884886b9e0184886bca420000").orElse(null);
        String signature = smsTemplate.getSignature();
        String sender = smsTemplate.getSender();
        String statusCallBack = smsTemplate.getStatusCallBack();
        String extend = smsTemplate.getExtend();
        String templateId = smsTemplate.getTemplateId();

        Map<String, String> map = Maps.newHashMap();
        map.put("from", sender);//发送方
        map.put("to", "+8613203115616");//接收方
        map.put("templateId", templateId);//模板id
        String templateParas = "游客vc02e,PA790934730492088320";
        if (!Strings.isNullOrEmpty(templateParas)) {
            List<String> paras = Arrays.stream(templateParas.split(",")).collect(Collectors.toList());
            map.put("templateParas", JSONObject.toJSONString(paras));
        }
        if (!Strings.isNullOrEmpty(statusCallBack)) {
            map.put("statusCallback", statusCallBack);//短信接收状态上报接口
        }
        if (!Strings.isNullOrEmpty(extend)) {
            map.put("extend", extend);//扩展参数
        }
        if (!Strings.isNullOrEmpty(signature)) {
            map.put("signature", signature);
        }

        StringBuilder sb = new StringBuilder();
        String temp = "";

        for (String s : map.keySet()) {
            temp = URLEncoder.encode(map.get(s), "UTF-8");
            sb.append(s).append("=").append(temp).append("&");
        }
        String s = sb.deleteCharAt(sb.length() - 1).toString();
        smsService.setAppKey(smsTemplate.getAppKey());
        smsService.setAppSecret(smsTemplate.getAppSecret());
        String response = smsService.batchSendSms(s);
        System.out.println(response);
    }


    @Test
    public void batchSendDiffSms() throws UnsupportedEncodingException {

        SmsTemplate smsTemplate = smsTemplateRepository.findOneByCode("8a93e2b884886b9e0184886bca420000").orElse(null);
        String signature = smsTemplate.getSignature();
        String sender = smsTemplate.getSender();
        String statusCallBack = smsTemplate.getStatusCallBack();
        String extend = smsTemplate.getExtend();
        String templateId = smsTemplate.getTemplateId();

        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setFrom(sender);

        SmsContentDTO smsContentDTO = new SmsContentDTO();
        smsContentDTO.setTo(new String[]{"+8613203115616"});
        smsContentDTO.setTemplateId(templateId);

        String templateParas = "游客vc02e,PA790934730492088320";
        if (!Strings.isNullOrEmpty(templateParas)) {
            smsContentDTO.setTemplateParas(Arrays.stream(templateParas.split(",")).toArray(String[]::new));
        }

        if (!Strings.isNullOrEmpty(signature)) {
            smsContentDTO.setSignature(signature);
        }
        smsDTO.setSmsContent(Lists.newArrayList(smsContentDTO));

        smsService.setAppKey(smsTemplate.getAppKey());
        smsService.setAppSecret(smsTemplate.getAppSecret());
        String bodyStr = JSONObject.toJSONString(smsDTO);
        String response = smsService.batchSendDiffSms(bodyStr);
        System.out.println(response);
    }
}
