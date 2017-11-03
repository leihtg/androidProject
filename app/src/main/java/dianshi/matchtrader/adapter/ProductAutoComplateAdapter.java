package dianshi.matchtrader.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dianshi.matchtrader.model.ProductModel_out;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;

/**
 * 自动补全列表的Adapter
 */
public class ProductAutoComplateAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<ProductModel_out> array;
    List<ProductModel_out> filtedArray;


    public ProductAutoComplateAdapter(Context context, List<ProductModel_out> array) {
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
        ProductModel_out product = filtedArray.get(i);
        String name = product.getName();
        String code = product.getCode();

        name_tv.setText(name);
        code_tv.setText(code);
        view.setTag(String.valueOf(product.getId()));
        return view;
    }


    /**
     * 添加过滤
     *
     * @return
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                String searchStr = charSequence.toString().toLowerCase().trim();


                ArrayList<ProductModel_out> suggest = new ArrayList<>();

                for (ProductModel_out item : array) {
                    //商品的名称和编号都是索引
                    String productNameCode = item.getCode().toLowerCase().trim() + item.getName().toLowerCase().trim();

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
                filtedArray = (ArrayList<ProductModel_out>) filterResults.values;


                if (filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();

                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
