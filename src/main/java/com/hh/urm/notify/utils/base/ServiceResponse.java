package com.hh.urm.notify.utils.base;

import com.google.common.base.MoreObjects;
import com.hh.urm.notify.enums.NotifyResultEnums;
import com.hh.urm.notify.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import static com.hh.urm.notify.consts.CommonConst.*;

/**
 * 统一的结果集
 */
@SuppressWarnings("unchecked")
public class ServiceResponse<T> implements Serializable {

    private static final long serialVersionUID = -7105469190103583078L;

    /**
     * 成功的code
     */
    public static final int SUCCESS_KEY = NotifyResultEnums.SUCCESS.getIndex();

    /**
     * 失败的code
     */
    public static final int FAIL_KEY = NotifyResultEnums.FAULT.getIndex();

    /**
     * 通用的失败响应
     */
    public static final ServiceResponse<Object> FAIL_RESPONSE = createFailResponse(null, NotifyResultEnums.FAULT.getIndex(), NotifyResultEnums.FAULT.getMsg());

    /**
     * 通用的成功响应
     */
    public static final ServiceResponse<Object> SUCCESS_RESPONSE = createSuccessResponse(null, null);

    /**
     * 本次服务调用的唯一标识
     */
    @Getter
    private String traceId;

    /**
     * 响应时间
     * 格式如2017-04-27 13:42:54
     */
    @Getter
    private String respTime;

    /**
     * 调用结果成功还是失败
     */
    @Getter
    private boolean success;

    /**
     * 响应状态代码
     */
    @Getter
    private int stateCode;

    /**
     * 数据
     */
    @Getter
    private T data;

    /**
     * 响应状态详情信息
     */
    @Getter
    private String stateDesc;

    /**
     * 响应状态详细信息，
     * 必要时可包含错误堆栈
     */
    @Getter
    @Setter
    private String stateDetail;

    private ServiceResponse() {
    }


    /**
     * @param traceId     请求标识
     * @param success     是否成功
     * @param stateCode   响应状态
     * @param data        结果数据
     * @param stateDesc   结果描述
     * @param stateDetail 错误堆栈
     */
    public ServiceResponse(String traceId, boolean success, int stateCode, T data, String stateDesc, String stateDetail) {
        this.traceId = traceId;
        this.respTime = TimeUtil.formatDate(new Date(), TimeUtil.YYYY_MM_DD_HH_MM_SS);
        this.success = success;
        this.stateCode = stateCode;
        this.data = data;
        this.stateDesc = stateDesc;
        this.stateDetail = stateDetail;
    }

    /**
     * @param traceId   请求标识
     * @param success   是否成功
     * @param stateCode 响应状态
     * @param data      结果数据
     * @param stateDesc 结果描述
     */
    public ServiceResponse(String traceId, boolean success, int stateCode, T data, String stateDesc) {
        this.traceId = traceId;
        this.respTime = TimeUtil.formatDate(new Date(), TimeUtil.YYYY_MM_DD_HH_MM_SS);
        this.success = success;
        this.stateCode = stateCode;
        this.data = data;
        this.stateDesc = stateDesc;
    }

    /**
     * 构建成功的响应
     *
     * @param data
     * @return
     */
    public static <T> ServiceResponse<T> createSuccessResponse(String traceId, T data) {
        return new ServiceResponse<T>(traceId, true, SUCCESS_KEY, data, NotifyResultEnums.SUCCESS.getMsg());
    }

    /**
     * 构建成功的响应
     *
     * @param data
     * @param msg
     * @return
     */
    public static <T> ServiceResponse<T> createSuccessResponse(String traceId, T data, String msg) {
        return new ServiceResponse<T>(traceId, true, SUCCESS_KEY, data, msg);
    }

    /**
     * 构建成功的响应
     *
     * @param data
     * @param msg
     * @return
     */
    public static <T> ServiceResponse<T> createSuccessResponse(String traceId) {
        return new ServiceResponse<T>(traceId, true, NotifyResultEnums.SUCCESS.getIndex(), null, NotifyResultEnums.SUCCESS.getMsg());
    }

    /**
     * 构建错误的响应
     *
     * @param code 错误代码
     * @param msg  错误描述
     * @return
     */
    public static <T> ServiceResponse<T> createFailResponse(String traceId, int code, String msg) {
        return new ServiceResponse<T>(traceId, false, code, null, msg);
    }

    /**
     * 构建错误的响应
     *
     * @param data 错误信息
     * @param msg  错误描述
     * @return
     */
    public static <T> ServiceResponse<T> createFailResponse(String traceId, T data, String msg) {
        return new ServiceResponse<T>(traceId, false, FAIL_KEY, data, msg);
    }

    /**
     * 构建错误的响应
     *
     * @param resultMsg 错误描述
     * @return
     */
    public static <T> ServiceResponse<T> createFailResponse(String traceId, NotifyResultEnums resultMsg) {
        return new ServiceResponse<T>(traceId, false, resultMsg.getIndex(), null, resultMsg.getMsg());
    }

    /**
     * 构建错误的响应
     *
     * @param resultMsg 错误描述
     * @return
     */
    public static <T> ServiceResponse<T> createFailResponse(String traceId, String msg) {
        return new ServiceResponse<T>(traceId, false, FAIL_KEY, null, msg);
    }

    /**
     * 构建错误的响应
     *
     * @param code
     * @param data
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServiceResponse<T> createFailResponse(String traceId, int code, T data, String msg) {
        return new ServiceResponse<T>(traceId, false, code, data, msg);
    }

    /**
     * 返回默认的失败响应
     *
     * @param <T>
     * @return
     */
    public static <T> ServiceResponse<T> defaultFailResponse(String traceId) {
        return createFailResponse(traceId, FAIL_KEY, "系统异常，请联系管理员！");
    }

    /**
     * 构建错误的响应
     */
    public static <T> ServiceResponse<T> createFailResponse(String traceId, NotifyResultEnums verifyFailed, T data) {
        return new ServiceResponse<T>(traceId, false, verifyFailed.getIndex(), data, verifyFailed.getMsg());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add(TRACE_ID, this.traceId)
                .add(RESP_TIME, this.respTime)
                .add(SUCCESS, this.success)
                .add(STATE_CODE, this.stateCode)
                .add(DATA, this.data)
                .add(STATE_DESC, this.stateDesc).toString();
    }
}