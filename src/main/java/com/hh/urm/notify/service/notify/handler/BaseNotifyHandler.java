package com.hh.urm.notify.service.notify.handler;

import com.alibaba.fastjson.JSONObject;

import static com.hh.urm.notify.consts.CommonConst.MESSAGE;
import static com.hh.urm.notify.consts.CommonConst.STATUS;

/**
 * @ClassName: BaseHandler
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/17 17:01
 * @Version: 1.0
 */
public class BaseNotifyHandler {

    /**
     * 结果构造
     *
     * @param result 返回结果
     * @param msg    描述
     * @param status 状态
     */
    public void getResultMsg(JSONObject result, String msg, String status) {
        result.put(MESSAGE, msg);
        result.put(STATUS, status);
    }
}
