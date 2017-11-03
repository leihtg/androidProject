package dianshi.matchtrader.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.MapProductNotice;
import com.dianshi.matchtrader.model.NoticeMsgType;
import com.dianshi.matchtrader.model.PositionModel_out;
import com.dianshi.matchtrader.product.ProductLoader;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.TradePositionAdapter;
import dianshi.matchtrader.customInterface.OnNoDoubleItemClickListener;
import dianshi.matchtrader.dialog.DetailDialog;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.dialog.WaitingDialog;
import dianshi.matchtrader.model.TradePositionModel;
import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.view.RingView;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class TradePositionFragment extends BaseFragment {

    View view;
    TextView totalTitleTextView;
    TextView totalTextView;
    TextView canTradeTitleTextView;
    TextView canTradeTextView;
    TextView noCanTradeTitleTextView;
    TextView noCanTradeTextView;
    RingView ringView;
    //持仓数据
    List<PositionModel_out> positionArray;
    //存储持仓商品信息的列表
    ConcurrentHashMap<Integer, PositionModel_out> positionDict;

    //加载等待框
    WaitingDialog waitingDialog;


    ListView listView;
    TradePositionAdapter adapter;

    ArrayList<TradePositionModel> array = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trade_operate_position, null);

        //控件绑定ID
        findViewById();

        //初始化饼状图
        setCharts(0, 0);


        //获取持仓
        loadPostion();


        //检测持仓
        GlobalSingleton.CreateInstance().ServerPushHandler.regPositionHandler(positionChangeHandler);
        //检测价格变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regProductPriceHandler(productChangeHandler);
        return view;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        //获取持仓
        loadPostion();
    }

    /**
     * 控件绑定ID
     */
    private void findViewById() {


        listView = (ListView) view.findViewById(R.id.listview_tradeOperate_position);


        totalTitleTextView = (TextView) view.findViewById(R.id.tv_moneyIn_totalAssets_title);
        totalTextView = (TextView) view.findViewById(R.id.tv_moneyIn_totalAssets);

        canTradeTitleTextView = (TextView) view.findViewById(R.id.tv_moneyIn_totalMarketValue_title);
        canTradeTextView = (TextView) view.findViewById(R.id.tv_moneyIn_canUseMoney);

        noCanTradeTitleTextView = (TextView) view.findViewById(R.id.tv_moneyIn_availableAmount_title);
        noCanTradeTextView = (TextView) view.findViewById(R.id.tv_moneyIn_frozenMoney);
        ringView = (RingView) view.findViewById(R.id.ringView_trade_percent);

        waitingDialog = new WaitingDialog(getActivity());
    }


    /**
     * 初始化listview
     */
    public void initListView(boolean isFirst) {


        positionArray = GlobalSingleton.CreateInstance().ProductPool.getpositionList();
        array.clear();
        double allPostionValue = 0;
        double allCanTradeValue = 0;

        synchronized (array) {

            for (PositionModel_out item : positionArray) {
                TradePositionModel model = new TradePositionModel();

                model.setName(item.getProductName());
                model.setMarketValue(item.getMarketValue());
                model.setAveragePrice(item.getAvgPrice());
                model.setProfit(item.getHoldProfit());

                model.setProfitRate(item.getProfitRate());
                model.setPosition(String.valueOf(item.getAllCount()).toString());
                model.setAvailable(String.valueOf(item.getCount()).toString());
                array.add(0, model);

                double nowPrice = Double.parseDouble(item.getMarketValue()) / item.getAllCount();
                allPostionValue += Double.parseDouble(item.getMarketValue());
                allCanTradeValue += item.getCount() * nowPrice;
            }
        }


        //饼状图修改
        setCharts(allCanTradeValue, allPostionValue);

        if (isFirst) {

            adapter = new TradePositionAdapter(getActivity(), array);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(onNoDoubleItemClickListener);
        } else {

            if (adapter != null) {
                adapter.notifyDataSetChanged();
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
    OnNoDoubleItemClickListener onNoDoubleItemClickListener = new OnNoDoubleItemClickListener() {
        @Override
        public void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
            //弹出持仓商品的详细信息
            initDialog(position);
        }
    };


    /**
     * 详细信息对话框
     *
     * @param position
     */
    public void initDialog(final int position) {

        String[] keys = getResources().getStringArray(R.array.tradePosition_dialog);

        String[] values = {
                array.get(position).getName(),
                MathUtil.toBigDecimal(array.get(position).getMarketValue(), 2).toString(),
                MathUtil.toBigDecimal(array.get(position).getAveragePrice(), 2).toString(),
                MathUtil.toBigDecimal(array.get(position).getProfit(), 2).toString(),
                MathUtil.strToPercentStr(array.get(position).getProfitRate(), 2, BigDecimal.ROUND_HALF_UP),
                array.get(position).getPosition(),
                array.get(position).getAvailable()
        };

        DetailDialog detailDialog = new DetailDialog(getActivity(), "持仓信息", "确认", "", keys, values, null);
        detailDialog.show();

    }

    /**
     * 加载持仓
     */
    private void loadPostion() {
//        waitingDialog.show(getResources().getString(R.string.tradeOperate_position_waitingLoading));
        ProductLoader productLoader = new ProductLoader();
        productLoader.loadPositionHandler = positionLoadHandler;
        productLoader.loadPositionProduct();

    }


    /**
     * 加载持仓handler
     */
    private Handler positionLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            waitingDialog.dismiss();
            if (msg.arg1 == 1) {
                initListView(true);
            } else {
                //显示提示对话框
                MyAlertDialog dialog = new MyAlertDialog(getActivity());
                dialog.tipDialog("获得持仓失败!");
            }

        }
    };

    /**
     * 饼状图参数设置
     *
     * @param canTradePosition
     * @param allpostion
     */
    private void setCharts(double canTradePosition, double allpostion) {


        totalTitleTextView.setText("总持仓");
        canTradeTitleTextView.setText("可交易");
        noCanTradeTitleTextView.setText("冻结中");
        //设置比例
        ringView.setRadio(((float) canTradePosition), (float) allpostion);


        double frozenPosition = allpostion - canTradePosition;


        totalTextView.setText(MathUtil.toBigDecimal(allpostion, 2).toString());
        canTradeTextView.setText(MathUtil.toBigDecimal(canTradePosition, 2).toString());
        noCanTradeTextView.setText(MathUtil.toBigDecimal(frozenPosition, 2).toString());

    }


    /**
     * 商品信息发生变化
     */
    private Handler productChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //当变化的商品包括持仓商品,更新持仓商品列表
            if (GlobalSingleton.CreateInstance().ProductPool.isContainPositionProduct((MapProductNotice) msg.obj)) {
                //初始化Listview
                initListView(false);
            }


        }
    };


    /**
     * 持仓发生变化
     */
    private Handler positionChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NoticeMsgType.SelfPostionChange && msg.arg1 == 1) {
//                initListView(false);
                loadPostion();
            }
        }
    };
}
