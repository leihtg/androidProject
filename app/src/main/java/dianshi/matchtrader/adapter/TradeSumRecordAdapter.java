package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.DoneTotalModel_out;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class TradeSumRecordAdapter extends BaseAdapter {

    Context context;
    List<DoneTotalModel_out> array;

    public TradeSumRecordAdapter(Context context, List<DoneTotalModel_out> array) {
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
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_trade_sum, parent, false);
        }
        TextView tv_name = (TextView) view.findViewById(R.id.tv_trade_sum_listitem_name);
        TextView tv_buyCount = (TextView) view.findViewById(R.id.tv_trade_sum_listitem_buyAmount);
        TextView tv_buyPrice = (TextView) view.findViewById(R.id.tv_trade_sum_listitem_buyAveragePrice);
        TextView tv_sellCount = (TextView) view.findViewById(R.id.tv_trade_sum_listitem_sellAmount);
        TextView tv_sellPrice = (TextView) view.findViewById(R.id.tv_trade_sum_listitem_sellAveragePrice);
        TextView tv_totalTaxRate = (TextView) view.findViewById(R.id.tv_trade_sum_listitem_totalTaxRate);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_trade_sum_listitem_time);

        DoneTotalModel_out item = array.get(position);
        if (item != null) {

            tv_name.setText(item.getProductName());
            tv_buyCount.setText(String.valueOf(item.getBuyCount()));
            tv_sellCount.setText(String.valueOf(item.getSellCount()));

            String timeStr = TimeZoneUtils.transfromTimeStr(item.getCollectDate().replace("T", " "), "yyyy-MM-dd HH:mm:ss");

            tv_time.setText(timeStr);
            tv_totalTaxRate.setText(MathUtil.toBigDecimal(item.getTotalCharge(), 2).toString());
            tv_buyPrice.setText(MathUtil.toBigDecimal(item.getBuyAvgPrice(), 2).toString());
            tv_sellPrice.setText(MathUtil.toBigDecimal(item.getSellAvgPrice(), 2).toString());
        }


        return view;
    }


    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<DoneTotalModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }
}
