package dianshi.matchtrader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 不同时区对应的时间处理工具类
 *
 * @author HuangWenwei
 * @date 2014年7月31日
 */
public class TimeZoneUtils {

    /**
     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        defaultVaule = TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
        return defaultVaule;
    }

    /**
     * 根据不同时区，转换时间 2014年7月31日
     */
    public static Date transformTime(Date date, TimeZone oldZone,
                                     TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;

    }


    /**
     * 将Unix时间转换成指定时间格式
     *
     * @param unixTime Unix时间
     * @param formats  指定时间格式
     * @return 转换后的时间字符串
     */
    public static String transfromUnix(int unixTime, String formats) {
        if (formats == null || formats.equals("")) {
            formats = "dd/MM/yyyy HH:mm:ss";
        }
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(unixTime * 1000));
        return date;
    }

    /**
     * 将时间字符串转换成指定时间格式
     *
     * @param timeStr Unix时间
     * @param formats 指定时间格式
     * @return 转换后的时间字符串
     */
    public static String transfromTimeStr(String timeStr, String formats) {
        if (formats == null || formats.equals("") || timeStr == null || timeStr.equals("")) {
            return timeStr;
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formats);
        Date date = null;
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;

        }
        String formatDate = sdf.format(date.getTime());
        return formatDate;
    }


    /**
     * 将时间字符串,从现在的时间格式转换为指定的时间格式
     *
     * @param timeStr    时间字符串
     * @param oldFormats
     * @param newFormats
     * @return
     */
    public static String transfromTimeStr(String timeStr, String oldFormats, String newFormats) {
        if (oldFormats == null || newFormats == null || timeStr == null || oldFormats.equals("") || newFormats.equals("") || timeStr.equals("")) {

            return timeStr;
        }
        SimpleDateFormat sdf = new java.text.SimpleDateFormat(oldFormats);

        Date date = null;
        try {
            date = sdf.parse(timeStr);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;

        }
        SimpleDateFormat newSdf = new java.text.SimpleDateFormat(oldFormats);
        String formatDate = newSdf.format(date.getTime());
        return formatDate;
    }

}
