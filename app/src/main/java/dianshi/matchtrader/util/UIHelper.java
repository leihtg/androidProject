package dianshi.matchtrader.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dianshi.matchtrader.Utils.AppManager;

import dianshi.matchtrader.R;


/**
 * @author 钱智慧[Email:qzhforthelife@163.com]
 * @date 2014-10-15 上午9:24:20
 */
public class UIHelper {

    public final static int LISTVIEW_DATA_MORE = 0x01;
    public final static int LISTVIEW_DATA_LOADING = 0x02;

    /**
     * 用弹出窗口来模拟popupMenu（因为系统自带的popupMenu设置样式很麻烦）
     *
     * @param context           环境
     * @param upView            环境上边的view（点击了这个view会弹出popupMenu）
     * @param popupMenuLayoutId 菜单布局文件Id
     * @param viewIds           菜单布局文件中所有需要绑定clickListener的组件
     * @param clickListener     菜单项点击Listener
     * @param xoff              水平偏移
     * @param yoff              竖直偏移 一般为0
     * @return
     * @author 钱智慧[Email:qzhforthelife@163.com]
     * @date 2014-10-17 下午2:37:22
     */
    public static PopupWindow showPopupMenu(final Context context, View upView,
                                            int popupMenuLayoutId, int[] viewIds, int xoff, int yoff,
                                            OnClickListener clickListener) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(popupMenuLayoutId, null);
        if (viewIds != null && clickListener != null && viewIds.length > 0) {
            for (int vId : viewIds) {
                View v1 = layout.findViewById(vId);
                if (v1 != null)
                    v1.setOnClickListener(clickListener);
            }
        }
        PopupWindow popupWindow = new PopupWindow(layout);
        popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        upView.getLocationOnScreen(location);
        popupWindow.showAsDropDown(upView, xoff, yoff);
        return popupWindow;
    }


    /**
     * 弹出Toast消息
     *
     * @param
     * @return
     * @author 钱智慧[Email:qzhforthelife@163.com]
     * @date 2014-10-16 上午9:37:42
     */
    public static void ToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context context, int resourceId) {
        Toast.makeText(context, context.getResources().getString(resourceId),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查设备的数据连接，若无连接，就弹出提示
     *
     * @param
     * @return
     * @author 钱智慧[Email:qzhforthelife@163.com]
     * @date 2014-10-22 下午1:46:29
     */
    public static void checkNet(Context context) {
        if (isConnect(context) == false) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.net_not_open_dialog_title)
                    .setMessage(R.string.network_not_connected)
                    .setPositiveButton(R.string.tradeOperate_killOrder_ensure,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    android.os.Process
                                            .killProcess(android.os.Process
                                                    .myPid());
                                    System.exit(0);
                                }
                            }).show();
        }
    }

    /**
     * 网络连接判断
     *
     * @param
     * @return
     * @author 钱智慧[Email:qzhforthelife@163.com]
     * @date 2014-10-22 下午1:44:50
     */
    private static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.v("error", e.toString());
        }
        return false;
    }


    /**
     * 更新提示对话框
     *
     * @param
     * @return
     * @author 钱智慧[qzhforthelife@163.com]
     * @date 2014-11-12 上午11:19:18
     */
    public static void dialogDownloadNotice(String title, String message,
                                            final FuncWrapper fwOk) {
        Context cont = AppManager.getAppManager().currentActivity();
        TextView tv = new TextView(cont);
        tv.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText(message);
        new AlertDialog.Builder(cont)
                .setTitle(title)
                .setView(tv)
                .setPositiveButton("立即更新",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                                if (fwOk != null) {
                                    fwOk.process();
                                }
                            }
                        })
                .setNegativeButton("以后再说",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        }).setCancelable(false).show();
    }

    public static void AlertExit(String msg) {
        new AlertDialog.Builder(AppManager.getAppManager().currentActivity())
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppManager.getAppManager().AppExit();
                    }
                })
                .show();
    }
}
