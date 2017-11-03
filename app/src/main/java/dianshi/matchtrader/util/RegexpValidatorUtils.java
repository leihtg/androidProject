package dianshi.matchtrader.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证类
 */
public class RegexpValidatorUtils {

    /**
     * 是否符合邮箱格式
     *
     * @param str 待验证的字符串
     * @return 如果是符合邮箱格式的字符串, 返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isEmail(String str) {
        String regex = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
        return match(regex, str);
    }

    /**
     * 是否符合网址格式
     *
     * @param str 待验证的字符串
     * @return 如果是符合网址格式的字符串, 返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isNetUrl(String str) {
        String regex = "(http[s]?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        // String regex = "http://(([a-zA-z0-9]|-){1,}\\\\.){1,}[a-zA-z0-9]{1,}-*" ;
        return match(regex, str);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


}
