package com.hh.urm.notify.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hh.urm.notify.consts.CommonConst;
import com.hh.urm.notify.consts.ResultCodeConst;
import com.hh.urm.notify.enmus.NotifyResultEnums;
import com.hh.urm.notify.utils.base.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.TRACE_ID;

/**
 * @ClassName: RequestValidateExceptionHandle
 * @Author: MaxWell
 * @Description: 校验统一处理异常
 * 如果校验不通过，就在异常中处理，统一输出格式
 * @Date: 2022/10/25 15:54
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    /**
     * 校验错误拦截处理
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ServiceResponse<Object> validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(result.getTarget()));
        String traceId = jsonObject.getString(TRACE_ID) == null ? "" : jsonObject.getString(TRACE_ID);
        String message = "";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    log.error("Data check failure : object{" + fieldError.getObjectName() + "},field{" + fieldError.getField() +
                            "},errorMessage{" + fieldError.getDefaultMessage() + "}");

                });
                if (errors.size() > 1) {
                    message = NotifyResultEnums.NO_TRACE_ID.getMsg();
                    Map<String, String> collect = errors.stream()
                            .collect(Collectors
                                    .toMap(
                                            key -> ((FieldError) key).getField(),
                                            value -> value.getDefaultMessage() != null ? value.getDefaultMessage() : "",
                                            (k1, k2) -> k1));
                    return ServiceResponse.createFailResponse(traceId, ResultCodeConst.PARAMETER_ERROR, collect, message);
                } else {
                    FieldError fieldError = (FieldError) errors.get(0);
                    message = fieldError.getDefaultMessage();
                }
            }
        }
        return ServiceResponse.createFailResponse(traceId, ResultCodeConst.PARAMETER_ERROR, message);
    }

    /**
     * 数据绑定失败
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ServiceResponse<Object> handleBindException(HttpServletRequest request, BindException ex) {
        Map<String, String> errors = Maps.newHashMap();
        String serverId = ServletRequestUtils.getStringParameter(request, CommonConst.TRACE_ID, "");
        ex.getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        String msg = ex.getFieldErrors() != null && !ex.getFieldErrors().isEmpty() ? ex.getFieldErrors().get(0).getDefaultMessage() : "";
        log.warn("Parameter binding failed：" + JSON.toJSONString(errors));
        return ServiceResponse.createFailResponse(serverId, ResultCodeConst.PARAMETER_ERROR, errors, msg);

    }

    /**
     * 数据绑定失败
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ServiceResponse<Object> ServletRequestParameterBindException(HttpServletRequest request, MissingServletRequestParameterException ex) {
        Map<String, String> errors = Maps.newHashMap();
        String serverId = ServletRequestUtils.getStringParameter(request, CommonConst.TRACE_ID, "");
        errors.put(ex.getParameterName(), ex.getLocalizedMessage());
        log.warn("ServletRequest Parameter binding failed：" + JSON.toJSONString(errors));
        return ServiceResponse.createFailResponse(serverId, ResultCodeConst.PARAMETER_ERROR, errors, ex.getMessage());

    }


    /**
     * 500 - Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServiceResponse<Object> handleException(HttpServletRequest request, Exception e) {
        String serverId = ServletRequestUtils.getStringParameter(request, CommonConst.TRACE_ID, "");
        log.warn("excption is error!", e);
        return ServiceResponse.defaultFailResponse(serverId);
    }

}