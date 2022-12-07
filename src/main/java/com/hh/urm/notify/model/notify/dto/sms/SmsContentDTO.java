package com.hh.urm.notify.model.notify.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SmsContentDTO
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/9 16:47
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsContentDTO {
    /**
     * 群发短信接收方的号码
     */
    private String[] to;
    /**
     * 模板Id
     */
    private String templateId;
    /**
     * 短信模板的变量值列表
     */
    private String[] templateParas;
    /**
     * 签名名称
     */
    private String signature;
}
