package com.hh.urm.notify.service.request;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: IService
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/18 15:59
 * @Version: 1.0
 */
public interface IRequest {

    /**
     * 方法执行
     *
     * @param params 请求参数对象
     * @return
     */
    <T> JSONObject execute(T params) throws Exception;
}
