package com.hh.urm.notify.service.notify.check.factory.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.notify.bo.NotifyBo;
import com.hh.urm.notify.model.template.entity.SmsTemplate;
import com.hh.urm.notify.repository.SmsTemplateRepository;
import com.hh.urm.notify.service.notify.check.factory.BaseCheck;
import com.hh.urm.notify.service.notify.check.factory.ICheck;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

import static com.hh.urm.notify.consts.NotifyConst.SMS_CODE;

/**
 * @ClassName: SmsCheck
 * @Author: MaxWell
 * @Description: 短信参数校验
 * @Date: 2022/11/17 14:02
 * @Version: 1.0
 */
@Component
public class SmsCheck extends BaseCheck implements ICheck {

    @Resource
    private SmsTemplateRepository smsTemplateRepository;

    @Override
    public void check(String code, NotifyBo notifyBo, JSONObject result) {
        if (Strings.isNullOrEmpty(code)) {
            result.put(NotifyServiceEnums.SMS.getName(), NotifyConst.LACK_TEMPLATE_PARAMS_MSG);
            return;
        }
        SmsTemplate smsTemplate = smsTemplateRepository.findOneByCode(code).orElse(null);
        if (Objects.isNull(smsTemplate)) {
            result.put(SMS_CODE, "短信模板不存在");
        } else {
            notifyBo.setTemplate(JSONObject.parseObject(JSONObject.toJSONString(smsTemplate)));
        }
        checkDataList(notifyBo, result);
    }
}
