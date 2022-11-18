package com.hh.urm.notify.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: StringHelper
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/18 14:29
 * @Version: 1.0
 */
public class StringHelper {
    /**
     * 替换模板内容
     *
     * @param content
     * @param map
     * @return
     */
    public static String renderString(String content, Map<String, String> map) {
        Set<Map.Entry<String, String>> sets = map.entrySet();
        for (Map.Entry<String, String> entry : sets) {
            String regex = "\\$\\{" + entry.getKey() + "\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll(entry.getValue());
        }
        return content;
    }

    /**
     * 按顺序获取模板中的变量名称
     *
     * @param soap
     * @return
     */
    public static List<String> getSubUtil(String soap) {
        String regex = "\\$\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(soap);
        List<String> list = new ArrayList<>();
        int i = 1;
        while (matcher.find()) {
            list.add(matcher.group(i));
        }
        return list;
    }

    /**
     * 参数填充
     *
     * @param content 填充内容
     * @param params  填充参数
     * @return
     */
    public static String paramsFill(String content, List<String> params) {
        String regex = "\\{\\d+\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(0));
        }
        for (int i = 0; i < list.size(); i++) {
            content = content.replace("{" + i + "}", params.get(i));
        }
        return content;
    }
}
