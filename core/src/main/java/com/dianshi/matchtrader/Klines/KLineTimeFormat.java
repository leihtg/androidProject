package com.dianshi.matchtrader.Klines;

/**
 * K线的时间格式
 */
public class KLineTimeFormat {

    /**
     * 不同时间周期的k线对应不同的时间格式
     * 1
     * 5
     * 15
     * 30
     * 1
     * 4
     * 日
     * 周
     * 月
     *
     */
    private  static String[] dateFormatArray = {
            "HH:mm",
            "HH:mm",
            "HH:mm",
            "HH:mm",
            "MM/dd HH:mm",
            "MM/dd HH:mm",
            "yy/MM/dd",
            "yy/MM/dd",
            "yy/MM/dd"};

    /**
     * 转换指定的时间格式
     *
     * @param pIndex
     * @return
     */
    public static  String timeFormat(int pIndex) {

        String result = "MM/dd HH:mm";
        if (pIndex >= 0 && pIndex < dateFormatArray.length) {
            result = dateFormatArray[pIndex];
        }
        return result;
    }
}
