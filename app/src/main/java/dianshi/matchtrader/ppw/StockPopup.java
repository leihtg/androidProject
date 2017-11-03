package dianshi.matchtrader.ppw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.StockPopAdapter;
import dianshi.matchtrader.util.ScreenUtils;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class StockPopup extends PopupWindow {


    Context context;
    View ppwView;
    AdapterView.OnItemClickListener onItemClickListener;
    String[] array;

    /**
     * 构造方法
     *
     * @param context
     */
    public StockPopup(Context context, AdapterView.OnItemClickListener onItemClickListener, String[] array) {
        super(context);

        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.array = array;
        //初始化PopupWindow设置
        initPopup();
        //初始化控件
        initView();

    }

    /**
     * 初始化PopupWindow设置
     */
    public void initPopup() {
        ppwView = LayoutInflater.from(context).inflate(R.layout.ppw_stock_detail_kline, null);
        this.setContentView(ppwView);
        // 窗体添加布局
        this.setContentView(ppwView);
        //将popupwindow的宽度设置为屏幕宽度的1/3
        this.setWidth(ScreenUtils.getScreenWidth(context) / 5);


        // 窗体高自适应
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 弹出窗体可点击
        this.setFocusable(true);
        //点击窗体外面消失
        this.setOutsideTouchable(false);

        // 弹出窗体动画效果
//		this.setAnimationStyle(R.style.PopupBottomAnimation);
//		this.setAnimationStyle(R.anim.push_bottom_out);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        // 设置自适应屏幕
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 初始化控件View
     */
    public void initView() {
        ListView listView = (ListView) ppwView.findViewById(R.id.listView_stockDetail_ppw);

        ArrayList<String> array = new ArrayList<>();

        //添加要要显示的数据
        for (String s : this.array) {
            array.add(s);
        }

        //绑定适配器
        adapter = new StockPopAdapter(context, array);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

    }

    StockPopAdapter adapter;


    int position;

    /**
     * 设置item选中效果
     *
     * @param position
     */
    public void setItemChecked(int position) {
        adapter.setItemChecked(position);
        adapter.notifyDataSetChanged();
        this.position = position;
    }


}
