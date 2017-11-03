package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.model.TradeMoneyModel;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class TradeMoneyAdapter extends BaseAdapter {

    Context context;
    ArrayList<TradeMoneyModel> array;

    public TradeMoneyAdapter(Context context, ArrayList<TradeMoneyModel> array) {
        this.context = context;
        this.array = array;
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
                    R.layout.listitem_trade_operate_sell, parent, false);
        }
        TextView tv_sellKind = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_listitem_sell_sellKind);
        TextView tv_price = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_listitem_sell_price);
        TextView tv_total = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_listitem_sell_total);


        tv_sellKind.setText(array.get(position).getSellKind());
        tv_price.setText(array.get(position).getPrice());
        tv_total.setText(array.get(position).getTotal());


        return convertView;
    }

    /**
     * 数据更新
     *
     * @param array
     */
    public void updateData(ArrayList<TradeMoneyModel> array) {
        this.array = array;
        notifyDataSetChanged();
    }
}
