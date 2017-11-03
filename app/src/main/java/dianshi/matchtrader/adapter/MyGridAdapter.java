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
import dianshi.matchtrader.model.HomeGridModel;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MyGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HomeGridModel> array;


    public MyGridAdapter(Context mContext, ArrayList<HomeGridModel> array) {
        super();
        this.mContext = mContext;
        this.array = array;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return array == null ? 0 : array.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(array.get(position).getImageId());
        tv.setText(array.get(position).getFunctionStr());
        return convertView;
    }

}

