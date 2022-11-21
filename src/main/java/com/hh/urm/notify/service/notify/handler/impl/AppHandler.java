package com.hh.urm.notify.service.notify.handler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.annotation.NotifyService;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.dto.message.AppMessageDTO;
import com.hh.urm.notify.model.dto.notify.NotifyServicePushFormV2;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.req.notify.AppReq;
import com.hh.urm.notify.service.BaseService;
import com.hh.urm.notify.service.notify.handler.INotifyHandler;
import com.hh.urm.notify.service.request.IRequest;
import com.hh.urm.notify.utils.StringHelper;
import com.hh.urm.notify.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Strings;
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.FAILED;

/**
 * @ClassName: AppHandler
 * @Author: MaxWell
 * @Description: OneApp通知
 * @Date: 2022/11/18 9:52
 * @Version: 1.0
 */
@Slf4j
@NotifyService(notifyService = NotifyServiceEnums.ONE_APP)
public class AppHandler extends BaseService implements INotifyHandler {

    @Resource
    private IRequest appService;

    @Override
    public JSONObject handler(String traceId, String dataStr, String config) {
        JSONObject result = new JSONObject();

        List<AppMessageDTO> data = JSONArray.parseArray(dataStr, AppMessageDTO.class);

        AppTemplate appTemplate = JSONObject.parseObject(config, AppTemplate.class);

        // 构建参数
        Pair<Boolean, List<NotifyServicePushFormV2>> booleanListPair = builderMessageParams(data, appTemplate, result);
        if (!booleanListPair.getFirst()) {
            return result;
        }
        for (NotifyServicePushFormV2 notifyServicePushFormV2 : booleanListPair.getSecond()) {
            try {
                JSONObject send = appService.execute(notifyServicePushFormV2);
            } catch (Exception e) {
                log.warn("app request Exception, request:{},Exception message:{}", JSONObject.toJSONString(notifyServicePushFormV2), e.getMessage());
            }
        }

        return null;
    }

    public  Pair<Boolean, List<NotifyServicePushFormV2>> builderMessageParams(List<AppMessageDTO> data, AppTemplate appTemplate, JSONObject result) {
        Integer businessType = appTemplate.getBusinessType();
        if (Objects.isNull(businessType)) {
            getResultMsg(result, "业务类型未必填", FAILED);
            return Pair.of(false, Lists.newArrayList());
        }
        Integer[] pushTypes = Arrays.stream(appTemplate.getPushTypes().split(",")).map(Integer::parseInt).toArray(Integer[]::new);
        if (pushTypes.length == 0) {
            getResultMsg(result, "推送类型必填", FAILED);
            return Pair.of(false, Lists.newArrayList());
        }
        Integer jpushType = appTemplate.getjPushType();
        String imageUrl = appTemplate.getImageUrl();
        String linkUrl = appTemplate.getLinkUrl();
        Integer fullType = appTemplate.getFullType();
        String businessExt = appTemplate.getBusinessExt();
        String popWindowExt = appTemplate.getPopWindowExt();
        String actionTime = TimeUtil.formatYYYYMMDDHHMMSS(new Date());

        List<NotifyServicePushFormV2> collect = data.stream().map(item -> {
            String title = appTemplate.getTitle();
            String content = appTemplate.getNotifyContent();

            NotifyServicePushFormV2 notifyServicePushFormV2 = new NotifyServicePushFormV2();
            notifyServicePushFormV2.setSuperId(item.getIdmId());
            notifyServicePushFormV2.setBusinessType(businessType);
            notifyServicePushFormV2.setPushTypes(pushTypes);
            if (!Objects.isNull(jpushType)) {
                notifyServicePushFormV2.setJpushType(jpushType);
            }
            notifyServicePushFormV2.setActionTime(actionTime);
            if (!Strings.isNullOrEmpty(item.getVin())) {
                notifyServicePushFormV2.setVin(item.getVin());
            }
            if (!Strings.isNullOrEmpty(imageUrl)) {
                notifyServicePushFormV2.setImageUrl(imageUrl);
            }
            if (!Strings.isNullOrEmpty(linkUrl)) {
                notifyServicePushFormV2.setLinkUrl(linkUrl);
            }
            if (!Objects.isNull(fullType)) {
                notifyServicePushFormV2.setFullType(fullType);
            }
            if (!Strings.isNullOrEmpty(businessExt)) {
                notifyServicePushFormV2.setBusinessExt(businessExt);
            }
            if (!Strings.isNullOrEmpty(popWindowExt)) {
                notifyServicePushFormV2.setPopWindowExt(popWindowExt);
            }
            if (!Objects.isNull(item.getTitleParams()) && !item.getTitleParams().isEmpty()) {
                List<String> titleParams = item.getTitleParams();
                title = StringHelper.paramsFill(title, titleParams);
                notifyServicePushFormV2.setTitle(title);
            } else {
                notifyServicePushFormV2.setTitle(title);
            }
            if (!Objects.isNull(item.getContentParams()) && !item.getContentParams().isEmpty()) {
                List<String> contentParams = item.getContentParams();
                content = StringHelper.paramsFill(content, contentParams);
                notifyServicePushFormV2.setContent(content);
            } else {
                notifyServicePushFormV2.setContent(content);
            }

            return notifyServicePushFormV2;
        }).collect(Collectors.toList());


        return Pair.of(true, collect);
    }
}