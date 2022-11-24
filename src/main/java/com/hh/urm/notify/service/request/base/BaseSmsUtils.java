package com.hh.urm.notify.service.request.base;

import com.hh.urm.notify.service.BaseService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: BaseSmsUtils
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/23 9:39
 * @Version: 1.0
 */
public class BaseSmsUtils {

    private String appKey;
    private String appSecret;

    protected String getXWsse() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        String nonce = UUID.randomUUID().toString().replace("-", "");
        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes());
        return String.format("UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"", appKey, passwordDigestBase64Str, nonce, time);
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
