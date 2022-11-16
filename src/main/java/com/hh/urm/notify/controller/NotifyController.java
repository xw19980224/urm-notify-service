package com.hh.urm.notify.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hh.urm.notify.consts.CommonConst;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enmus.NotifyResultEnums;
import com.hh.urm.notify.enmus.NotifyServiceEnums;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.model.req.notify.NotifyReq;
import com.hh.urm.notify.repository.SmsTemplateRepository;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.utils.base.ServiceResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: NotifyController
 * @Description: 消息通知控制层
 * @Date: 2022/10/24 15:02
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping(CommonConst.API_PATH_VERSION_1 + "/notify")
public class NotifyController {

    @Autowired
    private INotifyService notifyService;

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @ApiOperation(value = "消息通知接口", notes = "通知接口", httpMethod = "POST")
    @PostMapping("/sendMessage")
    public ServiceResponse<Object> sendMessage(@Validated @RequestBody NotifyReq notifyReq) {
        String traceId = notifyReq.getTraceId();

        // 1、校验通知类型
        String[] notifyType = notifyReq.getNotifyType();
        Boolean exist = NotifyServiceEnums.checkCodeIsExist(notifyType);
        if (!exist) {
            ServiceResponse.createFailResponse(traceId, NotifyConst.NOTIFY_TYPE_NOT_IN_RULES);
        }

        JSONObject result = new JSONObject();
        // 2、检验通知类型模板参数
        Boolean verifyResult = verifyNotifyTypeParams(notifyReq, result);
        if (!verifyResult) {
            return ServiceResponse.createFailResponse(traceId, result, NotifyResultEnums.FAULT.getMsg());
        }

        // 3、校验Data数据
        List<NotifyDataReq> data = notifyReq.getData();
        if (data == null || data.isEmpty()) {
            ServiceResponse.createFailResponse(traceId, NotifyConst.NO_REQUEST_DATA);
        }

        // 4、校验模板code 封装NotifyBo对象。
        return verifyAndConvert(notifyReq);
    }

    /**
     * 1、校验通知模板参数
     * 2、封装NotifyBo对象 {@link NotifyBo}
     *
     * @param notifyReq 通知DTO
     * @return NotifyBo
     */
    private ServiceResponse<Object> verifyAndConvert(NotifyReq notifyReq) {

        NotifyBo notifyBo = new NotifyBo();
        BeanUtils.copyProperties(notifyReq, notifyBo);

        List<String> notifyType = Lists.newArrayList(notifyBo.getNotifyType());

        JSONObject result = new JSONObject();

        if (notifyType.contains(NotifyServiceEnums.SMS.getCode())) {
            String smsCode = notifyReq.getSmsCode();
            SmsTemplate smsTemplate = smsTemplateRepository.findOneByCode(smsCode).orElse(null);
            if (Objects.isNull(smsTemplate)) {
                result.put("smsCode", "短信模板不存在");
            } else {
                notifyBo.setSmsTemplate(smsTemplate);
            }
        }
        if (!result.isEmpty()){
            return ServiceResponse.createFailResponse(notifyBo.getTraceId(),NotifyResultEnums.VERIFY_FAILED,result);
        }
        return notifyService.sendMessage(notifyBo);
    }

    /**
     * 检验通知模板配置
     *
     * @param notifyReq 请求参数
     * @param result    结果
     * @return 是否通过
     */
    private Boolean verifyNotifyTypeParams(NotifyReq notifyReq, JSONObject result) {

        ArrayList<String> list = Lists.newArrayList(notifyReq.getNotifyType());
        boolean containsAll = list.contains(NotifyServiceEnums.ALL.getCode());
        // 是否通知全部
        if (containsAll) {
            result.put(NotifyServiceEnums.ALL.getName(), NotifyConst.LACK_TEMPLATE_PARAMS_MSG);
            return !Strings.isNullOrEmpty(notifyReq.getSmsCode());
        }
        return list.stream().allMatch(item -> {
            if (NotifyServiceEnums.SMS.getCode().equals(item)) {
                if (Strings.isNullOrEmpty(notifyReq.getSmsCode())) {
                    result.put(NotifyServiceEnums.SMS.getName(), NotifyConst.LACK_TEMPLATE_PARAMS_MSG);
                    return false;
                }
            }
            return true;
        });
    }
}
