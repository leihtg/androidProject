package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.DoneDetailModel_out;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * Created by Administrator on 2016/5/14.
 */
public class TradeDetailListAdapter extends BaseAdapter {

    Context context;
    List<DoneDetailModel_out> array;

    public TradeDetailListAdapter(Context context, List<DoneDetailModel_out> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_trade_detail, viewGroup, false);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_trade_datail_listitem_name);
        TextView tv_time_day = (TextView) convertView.findViewById(R.id.tv_trade_datail_listitem_time_day);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_trade_datail_listitem_time);
        TextView tv_tradePrice = (TextView) convertView.findViewById(R.id.tv_trade_datail_listitem_tradePrice);
        TextView tv_tradeAmount = (TextView) convertView.findViewById(R.id.tv_trade_datail_listitem_tradeAmount);
        TextView tv_tradeMoney = (TextView) convertView.findViewById(R.id.tv_trade_datail_listitem_tradeMoney);
        TextView tv_type = (TextView) convertView.findViewById(R.id.tv_trade_datail_listitem_type);


        DoneDetailModel_out item = array.get(i);
        if (item != null) {
            tv_name.setText(item.getProductName());


            String timeStr = TimeZoneUtils.transfromTimeStr(item.getInputtime().replace("T", " "), "yyyy-MM-dd HH:mm:ss");
            String[] times = timeStr.split(",");
            tv_time_day.setText(timeStr);
            tv_time.setVisibility(View.GONE);
//            tv_time_day.setText(times[0]);
//            tv_time.setText(times[2]);

            tv_tradePrice.setText(MathUtil.toBigDecimal(item.getPrice(), 2).toString());
            tv_tradeAmount.setText(String.valueOf(item.getCount()));

            double tradeMoney = Double.valueOf(item.getCount()) * Double.valueOf(item.getPrice());
            tv_tradeMoney.setText(MathUtil.toBigDecimal(tradeMoney, 2).toString());

            if (item.getTradeType().equals("卖")) {
                tv_type.setText(item.getTradeType() + "出");
                tv_type.setTextColor(context.getResources().getColor(R.color.blue));
                tv_tradeMoney.setTextColor(context.getResources().getColor(R.color.blue));
            } else {
                tv_type.setText(item.getTradeType() + "入");
                tv_type.setTextColor(context.getResources().getColor(R.color.priceRedColor));
                tv_tradeMoney.setTextColor(context.getResources().getColor(R.color.priceRedColor));
            }

        }

        return convertView;
    }


    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<DoneDetailModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }

}
