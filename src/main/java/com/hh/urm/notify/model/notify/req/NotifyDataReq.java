package com.hh.urm.notify.model.notify.req;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName: NotifyDataDTO
 * @Author: MaxWell
 * @Description: 通知参数
 * @Date: 2022/11/4 14:38
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDataReq {

    @ApiModelProperty(value = "superId")
    private String superId;

    @ApiModelProperty(value = "接收人")
    private String receiver;

    @ApiModelProperty(value = "请求Id")
    private String requestId;

    @ApiModelProperty(value = "通知参数")
    private String params;

    @ApiModelProperty(value = "拓展字段")
    private JSONObject extend;

}
