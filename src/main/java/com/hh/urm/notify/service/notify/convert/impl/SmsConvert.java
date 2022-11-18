package com.hh.urm.notify.service.notify.convert.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.dto.message.SmsMessageDTO;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.model.req.notify.SmsReq;
import com.hh.urm.notify.service.notify.convert.IConvertHandler;
import com.hh.urm.notify.service.notify.convert.assembler.IMapping;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: NotifyConvertHandler
 * @Author: MaxWell
 * @Description: 短信消息转换类
 * @Date: 2022/11/16 9:33
 * @Version: 1.0
 */
@Service
public class SmsConvert implements IConvertHandler {

    @Resource
    private IMapping<SmsReq, SmsMessageDTO> smsMapping;

    @Override
    public Pair<String, String> convert(NotifyBo notifyBo, List<NotifyDataReq> data) {
        SmsTemplate smsTemplate = notifyBo.getSmsTemplate();
        List<SmsMessageDTO> list = data.stream().map(item -> {
            SmsMessageDTO smsMessageDTO = smsMapping.sourceToTarget(item.getSms());
            smsMessageDTO.setSuperId(item.getSuperId());
            return smsMessageDTO;
        }).collect(Collectors.toList());

        String dataStr = JSONObject.toJSONString(list);
        String config = JSONObject.toJSONString(smsTemplate);

        return Pair.of(config, dataStr);
    }
}
