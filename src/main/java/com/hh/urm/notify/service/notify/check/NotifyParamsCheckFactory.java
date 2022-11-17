package com.hh.urm.notify.service.notify.check;

import com.hh.urm.notify.service.notify.check.factory.ICheck;
import org.springframework.stereotype.Service;

/**
 * @ClassName: NotifyParamsCheckFactory
 * @Author: MaxWell
 * @Description: 通知参数校验工厂, 获取通知校验服务
 * @Date: 2022/11/17 13:58
 * @Version: 1.0
 */
@Service
public class NotifyParamsCheckFactory extends CheckConfig {

    public ICheck getCheckService(String notifyType) {
        return checkMap.get(notifyType);
    }
}
