package com.hh.urm.notify.service.notify.handler;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.annotation.NotifyService;
import com.hh.urm.notify.enmus.NotifyServiceEnums;
import com.hh.urm.notify.service.notify.IMessage;


/**
 * @ClassName: SmsServiceImpl
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 10:56
 * @Version: 1.0
 */
@NotifyService(notifyService = NotifyServiceEnums.SMS)
public class SmsServiceImpl implements IMessage {

    @Override
    public JSONObject sendMessage(String params) {
        return null;
    }
}
