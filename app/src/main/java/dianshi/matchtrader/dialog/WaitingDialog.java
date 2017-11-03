package dianshi.matchtrader.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import dianshi.matchtrader.R;

/**
 * Created by Administrator on 2016/5/13 0013.
 */
public class WaitingDialog {


    Context context;
    AlertDialog alertDialog;
    ImageView img_outside, img_inside;
    TextView tv_tips;


    public WaitingDialog(Context context) {
//        super(context);
        this.context = context;
        initView();
    }


    public void initView() {

        if (context == null) {
            return;
        }

        //创建对话框builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //将布局文件装入builder对象
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_waiting, null, false);


        img_outside = (ImageView) view.findViewById(R.id.img_WaitingDialog_outside);
        img_inside = (ImageView) view.findViewById(R.id.img_WaitingDialog_inside);
        tv_tips = (TextView) view.findViewById(R.id.tv_WaitingDialog);

        //加载动画
        loading();

        builder.setView(view);


        alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);// 触摸对话框外部不会引起取消
        alertDialog.setCancelable(false);//设置dialog不能被取消

    }

    /**
     * 设置对话框是否能被取消
     *
     * @param enable
     */
    public void setCancelable(boolean enable) {
        alertDialog.setCanceledOnTouchOutside(enable);
        alertDialog.setCancelable(enable);
    }

    public void loading() {
        if (context == null) {
            return;
        }

        final Animation am = AnimationUtils.loadAnimation(context, R.anim.rotate_dialog_waiting_outside);
        final Animation am1 = AnimationUtils.loadAnimation(context, R.anim.rotate_dialog_waiting_inside);
        img_inside.startAnimation(am1);

        img_outside.startAnimation(am);
        am.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                img_outside.startAnimation(am);
            }
        });
        am1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                img_inside.startAnimation(am1);
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
        //加载动画
        loading();
    }

    /**
     * 显示
     */
    public void show(String tips) {

        if (context == null) {
            return;
        }
        if (isShowing()) {
            tv_tips.setText(tips);

        } else {
            tv_tips.setText(tips);
            if (alertDialog != null) {
                alertDialog.show();
            }
            //加载动画
            loading();
        }


    }

    /**
     * 显示
     */
    public void changeTip(String tips) {
        if (context == null) {
            return;
        }
        tv_tips.setText(tips);
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
        if (alertDialog == null) {
            return false;
        }
        return alertDialog.isShowing();
    }

}
