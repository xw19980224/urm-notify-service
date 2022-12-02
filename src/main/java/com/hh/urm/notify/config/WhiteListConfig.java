package com.hh.urm.notify.config;

import lombok.Data;
import org.assertj.core.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @ClassName: WhiteListConfig
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/12/1 14:57
 * @Version: 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "notify.white-list")
@Data
public class WhiteListConfig {
    private boolean open;
    private String mobiles;
    private String emails;

    /**
     * 白名单校验
     *
     * @param value
     * @return
     */
    public boolean pass(String value,String type) {
        boolean pass = false;
        if (!open) return true;
        if (Strings.isNullOrEmpty(mobiles) && Strings.isNullOrEmpty(emails)) return false;
        if (!Strings.isNullOrEmpty(mobiles)&&) {
            pass = Arrays.asList(mobiles.split(",")).contains(value);
        }
        if (!Strings.isNullOrEmpty(emails)) {
            pass = Arrays.asList(emails.split(",")).contains(value);
        }
        return pass;
    }
}
