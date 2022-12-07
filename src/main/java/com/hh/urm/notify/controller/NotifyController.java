package com.hh.urm.notify.controller;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.consts.CommonConst;
import com.hh.urm.notify.consts.NotifyConst;
import com.hh.urm.notify.enums.NotifyResultEnums;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.notify.bo.NotifyBo;
import com.hh.urm.notify.model.notify.req.NotifyDataReq;
import com.hh.urm.notify.model.notify.req.NotifyReq;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.service.notify.check.NotifyParamsCheckFactory;
import com.hh.urm.notify.service.notify.check.factory.ICheck;
import com.hh.urm.notify.utils.base.ServiceResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private NotifyParamsCheckFactory notifyParamsCheckFactory;

    @ApiOperation(value = "消息通知接口", notes = "通知接口", httpMethod = "POST")
    @PostMapping("/send")
    public ServiceResponse<Object> send(@Validated @RequestBody NotifyReq notifyReq) {
        String traceId = notifyReq.getTraceId();

        // 1、校验通知类型
        String notifyType = notifyReq.getNotifyType();
        Boolean exist = NotifyServiceEnums.checkCodeIsExist(notifyType);
        if (!exist) {
            ServiceResponse.createFailResponse(traceId, NotifyConst.NOTIFY_TYPE_NOT_IN_RULES);
        }

        JSONObject result = new JSONObject();

        // 2、校验Data数据
        List<NotifyDataReq> data = notifyReq.getData();
        if (data == null || data.isEmpty()) {
            ServiceResponse.createFailResponse(traceId, NotifyConst.NO_REQUEST_DATA);
        }

        // 3、校验模板code 封装NotifyBo对象。

        NotifyBo notifyBo = new NotifyBo();
        BeanUtils.copyProperties(notifyReq, notifyBo);

        ICheck checkService = notifyParamsCheckFactory.getCheckService(notifyType);
        checkService.check(notifyReq.getTemplateCode(), notifyBo, result);

        if (!result.isEmpty()) {
            return ServiceResponse.createFailResponse(notifyBo.getTraceId(), NotifyResultEnums.VERIFY_FAILED, result);
        }

        return notifyService.notify(notifyBo);

    }

}
