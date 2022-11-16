package com.hh.urm.notify.model.req.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class AppMessageReq {

    /**
     * OneApp的用户Id
     */
    private String idmId;
    /**
     * 车辆vin码,当车控通知时必传
     */
    private String vin;
    /**
     * 通知内容
     */
    private String content;
    /**
     * 通知标题
     */
    private String title;
}
