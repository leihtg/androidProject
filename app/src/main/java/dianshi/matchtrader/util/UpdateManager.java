package dianshi.matchtrader.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dianshi.matchtrader.server.GlobalSingleton;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dianshi.matchtrader.R;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.model.UpdateQuery;

/**
 * @author 钱智慧[qzhforthelife@163.com]
 * @date 2014-11-12 下午1:23:40
 */
public class UpdateManager {
    private static final boolean ForceUpdate = true;// 强制下载更新并安装
    public static final String NEW_VERSION_URL = "http://newupdate.lymidas.com/Home/GetUpdate";

    private static UpdateManager updateManager;

    private Context mContext;

    private ProgressBar mPbDownload;
    private TextView mTvDownload;
    private boolean interruptFlag = false;
    private Dialog mDownloadDialog;
    private Dialog mUpdateDialog;
    private UpdateQuery mUpdate;
    public final static int DOWN_NOSDCARD = 0;
    public final static int DOWN_UPDATE = 1;
    public final static int DOWN_OVER = 2;
    public final static int DOWN_HASDOWNLOAD = 3;

    private String apkFileSize;
    private String tmpFileSize;
    private String apkUrl;
    private int progress;
    private String apkFilePath;
    private String savePath;

    public static UpdateManager getUpdateManager() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }
        return updateManager;
    }

    /**
     * 是否是最新版本
     *
     * @param
     * @return
     * @author 钱智慧[qzhforthelife@163.com]
     * @date 2014-11-11 下午3:41:10
     */
    public void checkAppUpdate(Activity context, final Handler handler) {
        mContext = context;
        final Application ac = (Application) mContext.getApplicationContext();
        PackageInfo pkg = null;
        try {
            pkg = ac.getPackageManager().getPackageInfo(ac.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (NameNotFoundException e) {
        }
//		String appName = pkg.applicationInfo.loadLabel(ac.getPackageManager())
//				.toString();
        String appName = GlobalSingleton.CreateInstance().SoftName;
        String version = pkg.versionName;


        final Map<String, String> map = new HashMap<String, String>();
        map.put("projectName", appName);
        map.put("version", version);

        final Handler mGetUpdateInfoHandler = new Handler() {
            Message message = new Message();

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HandlerMsgType.ReceivedData) {
                    String jsonStr = (String) msg.obj;
                    try {
                        mUpdate = new Gson().fromJson(jsonStr, UpdateQuery.class);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    // 3、解析，看是否需要更新
                    if (mUpdate.getIsNeedUpdate()) {
                        message.arg1 = 1;
                    }

                }
                if (handler != null) {
                    handler.sendMessage(message);
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                getWebContent(mGetUpdateInfoHandler, NEW_VERSION_URL, map);
            }
        }.start();


    }


    /**
     * 检查新版本
     *
     * @param
     * @return
     * @author 钱智慧[qzhforthelife@163.com]
     * @date 2014-11-11 下午3:41:10
     */
    public void checkAppUpdate(Activity context, final boolean showTip) {
        mContext = context;
        // 发送：ProjectName=X&Version=1.0
        // 返回：UpdateQuery:{FileList,VersionList,IsNeedUpdate}


        final Application ac = (Application) mContext.getApplicationContext();
        // 2、获得本地程序名称和版本号,远程访问Url获取Json数据
        PackageInfo pkg = null;
        try {
            pkg = ac.getPackageManager().getPackageInfo(ac.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (NameNotFoundException e) {
        }
//		String appName = pkg.applicationInfo.loadLabel(ac.getPackageManager()).toString();
        String appName = GlobalSingleton.CreateInstance().SoftName;
        String version = pkg.versionName;
        final Map<String, String> map = new HashMap<String, String>();
        map.put("projectName", appName);
        map.put("version", version);


        final Handler mGetUpdateInfoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HandlerMsgType.ReceivedData) {
                    String jsonStr = (String) msg.obj;
                    try {
                        mUpdate = new Gson().fromJson(jsonStr, UpdateQuery.class);
                    } catch (Exception e) {
                        UIHelper.ToastMessage(mContext, e.getMessage());
                        return;
                    }
                    // 3、解析，看是否需要更新
                    if (mUpdate.getIsNeedUpdate()) {

                        // 利用Json中的Url下载数据
                        apkUrl = mUpdate.getFileList().get(0).getUrl();
                        if (!ForceUpdate) {
                            // 若需要，弹出对话框，提示更新日志，让用户选择是否需要更新
                            UIHelper.dialogDownloadNotice("更新日志", mUpdate
                                            .getVersionList().get(0).getRemark(),
                                    new FuncWrapper() {
                                        @Override
                                        public void process() {
                                            // 显示下载进度框（带取消）
                                            showDownloadDialog();
                                        }
                                    });
                        } else {
                            // 强制下载更新，并安装
                            showDownloadDialog();
                        }
                    } else if (showTip) {
                        UIHelper.ToastMessage(mContext, "当前已经是最新版本");
                    }
                } else if (msg.what == HandlerMsgType.NetBad) {
                    if (showTip) {
                        UIHelper.ToastMessage(mContext,
                                R.string.network_not_connected);
                    }
                }

            }
        };


        new Thread() {
            @Override
            public void run() {
                getWebContent(mGetUpdateInfoHandler, NEW_VERSION_URL, map);
            }
        }.start();
    }


    /**
     * 拼接网址，请求网络
     *
     * @param handler
     * @param url
     * @param paramMap
     */
    public static void getWebContent(Handler handler, String url,
                                     Map<String, String> paramMap) {

        Message msg = new Message();
        String params = "";
        if (paramMap != null && paramMap.size() > 0) {
            Set<String> keys = paramMap.keySet();
            for (String key : keys) {
                String value = paramMap.get(key);
                try {
                    params += (key + "=" + URLEncoder.encode(value, "UTF-8")) + "&";
                } catch (Exception e) {
                }
            }

            url += ("?" + params);
        }
        HttpURLConnection conn = null;
        try {
            URL ur = new URL(url);
            conn = (HttpURLConnection) ur.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream(); // 字节流转换成字符串
                String str = StreamTools.streamToString(is);
                msg.obj = str;
                msg.what = HandlerMsgType.ReceivedData;
            } else {
                //网络访问失败;
                msg.what = HandlerMsgType.NetBad;
            }
        } catch (Exception e) {
            //网络访问失败;
            msg.what = HandlerMsgType.NetBad;
        } finally {
            try {
                conn.disconnect();
            } catch (Exception e) {
            }
        }
        if (handler != null)
            handler.sendMessage(msg);
    }

    /**
     * 由于下载进度框和下载逻辑过于耦合，所以没有把该函数独立到UIHelper类中
     *
     * @param
     * @return
     * @author 钱智慧[qzhforthelife@163.com]
     * @date 2014-11-12 上午11:32:42
     */
    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
