package com.hh.urm.notify.controller;

import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.repository.AppTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DemoController
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/4 10:24
 * @Version: 1.0
 */
@Slf4j
@RestController
public class DemoController {

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    @PostMapping("/demo")
    public String demo(@RequestBody JSONObject jsonObject) {
        AppTemplate appTemplate = new AppTemplate();
        appTemplate.setBusinessType(10000005);
        appTemplate.setTitle("20221027");
        appTemplate.setContent("2022102701");
        appTemplate.setjPushType(0);
        appTemplate.setPushTypes("0,1");
        appTemplateRepository.save(appTemplate);
        return "ok";
    }
}
