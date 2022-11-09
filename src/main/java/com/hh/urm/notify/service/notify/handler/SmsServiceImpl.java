package com.hh.urm.notify.service.notify.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hh.urm.notify.annotation.NotifyService;
import com.hh.urm.notify.enmus.NotifyServiceEnums;
import com.hh.urm.notify.model.dto.notify.SmsContentDTO;
import com.hh.urm.notify.model.dto.notify.SmsDTO;
import com.hh.urm.notify.service.notify.BaseNotifyService;
import com.hh.urm.notify.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.hh.urm.notify.consts.CommonConst.FAILED;
import static com.hh.urm.notify.consts.NotifyConst.Sms.*;
import static com.hh.urm.notify.consts.NotifyConst.TEMPLATE_ID;


/**
 * @ClassName: SmsServiceImpl
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 10:56
 * @Version: 1.0
 */
@Slf4j
@NotifyService(notifyService = NotifyServiceEnums.SMS)
public class SmsServiceImpl extends BaseNotifyService {

    @Value("${sms.host}")
    private String host;
    @Value("${sms.url.batchUrl}")
    private String batchUrl;

    private String appKey = "";
    private String appSecret = "";

    @Override
    protected Boolean checkParams(JSONObject jsonObject, JSONObject result) {

        // 1、获取参数
        String receiver = (String) jsonObject.getOrDefault(RECEIVER, "");

        // 2、校验参数
        if (Strings.isNullOrEmpty(receiver)) {
            getResultMsg(result, "接收者不存在", FAILED);
            return false;
        }

        return true;
    }

    @Override
    protected Pair<Boolean, JSONObject> buildParams(JSONObject jsonObject, JSONObject result) {
        String receiver = (String) jsonObject.getOrDefault(RECEIVER, "");
        JSONArray paramsArray = jsonObject.getJSONArray(TEMPLATE_PARAMS);

        String templateId = (String) jsonObject.getOrDefault(TEMPLATE_ID, "");
        String sender = (String) jsonObject.getOrDefault(SENDER, "");
        String signature = (String) jsonObject.getOrDefault(SIGNATURE, "");
        String statusCallback = (String) jsonObject.getOrDefault(STATUS_CALL_BACK, "");
        String extend = (String) jsonObject.getOrDefault(EXTEND, "");

        appKey = ((String) jsonObject.getOrDefault(APP_KEY, ""));
        appSecret = ((String) jsonObject.getOrDefault(APP_SECRET, ""));

        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setFrom(sender);
        if (!Strings.isNullOrEmpty(statusCallback)) {
            smsDTO.setStatusCallback(statusCallback);
        }
        if (!Strings.isNullOrEmpty(extend)) {
            smsDTO.setExtend(extend);
        }

        SmsContentDTO smsContentDTO = new SmsContentDTO();
        if (!receiver.contains("+86")) {
            receiver = "+86" + receiver;
        }
        smsContentDTO.setTo(new String[]{receiver});
        smsContentDTO.setTemplateId(templateId);
        if (!Strings.isNullOrEmpty(signature)) {
            smsContentDTO.setSignature(signature);
        }

        if (!paramsArray.isEmpty()) {
            List<String> template = JSONObject.parseArray(paramsArray.toJSONString(), String.class);
            smsContentDTO.setTemplateParas(template.toArray(new String[0]));
        }
        smsDTO.setSmsContent(new SmsContentDTO[]{smsContentDTO});

        return Pair.of(true, (JSONObject) JSONObject.toJSON(smsDTO));
    }

    @Override
    protected String send(JSONObject params) throws Exception {
        String bodyStr = paramsToString(params);
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
            log.error("Exception message:{} \n Exception stackTrace：{}", e.getMessage(), e.getStackTrace());
            throw new Exception(e.getMessage());
        }
    }

    @Override
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
