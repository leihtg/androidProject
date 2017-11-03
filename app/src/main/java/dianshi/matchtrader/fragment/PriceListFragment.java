package dianshi.matchtrader.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dianshi.matchtrader.model.MapProductNotice;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.activity.MainActivity;
import dianshi.matchtrader.activity.StockDetailActivity;
import dianshi.matchtrader.adapter.PriceListAdapter;
import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.view.MyHScrollView;

public class PriceListFragment extends BaseFragment implements AdapterView.OnItemClickListener {


    View view;
    ListView listView;
    RelativeLayout header;
    ArrayList<ProductModel_out> array;

    //分类ID
    int categoryId;

    //头布局中的横向ScrollView
    MyHScrollView myHScrollView;
    //记录是否同意触发OnItemClick
    private boolean isCanClick;

    //listview的适配器
    PriceListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_price_list, container, false);


        //控件绑定ID
        findViewById();
        //初始化actionBar
        initActionBar();
        //初始化分类ID，和主页面标题上的spinner绑定上
        initCategoryId();
        //初始化标题
        initHeader();


        GlobalSingleton.CreateInstance().ServerPushHandler.regProductPriceHandler(priceChangeHandler);

        return view;
    }


    /**
     * 初始化actionBar
     */
    public void initActionBar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.actionbarTitleChange(1);
    }


    /**
     * 控件绑定ID
     */
    public void findViewById() {

        listView = (ListView) view.findViewById(R.id.listview_priceList);
    }


    /**
     * 初始化头部
     */
    public void initHeader() {
        TextView tv_name = (TextView) view.findViewById(R.id.tv_priceList_header_name);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_priceList_header_price);
        TextView tv_twClose = (TextView) view.findViewById(R.id.tv_priceList_header_twClose);
        TextView tv_up = (TextView) view.findViewById(R.id.tv_priceList_header_up);
        TextView tv_down = (TextView) view.findViewById(R.id.tv_priceList_header_down);
        TextView tv_open = (TextView) view.findViewById(R.id.tv_priceList_header_open);
        TextView tv_top = (TextView) view.findViewById(R.id.tv_priceList_header_top);
        TextView tv_bottom = (TextView) view.findViewById(R.id.tv_priceList_header_bottom);
        TextView tv_volume = (TextView) view.findViewById(R.id.tv_priceList_header_volume);
        TextView tv_turnover_volume = (TextView) view.findViewById(R.id.tv_priceList_header_turnover_volume);

        tv_name.setText(R.string.priceList_name);
        tv_price.setText(R.string.priceList_price);
        tv_twClose.setText(R.string.priceList_twClose);
        tv_up.setText(R.string.priceList_up);
        tv_down.setText(R.string.priceList_down);
        tv_open.setText(R.string.priceList_open);
        tv_top.setText(R.string.priceList_top);
        tv_bottom.setText(R.string.priceList_bottom);
        tv_volume.setText(R.string.priceList_volume);
        tv_turnover_volume.setText(R.string.priceList_turnover_volume);

        header = (RelativeLayout) view.findViewById(R.id.header_priceList);
        header.setFocusable(true);
        header.setClickable(true);
        header.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        myHScrollView = (MyHScrollView) view.findViewById(R.id.horizontalScrollView1);
    }


    /**
     * 头部列名的监听事件
     */
    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            //当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
            HorizontalScrollView headSrcrollView = (HorizontalScrollView) header
                    .findViewById(R.id.horizontalScrollView1);
            headSrcrollView.onTouchEvent(arg1);

            //滚动条设置是否同意接收onItemClick监听事件
            myHScrollView.setIOK(new MyHScrollView.ICanClick() {

                public void canClick(boolean iCanClick) {
                    isCanClick = iCanClick;
                }
            });


            return false;
        }
    }

    /**
     * 从主框架获取选择的类别,然后加载商品列表
     */
    public void initCategoryId() {

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setOnSpinnerClickedListener(new MainActivity.OnSpinnerClickedListener() {
            @Override
            public void onSpinnerClick(int position) {
                categoryId = position;
                //初始化listview
                initListView(new ArrayList<String>(), true);
            }
        });

    }

    ;


    /**
     * 初始化listview
     */
    public void initListView(ArrayList<String> idArray, boolean isFirst) {


        //Listview往下传递事件给HorizontalScrollView
        listView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        //处理全部和其他的分类
        if (categoryId == -1) {
            array = (ArrayList<ProductModel_out>) GlobalSingleton.CreateInstance().ProductPool.getProduct();
        } else {
            array = (ArrayList<ProductModel_out>) GlobalSingleton.CreateInstance().ProductPool.getProductByCateId(categoryId);

        }

        if (isFirst) {
            adapter = new PriceListAdapter(getActivity(), array, header, idArray);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        } else {
            if (adapter != null) {
                adapter.updateData(array, idArray);

            }
        }


    }


    /**
     * 监听行单击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /**
         * 加判断，避免OnItemClick和myHScrollView的onScroll同时触发
         */
        if (isCanClick) {
            Intent intent = new Intent(getActivity(), StockDetailActivity.class);
            ProductModel_out product = (ProductModel_out) parent.getItemAtPosition(position);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);

        }
    }

    /**
     * 服务器实时更新价格
     */
    private Handler priceChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayList<String> idArr = new ArrayList<>();
            MapProductNotice notice = (MapProductNotice) msg.obj;

            //获取给更新的商品id
            for (String id : notice.keySet()) {
                idArr.add(id);
            }

            initListView(idArr, false);


        }
    };


}
