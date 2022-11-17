package com.hh.urm.notify.service.notify.handler;

import org.springframework.stereotype.Service;

/**
 * @ClassName: MessageHandOutFactory
 * @Author: MaxWell
 * @Description: 消息分发工厂
 * @Date: 2022/11/17 17:07
 * @Version: 1.0
 */
@Service
public class MessageHandOutFactory extends HandlerConfig {

    /**
     * 获取消息处理器
     *
     * @param notifyType 通知类型 {@link com.hh.urm.notify.enums.NotifyServiceEnums}
     * @return 处理器
     */
    public INotifyHandler getNotifyHandler(String notifyType) {
        return notifyHandlerMap.get(notifyType);
    }
}