//		builder.setTitle("正在下载新版本");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_update_progress, null);
        mPbDownload = (ProgressBar) v.findViewById(R.id.update_progress);
        mTvDownload = (TextView) v.findViewById(R.id.update_progress_text);

        builder.setView(v);
        if (!ForceUpdate) {
            builder.setNegativeButton("取消", new OnClickListener() {// 响应取消按钮引起的取消
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    interruptFlag = true;
                }
            });
            builder.setOnCancelListener(new OnCancelListener() {// 响应后退按钮引起的取消
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    interruptFlag = true;
                }
            });
        } else {
            builder.setCancelable(false);
        }
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);// 触摸对话框外部不会引起取消
        mDownloadDialog.show();

        new Thread(mDownApkRunnable).start();// 要注意:Android的AlertDialog是非阻塞的
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }

        // 安装之前把Update目录下除了此版本之外的其他版本都删除
        Map<String, String> map = new HashMap<String, String>();
        FileUtils.getAllFiles(savePath, map);
        for (String key : map.keySet()) {
            if (!key.equals(apkFilePath)) {
                File f = new File(key);
                f.delete();
            }
        }

        //安装软件
        //要保证安装完成之后会显示是完成或者打开的按钮界面,不会直接关闭
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private Handler mDownApkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_NOSDCARD:
                    mDownloadDialog.dismiss();
                    UIHelper.ToastMessage(mContext, "无法下载安装文件，请检查SD卡是否挂载");
                    break;
                case DOWN_UPDATE:
                    mPbDownload.setProgress(progress);
                    mTvDownload.setText(tmpFileSize + "/" + apkFileSize);
                    break;
                case DOWN_OVER:
                    if (!ForceUpdate) {
                        mDownloadDialog.dismiss();
                        installApk();
                    } else {
                        // 如果是强制更新，这里显示更新日志
                        mDownloadDialog.dismiss();
                        showUpdateLogDlg();
                    }
                    break;
                case DOWN_HASDOWNLOAD:
                    mDownloadDialog.dismiss();
                    if (ForceUpdate) {
                        showUpdateLogDlg();
                    } else {
                        installApk();
                    }
                    break;
            }

        }
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private Runnable mDownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String apkName = "DianShiTrade_"
                        + mUpdate.getVersionList().get(0).getName() + ".apk";
                String tmpApk = "DianShiTrade_"
                        + mUpdate.getVersionList().get(0).getName() + ".tmp";
                // 判断是否挂载了SD卡
                String tmpFilePath = null;
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    savePath = APPFinal.FILE_UPDATE;
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    apkFilePath = savePath + apkName;
                    tmpFilePath = savePath + tmpApk;
                }

                // 没有挂载SD卡，无法下载文件
                if (apkFilePath == null || apkFilePath == "") {
                    mDownApkHandler.sendEmptyMessage(DOWN_NOSDCARD);
                    return;
                }

                File ApkFile = new File(apkFilePath);


                // 输出临时下载文件
                File tmpFile = new File(tmpFilePath);
                FileOutputStream fos = new FileOutputStream(tmpFile);

                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                // 显示文件大小格式：2个小数点显示
                DecimalFormat df = new DecimalFormat("0.00");
                // 进度条下面显示的总文件大小
                apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 进度条下面显示的当前下载文件大小
                    tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
                    // 当前进度值
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mDownApkHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成 - 将临时下载文件转成APK文件
                        if (tmpFile.renameTo(ApkFile)) {
                            // 通知安装
                            mDownApkHandler.sendEmptyMessage(DOWN_OVER);
                        }
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interruptFlag);// 点击取消就停止下载

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 显示更新日志的dialog
     *
     * @param
     * @return
     * @author 钱智慧[qzhforthelife@163.com]
     * @date 2014-11-16 上午11:18:40
     */
    private void showUpdateLogDlg() {

        Builder builder = new Builder(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_update_log, null);

        TextView tv_versionName = (TextView) v.findViewById(R.id.updateLog_versionName);
        TextView tv_updateLog = (TextView) v.findViewById(R.id.updateLog_log);
        Button btn_install = (Button) v.findViewById(R.id.btn_updateLog_ensure);

        builder.setView(v);

        mUpdateDialog = builder.create();
        mUpdateDialog.setCanceledOnTouchOutside(false);// 触摸对话框外部不会引起取消
        mUpdateDialog.setCancelable(false);//触摸对话框无法取消
        mUpdateDialog.show();

        //新版本名称
        tv_versionName.setText(mUpdate.getVersionList().get(0).getName() + "更新日志");
        //新版本特性日志
        tv_updateLog.setText(mUpdate.getVersionList().get(0).getRemark());
        //安装apk
        btn_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateDialog.dismiss();
                installApk();
            }
        });

    }
}
