package com.hh.urm.notify.service.notify.handler;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;

import java.util.List;

/**
 * @ClassName: IMessage
 * @Author: MaxWell
 * @Description: 消息处理接口
 * @Date: 2022/10/26 13:34
 * @Version: 1.0
 */
public interface INotifyHandler {

    /**
     * @param traceId 链路Id
     * @param data 通知内容
     * @param config  通知配置
     * @return 处理结果
     */
    JSONObject handler(String traceId, List<NotifyDataReq> data, String config);

}
