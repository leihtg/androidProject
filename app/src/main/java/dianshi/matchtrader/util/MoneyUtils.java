package dianshi.matchtrader.util;

import java.math.BigDecimal;

/**
 * 金额处理类
 */
public class MoneyUtils {

    /**
     * 数字如果超过了十万,就转换成以万为单位
     * 如果超过了亿,就以亿为单位
     *
     * @param doubleValue
     * @return
     */
    public static String numberToW(double doubleValue) {


        String str = String.valueOf(doubleValue);
        //如果超过了亿,就转换成以亿为单位
        if (doubleValue > 100000000) {
            BigDecimal number = BigDecimal.valueOf(doubleValue / 100000000.0);
            number = number.setScale(2, BigDecimal.ROUND_HALF_UP);
            str = number + "亿";
            return str;
        }

        //如果超过了十万,就转换成以万为单位
        if (doubleValue > 100000) {
            BigDecimal number = BigDecimal.valueOf(doubleValue / 10000.0);
            number = number.setScale(2, BigDecimal.ROUND_HALF_UP);
            str = number + "万";
            return str;
        }

        return str;
    }
}
