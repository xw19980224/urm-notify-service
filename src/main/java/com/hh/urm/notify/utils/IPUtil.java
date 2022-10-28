package com.hh.urm.notify.utils;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理IP相关的工具类
 * <p/>
 */
public final class IPUtil {

    private IPUtil() {
    }

    /**
     * 取得当前用户IP
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        // 根据优先级规则，如果取得一个有效ip，立即返回
        String ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("x-forwarded-for");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * 是否是有效的ip地址
     *
     * @param ip
     * @return
     */
    private static boolean isValidIp(String ip) {
        return (!Strings.isNullOrEmpty(ip)) && (!"unknown".equalsIgnoreCase(ip));
    }


    /**
     * 检查ip 是否在网段内
     *
     * @param ip
     * @param cidr
     * @return
     */
    public static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

}

