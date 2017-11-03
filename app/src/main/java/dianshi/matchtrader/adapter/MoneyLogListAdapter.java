package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.model.MoneyLogModel_out;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.TextViewUtils;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * Created by mac on 16/5/14.
 */
public class MoneyLogListAdapter extends BaseAdapter {

    List<MoneyLogModel_out> array;
    Context context;

    public MoneyLogListAdapter(Context context, List<MoneyLogModel_out> array) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_money_log, viewGroup, false);
        }

        TextView tv_tradeMoney = (TextView) view.findViewById(R.id.tv_money_log_listitem_tradeMoney);
        TextView tv_availableMoney = (TextView) view.findViewById(R.id.tv_money_log_listitem_availableMoney);
        TextView tv_moneyTo = (TextView) view.findViewById(R.id.tv_money_log_listitem_moneyTo);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_money_log_listitem_time);


        MoneyLogModel_out item = array.get(i);
        if (item != null) {
            double lastMoney = item.getLastMoney();
            double money = item.getMoney();
            double newMoney = item.getNewMoney();

            BigDecimal lastMoneyDec = BigDecimal.valueOf(lastMoney);
            BigDecimal moneyDec = BigDecimal.valueOf(money);
            BigDecimal newMoneyDec = BigDecimal.valueOf(newMoney);

            lastMoneyDec.setScale(2, BigDecimal.ROUND_HALF_UP);
            moneyDec.setScale(2, BigDecimal.ROUND_HALF_UP);
            newMoneyDec.setScale(2, BigDecimal.ROUND_HALF_UP);

            tv_moneyTo.setText(item.getNote());
            //接口返回的时间有的时候包含"T"这个字母,为了正常转换时间,我们需要特殊处理一下
            tv_time.setText(TimeZoneUtils.transfromTimeStr(item.getSysCreateTime().replace("T", " "), "yyyy-MM-dd HH:mm"));
            tv_tradeMoney.setText(moneyDec.toString());
            tv_availableMoney.setText(newMoneyDec.toString());

            if (moneyDec.doubleValue() > 0) {
                tv_tradeMoney.setTextColor(context.getResources().getColor(R.color.priceRedColor));
            } else {
                tv_tradeMoney.setTextColor(context.getResources().getColor(R.color.blue));
            }

            //设置跑马灯效果
            TextViewUtils.setTextMarquee(tv_availableMoney);

        }

        return view;
    }


    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<MoneyLogModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }
}
