package com.hh.urm.notify.service.notify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: IMessage
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 13:34
 * @Version: 1.0
 */
public interface IMessage {

    JSONObject sendMessage(JSONObject jsonObject);

}
