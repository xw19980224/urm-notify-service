package com.hh.urm.notify.model.req.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: SmsMessageDTO
 * @Author: MaxWell
 * @Description: 短信参数
 * @Date: 2022/10/25 17:43
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsMessageReq {
    /**
     * 接收人
     */
    private String receiver;
    /**
     * 短信参数
     */
    private List<String> templateParams;

}
