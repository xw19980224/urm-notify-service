package com.hh.urm.notify.service.notify.check.factory.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.req.notify.NotifyReq;
import com.hh.urm.notify.repository.AppTemplateRepository;
import com.hh.urm.notify.service.notify.check.factory.ICheck;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName: AppCheck
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/17 14:18
 * @Version: 1.0
 */
@Component
public class AppCheck implements ICheck {

    @Resource
    private AppTemplateRepository appTemplateRepository;

    @Override
    public void check(NotifyReq notifyReq, NotifyBo notifyBo, JSONObject result) {
        String appCode = notifyReq.getAppCode();
        if (Strings.isNullOrEmpty(appCode)) {
            result.put(NotifyServiceEnums.ONE_APP.getName(), NotifyConst.LACK_TEMPLATE_PARAMS_MSG);
            return;
        }
        AppTemplate appTemplate = appTemplateRepository.findOneByCode(appCode).orElse(null);
        if (Objects.isNull(appTemplate)) {
            result.put("appCode", "oneApp模板不存在");
        } else {
            notifyBo.setAppTemplate(appTemplate);
        }
    }
}
