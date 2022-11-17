package com.hh.urm.notify.service.notify.convert.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.req.notify.AppMessageReq;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.service.notify.convert.IConvertHandler;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.SUPER_ID;

/**
 * @ClassName: AppConvert
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/17 11:11
 * @Version: 1.0
 */
@Service
public class AppConvert implements IConvertHandler {
    @Override
    public Pair<JSONObject, String> convert(NotifyBo notifyBo, List<NotifyDataReq> data) {
        AppTemplate appTemplate = notifyBo.getAppTemplate();
        List<JSONObject> list = data.stream().map(item -> {
            AppMessageReq appMessage = item.getAppMessage();
            JSONObject tempJsonObject = JSONObject.parseObject(JSONObject.toJSONString(appMessage));
            tempJsonObject.put(SUPER_ID, item.getSuperId());
            return tempJsonObject;
        }).collect(Collectors.toList());

        String dataStr = JSONObject.toJSONString(list);
        JSONObject config = JSONObject.parseObject(JSONObject.toJSONString(appTemplate));

        return Pair.of(config, dataStr);
    }
}
