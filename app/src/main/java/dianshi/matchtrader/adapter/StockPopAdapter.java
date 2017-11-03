package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dianshi.matchtrader.R;

/**
 * K线页面-显示周期和指标的适配器
 *
 * @author DR
 *         Created by Administrator on 2016/5/3 0003.
 */
public class StockPopAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> array;

    public StockPopAdapter(Context context, ArrayList<String> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array == null ? 0 : array.size();
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
                    R.layout.listitem_stock_detail_ppw, null);
        }
        TextView tv_title = BaseViewHolder.get(convertView, R.id.tv_stockDetail_ppw_listitem);
        tv_title.setText(array.get(position));

        if (position == selectedChecked) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.bg_stock_btn_checked));
            tv_title.setTextColor(context.getResources().getColor(R.color.priceRedColor));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.bg_stock_btn_unchecked));
            tv_title.setTextColor(context.getResources().getColor(R.color.white));

        }


        return convertView;
    }

    public void setItemChecked(int position) {
        selectedChecked = position;
    }

    int selectedChecked = -1;
}
