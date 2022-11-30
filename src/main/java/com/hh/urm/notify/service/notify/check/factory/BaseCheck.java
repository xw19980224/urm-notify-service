package com.hh.urm.notify.service.notify.check.factory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hh.urm.notify.model.bo.NotifyBo;

import java.util.ArrayList;
import java.util.List;

import static com.hh.urm.notify.consts.CommonConst.SUPER_ID;
import static com.hh.urm.notify.consts.NotifyConst.*;

/**
 * @ClassName: BaseCheck
 * @Author: MaxWell
 * @Description: 通用校验类
 * @Date: 2022/11/30 10:06
 * @Version: 1.0
 */
public class BaseCheck {

    protected void checkDataList(NotifyBo notifyBo, JSONObject result) {
        List<JSONObject> errorMapsList = new ArrayList<>();
        notifyBo.getData().forEach(item -> {
            JSONObject jsonObject = new JSONObject();

            String notifier = item.getReceiver();
            if (Strings.isNullOrEmpty(notifier)) {
                jsonObject.put(RECEIVER, false);
            }

            String requestId = item.getRequestId();
            if (Strings.isNullOrEmpty(requestId)) {
                jsonObject.put(REQUEST_ID, false);
            }

            String superId = item.getSuperId();
            if (Strings.isNullOrEmpty(superId)) {
                jsonObject.put(SUPER_ID, false);
            }

            if (!jsonObject.isEmpty()) {
                errorMapsList.add(jsonObject);
            }
        });
        if (!errorMapsList.isEmpty()) {
            result.put(ERROR_MAPS_LIST, errorMapsList);
        }
    }
}
