package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.model.UserInfoModel;

/**
 * 主页面-行情碎片上方显示类别的spinner的自定义适配器
 *
 * @author DR
 *         Created by Administrator on 2016/5/3 0003.
 */
public class UserInfoAdapter extends BaseAdapter {

    Context context;
    ArrayList<UserInfoModel> array;

    public UserInfoAdapter(Context context, ArrayList<UserInfoModel> array) {
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
                    R.layout.listitem_userinfo, null);
        }
        TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_userInfo_listitem_name);
        TextView tv_value = BaseViewHolder.get(convertView, R.id.img_userInfo_listitem_value);

        tv_name.setText(array.get(position).getName());
        tv_value.setText(array.get(position).getValue());

        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.color.nav_list_1);
        } else {
            convertView.setBackgroundResource(R.color.nav_list_2);
        }


        return convertView;
    }
}
