package com.hh.urm.notify.service.notify;

import com.hh.urm.notify.model.bo.NotifyBo;
import com.hh.urm.notify.utils.base.ServiceResponse;

/**
 * @ClassName: INotifyService
 * @Author: MaxWell
 * @Description: 通知业务层接口
 * @Date: 2022/10/25 15:35
 * @Version: 1.0
 */
public interface INotifyService {

    /**
     * 发送通知
     *
     * @param notifyBo 消息参数 {@link NotifyReq}
     * @return 响应结果
     */
    ServiceResponse<Object> sendMessage(NotifyBo notifyBo);
}
