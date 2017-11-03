package dianshi.matchtrader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dianshi.matchtrader.model.PositionModel_out;
import com.dianshi.matchtrader.model.ProductModel_out;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;

/**
 * Created by Administrator on 2016/5/12.
 */
public class PositionAutoComplateAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<PositionModel_out> array;
    List<PositionModel_out> filtedArray;

    public PositionAutoComplateAdapter(Context context, List<PositionModel_out> array) {
        this.context = context;
        this.array = array;
        this.filtedArray = array;
    }

    @Override
    public int getCount() {
        return filtedArray != null ? filtedArray.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return filtedArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (view == null) {
            view = inflater.inflate(R.layout.listitem_product_auto, null);
        }
        TextView name_tv = (TextView) view.findViewById(R.id.product_auto_item_name);
        TextView code_tv = (TextView) view.findViewById(R.id.product_auto_item_code);
        PositionModel_out product = filtedArray.get(i);
        String name = product.getProductName();
        String code = product.getProductCode();

        name_tv.setText(name);
        code_tv.setText(code);
        view.setTag(String.valueOf(product.getId()));
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                String searchStr = charSequence.toString().toLowerCase().trim();


                ArrayList<PositionModel_out> suggest = new ArrayList<>();

                for (PositionModel_out item : array) {
                    //商品的名称和编号都是索引
                    String productNameCode = item.getProductCode().toLowerCase().trim() + item.getProductName().toLowerCase().trim();

                    if (productNameCode.contains(searchStr)) {

                        suggest.add(item);

                    }
                }
                filterResults.values = suggest;
                filterResults.count = suggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtedArray = (ArrayList<PositionModel_out>) filterResults.values;


                if (filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();

                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
