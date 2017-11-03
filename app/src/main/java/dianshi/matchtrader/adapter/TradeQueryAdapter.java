package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dianshi.matchtrader.R;

/**
 * 主页面-行情碎片上方显示类别的spinner的自定义适配器
 *
 * @author DR
 *         Created by Administrator on 2016/5/3 0003.
 */
public class TradeQueryAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> array;

    public TradeQueryAdapter(Context context, ArrayList<String> array) {
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
                    R.layout.listitem_nav, null);
        }

        TextView tv_title = BaseViewHolder.get(convertView, R.id.tv_nav_listitem_name);
        tv_title.setText(array.get(position));

        ImageView img_more = BaseViewHolder.get(convertView, R.id.img_nav_listitem_more);
        img_more.setVisibility(View.VISIBLE);

        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.color.nav_list_1);
        } else {
            convertView.setBackgroundResource(R.color.nav_list_2);
        }


        return convertView;
    }
}
