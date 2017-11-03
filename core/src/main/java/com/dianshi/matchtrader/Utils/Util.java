package com.dianshi.matchtrader.Utils;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.util.Log;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/4/22.
 */
public class Util {
    public static String MD5(String val){
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(val.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    public static String DictionaryToUrl(ConcurrentHashMap<String,String> map){
        String result = "";
        StringBuilder myBuilder = new StringBuilder();
        for (Map.Entry<String,String> e: map.entrySet()){
            myBuilder.append(e.getKey());
            myBuilder.append("=");
            myBuilder.append(e.getValue());
            myBuilder.append("&");
        }
        if(myBuilder.length()>0){
            myBuilder.deleteCharAt(myBuilder.length()-1);
        }
        result = myBuilder.toString();

        return  result;
    }

    public static int TimeToUnix(Date date){
        int result = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        result = (int)(calendar.getTimeInMillis()/1000);
        return  result;
    }
    public static Date UnixToTime(int time){

        long ltime = ((long)time)*1000;
        Date date = new Date(ltime);

        return date;
    }
}
