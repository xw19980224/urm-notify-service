package com.hh.urm.notify.model.req.notify;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

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

    @NotEmpty
    @ApiModelProperty(value = "superId")
    private String superId;

    @NotEmpty
    @ApiModelProperty(value = "通知人")
    private String notifier;

    @ApiModelProperty(value = "通知参数")
    private JSONObject params;

    @ApiModelProperty(value = "拓展字段")
    private JSONObject replace;

}
