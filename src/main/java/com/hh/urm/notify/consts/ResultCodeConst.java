package com.hh.urm.notify.consts;

import com.hh.urm.notify.utils.base.ServiceResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 返回结果中 code 值的定义
 */
@ApiModel
public final class ResultCodeConst {

    public static final String ERROR_MSG = "服务器处理失败.错误码:%s";
    /**
     * 成功
     */
    @ApiModelProperty("响应成功的code值")
    public static final int SUCCESS = ServiceResponse.SUCCESS_KEY;
    /**
     * 失败
     */
    public static final int FAULT = ServiceResponse.FAIL_KEY;

    /**
     * 需要登录
     */
    @ApiModelProperty("需要登录的code值")
    public static final int LOGIN_REQUIRED = 3;

    /**
     * 提示错误
     */
    public static final int PROMPT_ERROR = 4;

    /**
     * 权限不足
     */
    public static final int NO_PERMISSION = 5;

    /**
     * 账户无效
     */
    public static final int ACCOUNT_NOT_ENABLED = 6;

    /**
     * 需要登录
     */
    public static final int LOGIN_FIRST = 7;

    /**
     * 系统异常
     */
    public static final int SYSTEM_ERROR = 99;

    /**
     * 服务器参数校验未通过
     */
    public static final int PARAMETER_ERROR = 100;


    private ResultCodeConst() {
    }
}

