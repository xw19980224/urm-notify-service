package com.hh.urm.notify.model.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: SmsContentDTO
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/31 16:18
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsDTO {
    /**
     * 短信发送方的号码
     */
    private String from;
    /**
     * 用户的回调地址，用于接收短信状态报告
     */
    private String statusCallback;
    /**
     * 发送的通知短信内容，短信内容不能超过64KB。
     */
    private List<SmsContentDTO> smsContent;
    /**
     * 扩展参数，在状态报告中会原样返回。
     */
    private String extend;
}
