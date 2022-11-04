package com.hh.urm.notify.model.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * CommonResultBean
 */
@ApiModel("HttpResultBean")
@Data
public class HttpResultBean {
    private int code;
    private String msg;
    private Object data;
}
