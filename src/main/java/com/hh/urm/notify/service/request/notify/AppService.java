package com.hh.urm.notify.service.request.notify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hh.opengateway.constant.Constant;
import com.hh.opengateway.utils.CheckSignUtil;
import com.hh.urm.notify.model.notify.dto.NotifyServicePushFormV2;
import com.hh.urm.notify.service.BaseService;
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
 * @ClassName: AppService
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/18 15:57
 * @Version: 1.0
 */
@Slf4j
@Service
public class AppService extends BaseService {

    @Value("${notify.app.host}")
    private String host;
    @Value("${notify.app.ak}")
    private String ak;
    @Value("${notify.app.sk}")
    private String sk;
    @Value("${notify.app.url.pushPath}")
    private String pushPathUrl;

    public JSONObject execute(NotifyServicePushFormV2 params) throws Exception {
        JSONObject result = new JSONObject();

        long currentTimeMillis = System.currentTimeMillis();
        String body = JSON.toJSONString(params);
        String signStr = CheckSignUtil.getSign(pushPathUrl, null, body, ak, sk, currentTimeMillis);

        HttpUriRequest request = RequestBuilder.create("POST")
                .setUri(host + pushPathUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader(Constant.ACCESS_KEY_ID, ak)
                .addHeader(Constant.TIME_STAMP, String.valueOf(currentTimeMillis))
                .addHeader(Constant.SIGN, signStr)
                .setEntity(new StringEntity(body, "utf-8"))
                .build();

        String resStr = "{}";
        try (CloseableHttpClient client = HttpUtil.createAllTrustingClient()) {
            resStr = EntityUtils.toString(client.execute(request).getEntity(), "utf-8");
            log.info("app api request: {}, response: {}", body, resStr);
            JSONObject resObj = JSONObject.parseObject(resStr);
            if (!resObj.getOrDefault(CODE, "").equals(SUCCESS_CODE)) {
                getResultMsg(result, "发送失败", FAILED);
            }
        } catch (Exception e) {
            getResultMsg(result, "app通知异常：" + e.getMessage(), FAILED);
            return result;
        }
        result.put("res", JSONObject.parseObject(resStr));
        result.put("url", host + pushPathUrl);
        result.put("body", params);

        getResultMsg(result, "成功", SUCCESS);
        return result;
    }
}
