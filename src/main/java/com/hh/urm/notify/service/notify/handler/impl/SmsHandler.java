package com.hh.urm.notify.service.notify.handler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hh.urm.notify.annotation.NotifyService;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.dto.message.SmsMessageDTO;
import com.hh.urm.notify.model.dto.notify.SmsContentDTO;
import com.hh.urm.notify.model.dto.notify.SmsDTO;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.service.BaseService;
import com.hh.urm.notify.service.notify.handler.INotifyHandler;
import com.hh.urm.notify.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    @Value("${notify.sms.host}")
    private String host;
    @Value("${notify.sms.url.batchUrl}")
    private String batchUrl;

    private String appKey = "";
    private String appSecret = "";

    @Override
    public JSONObject handler(String traceId, String dataStr, String config) {

        JSONObject result = new JSONObject();
        SmsTemplate smsTemplate = JSONObject.parseObject(config, SmsTemplate.class);
        List<SmsMessageDTO> data = JSONArray.parseArray(dataStr, SmsMessageDTO.class);
        // 1???????????????
        Boolean checkResult = checkParams(data, smsTemplate, result);
        if (!checkResult) {
            return result;
        }

        // 2???????????????
        Pair<Boolean, JSONObject> buildParamsResult = buildParams(data, smsTemplate, result);
        if (!buildParamsResult.getFirst()) {
            return result;
        }
        JSONObject params = buildParamsResult.getSecond();

        // 3???????????????
        String sendResp = "";
        try {
            sendResp = send(params);
        } catch (Exception e) {
            log.error("traceId:{},?????????????????????????????????????????????Exception Message:{} \n Exception stackTrace:{}", traceId, e.getMessage(), e.getStackTrace());
            sendResp = EXCEPTION;
        }
        log.info("traceId:{},?????????????????????{},???????????????{}", traceId, params, sendResp);

        recordHistory(params, sendResp);

        return result;
    }

    protected Boolean checkParams(List<SmsMessageDTO> data, SmsTemplate smsTemplate, JSONObject result) {

        // 1???????????????
        if (data.isEmpty()) {
            getResultMsg(result, "??????????????????", FAILED);
            return false;
        }

        return true;
    }

    protected Pair<Boolean, JSONObject> buildParams(List<SmsMessageDTO> data, SmsTemplate smsTemplate, JSONObject result) {

        String templateId = smsTemplate.getTemplateId();
        String sender = smsTemplate.getSender();
        String signature = smsTemplate.getSignature();
        String statusCallback = smsTemplate.getStatusCallBack();
        String extend = smsTemplate.getExtend();

        appKey = smsTemplate.getAppKey();
        appSecret = smsTemplate.getAppSecret();

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
            List<String> templateParams = item.getTemplateParams();
            SmsContentDTO smsContentDTO = new SmsContentDTO();
            if (!receiver.contains("+86")) {
                receiver = "+86" + receiver;
            }
            smsContentDTO.setTo(new String[]{receiver});
            smsContentDTO.setTemplateId(templateId);
            if (!Strings.isNullOrEmpty(signature)) {
                smsContentDTO.setSignature(signature);
            }

            if (!templateParams.isEmpty()) {
                smsContentDTO.setTemplateParas(templateParams.toArray(new String[0]));
            }
            return smsContentDTO;
        }).collect(Collectors.toList());
        smsDTO.setSmsContent(collect);

        return Pair.of(true, (JSONObject) JSONObject.toJSON(smsDTO));
    }

    protected String send(JSONObject params) throws Exception {
        String bodyStr = params.toJSONString();
        try (CloseableHttpClient client = HttpUtil.createAllTrustingClient()) {
            HttpUriRequest request = RequestBuilder.create("POST")
                    .setUri(host + batchUrl)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"")
                    .addHeader("X-WSSE", getXWsse())
                    .setEntity(new StringEntity(bodyStr))
                    .build();
            return EntityUtils.toString(client.execute(request).getEntity(), "utf-8");
        } catch (Exception e) {
            log.error("Exception message:{} \n Exception stackTrace???{}", e.getMessage(), e.getStackTrace());
            throw new Exception(e.getMessage());
        }
    }

    protected void recordHistory(JSONObject params, String sendResp) {

    }

    private String paramsToString(JSONObject params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String temp = "";

        for (String key : params.keySet()) {
            String value = params.getString(key);
            if (Strings.isNullOrEmpty(value)) {
                continue;
            }
            temp = URLEncoder.encode(value, "UTF-8");
            sb.append(key).append("=").append(temp).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    private String getXWsse() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        String nonce = UUID.randomUUID().toString().replace("-", "");
        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes());
        return String.format("UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"", appKey, passwordDigestBase64Str, nonce, time);
    }
}
