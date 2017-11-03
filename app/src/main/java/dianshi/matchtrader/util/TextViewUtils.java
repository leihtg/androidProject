package dianshi.matchtrader.util;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class TextViewUtils {

    /**
     * 设置跑马灯效果
     *
     * @param textView
     */
    public static void setTextMarquee(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }
}
