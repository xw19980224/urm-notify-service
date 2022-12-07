package com.hh.urm.notify.service.notify.check.factory.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.notify.bo.NotifyBo;
import com.hh.urm.notify.model.template.entity.AppTemplate;
import com.hh.urm.notify.repository.AppTemplateRepository;
import com.hh.urm.notify.service.notify.check.factory.BaseCheck;
import com.hh.urm.notify.service.notify.check.factory.ICheck;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

import static com.hh.urm.notify.consts.NotifyConst.*;

/**
 * @ClassName: AppCheck
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/17 14:18
 * @Version: 1.0
 */
@Component
public class AppCheck extends BaseCheck implements ICheck {

    @Resource
    private AppTemplateRepository appTemplateRepository;

    @Override
    public void check(String code, NotifyBo notifyBo, JSONObject result) {
        if (Strings.isNullOrEmpty(code)) {
            result.put(NotifyServiceEnums.ONE_APP.getName(), NotifyConst.LACK_TEMPLATE_PARAMS_MSG);
            return;
        }
        AppTemplate appTemplate = appTemplateRepository.findOneByCode(code).orElse(null);
        if (Objects.isNull(appTemplate)) {
            result.put(APP_CODE, "oneApp模板不存在");
        } else {
            notifyBo.setTemplate(JSONObject.parseObject(JSONObject.toJSONString(appTemplate)));
        }
        checkDataList(notifyBo, result);
    }
}
