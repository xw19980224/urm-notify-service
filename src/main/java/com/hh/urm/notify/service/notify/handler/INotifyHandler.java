package com.hh.urm.notify.service.notify.handler;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: IMessage
 * @Author: MaxWell
 * @Description: 消息处理接口
 * @Date: 2022/10/26 13:34
 * @Version: 1.0
 */
public interface INotifyHandler {

    /**
     * @param jsonObject 通知内容
     * @param config     通知配置
     * @return 处理结果
     */
    JSONObject handler(JSONObject jsonObject, JSONObject config);

}
