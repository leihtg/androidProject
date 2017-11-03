package com.dianshi.matchtrader.Utils;

import android.app.Activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * Created by Administrator on 2016/5/18.
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }
    /**
     * 判断堆栈是否只有当前的activity了
     */
    public boolean isOnlyCurrentActivity() {

        return activityStack.size() ==1 ;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        if (activityStack == null || activityStack.size() == 0){
            return null;
        }
        Activity activity=activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){

        Activity activity=activityStack.lastElement();
        finishActivity(activity);

    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){

            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                finishActivity(activity);
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        while(activityStack.size()>0){
            Activity activity = activityStack.pop();
            activity.finish();//finish会引起onDestory的调用，而BaseActivity重写了onDestory，其中调用了activityStack的remove
        }
    }
    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * Created by Administrator on 2016/5/10 0010.
     */
    public static class StreamTools {
        /**
         * 将输入流转换成字符串
         *
         * @param is 从网络获取的输入流
         * @return
         */
        public static String streamToString(InputStream is) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                baos.close();
                is.close();
                byte[] byteArray = baos.toByteArray();
                return new String(byteArray, "UTF8");
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 输入流获取转换成字节数组
         *
         * @param inputStream
         * @return
         * @throws Exception
         */
        public static byte[] readInputStream(InputStream inputStream) throws IOException {
            byte[] buffer = new byte[1024];
            int len = -1;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();


            return outputStream.toByteArray();
        }

    }
}

