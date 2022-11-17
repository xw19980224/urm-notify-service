package com.hh.urm.notify.model.bo;

import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.model.entity.SmsTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: NotifyBo
 * @Author: MaxWell
 * @Description: 通知业务对象
 * @Date: 2022/11/9 9:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotifyBo {

    /**
     * traceId 链路Id
     */
    private String traceId;

    /**
     * 标识
     */
    private String sign;

    /**
     * 通知类型：{@link com.hh.urm.notify.enums.NotifyServiceEnums}通知类型：0、All 1、短信 2、邮箱 3、APP 4、飞书 5、企微 6、其他
     */
    private List<String> notifyType;

    /**
     * 短信模板对象
     */
    private SmsTemplate smsTemplate;

    /**
     * 站内信模板
     */
    private AppTemplate appTemplate;

    /**
     * 通知内容
     */
    private List<NotifyDataReq> data;

}
