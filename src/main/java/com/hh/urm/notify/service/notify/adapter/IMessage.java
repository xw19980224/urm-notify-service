package com.hh.urm.notify.service.notify.adapter;

import com.alibaba.fastjson.JSONObject;

import static com.hh.urm.notify.consts.CommonConst.MESSAGE;
import static com.hh.urm.notify.consts.CommonConst.STATUS;

/**
 * @ClassName: IMessage
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 13:34
 * @Version: 1.0
 */
public interface IMessage {

    JSONObject sendMessage(JSONObject jsonObject, JSONObject config);

    /**
     * 结果构造
     *
     * @param result 返回结果
     * @param msg    描述
     * @param status 状态
     */
    default void getResultMsg(JSONObject result, String msg, String status) {
        result.put(MESSAGE, msg);
        result.put(STATUS, status);
    }

}
