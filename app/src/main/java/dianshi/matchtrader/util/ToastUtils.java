package dianshi.matchtrader.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast消息提示框 防止重复弹出
 */
public class ToastUtils {

    private static Toast mToast;

    public static Toast CreateInstance() {
        return mToast;
    }


    /**
     * 弹出提示消息
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    /**
     * 关闭提示消息
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }


}
