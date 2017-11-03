package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.model.ProductCategoryModel_out;

import java.util.ArrayList;

import dianshi.matchtrader.R;

/**
 * 主页面-行情碎片上方显示类别的spinner的自定义适配器
 *
 * @author DR
 *         Created by Administrator on 2016/5/3 0003.
 */
public class MainSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<ProductCategoryModel_out> array;

    public MainSpinnerAdapter(Context context, ArrayList<ProductCategoryModel_out> array) {
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
                    R.layout.spinner_item, parent, false);
        }
        TextView tv_title = BaseViewHolder.get(convertView, R.id.tv_main_spinner_title);
        tv_title.setText(array.get(position).getName());


        return convertView;
    }
}
