package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.model.TradeKillOrderModel;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class TradeKillOrderAdapter extends BaseAdapter {

    Context context;
    ArrayList<TradeKillOrderModel> array;

    public TradeKillOrderAdapter(Context context, ArrayList<TradeKillOrderModel> array) {
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
                    R.layout.listitem_trade_operate_kill_order, parent, false);
        }
        TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_killOrder_listitem_name);
        TextView tv_time = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_killOrder_listitem_time);
        TextView tv_price = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_killOrder_listitem_price);
        TextView tv_commission = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_killOrder_listitem_commission);
        TextView tv_trade = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_killOrder_listitem_trade);
        TextView tv_type = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_killOrder_listitem_type);

        int color = 0;
        if (array.get(position).getType().equals("买")) {
            color = context.getResources().getColor(R.color.priceRedColor);
        } else {
            color = context.getResources().getColor(R.color.blue);
        }
        tv_name.setTextColor(color);
        tv_time.setTextColor(color);
        tv_price.setTextColor(color);
        tv_commission.setTextColor(color);
        tv_trade.setTextColor(color);
        tv_type.setTextColor(color);


        tv_name.setText(array.get(position).getName());
        String timeStr = TimeZoneUtils.transfromTimeStr(array.get(position).getTime().replace("T", " "), "HH:mm:ss");

        tv_time.setText(timeStr);
        tv_price.setText(MathUtil.toBigDecimal(array.get(position).getPrice(), 2).toString());
        tv_commission.setText(array.get(position).getCommission());
        tv_trade.setText(array.get(position).getTrade());
        tv_type.setText(array.get(position).getType());


        return convertView;
    }

    /**
     * 更新数据
     */
    public void updateData(ArrayList<TradeKillOrderModel> array) {
        this.array = array;
    }
}
