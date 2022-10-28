package com.hh.urm.notify.utils.log;

import lombok.Data;

import java.io.Serializable;

/**
 * Log
 */
@Data
public class AliLog implements Serializable {

    //操作描述
    private String description;
    //traceId
    private String traceId;
    //URI
    private String uri;
    //URL
    private String url;
    //请求类型
    private String method;
    //IP地址
    private String ip;
    //根路径
    private String basePath;
    //request
    private String request;
    //response
    private String response;
}
