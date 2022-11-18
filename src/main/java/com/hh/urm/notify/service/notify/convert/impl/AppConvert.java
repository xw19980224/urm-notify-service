package com.hh.urm.notify.service.notify.convert.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.model.dto.message.AppMessageDTO;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.req.notify.AppReq;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.service.notify.convert.IConvertHandler;
import com.hh.urm.notify.service.notify.convert.assembler.IMapping;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: AppConvert
 * @Author: MaxWell
 * @Description: App数据转换类
 * @Date: 2022/11/17 11:11
 * @Version: 1.0
 */
@Service
public class AppConvert implements IConvertHandler {

    @Resource
    private IMapping<AppReq, AppMessageDTO> appMapping;

    @Override
    public Pair<String, String> convert(NotifyBo notifyBo, List<NotifyDataReq> data) {
        AppTemplate appTemplate = notifyBo.getAppTemplate();
        List<AppMessageDTO> list = data.stream().map(item -> {
            AppMessageDTO appMessageDTO = appMapping.sourceToTarget(item.getApp());
            appMessageDTO.setSuperId(item.getSuperId());
            return appMessageDTO;
        }).collect(Collectors.toList());

        String dataStr = JSONObject.toJSONString(list);
        String config = JSONObject.toJSONString(appTemplate);

        return Pair.of(config, dataStr);
    }
}
