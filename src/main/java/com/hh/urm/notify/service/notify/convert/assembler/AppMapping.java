package com.hh.urm.notify.service.notify.convert.assembler;

import com.hh.urm.notify.model.dto.message.AppMessageDTO;
import com.hh.urm.notify.model.dto.message.SmsMessageDTO;
import com.hh.urm.notify.model.req.notify.AppReq;
import com.hh.urm.notify.model.req.notify.SmsReq;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @ClassName: AppMapping
 * @Author: MaxWell
 * @Description: 对象转换配置
 * @Date: 2022/11/18 17:20
 * @Version: 1.0
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AppMapping extends IMapping<AppReq, AppMessageDTO> {

    @Override
    AppMessageDTO sourceToTarget(AppReq var1);
}
