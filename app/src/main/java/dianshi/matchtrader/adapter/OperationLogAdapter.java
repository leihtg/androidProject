package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.model.OperateLogModel;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class OperationLogAdapter extends BaseAdapter {

    Context context;
    ArrayList<OperateLogModel> array;

    public OperationLogAdapter(Context context, ArrayList<OperateLogModel> array) {
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
                    R.layout.listitem_operation_log, parent, false);
        }
        TextView tv_detail = BaseViewHolder.get(convertView, R.id.tv_operationLog_listitem_detail);
        TextView tv_ip = BaseViewHolder.get(convertView, R.id.tv_operationLog_listitem_ip);
        TextView tv_address = BaseViewHolder.get(convertView, R.id.tv_operationLog_listitem_address);
        TextView tv_time = BaseViewHolder.get(convertView, R.id.tv_operationLog_listitem_time);


        tv_detail.setText(array.get(position).getNote());
        tv_ip.setText(array.get(position).getIP());
        tv_address.setText(array.get(position).getProvince() + " " + array.get(position).getCity());

        tv_time.setText(TimeZoneUtils.transfromTimeStr(array.get(position).getSysCreateTime().replace("T", " "), "yyyy-MM-dd HH:mm"));

        return convertView;
    }


    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<OperateLogModel> array) {
        this.array = array;
        notifyDataSetChanged();
    }

}
