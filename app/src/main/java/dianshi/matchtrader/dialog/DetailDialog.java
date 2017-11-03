package dianshi.matchtrader.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dianshi.matchtrader.R;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class DetailDialog {

    Context context;

    AlertDialog alertDialog;
    Button btn_ensure, btn_cancel;
    TextView tv_title;
    TextView tv_key_1, tv_key_2, tv_key_3, tv_key_4, tv_key_5, tv_key_6, tv_key_7, tv_key_8;
    TextView tv_value_1, tv_value_2, tv_value_3, tv_value_4, tv_value_5, tv_value_6, tv_value_7, tv_value_8;

    MyAlertDialog.DialogCallBack dialogCallBack;

    TextView[] keyViews = null;
    TextView[] valueViews = null;

    String[] keyStrs = null;
    String[] valueStrs = null;

    String titleStr, positiveStr, negativeStr;

    /**
     * 构造方法
     *
     * @param context
     */
    public DetailDialog(Context context, String titleStr, String positiveStr, String negativeStr, String[] keyStrs, String[] valueStrs, MyAlertDialog.DialogCallBack dialogCallBack) {
        this.context = context;
        this.keyStrs = keyStrs;
        this.valueStrs = valueStrs;
        this.titleStr = titleStr;
        this.positiveStr = positiveStr;
        this.negativeStr = negativeStr;

        if (dialogCallBack != null) {
            this.dialogCallBack = dialogCallBack;
        }


        initDialog();
        setView(tv_title, btn_ensure, btn_cancel, keyViews, valueViews);
    }


    /**
     * 设置数据
     *
     * @param tv_title
     * @param positiveBtn
     * @param nagativeBtn
     * @param keyViews
     * @param valueViews
     */
    public void setView(TextView tv_title, Button positiveBtn, Button nagativeBtn, TextView[] keyViews, TextView[] valueViews) {
    }

    ;


    /**
     * 初始化对话框控件绑定
     */
    public void initDialog() {

        if (context == null) {
            return;
        }
        if (keyStrs == null || valueStrs == null || keyStrs.length == 0 || valueStrs.length == 0 || keyStrs.length != valueStrs.length) {

            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //将布局文件装入builder对象
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_trade_kill_order, null, false);
        builder.setView(view);
        //创建对话框builder对象
        alertDialog = builder.create();


        //绑定布局里的控件ID
        tv_title = (TextView) view.findViewById(R.id.tv_detail_dialog_title);
        btn_ensure = (Button) view.findViewById(R.id.btn_detail_dialog_ensure);
        btn_cancel = (Button) view.findViewById(R.id.btn_detail_dialog_cancel);
        tv_key_1 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_1);
        tv_key_2 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_2);
        tv_key_3 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_3);
        tv_key_4 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_4);
        tv_key_5 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_5);
        tv_key_6 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_6);
        tv_key_7 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_7);
        tv_key_8 = (TextView) view.findViewById(R.id.tv_detail_dialog_key_8);
        tv_value_1 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_1);
        tv_value_2 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_2);
        tv_value_3 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_3);
        tv_value_4 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_4);
        tv_value_5 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_5);
        tv_value_6 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_6);
        tv_value_7 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_7);
        tv_value_8 = (TextView) view.findViewById(R.id.tv_detail_dialog_value_8);
        View line_btn_devide = view.findViewById(R.id.line_detail_dialog_btn_devide);


        //控件装载数组
        keyViews = new TextView[]{tv_key_1, tv_key_2, tv_key_3, tv_key_4, tv_key_5, tv_key_6, tv_key_7, tv_key_8};
        valueViews = new TextView[]{tv_value_1, tv_value_2, tv_value_3, tv_value_4, tv_value_5, tv_value_6, tv_value_7, tv_value_8};


        //对话框标题
        if (titleStr != null && !titleStr.equals("")) {
            tv_title.setText(titleStr);
        } else {
            tv_title.setText("提示");
        }

        //确定和取消按钮
        if (positiveStr != null && !positiveStr.equals("")) {
            btn_ensure.setText(positiveStr);
        } else {

            btn_ensure.setVisibility(View.GONE);
            line_btn_devide.setVisibility(View.GONE);
        }
        if (negativeStr != null && !negativeStr.equals("")) {
            btn_cancel.setText(negativeStr);
        } else {
            btn_cancel.setVisibility(View.GONE);
            line_btn_devide.setVisibility(View.GONE);
        }

        //参数名称
        if (keyStrs != null) {
            for (int i = 0; i < keyStrs.length; i++) {
                keyViews[i].setText(keyStrs[i]);
                valueViews[i].setText(valueStrs[i]);
            }
        }

        //参数内容
        if (valueStrs != null) {

            for (int i = 0; i < valueStrs.length; i++) {
                keyViews[i].setText(keyStrs[i]);
            }

        }


        //未设置的值隐藏
        int count = Math.min(keyStrs.length, valueStrs.length);
        for (int i = count; i < keyViews.length; i++) {
            keyViews[i].setVisibility(View.GONE);
            valueViews[i].setVisibility(View.GONE);
        }


        //确认按钮
        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接口-确定方法
                if (dialogCallBack != null) {
                    dialogCallBack.onEnSure();
                } else {
                    //关闭dialog
                    alertDialog.dismiss();
                }
            }
        });

        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接口-确定方法
                if (dialogCallBack != null) {
                    dialogCallBack.onCancel();
                } else {
                    //关闭dialog
                    alertDialog.dismiss();
                }
            }
        });
    }


    /**
     * 显示
     */
    public void show() {
        if (alertDialog != null) {
            alertDialog.show();
        }

    }


    /**
     * 关闭
     */
    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /**
     * 关闭
     */
    public boolean isShowing() {
        return alertDialog.isShowing();
    }


}
