package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.AskRecordModel_out;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;

/**
 * 申购结果的列表Adapter
 */
public class ProductPostResultListAdapter extends BaseAdapter {

    Context context;
    List<AskRecordModel_out> array;
    View header;

    public ProductPostResultListAdapter(Context context, List<AskRecordModel_out> array) {
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
                    R.layout.listitem_new_product_post_result_list, parent, false);
        }


        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_newProductPostResultList_listitem_name);
        TextView tv_code = (TextView) convertView.findViewById(R.id.tv_newProductPostResultList_listitem_code);
        TextView tv_successCount = (TextView) convertView.findViewById(R.id.tv_newProductPostResultList_listitem_successCount);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_newProductPostResultList_listitem_count);
        TextView tv_successMoney = (TextView) convertView.findViewById(R.id.tv_newProductPostResultList_listitem_successMoney);
        TextView tv_money = (TextView) convertView.findViewById(R.id.tv_newProductPostResultList_listitem_money);


        AskRecordModel_out item = array.get(position);
        if (item != null) {
            tv_name.setText(item.getProductName());
            tv_code.setText(item.getCode());
            tv_count.setText(String.valueOf(item.getCount()));
            tv_successCount.setText(String.valueOf(item.getSuccCount()));
            tv_successMoney.setText(MathUtil.toBigDecimal(item.getUsedMoney(), 2).toString());
            tv_money.setText(MathUtil.toBigDecimal(item.getAskMoney(), 2).toString());

        }


        return convertView;
    }

    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<AskRecordModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }
}
