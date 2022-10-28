package com.hh.urm.notify.model.dto.message;

import java.util.List;

/**
 * @ClassName: SmsMessageDTO
 * @Author: MaxWell
 * @Description: 短信参数
 * @Date: 2022/10/25 17:43
 * @Version: 1.0
 */
public class SmsMessageDTO {
    /**
     * 接收人
     */
    private String toMobile;
    /**
     * 短信参数
     */
    private String[] params;
    /**
     * 短信模板Id
     */
    private String smsTemplateId;
}
