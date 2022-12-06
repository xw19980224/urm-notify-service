package com.hh.urm.notify.service.notify.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.annotation.NotifyService;
import com.hh.urm.notify.enums.NotifyServiceEnums;
import com.hh.urm.notify.model.dto.notify.NotifyServicePushFormV2;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.req.notify.NotifyDataReq;
import com.hh.urm.notify.service.BaseService;
import com.hh.urm.notify.service.notify.handler.INotifyHandler;
import com.hh.urm.notify.service.request.notify.AppService;
import com.hh.urm.notify.utils.NotifyException;
import com.hh.urm.notify.utils.ObjectCopyUtils;
import com.hh.urm.notify.utils.StringHelper;
import com.hh.urm.notify.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hh.urm.notify.consts.CommonConst.*;

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
    private AppService appService;

    @Override
    public JSONObject handler(String traceId, List<NotifyDataReq> data, String config) {
        JSONObject result = new JSONObject();

        AppTemplate appTemplate = JSONObject.parseObject(config, AppTemplate.class);

        // 构建参数
        Pair<Boolean, List<NotifyServicePushFormV2>> booleanListPair = builderMessageParams(data, appTemplate, result);
        if (!booleanListPair.getFirst()) {
            return result;
        }
        for (NotifyServicePushFormV2 notifyServicePushFormV2 : booleanListPair.getSecond()) {
            JSONObject send = null;
            try {
                send = appService.execute(notifyServicePushFormV2);
            } catch (Exception e) {
                log.warn("app request Exception, request:{},Exception message:{}", JSONObject.toJSONString(notifyServicePushFormV2), e.getMessage());
                send = new JSONObject();
                send.put(MSG, e.getMessage());
                send.put(STATUS, EXCEPTION);
            }
        }

        return null;
    }

    public Pair<Boolean, List<NotifyServicePushFormV2>> builderMessageParams(List<NotifyDataReq> data, AppTemplate appTemplate, JSONObject result) {
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
            String paramsStr = item.getParams();

            NotifyServicePushFormV2 notifyServicePushFormV2 = new NotifyServicePushFormV2();
            notifyServicePushFormV2.setSuperId(item.getReceiver());
            notifyServicePushFormV2.setBusinessType(businessType);
            notifyServicePushFormV2.setPushTypes(pushTypes);
            if (!Objects.isNull(jpushType)) {
                notifyServicePushFormV2.setJpushType(jpushType);
            }
            notifyServicePushFormV2.setActionTime(actionTime);
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
            notifyServicePushFormV2.setTitle(title);

            if (!Strings.isNullOrEmpty(paramsStr)) {
                List<String> contentParams = Lists.newArrayList(paramsStr.split(","));
                if (!contentParams.isEmpty()) {
                    content = StringHelper.paramsFill(content, contentParams);
                    notifyServicePushFormV2.setContent(content);
                }
            }

            JSONObject extend = item.getExtend();
            if (!Objects.isNull(extend)) {
                templateReplace(extend, notifyServicePushFormV2);
            }

            return notifyServicePushFormV2;
        }).collect(Collectors.toList());

        return Pair.of(true, collect);
    }

    /**
     * 模板信息替换
     *
     * @param replace 拓展字段
     * @param target  通知服务
     */
    private static void templateReplace(JSONObject replace, NotifyServicePushFormV2 target) {
        NotifyServicePushFormV2 source = replace.toJavaObject(NotifyServicePushFormV2.class);
        BeanUtils.copyProperties(source, target, ObjectCopyUtils.getNullProps(source));
    }
}