package com.hh.urm.notify.model.req.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: AppMessageDTO
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/26 17:57
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppReq {

    /**
     * OneApp的用户Id
     */
    private String idmId;
    /**
     * 车辆vin码,当车控通知时必传
     */
    private String vin;
    /**
     * 通知标题参数
     */
    private List<String> titleParams;
    /**
     * 通知内容参数
     */
    private List<String> contentParams;

}
