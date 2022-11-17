package com.hh.urm.notify.service.notify.check.factory.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.model.req.notify.NotifyReq;
import com.hh.urm.notify.repository.SmsTemplateRepository;
import com.hh.urm.notify.service.notify.check.factory.ICheck;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName: SmsCheck
 * @Author: MaxWell
 * @Description: 短信参数校验
 * @Date: 2022/11/17 14:02
 * @Version: 1.0
 */
@Component
public class SmsCheck implements ICheck {

    @Resource
    private SmsTemplateRepository smsTemplateRepository;

    @Override
    public void check(NotifyReq notifyReq, NotifyBo notifyBo, JSONObject result) {
        String smsCode = notifyReq.getSmsCode();
        if (Strings.isNullOrEmpty(smsCode)) {
            result.put(NotifyServiceEnums.SMS.getName(), NotifyConst.LACK_TEMPLATE_PARAMS_MSG);
            return;
        }
        SmsTemplate smsTemplate = smsTemplateRepository.findOneByCode(smsCode).orElse(null);
        if (Objects.isNull(smsTemplate)) {
            result.put("smsCode", "短信模板不存在");
        } else {
            notifyBo.setSmsTemplate(smsTemplate);
        }
    }
}
