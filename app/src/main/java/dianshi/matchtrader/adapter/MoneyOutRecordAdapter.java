package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.MoneyOutModel_out;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.StringUtils;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class MoneyOutRecordAdapter extends BaseAdapter {

    Context context;
    List<MoneyOutModel_out> array;
    View header;

    public MoneyOutRecordAdapter(Context context, List<MoneyOutModel_out> array) {
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
                    R.layout.listitem_money_out, parent, false);
        }


        TextView tv_bankName = (TextView) convertView.findViewById(R.id.tv_money_in_listitem_bankName);
        TextView tv_bankCardNumber = (TextView) convertView.findViewById(R.id.tv_money_in_listitem_bankCardNumber);
        TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money_in_listitem_money);
        TextView tv_state = (TextView) convertView.findViewById(R.id.tv_money_in_listitem_state);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_money_in_listitem_time);


        MoneyOutModel_out item = array.get(position);
        if (item != null) {
            tv_bankCardNumber.setText(StringUtils.passwordBankCardNumber(String.valueOf(item.getBankCard())));
            tv_bankName.setText(item.getBankName());
            tv_money.setText(MathUtil.toBigDecimal(item.getMoney(), 2).toString());
            String status = item.getStatus();
            if (status == null || status.equals("") || status.equals("nil")) {
                status = "无";
            }
            tv_state.setText(status);

            //接口返回的时间有的时候包含"T"这个字母,为了正常转换时间,我们需要特殊处理一下
            String time = item.getFinishTime().replace("T", " ");
            tv_time.setText(TimeZoneUtils.transfromTimeStr(time, "yy-MM-dd HH:mm"));

        }


        return convertView;
    }

    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<MoneyOutModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }
}
