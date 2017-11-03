package dianshi.matchtrader.ppw;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupWindow;

import com.dianshi.matchtrader.server.GlobalSingleton;
import com.dianshi.matchtrader.model.ProductModel_out;

import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.ProductAutoComplateAdapter;

/**
 * Created by Administrator on 2016/5/8 0008.
 */
public class PriceListPopup extends PopupWindow {

    Context context;
    View ppwView;
    AutoCompleteTextView productAutoComplateTextView;
    ProductModel_out productSelected;

    public PriceListPopup(Context context) {

        this.context = context;

        //初始化PopupWindow设置
        initPopup();

        //初始化popupWindow的控件参数
        initView();

        //藏品的自动补全列表
        initAutoComplate();


    }

    /**
     * 初始化PopupWindow设置
     */
    public void initPopup() {
        ppwView = LayoutInflater.from(context).inflate(R.layout.activity_add_choose, null);
        this.setContentView(ppwView);
        // 窗体添加布局
        this.setContentView(ppwView);
        // 窗体高铺满
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 窗体高自适应
        this.setHeight(LayoutParams.MATCH_PARENT);

        // 弹出窗体可点击
        this.setFocusable(true);
        // 弹出窗体动画效果
//		this.setAnimationStyle(R.style.PopupBottomAnimation);
//		this.setAnimationStyle(R.anim.push_bottom_out);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置自适应屏幕
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // ppwView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        ppwView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = ppwView.findViewById(R.id.layout_priceList_ppw).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 藏品的自动补全列表
     */
    public void initAutoComplate() {
        productAutoComplateTextView = (AutoCompleteTextView) ppwView.findViewById(R.id.edt_priceList_ppw_stockCode);
        productAutoComplateTextView.setFocusable(true);
        final List<ProductModel_out> array = GlobalSingleton.CreateInstance().ProductPool.getProduct();
        ProductAutoComplateAdapter adapter = new ProductAutoComplateAdapter(context, array);
        productAutoComplateTextView.setAdapter(adapter);
        productAutoComplateTextView.setThreshold(1);
        productAutoComplateTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productAutoComplateTextView.setText(array.get(position).getName() + "【" + array.get(position).getCode() + "】");
            }
        });

        productAutoComplateTextView.setHint(GlobalSingleton.CreateInstance().ProductTypeName + context.getResources().getString(R.string.chooseList_stockCode));

    }


    /**
     * 初始化popupWindow的控件参数
     */
    public void initView() {


        Button btn_cancel = (Button) ppwView.findViewById(R.id.btn_priceList_ppw_cancel);
        btn_cancel.setOnClickListener(onClickListener);

        Button btn_add = (Button) ppwView.findViewById(R.id.btn_priceList_ppw_add);
        btn_add.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_priceList_ppw_cancel:
                    //关闭ppw
                    dismiss();
                    break;
                case R.id.btn_priceList_ppw_add:
                    //关闭ppw
                    dismiss();
                    break;
            }
        }
    };


}
