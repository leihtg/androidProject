package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.model.NewsModel_out;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.DateUtils;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class NoticeAdapter extends BaseAdapter {

    Context context;
    ArrayList<NewsModel_out> array;

    public NoticeAdapter(Context context, ArrayList<NewsModel_out> array) {
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
                    R.layout.listitem_notice, parent, false);
        }
        TextView tv_title = BaseViewHolder.get(convertView, R.id.tv_notice_title);
        TextView tv_discrible = BaseViewHolder.get(convertView, R.id.tv_notice_discrible);
        TextView tv_time = BaseViewHolder.get(convertView, R.id.tv_notice_time);

        tv_title.setText(array.get(position).getTitle());
        tv_discrible.setText(array.get(position).getTitle());
        tv_time.setText(DateUtils.formatStr(array.get(position).getSysCreateTime().replace("T", " "), "yyyy-MM-dd HH:mm:ss"));
        return convertView;
    }

    /**
     * 刷新适配器数据
     *
     * @param array
     */
    public void refresh(ArrayList<NewsModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }
}
