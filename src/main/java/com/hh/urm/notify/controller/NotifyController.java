package com.hh.urm.notify.controller;

import com.alibaba.fastjson.JSON;
import com.hh.urm.notify.consts.CommonConst;
import com.hh.urm.notify.enmus.NotifyResultEnums;
import com.hh.urm.notify.model.dto.NotifyDTO;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.utils.base.ServiceResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "消息通知接口", notes = "通知接口", httpMethod = "POST")
    @PostMapping("/sendMessage")
    public ServiceResponse<Object> sendMessage(@Validated @RequestBody NotifyDTO notifyDTO) {
        String traceId = notifyDTO.getTraceId();
        log.info("traceId:{}, sendMessage Request Params:{}", traceId, JSON.toJSONString(notifyDTO));

        notifyService.sendMessage(notifyDTO);

        return ServiceResponse.createSuccessResponse(traceId);
    }
}
