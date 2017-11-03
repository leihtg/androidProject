package dianshi.matchtrader.catchexception;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dianshi.matchtrader.Utils.AppManager;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.util.FileUtils;
import dianshi.matchtrader.util.PackageUtils;
import dianshi.matchtrader.util.PhoneUtils;
import dianshi.matchtrader.util.ScreenUtils;
import dianshi.matchtrader.util.StreamTools;

/**
 * 收集手机全局崩溃时的exception,并log到本地
 *
 * @author Jackland_zgl
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos;


    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
        infos = new HashMap<String, String>();
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);


    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {


        //接到异常开始处理
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将用户ID统计登出
            MobclickAgent.onProfileSignOff();
            System.runFinalizersOnExit(true);
            AppManager.getAppManager().AppExit();
        }
    }


    /**
     * 收集异常信息
     *
     * @param exception
     */
    public void collectExceptionInfo(Throwable exception) {
        //递归获取全部的exception信息
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        exception.printStackTrace(printWriter);

        //原因未知或者不存在的时候getCause()为空
        Throwable cause = exception.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);


        infos.put("exceptionInfo", sb.toString());
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo() {

        //获取软件信息
        String versionName = PackageUtils.getVersionName(mContext);
        String versionCode = PackageUtils.getVersionCode(mContext) + "";
        String applicationName = "Android_" + PackageUtils.getApplicationName(mContext);
        infos.put("applicationName", applicationName);
        infos.put("versionName", versionName);
        infos.put("versionCode", versionCode);


        //getDeclaredFields是获取所有申明信息
        StringBuffer sb = new StringBuffer();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                sb.append(field.getName() + " : " + field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }

        //获取手机所有类型信息
        infos.put("deviceInfo", PhoneUtils.getBrand() + " 总内存:" + PhoneUtils.getTotalMemory(mContext) + "可用内存:" + PhoneUtils.getAvailMemory(mContext));


        try {
            //这里获取的是手机imei信息,我写在另一个类里了，参见上篇关于TelephonyManager的博文
            infos.put("imei", PhoneUtils.getDeviceID(mContext));
            //通过WifiManager获取手机MAC地址 只有手机开启wifi才能获取到mac地址
            infos.put("mac", PhoneUtils.getMacAddress(mContext));
            //这个获取的是手机屏幕信息，在另一个类里，就不po文了
            infos.put("screen", ScreenUtils.getScreenHeight(mContext) + "*" + ScreenUtils.getScreenWidth(mContext));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {

        if (ex == null) {
            return false;
        }

        //收集设备参数信息
        collectDeviceInfo();
        //收集异常信息
        collectExceptionInfo(ex);
        //上传错误信息
        uploadErrorInfo();

        //使用Dialog来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                initDialog();
                Looper.loop();
            }
        }.start();

        return true;
    }

    Handler handler;


    /**
     * 初始化对话框
     */
    public void initDialog() {
        Activity currentActivity = AppManager.getAppManager().currentActivity();
        //显示提示对话框
        MyAlertDialog dialog = new MyAlertDialog(currentActivity);
        dialog.ensureDialog("提示", "很抱歉,程序出现了bug,请将错误信息发送给我们,以便来更好的服务于大家.", "", "感激不尽~,3秒后自动退出程序", "", new MyAlertDialog.DialogCallBack() {
            @Override
            public void onEnSure() {
                //将用户ID统计登出
                MobclickAgent.onProfileSignOff();
                System.runFinalizersOnExit(true);

                //退出程序
                AppManager.getAppManager().AppExit();

            }

            @Override
            public void onCancel() {

            }
        });

    }

    boolean isSuccess = false;
    String params = null;

    /**
     * 上传错误信息
     */
    public void uploadErrorInfo() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String content = infos.get("exceptionInfo");
                Bug_in bug_in = new Bug_in();
                bug_in.setSoftType("android移动端");
                bug_in.setUserName(GlobalSingleton.CreateInstance().UserName == null ? "未登录" : GlobalSingleton.CreateInstance().UserName);
                bug_in.setOS(PhoneUtils.getOS());
                bug_in.setDotNet("无");
                bug_in.setSoft(infos.get("applicationName"));
                bug_in.setVersion(infos.get("versionName"));
                bug_in.setResolution(infos.get("screen"));
                bug_in.setMac(infos.get("mac"));
                bug_in.setHardwareInfo(infos.get("deviceInfo"));
                bug_in.setContent(content);


                //URL的最大长度是8192,所以要使用post请求方式,因为参数的长度不会受到限制,否则会出现404错误
                params = SplitParam(bug_in);
                HttpURLConnection conn = null;


                try {
                    URL ur = new URL(APPFinal.URL_BUGSEND);
                    conn = (HttpURLConnection) ur.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("POST");//设置URL请求方法
                    //3.设置post提交内容的类型和长度
                    conn.setRequestProperty("contentType", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));
                    //默认为false
                    conn.setDoOutput(true);
                    //4.向服务器写入数据
                    conn.getOutputStream().write(params.getBytes());

                    //服务器的应答
                    int code = conn.getResponseCode();
                    if (code == 200) {

                        InputStream is = conn.getInputStream(); // 字节流转换成字符串
                        String str = StreamTools.streamToString(is);
                        if (str.equals("success")) {
                            isSuccess = true;
                        }

                    } else {
                        isSuccess = false;
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    e.printStackTrace();
                } finally {

                    //关闭连接
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                    }

                    //如果发送失败,就存储起来,等待下次打开软件的时候上传到服务器
                    if (!isSuccess) {
                        //用于格式化日期,作为日志文件名的一部分
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                        String time = formatter.format(new Date());
                        String fileName = "crash-" + time + ".log";
                        FileUtils.saveFile(params, APPFinal.FILE_BUG, fileName);
                    }
                }


            }
        }).start();


    }


    /**
     * 拼接字符串，以便发送给服务端
     *
     * @param model_in
     * @return
     */
    private String SplitParam(Bug_in model_in) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        //java的反射
        StringBuilder myBuilder = new StringBuilder();
        Class userCla = (Class) model_in.getClass();
        Field[] fs = userCla.getFields();


        for (int i = 0; i < fs.length; i++) {
            try {
                Field f = fs[i];
                f.setAccessible(true);
                Object val = f.get(model_in);
                String name = f.getName();
                //将所有键的名值对放入map中
                if (val != null) {//由于instant Run的勾选,会多出来一个$change的空属性
                    map.put(name, val.toString());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        for (String key : map.keySet()) {
            String value = map.get(key);
            try {
                myBuilder.append(key + "=" + URLEncoder.encode(value, "UTF-8") + "&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }


        return myBuilder.toString();
    }

}