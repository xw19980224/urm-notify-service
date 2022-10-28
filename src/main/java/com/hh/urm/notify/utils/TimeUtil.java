package com.hh.urm.notify.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 时间相关的工具类
 */
@SuppressWarnings("all")
public class TimeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY = "yyyy";
    private static final long MINUTE = 1000 * 60;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    public static final List<DayOfWeek> WORKING_DAY_LIST = Lists.newArrayList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

    private static final Map<String, FastDateFormat> FAST_DATE_FORMAT_MAP = initFormatMap();

    private TimeUtil() {
    }

    private static Map<String, FastDateFormat> initFormatMap() {
        Map<String, FastDateFormat> formatMap = Maps.newHashMapWithExpectedSize(10);
        formatMap.put(YYYYMM, FastDateFormat.getInstance(YYYYMM));
        formatMap.put(YYYY_MM, FastDateFormat.getInstance(YYYY_MM));
        formatMap.put(YYYYMMDD, FastDateFormat.getInstance(YYYYMMDD));
        formatMap.put(YYYY_MM_DD, FastDateFormat.getInstance(YYYY_MM_DD));
        formatMap.put(YYYY_MM_DD_HH_MM, FastDateFormat.getInstance(YYYY_MM_DD_HH_MM));
        formatMap.put(YYYYMMDDHHMMSS, FastDateFormat.getInstance(YYYYMMDDHHMMSS));
        formatMap.put(YYYY_MM_DD_HH_MM_SS, FastDateFormat.getInstance(YYYY_MM_DD_HH_MM_SS));
        return formatMap;
    }

    /**
     * 根据指定模式获得一个 FastDateFormat
     *
     * @param pattern
     * @return
     */
    public static FastDateFormat getFastDateFormat(String pattern) {
        FastDateFormat fastDateFormat = FAST_DATE_FORMAT_MAP.get(pattern);
        if (fastDateFormat == null) {
            fastDateFormat = FastDateFormat.getInstance(pattern);
        }
        return fastDateFormat;
    }

    /**
     * 取得当前时间
     *
     * @return
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 取得当前时间
     *
     * @return
     */
    public static Long currentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 根据指定模式解析日期
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static final Date parseDate(String dateStr, String pattern) {
        try {
            return DateUtils.parseDate(dateStr, new String[]{pattern});
        } catch (ParseException e) {
            LOGGER.error(String.format("parse date error. %s ， %s", dateStr, pattern), e);
            return null;
        }
    }

    /**
     * 获取格式化后当天
     *
     * @param pattern
     * @return
     */
    public static final String formatToday(String pattern) {
        return formatDate(new Date(), pattern);
    }

    /**
     * 获取格式化后的昨天
     *
     * @param pattern
     * @return
     */
    public static final String formatYesterday(String pattern) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return formatDate(calendar.getTime(), pattern);
    }

    /**
     * 将Date format成指定的模式
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String formatDate(Date date, String pattern) {
        FastDateFormat fastDateFormat = getFastDateFormat(pattern);
        if (fastDateFormat != null) {
            return fastDateFormat.format(date);
        }
        return null;
    }

    /**
     * 格式化成完整时间 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static final String formatYYYYMMDDHHMMSS(Date date) {
        return formatDate(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取某天的起始时间到结束时间
     * <p/>
     * start : 00:00:00
     * end   : 23:59:59
     *
     * @param date
     * @return
     */
    public static Pair<Date, Date> getDayStartAndEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        return Pair.of(startDate, endDate);
    }

    /**
     * 获取指定日期当月的第一天和最后一天
     * <p/>
     * 1.第一天   00:00:00
     * 2.最后一天 23:59:59
     *
     * @param date
     * @return
     */
    public static Pair<Date, Date> getMonthFirstLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取第一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        // 获取最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        return Pair.of(startDate, endDate);
    }

}
