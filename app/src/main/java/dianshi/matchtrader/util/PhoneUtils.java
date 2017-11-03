package dianshi.matchtrader.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import com.dianshi.matchtrader.Utils.AppManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class PhoneUtils {


    /**
     * 获取手机的GSM手机的IMEI 即 CDMA手机的 MEID.
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //手机串号:GSM手机的IMEI 和 CDMA手机的 MEID.
        String deviceID = telephonyManager.getDeviceId();
        return deviceID;
    }


    /**
     * 获取手机的mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * 获取手机的操作系统信息
     *
     * @return
     */
    public static String getOS() {

        String os = "API"
                + android.os.Build.VERSION.SDK + ",Android"
                + android.os.Build.VERSION.RELEASE;
        return os;

    }

    /**
     * 获取手机的品牌信息
     *
     * @return
     */
    public static String getBrand() {

        String os = "品牌:" + android.os.Build.BRAND + " 型号:" + android.os.Build.MODEL;
        return os;

    }

    /**
     * 获取手机可用内存
     *
     * @param context
     * @return
     */
    public static String getAvailMemory(Context context) {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }


    /**
     * 获取手机总内存
     *
     * @param context
     * @return
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            //如果用int类型会发生溢出情况,所以要转换成long类型
            Integer a = Integer.valueOf(arrayOfString[1]);
            initial_memory = a.longValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte

            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }


}
