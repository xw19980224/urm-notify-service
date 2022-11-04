package com.hh.urm.notify.utils;

import com.alibaba.fastjson.util.IOUtils;
import com.hh.urm.notify.model.bean.HttpResultBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * HttpUtil
 */
@Slf4j
@SuppressWarnings("all")
public class HttpUtil {
    private HttpUtil() {

    }

    public static String get(String url) {
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            log.warn("Exception occur when send http get request!", e);
        }
        // 使用finally块来关闭输入流
        finally {
            IOUtils.close(in);
        }
        return null;
    }


    public static HttpResultBean jsonPost(String strURL, Map<String, Object> params, String token, boolean isCms) {
        HttpResultBean httpResultBean = new HttpResultBean();
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            if (isCms) {
                connection.setRequestProperty("Accept", "application/hal_json"); // 设置接收数据的格式
            } else {
                connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            }
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.setRequestProperty("Authorization", "Basic YWRtaW46MTIzNDU2");
            connection.setRequestProperty("X-CSRF-Token", token);
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(JSONUtil.object2json(params));
            out.flush();
            out.close();
            int code = connection.getResponseCode();
            InputStream is = null;
            if (code == 200) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                httpResultBean.setCode(code);
                httpResultBean.setData(result);
                return httpResultBean;
            }
        } catch (IOException e) {
            log.warn("Exception occur when send http post request!", e);
        }
        httpResultBean.setCode(500);
        httpResultBean.setData("error");
        return httpResultBean;
    }

    public static CloseableHttpClient createAllTrustingClient() {
        CloseableHttpClient httpclient = null;
        try {
            final SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial((TrustStrategy) (X509Certificate[] chain, String authType) -> true);
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build(),
                    NoopHostnameVerifier.INSTANCE);
            httpclient = HttpClients
                    .custom()
                    .setSSLSocketFactory(sslsf)
                    .setMaxConnTotal(1000)
                    .setMaxConnPerRoute(1000)
                    .build();
        } catch (Exception e) {
            log.error("https init error: " + e.getMessage());
        }
        return httpclient;
    }
}
