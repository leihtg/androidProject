package dianshi.matchtrader.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.AppManager;
import com.dianshi.matchtrader.Utils.Util;
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.model.IDModel_in;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.model.RegProductDetailModel_out;
import com.dianshi.matchtrader.product.ProductLoader;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.dianshi.matchtrader.server.HeartBeat;
import com.dianshi.matchtrader.userinfo.UserInfoLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.dialog.WaitingDialog;
import dianshi.matchtrader.model.LoginResultModel_out;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.util.FileUtils;
import dianshi.matchtrader.util.NetWorkUtils;
import dianshi.matchtrader.util.RegexpValidatorUtils;
import dianshi.matchtrader.util.StreamTools;
import dianshi.matchtrader.util.ToastUtils;
import dianshi.matchtrader.util.UpdateManager;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class LoginActivity extends ToolBarActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();
    Context context;
    //等待框
    WaitingDialog waitingDialog;
    //用户名和密码输入编辑框
    EditText edtPassword, edtUsername;
    //用户名
    private String userName;
    //密码
    private String password;
    //存储用户名的文件
    SharedPreferences sharedPreferences;

    //服务器心跳
    HeartBeat heartBeat;


    //记住用户名
    CheckBox checkBox_rememberUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        //禁止默认的页面统计方式，这样将不会再自动统计Activity的界面
        MobclickAgent.openActivityDurationTrack(false);

        //打开调试模式
        MobclickAgent.setDebugMode(true);

        // 检测新版本
        CheckUpdate();

        //初始化控件
        initView();

        //得到设备的识别码,用于友盟的识别设置
        String code = getDeviceInfo(context);


        //上传bug信息
        updateBugInfo();
    }

    /**
     * 上传bug信息
     */
    private void updateBugInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(APPFinal.FILE_BUG);
                if (file.exists()) {
                    ArrayList<String> files = new ArrayList<>();
                    FileUtils.getAllFiles(APPFinal.FILE_BUG, files);
                    if (files != null && files.size() > 0) {

                        String content = FileUtils.readFile(files.get(0));
                        HttpURLConnection conn = null;
                        try {
                            URL ur = new URL(APPFinal.URL_BUGSEND);
                            conn = (HttpURLConnection) ur.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestMethod("POST");//设置URL请求方法
                            //3.设置post提交内容的类型和长度
                            conn.setRequestProperty("contentType", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));
                            //默认为false
                            conn.setDoOutput(true);
                            //4.向服务器写入数据
                            conn.getOutputStream().write(content.getBytes());

                            //服务器的应答
                            int code = conn.getResponseCode();
                            if (code == 200) {

                                InputStream is = conn.getInputStream(); // 字节流转换成字符串
                                String str = StreamTools.streamToString(is);
                                if (str.equals("success")) {

                                    FileUtils.DeleteFile(new File(APPFinal.FILE_BUG));
                                }
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            //关闭连接
                            try {
                                conn.disconnect();
                            } catch (Exception e) {
                            }

                        }
                    }
                }

            }
        }).start();


    }


    /**
     * 检查设备的权限
     *
     * @param context
     * @param permission
     * @return
     */
    @SuppressLint("NewApi")
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 检测权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            UpdateManager.getUpdateManager().checkAppUpdate(activity, false);
        }
    }

    private void CheckUpdate() {
        verifyStoragePermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (permissions.length >= 2) {
                UpdateManager.getUpdateManager().checkAppUpdate((Activity) context, false);

            } else {
                alert("暂无权限下载更新！");
            }
        }
    }

    /**
     * 得到设备的识别码,在做系统集成测试的时候会用到
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * toolbar设置
     *
     * @param toolbar
     */
    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);

        ImageView img_back = (ImageView) findViewById(R.id.img_toolbar_logo);
        img_back.setVisibility(View.GONE);
    }

    /**
     * toolbar标题设置
     *
     * @param tv
     */
    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv.setText(R.string.login_userLogin);
    }


    /**
     * 初始化控件
     */
    public void initView() {

        //绑定控件ID
        Button btn_login = (Button) findViewById(R.id.btn_login_login);
        Button btn_register = (Button) findViewById(R.id.btn_login_register);
        TextView tv_forgetPassword = (TextView) findViewById(R.id.tv_login_forgetPassword);
        checkBox_rememberUserName = (CheckBox) findViewById(R.id.checkbox_login_rememberUsername);
        edtUsername = (EditText) findViewById(R.id.edt_login_username);
        edtPassword = (EditText) findViewById(R.id.edt_login_password);
        CheckBox checkBox_password = (CheckBox) findViewById(R.id.checkbox_login_password);

        //绑定单击监听事件
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        checkBox_rememberUserName.setOnCheckedChangeListener(this);
        checkBox_password.setOnCheckedChangeListener(this);


        //加载等待框初始化
        waitingDialog = new WaitingDialog(LoginActivity.this);


        //取出存储的用户名
        sharedPreferences = getSharedPreferences(APPFinal.ShAERD_FILE_USER, MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "");

        if (!userName.equals("")) {
            edtUsername.setText(userName);
            edtPassword.setFocusable(true);
            checkBox_rememberUserName.setChecked(true);
        } else {
            checkBox_rememberUserName.setChecked(false);
        }


    }

    /**
     * checkbox的监听事件
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.checkbox_login_password:
                if (isChecked) {  //显示密码
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else { //隐藏密码
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
                break;
            case R.id.checkbox_login_rememberUsername:
                if (!isChecked) { //记住用户名
                    sharedPreferences.edit().clear().commit();
                }
                break;
        }

    }


    /**
     * 各类控件的单击监听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_login_forgetPassword://跳转到忘记密码界面
                intent = new Intent(context, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login_register://跳转到注册页面

                //判断网络连接
                if (!NetWorkUtils.isNetworkAvailable(LoginActivity.this)) {
                    alert(getResources().getString(R.string.login_netFail));
                    return;
                }

                if (GlobalSingleton.RefOpenUrl == null || GlobalSingleton.RefOpenUrl.equals("") || !RegexpValidatorUtils.isNetUrl(GlobalSingleton.RefOpenUrl)) {
                    ToastUtils.showToast(LoginActivity.this, "敬请期待~");
                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(GlobalSingleton.RefOpenUrl));
                    startActivity(intent);
                }


                break;
            case R.id.btn_login_login://开始递交给服务器端,
                //判断网络连接
                if (!NetWorkUtils.isNetworkAvailable(LoginActivity.this)) {
                    alert(getResources().getString(R.string.login_netFail));
                    return;
                }
                //得到输入的用户名和密码
                userName = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                //判断是否为空
                if (userName.isEmpty() || userName.trim().equals("")
                        || password.isEmpty() || password.trim().equals("")) {
                    //为空弹出提示对话框
                    alert("请输入用户名和密码!");
                } else {
                    //显示等待框
                    waitingDialog.show();

                    //连接服务器
                    connectServer();

                }

                break;
        }
    }

    /**
     * 连接服务器
     */
    public void connectServer() {

        //开始连接

        byte[] a = null;
        try {
            a = userName.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        globalSingleton.UserName = userName.trim();
        globalSingleton.PasswordMD5 = Util.MD5(password).trim();//密码MD5加密
        globalSingleton.CustomerId = 0;

        globalSingleton.getTCPSingleton().connectFailHandler = ConnectFail;
        globalSingleton.getTCPSingleton().connectSuccHandler = ConnectSucc;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //开始网络连接
                globalSingleton.getTCPSingleton().Connect();
            }
        }).start();
    }


    /**
     * 申请访问登录接口
     */
    private void login() {
        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        FuncCall<ModelInBase, LoginResultModel_out> funcCall = new FuncCall<>();
        funcCall.FuncResultHandler = loginResultHandler;
        funcCall.FuncErrHandler = loginErrHandler;
        funcCall.Call("Login", new ModelInBase(), LoginResultModel_out.class, errorDict);
        if (errorDict.keys().hasMoreElements()) {
            alert("登录失败");
        }


    }

    /**
     * 登录失败
     */
    private Handler loginErrHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            if (GlobalSingleton.CreateInstance().isRestartConnect) {//重连情况下的失败
                alertReconnectFail(errorModel_out.getErrorMsg());
            } else {
                alert("登录失败" + errorModel_out.getErrorMsg());

            }
        }
    };
    /**
     * 登录返回结果
     * {"IsSucc":true,"Msg":"登录成功","CustomerId":430,"CustomerUserName":"dianshi11","TrueName":"111111","SignStr":"852f47e063085e6cf3afff40e1087e74","KLineDownLoadWebSite":"http://114.55.60.3:52002","Timestamp":"55d8670d-68e5-4e8d-83cf-9adf1c50baab"}
     */
    private Handler loginResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LoginResultModel_out result = (LoginResultModel_out) msg.obj;
            if (result.isSucc()) {
                //将用户信息存储到单例类中
                globalSingleton.UserName = result.getCustomerUserName();
                globalSingleton.CustomerId = result.getCustomerId();
                globalSingleton.SignStr = result.getSignStr();
                globalSingleton.TrueName = result.getTrueName();
                globalSingleton.IsLogined = true;
                globalSingleton.IsOnLine = true;
                globalSingleton.KlineDownLoadSite = result.getKLineDownLoadWebSite();

                //存储用户名
                if (checkBox_rememberUserName.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", globalSingleton.UserName);
                    editor.commit();
                }

                //将用户ID传给友盟
                MobclickAgent.onProfileSignIn(result.getCustomerId() + "");
                waitingDialog.changeTip("登录成功,正在加载个人信息...");

                //登录成功后要重新连接一下五档，否则服务端不推送武当信息
                if (GlobalSingleton.CreateInstance().isRestartConnect) {
                    regProduct();

                } else {
                    //加载用户信息
                    UserInfoLoader userInfoLoader = new UserInfoLoader();
                    userInfoLoader.loadSuccHandler = UserInfoLoadSucc;
                    userInfoLoader.load();
                }


            } else {

                if (GlobalSingleton.CreateInstance().isRestartConnect) {
                    alertReconnectFail(result.getMsg());
                } else {
                    alert(result.getMsg());

                }

            }
        }
    };


    /**
     * 网络加载五档
     */
    private void regProduct() {
        //取出存储的商品信息
        SharedPreferences sharedPreferences = getSharedPreferences(APPFinal.ShAERD_FILE_PRODUCT, Context.MODE_PRIVATE);
        int product_id = sharedPreferences.getInt("product_id", -1);
        ProductModel_out productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);

        if (AppManager.getAppManager().currentActivity() instanceof StockDetailActivity) {
            //调用方法
            StockDetailActivity activity = (StockDetailActivity) AppManager.getAppManager().currentActivity();
            activity.initDate();
        }
        if (productModel_out != null) {
            ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
            IDModel_in model_in = new IDModel_in();
            model_in.setId(product_id);
            FuncCall<ModelInBase, RegProductDetailModel_out> funcCall = new FuncCall<>();
            funcCall.FuncResultHandler = regSuccHandler;
            funcCall.FuncErrHandler = ConnectFail;
            funcCall.Call("RegProductDetail", model_in, RegProductDetailModel_out.class, dict);
            return;
        }
        regSuccHandler.obtainMessage().sendToTarget();

    }

    /**
     * 五档加载成功
     */
    private Handler regSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            alert("重连成功");
            GlobalSingleton.CreateInstance().isRestartConnect = false;
        }
    };


    /**
     * 连接成功
     */
    private Handler ConnectSucc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            GlobalSingleton.CreateInstance().CustomerId = 0;
            //访问登录窗口
            login();


        }
    };
    /**
     * 连接失败
     */
    private Handler ConnectFail = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            alertConnect("重新连接", "退出程序");
        }
    };

    /**
     * 产品加载成功
     */
    private Handler ProductLoadSucc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //产品加载成功
            if (msg.arg1 == 1) {
                waitingDialog.dismiss();
                //跳转到主页面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                alert(msg.obj.toString());
            }
        }
    };

    /**
     * 用户加载成功
     */
    private Handler UserInfoLoadSucc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //产品加载成功

            if (msg.arg1 == 1) {
                waitingDialog.changeTip("正在加载商品...");
                //加载产品
                ProductLoader loader = new ProductLoader();
                loader.loadSuccHandler = ProductLoadSucc;
                loader.load();
            } else {
                alert(msg.obj.toString());
            }
        }
    };

    MyAlertDialog dialog;


    /**
     * 对话提示框
     *
     * @param msg
     */
    private void alert(String msg) {

        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }

        Activity currentActivity = AppManager.getAppManager().currentActivity();
        //显示提示对话框
        MyAlertDialog dialog = new MyAlertDialog(currentActivity);
        dialog.tipDialog(msg);
    }

    /**
     * 重连失败的对话框
     *
     * @param msg
     */
    private void alertReconnectFail(String msg) {
        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }

        final Activity currentActivity = AppManager.getAppManager().currentActivity();
        final MyAlertDialog dialog = new MyAlertDialog(currentActivity);

        dialog.tipDialog(msg, "退出程序", "重新登录", new MyAlertDialog.DialogCallBack() {
            @Override
            public void onEnSure() {
                AppManager.getAppManager().AppExit();
            }

            @Override
            public void onCancel() {
                if (currentActivity instanceof LoginActivity) {
                    dialog.dismiss();
                } else {
                    Intent intent = new Intent(currentActivity, LoginActivity.class);
                    startActivity(intent);
                    currentActivity.finish();
                }

            }
        });
    }

    /**
     * 网络断开连接的对话框
     */
    private void alertConnect(String positiveBtnStr, String negativeBtnStr) {

        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }

        //弹出提示对话框
        dialog = new MyAlertDialog(AppManager.getAppManager().currentActivity());
        dialog.tipDialog(getResources().getString(R.string.login_netFail), positiveBtnStr, negativeBtnStr, new MyAlertDialog.DialogCallBack() {
            @Override
            public void onEnSure() {
                //关闭对话框,这个一定要加上的,防止内存泄露
                dialog.dismiss();
                if (waitingDialog == null) {
                    waitingDialog = new WaitingDialog(context);
                    waitingDialog.show("正在重新连接...");
                } else {
                    waitingDialog.changeTip("正在重新连接...");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GlobalSingleton.CreateInstance().isRestartConnect = true;
                        //重新连接
                        globalSingleton.getTCPSingleton().Connect();
                    }
                }).start();

                dialog.dismiss();

            }

            @Override
            public void onCancel() {
                MobclickAgent.onProfileSignOff();
                System.runFinalizersOnExit(true);
                //退出程序
                AppManager.getAppManager().AppExit();
                dialog.dismiss();
            }
        });
    }


    /**
     * 返回键监控 弹出退出提示框
     */
    @Override
    public void onBackPressed() {

        //显示提示对话框
        final MyAlertDialog dialog = new MyAlertDialog(LoginActivity.this);
        dialog.ensureDialog("确定退出吗？", new MyAlertDialog.DialogCallBack() {
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
                //关闭对话框
                dialog.dismiss();
            }
        });


    }


}
