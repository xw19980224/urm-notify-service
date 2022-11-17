package com.hh.urm.notify.service.notify.convert.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.model.req.notify.SmsMessageReq;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.service.notify.convert.IConvertHandler;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.SUPER_ID;

/**
 * @ClassName: NotifyConvertHandler
 * @Author: MaxWell
 * @Description: 消息转换适配器
 * @Date: 2022/11/16 9:33
 * @Version: 1.0
 */
@Service
public class SmsConvert implements IConvertHandler {

    @Override
    public Pair<JSONObject, String> convert(NotifyBo notifyBo, List<NotifyDataReq> data) {
        SmsTemplate smsTemplate = notifyBo.getSmsTemplate();
        List<JSONObject> list = data.stream().map(item -> {
            SmsMessageReq smsMessage = item.getSmsMessage();
            JSONObject tempJsonObject = JSONObject.parseObject(JSONObject.toJSONString(smsMessage));
            tempJsonObject.put(SUPER_ID, item.getSuperId());
            return tempJsonObject;
        }).collect(Collectors.toList());

        String dataStr = JSONObject.toJSONString(list);
        JSONObject config = JSONObject.parseObject(JSONObject.toJSONString(smsTemplate));

        return Pair.of(config, dataStr);
    }
}
