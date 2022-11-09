package com.hh.urm.notify.model.bo;

import com.hh.urm.notify.model.dto.NotifyDataDTO;
import com.hh.urm.notify.model.entity.SmsMetadata;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
     * 通知类型：{@link com.hh.urm.notify.enmus.NotifyServiceEnums}通知类型：0、All 1、短信 2、邮箱 3、APP 4、飞书 5、企微 6、其他
     */
    private String[] notifyType;

    /**
     * 短信模板对象
     */
    private SmsMetadata smsMetadata;

    /**
     * 通知内容
     */
    private List<NotifyDataDTO> data;

}
