package com.hh.urm.notify.service.notify.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hh.urm.notify.annotation.NotifyService;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.dto.notify.SmsContentDTO;
import com.hh.urm.notify.model.dto.notify.SmsDTO;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.service.BaseService;
import com.hh.urm.notify.service.notify.handler.INotifyHandler;
import com.hh.urm.notify.service.request.notify.SmsService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.EXCEPTION;


/**
 * @ClassName: SmsServiceImpl
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 10:56
 * @Version: 1.0
 */
@Slf4j
@NotifyService(notifyService = NotifyServiceEnums.SMS)
public class SmsHandler extends BaseService implements INotifyHandler {

    @Resource
    private SmsService smsService;

    @Override
    public JSONObject handler(String traceId, List<NotifyDataReq> data, String config) {

        JSONObject result = new JSONObject();
        SmsTemplate smsTemplate = JSONObject.parseObject(config, SmsTemplate.class);

        data = checkData(data);

        // 3、发送消息
        String sendResp = "";
        String paramsStr = "";
        try {
            // 2、构建参数
            paramsStr = buildParams(data, smsTemplate);
            sendResp = smsService.batchSendDiffSms(paramsStr);
        } catch (Exception e) {
            log.error("traceId:{},消息发送出现异常，请及时处理。Exception Message:{} \n Exception stackTrace:{}", traceId, e.getMessage(), e.getStackTrace());
            sendResp = EXCEPTION;
        }
        log.info("traceId:{},消息发送参数：{},响应结果：{}", traceId, paramsStr, sendResp);

        recordHistory(paramsStr, sendResp);

        return result;
    }

    /**
     * 校验参数
     *
     * @param data
     * @return
     */
    private List<NotifyDataReq> checkData(List<NotifyDataReq> data) {
        return null;
    }

    /**
     * 构建参数
     *
     * @param data        通知参数
     * @param smsTemplate 短信模板参数
     * @return 短信请求参数字符串
     */
    protected String buildParams(List<NotifyDataReq> data, SmsTemplate smsTemplate) {

        String templateId = smsTemplate.getTemplateId();
        String sender = smsTemplate.getSender();
        String signature = smsTemplate.getSignature();
        String statusCallback = smsTemplate.getStatusCallBack();
        String extend = smsTemplate.getExtend();

        smsService.setAppKey(smsTemplate.getAppKey());
        smsService.setAppSecret(smsTemplate.getAppSecret());

        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setFrom(sender);
        if (!Strings.isNullOrEmpty(statusCallback)) {
            smsDTO.setStatusCallback(statusCallback);
        }
        if (!Strings.isNullOrEmpty(extend)) {
            smsDTO.setExtend(extend);
        }

        List<SmsContentDTO> collect = data.stream().map(item -> {
            String receiver = item.getReceiver();
            SmsContentDTO smsContentDTO = new SmsContentDTO();
            if (!receiver.contains("+86")) {
                receiver = "+86" + receiver;
            }
            smsContentDTO.setTo(new String[]{receiver});
            smsContentDTO.setTemplateId(templateId);
            if (!Strings.isNullOrEmpty(signature)) {
                smsContentDTO.setSignature(signature);
            }

            String paramsStr = item.getParams();
            if (!Strings.isNullOrEmpty(paramsStr)) {
                if (!Strings.isNullOrEmpty(paramsStr)) {
                    smsContentDTO.setTemplateParas(Arrays.stream(paramsStr.split(",")).toArray(String[]::new));
                }
            }
            return smsContentDTO;
        }).collect(Collectors.toList());
        smsDTO.setSmsContent(collect);

        return JSONObject.toJSONString(smsDTO);
    }

    protected void recordHistory(String paramsStr, String sendResp) {

    }

}
