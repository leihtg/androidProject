package dianshi.matchtrader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {


    /**
     * 默认的日期时间格式(12小时制)
     */
    private static String defaultPattern12 = "yyyy-MM-dd hh:mm:ss";
    /**
     * 默认的日期时间格式(24小时制)(不设置时是默认为24小时制)
     */
    private static String defaultPattern24 = "yyyy-MM-dd HH:mm:ss";


    /**
     * 将时间字符串转换为指定格式的时间字符串
     *
     * @param timeStr
     * @param pattern
     * @return
     */
    public static String formatStr(String timeStr, String pattern) {

        if (timeStr == null || timeStr.equals("")) {
            return null;
        }

        if (pattern == null || pattern.equals("")) {
            pattern = defaultPattern24;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        if (date != null) {
            return format.format(date);
        } else {
            return null;
        }


    }

    /**
     * 比较两个日期字符串的大小
     * -1 是 startDateStr 在 endDateStr之前
     * 0 是二者相等
     * 1 是 startDateStr 在 endDateStr 之后
     * -2 是 执行过程中出现了问题
     *
     * @param startDateStr 日期的字符串
     * @param endDateStr   日期的字符串
     * @param pattern      两个字符串的对应的日期格式
     * @return 返回比较结果
     */
    public static int compareDateStr(String startDateStr, String endDateStr, String pattern) {

        if (startDateStr == null || startDateStr.equals("") || pattern == null || pattern.equals("") || endDateStr == null || endDateStr.equals("")) {
            return -2;
        }

        try {
            Date start = new SimpleDateFormat(pattern, Locale.ENGLISH)
                    .parse(startDateStr);
            Date end = new SimpleDateFormat(pattern, Locale.ENGLISH)
                    .parse(endDateStr);
            return start.compareTo(end);

        } catch (ParseException e) {
            e.printStackTrace();
            return -2;
        }
    }


    /**
     * 比较两个日期字符串的大小
     * -1 是 dateStr 在 当前时间 之前
     * 0 是二者相等
     * 1 是 dateStr 在 当前时间 之后
     * -2 是 执行过程中出现了问题
     *
     * @param dateStr 日期的字符串
     * @param pattern 两个字符串的对应的日期格式
     * @return 返回比较结果
     */
    public static int compareNowDateStr(String dateStr, String pattern) {

        if (dateStr == null || dateStr.equals("") || pattern == null || pattern.equals("")) {
            return -2;
        }

        try {
            Date start = new SimpleDateFormat(pattern, Locale.ENGLISH)
                    .parse(dateStr);
            Date end = new Date();
            return start.compareTo(end);

        } catch (ParseException e) {
            e.printStackTrace();
            return -2;
        }
    }
}
