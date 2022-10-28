package com.hh.urm.notify.service.notify.impl;

import com.hh.urm.notify.model.dto.NotifyDTO;
import com.hh.urm.notify.service.notify.INotifyService;
import com.hh.urm.notify.service.notify.NotifyServiceSupport;
import com.hh.urm.notify.utils.base.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName: NotifyServiceImpl
 * @Author: MaxWell
 * @Description: 通知业务层
 * @Date: 2022/10/25 15:36
 * @Version: 1.0
 */
@Service
public class NotifyServiceImpl extends NotifyServiceSupport implements INotifyService {
    @Override
    public ServiceResponse<Object> sendMessage(NotifyDTO notifyDTO) {

        String notifyType = notifyDTO.getNotifyType();

        Date date = new Date();
        return null;
    }
}
