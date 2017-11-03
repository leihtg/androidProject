package dianshi.matchtrader.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/11/29.
 */
public class LengthFilterEditTextView extends EditText {


    public LengthFilterEditTextView(Context context) {
        this(context, null);
    }

    public LengthFilterEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //加入长度限制
        setFilters(new InputFilter[]{lengthfilter});
    }


    /**
     * 设置想要控制的小数位数
     *
     * @param digit
     */
    public void SetDecimalDigits(int digit) {
        if (digit < 0) {
            return;
        }

        DECIMAL_DIGITS = digit;

    }


    /**
     * 输入框小数的位数
     */
    private static int DECIMAL_DIGITS = 2;
    /**
     * 设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };


}
