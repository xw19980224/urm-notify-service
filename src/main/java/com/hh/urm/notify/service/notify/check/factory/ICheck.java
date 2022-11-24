package com.hh.urm.notify.service.notify.check.factory;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.bo.NotifyBo;

/**
 * @ClassName: ICheck
 * @Author: MaxWell
 * @Description: 校验接口
 * @Date: 2022/11/17 13:38
 * @Version: 1.0
 */
public interface ICheck {

    /**
     * 通知参数校验
     *  @param code 请求消息
     * @param notifyBo  封装对象
     * @param result    校验结果
     */
    void check(String code, NotifyBo notifyBo, JSONObject result);
}
