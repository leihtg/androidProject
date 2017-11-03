package dianshi.matchtrader.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.ProductModel_out;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.view.MyHScrollView;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class PriceListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ProductModel_out> array;
    ArrayList<String> productChangeIdArray;
    View header;
    int priceRedColor;
    int priceGreenColor;
    int priceGrayColor;


    /**
     * 数据刷新
     *
     * @param array
     * @param productChangeIdArray
     */
    public void updateData(ArrayList<ProductModel_out> array, ArrayList<String> productChangeIdArray) {
        this.array = array;
        this.productChangeIdArray = productChangeIdArray;
        notifyDataSetChanged();

    }


    public PriceListAdapter(Context context, ArrayList<ProductModel_out> array, View header, ArrayList<String> productChangeIdArray) {
        this.context = context;
        this.array = array;
        this.header = header;
        this.productChangeIdArray = productChangeIdArray;


        // 同时兼容高、低版本
        // ContextCompat.getColor(Context context, int id);
        priceRedColor = ContextCompat.getColor(context, R.color.priceRedColor);
        priceGreenColor = ContextCompat.getColor(context, R.color.priceGreenColor);
        priceGrayColor = ContextCompat.getColor(context, R.color.priceGaryColor);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listitem_price_list, parent, false);
        }
        TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_name);
        TextView tv_code = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_code);
        TextView tv_price = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_price);
        TextView tv_twClose = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_twClose);
        TextView tv_up = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_up);
        TextView tv_down = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_down);
        TextView tv_open = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_open);
        TextView tv_top = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_top);
        TextView tv_bottom = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_bottom);
        TextView tv_volume = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_volume);
        TextView tv_turnover_volume = BaseViewHolder.get(convertView, R.id.tv_priceList_listitem_turnover_volume);

        ProductModel_out product = array.get(position);

        String nameStr = product.getName();
        String codeStr = product.getCode();
        double price = product.getPrice();
        double openPrice = product.getOpen();
        double ipoPrice = product.getIPOPrice();
        double tw_price = product.getTW_Close();
        double lastPrice = tw_price == 0 ? ipoPrice : tw_price;
        double maxPrice = product.getMaxPrice();
        double minPrice = product.getMinPrice();
        int allDayCount = product.getDayAllTrade();
        double allMoney = product.getAllMoney();

        double updonwMoney = price != 0 && lastPrice != 0 ? (price - lastPrice) : 0;
        double updownRate = price == 0 || lastPrice == 0 ? 0 : (price - lastPrice) / lastPrice;


        String productIdStr = String.valueOf(product.getId());
        boolean isNewPrice = productChangeIdArray.contains(productIdStr);


        tv_name.setText(nameStr);
        tv_code.setText(codeStr);
        tv_twClose.setText(MathUtil.toBigDecimal(tw_price, 2).toString());
        if (isNewPrice) {
            tv_name.getPaint().setFakeBoldText(true);
        } else {
            tv_name.getPaint().setFakeBoldText(false);
        }
        setTextValue(tv_price, price, lastPrice, isNewPrice, false, true, price, false);
        setTextValue(tv_up, updonwMoney, lastPrice, isNewPrice, false, false, price, false);
        setTextValue(tv_down, updownRate, lastPrice, isNewPrice, true, false, price, false);
        setTextValue(tv_open, openPrice, lastPrice, isNewPrice, false, false, price, false);
        setTextValue(tv_top, maxPrice, lastPrice, isNewPrice, false, true, price, false);
        setTextValue(tv_bottom, minPrice, lastPrice, isNewPrice, false, true, price, false);
        setTextValue(tv_volume, allDayCount, lastPrice, isNewPrice, false, true, price, true);
        setTextValue(tv_turnover_volume, allMoney, lastPrice, isNewPrice, false, false, price, false);

//        tv_volume.setText(String.valueOf(product.getDayAllTrade()));
//        tv_volume.setText(String.valueOf(product.getBuyVolume() + product.getSellVolume()));

        //得到行布局中的横向scrollView
        MyHScrollView scrollView1 = (MyHScrollView) convertView
                .findViewById(R.id.horizontalScrollView1);


        //得到头部的横向scrollView
        MyHScrollView headSrcrollView = (MyHScrollView) header
                .findViewById(R.id.horizontalScrollView1);


        //头部的横向scrollView设置滑动监听
        headSrcrollView
                .AddOnScrollChangedListener(new OnScrollChangedListenerImp(
                        scrollView1));

        return convertView;
    }


    /**
     * 处理获取到的数据
     *
     * @param textView
     * @param price
     * @param lastPrice
     * @param isNew
     * @param isMulti100
     * @param isJadge
     * @param productPrice
     */
    private void setTextValue(TextView textView, double price, double lastPrice, boolean isNew, boolean isMulti100, boolean isJadge, double productPrice, boolean isAmount) {
        int color = getColor(productPrice, lastPrice);
        String textStr = "--";
        if (productPrice > 0) {


            String endStr = "";
            if (isMulti100) {
                price = price * 100;
                lastPrice = lastPrice * 100;
                endStr = "%";
            }

            String priceStr = null;
            if (isAmount) {
                priceStr = MathUtil.toBigDecimal(price, 0).toString();
            } else {
                priceStr = MathUtil.toBigDecimal(price, 2).toString();
            }

            textStr = price == 0 && isJadge ? "--" : (priceStr + endStr);


        }
        textView.setText(textStr);
        textView.setTextColor(color);

        if (isNew) {
            textView.getPaint().setFakeBoldText(true);
        } else {
            textView.getPaint().setFakeBoldText(false);
        }
    }


    /**
     * 根据价钱涨跌情况确定字体颜色
     *
     * @param price
     * @param lastPrice
     * @return
     */
    private int getColor(double price, double lastPrice) {
        int retColor = priceGrayColor;
        if (price > lastPrice) {
            retColor = priceRedColor;
        } else if (price < lastPrice) {
            retColor = priceGreenColor;
        }
        return retColor;
    }

    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<ProductModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }


}


/**
 * 横向scrollView的滑动监听事件
 */
class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
    MyHScrollView mScrollViewArg;

    public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
        mScrollViewArg = scrollViewar;
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        /**
         * 跟随手指的滑动而滑动，这样可以保证滑其中一行，Listview所有的行跟随一起滑动
         */
        mScrollViewArg.smoothScrollTo(l, t);
    }
}
