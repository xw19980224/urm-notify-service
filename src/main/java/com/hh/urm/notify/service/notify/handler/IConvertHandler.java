package com.hh.urm.notify.service.notify.handler;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @ClassName: BaseConvertHandler
 * @Author: MaxWell
 * @Description: 发送消息转换处理器
 * @Date: 2022/11/16 9:36
 * @Version: 1.0
 */
public interface IConvertHandler {

    /**
     * 短信消息转换
     *
     * @return Pair 1、短信模板 2、消息内容
     */
    Pair<JSONObject, String> convert(NotifyBo notifyBo, List<NotifyDataReq> data);

}
