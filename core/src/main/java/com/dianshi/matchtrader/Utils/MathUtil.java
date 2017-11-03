package com.dianshi.matchtrader.Utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 钱智慧[Email:qzhforthelife@163.com]
 * @date 2014-10-13 下午6:26:28
 */
public class MathUtil {
	/*
	 * BigDecimal使用注意：
	 * 1、用float double无法构造出一个精确的BigDecimal，而String 可以
	 * 2、BigDecimal有些地方跟String很像:它的很多set方法都返回BigDecimal， 
	 * 		必须去接收这 个对象，因为原有对象并未被更改
	 * 3、Scale是小数点后的非零数字位数，比如0.0090，若setScale(2,BigDecimal.ROUND_DOWN)
	 * 则输出0.00
	 * 4、使用setScale的时候，new BigDecimal("12.0510").setScale(2)会抛异常，
	 * 因为没有指定RoundingMode，并且被切掉的数字有非零数字
	 * 5、关于prcise和scale:1/128=0.0078125,其中scale=7 precise=4
	 * 6、直接构造的Decimal也是有scale的，就是实际的小数位数：
	 * new BigDecimal("0.124").scale()为3
	 * 7、默认情况下(不调用可以指定scale和round的函数)BigDecimal的四则运算对Scaler的影响：
	 * add:
	 * 		The scale of the result is the maximum of the scales of the two arguments.
	 * subtract:
	 * 		The scale of the result is the maximum of the scales of the two arguments
	 * multiply: 
	 * 		The scale of the result is the sum of the scales of the two arguments.
	 * divide:
	 * 		The scale of the result is the difference of the scales of this and divisor.
	 *  If the exact result requires more digits, then the scale is adjusted accordingly. 
	 *  For example, 1/128 = 0.0078125 which has a scale of 7 and precision 5.
	 *  	除法是比较特殊的，如果得到的结果是无限循环小数，即除不尽，则会抛异常
	 */
	/**
	 * 多项求和(最终的结果保留2位小数,四舍五入模式：舍去)
	 * @param numList
	 * @return
	 */
	public static String addMany(int scale,int roundingMode,String...numList){
		BigDecimal sum = new BigDecimal(0);
		for (String str : numList) {
			BigDecimal bd = new BigDecimal(str.trim());
			sum = sum.add(bd);
		}
		return sum.setScale(scale, roundingMode).toString();
	}
	
	public static BigDecimal addMany(String...numList ){
		BigDecimal sum = new BigDecimal(0);
		for (String str : numList) {
			BigDecimal bd = new BigDecimal(str.trim());
			sum = sum.add(bd);
		}
		return sum;
	}
	
	/**
	 * 两数求和
	 * @param s1
	 * @param s2
	 * @param scale 保留的小数位数
	 * @param roundingMode 四舍五入模式
	 * @return
	 */
	public static String add(String s1,String s2,int scale,int roundingMode){
		BigDecimal bd1=new BigDecimal(s1.trim());
		BigDecimal bd2=new BigDecimal(s2.trim());
		return bd1.add(bd2).setScale(scale,roundingMode).toString();
	}
	/**
	 * 求差
	 * @param s1
	 * @param s2
	 * @param scale
	 * @param roundingMode
	 * @return
	 */
	public static String subtract(String s1,String s2,int scale,int roundingMode){
		BigDecimal bd1=new BigDecimal(s1.trim());
		BigDecimal bd2=new BigDecimal(s2.trim());
		return bd1.subtract(bd2).setScale(scale,roundingMode).toString();
	}
	
	public static String multiply(String s1,String s2,int scale,int roundingMode){
		BigDecimal bd1=new BigDecimal(s1.trim());
		BigDecimal bd2=new BigDecimal(s2.trim());
		return bd1.multiply(bd2).setScale(scale,roundingMode).toString();
	}
	
	public static String multiMany(int scale,int roundingMode,String... args){
		BigDecimal bd=new BigDecimal(1);
		for(String s:args){
			bd=bd.multiply(new BigDecimal(s.trim()));
		}
		return bd.setScale(scale,roundingMode).toString();
	}
	
	public static BigDecimal multiMany(String...args){
		BigDecimal bd=new BigDecimal(1);
		for(String s:args){
			bd=bd.multiply(new BigDecimal(s.trim()));
		}
		return bd;
	}

	/**
	 * 两数相除
	 * @param s1
	 * @param s2
	 * @param scale 结果保留的小数位数
	 * @param roundingMode 四舍五入模式
	 * @return
	 */
	public static String divide(String s1,String s2,int scale,int roundingMode){
		s1=s1.trim();s2=s2.trim();
		BigDecimal bd1=new BigDecimal(s1);
		BigDecimal bd2=new BigDecimal(s2);
		return bd1.divide(bd2, scale, roundingMode).toString();
	}
	
	
	public static String subtractMany(int scale,int roundingMode,String... strList){
		BigDecimal bd=new BigDecimal(strList[0].trim());
		for(int i=1;i<strList.length;++i){
			bd=bd.subtract(new BigDecimal(strList[i].trim()));
		}
		
		return bd.setScale(scale,roundingMode).toString();
	}
	
