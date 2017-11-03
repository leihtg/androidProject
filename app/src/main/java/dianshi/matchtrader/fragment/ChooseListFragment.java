package dianshi.matchtrader.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.AppManager;
import com.dianshi.matchtrader.model.MapProductNotice;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.activity.AddChooseActivity;
import dianshi.matchtrader.activity.StockDetailActivity;
import dianshi.matchtrader.adapter.PriceListAdapter;
import dianshi.matchtrader.database.SQLiteHelper;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.view.MyHScrollView;


public class ChooseListFragment extends BaseFragment implements View.OnClickListener {


    Context context;
    View view;
    ListView listView;
    RelativeLayout header;
    Button btn_addChoose;
    GlobalSingleton globalSingleton;

    ArrayList<ProductModel_out> array;
    SQLiteDatabase db;//数据库
    SQLiteHelper sqlHelper;//数据库实体类
    PriceListAdapter adapter;


    //listview行布局的父布局
    MyHScrollView myHScrollView;
    //记录是否同意触发OnItemClick
    private boolean isCanClick;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 因为AddChooseActivity将activity的背景设为了透明色，所以当从此碎片跳转到自选页面的时候，会改变生命周期
     * 二者跳转切换时调用生命周期onPause() -- onResume()
     * 所以把数据更新放在onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        //数据库查询
        List<Integer> produceIdList = sqlHelper.query(db);
        //更新数据
        array = (ArrayList<ProductModel_out>) globalSingleton.CreateInstance().ProductPool.getProductList(produceIdList);
        adapter.refresh(array);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose_list, container, false);

        // 得到sql数据库
        sqlHelper = new SQLiteHelper(AppManager.getAppManager().currentActivity());

        //初始化标题
        initHeader();
        //初始化listview
        initListView(new ArrayList<String>());
        //初始化popupWindow
        initPopup();
        GlobalSingleton.CreateInstance().ServerPushHandler.regProductPriceHandler(priceChangeHandler);

        return view;
    }

    private Handler priceChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayList<String> idArr = new ArrayList<>();
            MapProductNotice notice = (MapProductNotice) msg.obj;
            for (String id : notice.keySet()) {
                idArr.add(id);
            }
            initListView(idArr);

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        TextView tv_top = (TextView) view.findViewById(R.id.tv_priceList_header_top);
        TextView tv_bottom = (TextView) view.findViewById(R.id.tv_priceList_header_bottom);
        TextView tv_volume = (TextView) view.findViewById(R.id.tv_priceList_header_volume);
        TextView tv_turnover_volume = (TextView) view.findViewById(R.id.tv_priceList_header_turnover_volume);

        tv_name.setText(R.string.priceList_name);
        tv_price.setText(R.string.priceList_price);
        tv_twClose.setText(R.string.priceList_twClose);
        tv_up.setText(R.string.priceList_up);
        tv_down.setText(R.string.priceList_down);
        tv_top.setText(R.string.priceList_top);
        tv_bottom.setText(R.string.priceList_bottom);
        tv_volume.setText(R.string.priceList_volume);
        tv_turnover_volume.setText(R.string.priceList_turnover_volume);

        header = (RelativeLayout) view.findViewById(R.id.header_chooseList);
        header.setFocusable(true);
        header.setClickable(true);
        header.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        myHScrollView = (MyHScrollView) view.findViewById(R.id.horizontalScrollView1);
    }

    /**
     * 初始化listview
     */
    public void initListView(ArrayList<String> idArray) {
        listView = (ListView) view.findViewById(R.id.listview_chooseList);
        listView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        // 得到可写的数据库
        db = sqlHelper.getWritableDatabase();

        //判断数据表是否存在，不存在就创建
        if (!sqlHelper.isTableExist(db)) {
            sqlHelper.onCreate(db);
        }
        // 通过查询数据库，得到存储已经自选的商品
        List<Integer> produceIdList = sqlHelper.query(db);

        array = (ArrayList<ProductModel_out>) globalSingleton.CreateInstance().ProductPool.getProductList(produceIdList);
        adapter = new PriceListAdapter(context, array, header, idArray);
        listView.setAdapter(adapter);
        /**
         *行单击事件监听
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });

        /**
         * 行长按事件监听
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //弹出删除提示对话框
                initDialog(position);
                return true;
            }
        });

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
     * 初始化popupWindow
     */
    public void initPopup() {
        btn_addChoose = (Button) view.findViewById(R.id.btn_chooseList_addChoose);
        btn_addChoose.setOnClickListener(this);
    }


    MyAlertDialog detailDialog;

    /**
     * 提示对话框
     */
    public void initDialog(final int position) {

        detailDialog = new MyAlertDialog(getActivity());

        detailDialog.ensureDialog("删除确认", "确认删除", array.get(position).getName() + "?", new MyAlertDialog.DialogCallBack() {
            @Override
            public void onEnSure() {

                //从数据库中删除
                int id = array.get(position).getId();
                sqlHelper.delete(db, id);
                //数据库查询
                List<Integer> produceIdList = sqlHelper.query(db);
                //更新数据
                array = (ArrayList<ProductModel_out>) globalSingleton.CreateInstance().ProductPool.getProductList(produceIdList);
                adapter.refresh(array);
                //关闭对话框
                detailDialog.dismiss();
            }

            @Override
            public void onCancel() {
                //关闭对话框
                detailDialog.dismiss();
            }
        });
        detailDialog.show();
    }


    /**
     * 各类控件单击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chooseList_addChoose:
                //添加自选
                Intent intent = new Intent(getActivity(), AddChooseActivity.class);
                startActivity(intent);
                break;
        }

    }


    @Override
    public void onDestroy() {
        //关闭数据库
        db.close();
        super.onDestroy();
    }
}
