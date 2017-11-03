package dianshi.matchtrader.toolbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dianshi.matchtrader.R;

/**
 * 带ToolBar的activity
 * 默认ToolBar是左边带返回键，中央带一个title
 * 如果想改动默认ToolBar，去ToolBarHelper中改动就可
 * 如果不改动默认Toolbar的情况下，想换toolbar，在继承ToolBarActivity的activity中重写onCreateCustomToolBar，可以自定义相关参数，包括布局
 *
 * @author DR
 */
public abstract class ToolBarActivity extends BaseActivity {
    private ToolBarHelper mToolBarHelper;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {

        //实例化mToolBarHelper，将activity的布局和toolBar的布局连接到一起
        mToolBarHelper = new ToolBarHelper(this, layoutResID);

        //得到Toolbar对象
        toolbar = mToolBarHelper.getToolBar();

        setContentView(mToolBarHelper.getContentView());

        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);

        //设置toolbar的一些参数
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);




        /*toolbar自定义的一些操作,如果需要自定义操作就来重写这个方法*/
        onCreateCustomToolBar(toolbar);
    }

    /**
     * toolbar的参数
     *
     * @param toolbar
     */
    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
        //返回控件绑定ID
        ImageView img = (ImageView) toolbar.findViewById(R.id.img_toolbar_logo);
        LinearLayout layout_back = (LinearLayout) toolbar.findViewById(R.id.layout_toolbar_back);
        //返回控件设置监听事件
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭activity
                finish();
            }
        });

        //标题控件绑定ID
        TextView tv = (TextView) toolbar.findViewById(R.id.txt_toolbar_title);
        //标题控件设置数据
        setTitle(tv);


    }

    public void setTitle(TextView tv) {
    }


}