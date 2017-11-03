package dianshi.matchtrader.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dianshi.matchtrader.R;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class MyAlertDialog {

    Context context;
    AlertDialog alertDialog;


    public MyAlertDialog(Context context) {
        this.context = context;

    }


    private void buildDialog(String title, String message, String messageHight, String positiveBtnStr, String negativeBtnStr, final DialogCallBack dialogCallBack) {

        if (context == null) {
            return;
        }

        //创建对话框builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //将布局文件装入builder对象
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tip, null, false);
        builder.setView(view);


        //点击外部不能关闭
        builder.setCancelable(false);

        alertDialog = builder.create();
        //绑定布局里的控件ID
        Button btn_ensure = (Button) view.findViewById(R.id.btn_dialog_tips_ensure);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_dialog_tips_cancel);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_dialog_tip_title);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_dialog_tip_message);
        TextView tv_message_highLight = (TextView) view.findViewById(R.id.tv_dialog_tip_message_highLight);
        View line_btn_devide = view.findViewById(R.id.line_dialog_tips_btn_devide);

        //控件赋值
        tv_title.setText(title);
        tv_message.setText(message);
        tv_message_highLight.setText(messageHight);


        if (positiveBtnStr == null || positiveBtnStr.equals("")) {
            line_btn_devide.setVisibility(View.GONE);
            btn_ensure.setVisibility(View.GONE);
        }
        if (positiveBtnStr == null || negativeBtnStr.equals("")) {
            line_btn_devide.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        }

        btn_ensure.setText(positiveBtnStr);
        btn_cancel.setText(negativeBtnStr);


        //按钮监听事件
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

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接口-取消方法
                if (dialogCallBack != null) {
                    dialogCallBack.onCancel();
                }

            }
        });

        alertDialog.show();

    }


    /**
     * 提示对话框,默认标题是提示，一个点击按钮，点击关闭对话框
     *
     * @param message 内容
     */
    public void tipDialog(String message) {

        buildDialog("提示", message, "", "确定", "", null);

    }


    /**
     * 提示对话框,默认标题是提示，二个点击按钮，点击关闭对话框
     *
     * @param message 内容
     */
    public void tipDialog(String message, String positiveBtnStr, String negativeBtnStr, DialogCallBack dialogCallBack) {

        buildDialog("提示", message, "", positiveBtnStr, negativeBtnStr, dialogCallBack);

    }

    /**
     * 确认对话框,默认两个点击按钮,标题是提示
     *
     * @param message        内容
     * @param dialogCallBack 按钮点击事件接口回调
     */
    public void ensureDialog(String message, DialogCallBack dialogCallBack) {

        buildDialog("提示", message, "", "确定", "取消", dialogCallBack);

    }


    /**
     * 确认对话框,默认两个点击按钮
     *
     * @param title          对话框标题
     * @param message        内容
     * @param dialogCallBack 按钮点击事件接口回调
     */
    public void ensureDialog(String title, String message, DialogCallBack dialogCallBack) {

        buildDialog(title, message, "", "确定", "取消", dialogCallBack);

    }

    /**
     * 确认对话框，默认两个点击按钮
     *
     * @param title          对话框标题
     * @param message        内容（例如“确认删除”）
     * @param messageHight   重点内容（例如“XX交易”）
     * @param dialogCallBack 按钮点击事件接口回调
     */
    public void ensureDialog(String title, String message, String messageHight, DialogCallBack dialogCallBack) {

        buildDialog(title, message, messageHight, "确定", "取消", dialogCallBack);

    }

    /**
     * 确认对话框
     *
     * @param title          对话框标题
     * @param message        内容（例如“确认删除”）
     * @param messageHight   重点内容（例如“XX交易”）
     * @param positiveBtnStr 左侧按钮的文字
     * @param negativeBtnStr 右侧按钮的文字
     * @param dialogCallBack 按钮点击事件接口回调
     */
    public void ensureDialog(String title, String message, String messageHight, String positiveBtnStr, String negativeBtnStr, DialogCallBack dialogCallBack) {

        buildDialog(title, message, messageHight, positiveBtnStr, negativeBtnStr, dialogCallBack);

    }


    /**
     * 自定义接口
     */
    public interface DialogCallBack {
        public void onEnSure();

        public void onCancel();
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


}
