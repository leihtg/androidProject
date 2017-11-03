package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.ProductPostModel_out;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * 新品申购的列表Adapter
 */
public class ProductPostListAdapter extends BaseAdapter {

    Context context;
    List<ProductPostModel_out> array;
    View header;

    public ProductPostListAdapter(Context context, List<ProductPostModel_out> array) {
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
                    R.layout.listitem_new_product_post_list, parent, false);
        }


        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_newProductPostList_listitem_name);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_newProductPostList_listitem_price);
        TextView tv_startTime = (TextView) convertView.findViewById(R.id.tv_newProductPostList_listitem_startTime);
        TextView tv_endTime = (TextView) convertView.findViewById(R.id.tv_newProductPostList_listitem_endTime);


        ProductPostModel_out item = array.get(position);
        if (item != null) {
            tv_name.setText(item.getProductName());
            tv_price.setText(MathUtil.toBigDecimal(item.getPrice(), 2).toString());

            //接口返回的时间有的时候包含"T"这个字母,为了正常转换时间,我们需要特殊处理一下
            String startTime = item.getStartAskTime().replace("T", " ");
            String endTime = item.getEndAskTime().replace("T", " ");
            tv_startTime.setText(TimeZoneUtils.transfromTimeStr(startTime, "yyyy-MM-dd HH:mm:ss"));
            tv_endTime.setText(TimeZoneUtils.transfromTimeStr(endTime, "yyyy-MM-dd HH:mm:ss"));


        }


        return convertView;
    }

    /**
     * adapter更新绑定的列表数据
     *
     * @param array
     */
    public void refresh(ArrayList<ProductPostModel_out> array) {
        this.array = array;
        notifyDataSetChanged();
    }
}
