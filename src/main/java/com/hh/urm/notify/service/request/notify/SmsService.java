package com.hh.urm.notify.service.request.notify;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.service.request.base.BaseSmsUtils;
import com.hh.urm.notify.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.hh.urm.notify.consts.CommonConst.*;

/**
 * @ClassName: SmsService
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/23 9:24
 * @Version: 1.0
 */
@Slf4j
@Service
public class SmsService extends BaseSmsUtils {

    @Value("${notify.sms.host}")
    private String host;
    @Value("${notify.sms.url.batchSendSms}")
    private String batchSendSms;
    @Value("${notify.sms.url.batchSendDiffSms}")
    private String batchSendDiffSms;

    public String batchSendSms(String bodyStr) {
        String responseStr;
        try {
            responseStr = send(host + batchSendSms, "application/x-www-form-urlencoded", bodyStr);
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put(SUCCESS, false);
            result.put(STATUS, EXCEPTION);
            result.put(MESSAGE, e.getMessage());
            result.put(EXCEPTION, e.getStackTrace());
            log.info("发送短信出现异常，request Body:{} exception message:{} ,Exception stackTrace:{}", bodyStr, e.getMessage(), e.getStackTrace());
            responseStr = result.toJSONString();
        }
        return responseStr;
    }

    public String batchSendDiffSms(String bodyStr) {
        String responseStr;
        try {
            responseStr = send(host + batchSendDiffSms, "application/json", bodyStr);
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put(SUCCESS, false);
            result.put(STATUS, EXCEPTION);
            result.put(MESSAGE, e.getMessage());
            result.put(EXCEPTION, e.getStackTrace());
            log.info("发送分批短信出现异常，request Body:{} exception message:{} ,Exception stackTrace:{}", bodyStr, e.getMessage(), e.getStackTrace());
            responseStr = result.toJSONString();
        }
        return responseStr;
    }

    protected String send(String url, String contentTypeValue, String bodyStr) throws Exception {
        try (CloseableHttpClient client = HttpUtil.createAllTrustingClient()) {
            HttpUriRequest request = RequestBuilder.create("POST")
                    .setUri(url)
                    .addHeader("Content-Type", contentTypeValue)
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

}
