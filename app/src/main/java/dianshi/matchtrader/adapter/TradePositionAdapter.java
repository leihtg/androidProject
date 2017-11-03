package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.model.TradePositionModel;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class TradePositionAdapter extends BaseAdapter {

    Context context;
    ArrayList<TradePositionModel> array;

    public TradePositionAdapter(Context context, ArrayList<TradePositionModel> array) {
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
                    R.layout.listitem_trade_operate_position, parent, false);
        }
        TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_position_listitem_name);
        TextView tv_marketValue = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_position_listitem_marketValue);
        TextView tv_averagePrice = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_position_listitem_averagePrice);
        TextView tv_profit = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_position_listitem_profit);
        TextView tv_profitRatio = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_position_listitem_profitRatio);
        TextView tv_position = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_position_listitem_position);
        TextView tv_avaiable = BaseViewHolder.get(convertView, R.id.tv_tradeOperate_position_listitem_avaiable);


        int color;
        if (Double.parseDouble(array.get(position).getProfit()) > 0) {
            color = context.getResources().getColor(R.color.priceRedColor);
        } else {
            color = context.getResources().getColor(R.color.blue);
        }
        tv_name.setTextColor(color);
        tv_marketValue.setTextColor(color);
        tv_averagePrice.setTextColor(color);
        tv_profit.setTextColor(color);
        tv_profitRatio.setTextColor(color);
        tv_position.setTextColor(color);
        tv_avaiable.setTextColor(color);


        tv_name.setText(array.get(position).getName());
        tv_marketValue.setText(MathUtil.toBigDecimal(array.get(position).getMarketValue(), 2).toString());
        tv_averagePrice.setText(array.get(position).getAveragePrice());
        tv_position.setText(array.get(position).getPosition());
        tv_profit.setText(MathUtil.toBigDecimal(array.get(position).getProfit(), 2).toString());
        tv_profitRatio.setText(MathUtil.strToPercentStr(array.get(position).getProfitRate(), 2, BigDecimal.ROUND_HALF_UP));
        tv_avaiable.setText(array.get(position).getAvailable());

        return convertView;
    }
}
