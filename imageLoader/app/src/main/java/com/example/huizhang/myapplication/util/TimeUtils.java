package com.example.huizhang.myapplication.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_CH_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat CHINESE_DATE_FORMAT_DATE = new SimpleDateFormat("yyyy年MM月dd日");
    // 适用于订单生成的12位时间
    public static final SimpleDateFormat ORDER_CREATE_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmss");

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    public static String getCHTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_CH_DATE_FORMAT);
    }

    public static String getOrderCreateTime(long timeInMillis) {
        return getTime(timeInMillis, ORDER_CREATE_DATE_FORMAT);
    }

    public static String getDate(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 获取天数
     *
     * @return
     */
    public static int getDate(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long time = 0;
        try {
            Date dt1 = df.parse(startDate);
            Date dt2 = df.parse(endDate);
            time = (dt2.getTime() - dt1.getTime()) / (1000 * 3600 * 24);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return (int) time;
    }

    /**
     * 对时间作对比
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(String date1, String date2) {
        DateFormat df = DATE_FORMAT_DATE;
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.before(dt2)) {
                // System.out.println("dt1在dt2前");
                return -1;
            } else if (dt1.after(dt2)) {
                // System.out.println("dt1在dt2后");
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前天数指定的前N天或后N天
     *
     * @param paramDay  传入的某一天
     * @param offsetDay 某一天的后N天或前N天
     * @return
     */
    public static String getDefineDate(String paramDay, int offsetDay) {
        try {
            SimpleDateFormat sdf = TimeUtils.DATE_FORMAT_DATE;
            Date currentDay = sdf.parse(paramDay);
            Calendar calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(currentDay); // 把当前时间赋值给日历
            calendar.add(Calendar.DAY_OF_MONTH, offsetDay); //
            String defineDay = sdf.format(calendar.getTime()); // 格式化当前时间
            paramDay = defineDay;
            return defineDay;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 比较时间
     *
     * @return
     */
    public static boolean getTime(String t1, String t2) {
        Date date1, date2;
        DateFormat formart = new SimpleDateFormat("hh");
        try {
            date1 = formart.parse(t1);
            date2 = formart.parse(t2);
            if (date1.compareTo(date2) < 0) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当前日期是周几
     *
     * @param dt
     * @return 当前日期是周几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
