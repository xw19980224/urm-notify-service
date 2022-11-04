package com.hh.urm.notify.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/demo")
    public String demo(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject.toJSONString());
        return "ok";
    }
}
