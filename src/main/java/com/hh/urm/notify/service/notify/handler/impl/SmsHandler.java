package com.hh.urm.notify.service.notify.handler.impl;

import com.alibaba.fastjson.JSONArray;
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
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.EXCEPTION;
import static com.hh.urm.notify.consts.CommonConst.FAILED;


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
        // 1、检查参数
        Boolean checkResult = checkParams(data, smsTemplate, result);
        if (!checkResult) {
            return result;
        }

        // 2、构建参数
        Pair<Boolean, String> buildParamsResult = buildParams(data, smsTemplate, result);
        if (!buildParamsResult.getFirst()) {
            return result;
        }
        String paramsStr = buildParamsResult.getSecond();

        // 3、发送消息
        String sendResp = "";
        try {
            sendResp = smsService.batchSendDiffSms(paramsStr);
        } catch (Exception e) {
            log.error("traceId:{},消息发送出现异常，请及时处理。Exception Message:{} \n Exception stackTrace:{}", traceId, e.getMessage(), e.getStackTrace());
            sendResp = EXCEPTION;
        }
        log.info("traceId:{},消息发送参数：{},响应结果：{}", traceId, paramsStr, sendResp);

        recordHistory(paramsStr, sendResp);

        return result;
    }

    protected Boolean checkParams(List<NotifyDataReq> data, SmsTemplate smsTemplate, JSONObject result) {

        // 1、校验参数
        if (data.isEmpty()) {
            getResultMsg(result, "接收者不存在", FAILED);
            return false;
        }

        return true;
    }

    protected Pair<Boolean, String> buildParams(List<NotifyDataReq> data, SmsTemplate smsTemplate, JSONObject result) {

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
            String receiver = item.getNotifier();
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

        String bodyStr = JSONObject.toJSONString(smsDTO);
        return Pair.of(true, bodyStr);
    }

    protected void recordHistory(String paramsStr, String sendResp) {

    }

    private String paramsToString(JSONObject params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String temp = "";

        for (String s : params.keySet()) {
            temp = URLEncoder.encode(params.getString(s), "UTF-8");
            sb.append(s).append("=").append(temp).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