	public static BigDecimal add(String s1,String s2){
		s1=s1.trim();s2=s2.trim();
		return new BigDecimal(s1).add(new BigDecimal(s2));
	}
	public static BigDecimal subtract(String s1,String s2){
		s1=s1.trim();s2=s2.trim();
		return new BigDecimal(s1).subtract(new BigDecimal(s2));
	}
	public static BigDecimal multiply(String s1,String s2){
		s1=s1.trim();s2=s2.trim();
		return new BigDecimal(s1).multiply(new BigDecimal(s2));
	}
	public static BigDecimal divide(String s1,String s2){
		//由于除尘的特殊性：除不尽会抛异常，所以这里指定了额外的参数
		return new BigDecimal(s1.trim()).divide(new BigDecimal(s2.trim()),BigDecimal.ROUND_DOWN);
	}
	
	
	/**
	 * 字符串转BigDecimal
	 * @param str decimal字符串
	 * @param scale 指定的小数位数
	 * @param roundingMode 四舍五入模式 BigDecimal.
	 * @return
	 */
	public static BigDecimal strToDecimal(String str,int scale,int roundingMode){
		str=str.trim();
		BigDecimal bd=new BigDecimal(str).setScale(scale,roundingMode);
		return bd;
	}
	
	/**
	 * 字符串转BigDecimal 后的字符串
	 * @param str decimal字符串
	 * @param scale 指定的小数位数
	 * @param roundingMode 四舍五入模式 BigDecimal.
	 * @return
	 */
	public static String strToDecimalStr(String str,int scale,int roundingMode){
		BigDecimal bd=new BigDecimal(str.trim()).setScale(scale,roundingMode);
		return bd.toString();
	}
	
	/**
	 * 将一个小数字符串转成指定小数位的百分比表示形式
	 * 
	 * @param str
	 * @param scale 转成百分比后小数点的位数，如str=0.00123,scale=2,roundingMode=Down,则结果是
	 * 0.12%
	 * @param roundingMode
	 * @return
	 */
	public static String strToPercentStr(String str,int scale,int roundingMode){

		BigDecimal bd=new BigDecimal(str.trim());
		return bd.multiply(new BigDecimal(100)).setScale(scale,roundingMode)+"%";
	}


	/**
	 * 将一个字符串或者Double 转成指定小数位的BigDecimal
	 * @param str
     */
	public static BigDecimal toBigDecimal(String str,int scale){

		BigDecimal bigDecimal=new BigDecimal(str);
		bigDecimal = bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP);
		return bigDecimal;
	}
	/**
	 * 将一个字符串或者Double 转成指定小数位的BigDecimal
	 * @param scale
	 * @return
	 */
	public static BigDecimal toBigDecimal(double number,int scale){

		BigDecimal bigDecimal=BigDecimal.valueOf(number);
		bigDecimal = bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP);
		return bigDecimal;
	}

	/**
	 * 将一个字符串或者Double 转成指定小数位的BigDecimal,指定保留小数的模式
	 * @param number
	 * @param scale
	 * @param roundMode
     * @return
     */
	public static BigDecimal toBigDecimal(double number,int scale,int roundMode){

		BigDecimal bigDecimal=BigDecimal.valueOf(number);
		bigDecimal = bigDecimal.setScale(scale,roundMode);
		return bigDecimal;
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1,double v2,int scale){
		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 判断字符串是否是个数字,包括小数点和负数
	 * @param str
	 * @return
     */
	public static boolean isNumericDecimal(String str) {

		Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 数字如果超过了十万,就转换成以万为单位
	 * 如果超过了亿,就以亿为单位
	 * @param doubleValue
	 * @return
	 */
	public static String numberToW( double doubleValue,int decimalPlace){

		BigDecimal decimal = BigDecimal.valueOf(doubleValue);
		//保留decimalPlace个小数,四舍五入
		decimal = decimal.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		String str =String .valueOf(decimal.toString());
		//如果超过了亿,就转换成以亿为单位
		if( doubleValue > 100000000){
			BigDecimal number =  BigDecimal.valueOf(doubleValue/100000000.0);
			number = number.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			str = number+"亿";
			return str;
		}

		//如果超过了十万,就转换成以万为单位
		if( doubleValue > 100000){
			BigDecimal number =  BigDecimal.valueOf(doubleValue/10000.0);
			number = number.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			str = number+"万";
			return str;
		}

		return str;
	}
}
