package com.hh.urm.notify.service.notify;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;

import static com.hh.urm.notify.consts.CommonConst.*;

/**
 * @ClassName: BaseNotifyService
 * @Author: MaxWell
 * @Description: 通知服务基类
 * @Date: 2022/10/31 16:42
 * @Version: 1.0
 */
@Slf4j
public abstract class BaseNotifyService implements IMessage {

    @Override
    public JSONObject sendMessage(JSONObject jsonObject, JSONObject config) {
        String traceId = jsonObject.getString(TRACE_ID);

        JSONObject result = new JSONObject();
        // 1、检查参数
        Boolean checkResult = checkParams(jsonObject, config, result);
        if (!checkResult) {
            return result;
        }

        // 2、构建参数
        Pair<Boolean, JSONObject> buildParamsResult = buildParams(jsonObject, config, result);
        if (!buildParamsResult.getFirst()) {
            return result;
        }
        JSONObject params = buildParamsResult.getSecond();

        // 3、发送消息
        String sendResp = "";
        try {
            sendResp = send(params);
        } catch (Exception e) {
            log.error("traceId:{},消息发送出现异常，请及时处理。Exception Message:{} \n Exception stackTrace:{}", traceId, e.getMessage(), e.getStackTrace());
            sendResp = EXCEPTION;
        }
        log.info("traceId:{},消息发送参数：{},响应结果：{}", traceId, params, sendResp);

        recordHistory(params, sendResp);

        return result;
    }

    /**
     * 规则校验
     *
     * @param jsonObject 动作上报参数
     * @param config     配置文件
     * @param result     校验结果
     * @return 是否校验通过
     */
    protected abstract Boolean checkParams(JSONObject jsonObject, JSONObject config, JSONObject result);

    /**
     * 构建参数
     *
     * @param jsonObject 请求参数
     * @param config     配置文件
     * @param result     失败结果
     * @return Pair 1、是否构建成功 2、参数
     */
    protected abstract Pair<Boolean, JSONObject> buildParams(JSONObject jsonObject, JSONObject config, JSONObject result);

    /**
     * 消息发送
     *
     * @param params 请求参数
     * @return 发送结果
     */
    protected abstract String send(JSONObject params) throws Exception;

    /**
     * 记录发送历史
     *
     * @param params   请求参数
     * @param sendResp 响应参数
     */
    protected abstract void recordHistory(JSONObject params, String sendResp);

    /**
     * 结果封装
     *
     * @param result 返回结果
     * @param msg    描述
     * @param status 状态
     * @return
     */
    protected JSONObject getResultMsg(JSONObject result, String msg, String status) {
        result.put(MESSAGE, msg);
        result.put(STATUS, status);
        return result;
    }
}
