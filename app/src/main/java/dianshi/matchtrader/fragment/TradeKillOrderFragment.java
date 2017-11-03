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
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.model.CancelDeputeModel_in;
import com.dianshi.matchtrader.model.DeputyModel_out;
import com.dianshi.matchtrader.model.ListDeputeModel_out;
import com.dianshi.matchtrader.model.LoadDeputeModel_in;
import com.dianshi.matchtrader.model.ResultModel_out;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.dianshi.matchtrader.userinfo.UserInfoLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.TradeKillOrderAdapter;
import dianshi.matchtrader.customInterface.OnNoDoubleItemClickListener;
import dianshi.matchtrader.model.TradeKillOrderModel;
import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.dialog.DetailDialog;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.view.RingView;
import dianshi.matchtrader.dialog.WaitingDialog;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class TradeKillOrderFragment extends BaseFragment {

    View view;
    RingView ringView;

    TextView allMoneyTitleTextView;
    TextView allMoneyTextView;
    TextView buyMoneyTitleTextView;
    TextView buyMoneyTextView;
    TextView sellMoneyTitleTextView;
    TextView sellMoneyTextView;

    ArrayList<TradeKillOrderModel> array;
    DetailDialog alertDialog;
    //加载等待框
    WaitingDialog waitingDialog;
    List<DeputyModel_out> DeputeList;


    TradeKillOrderAdapter adapter;
    ListView listView;

    int position;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trade_operate_kill_order, null);

        //控件绑定ID
        findViewById();

        //初始化饼状图
        setCharts(0, 0);

        //获取挂单信息
        loadDepute();


        //监控服务器端的单信息变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regDeputeHandler(deputeChangeHandler);
        return view;
    }


    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        //获取挂单信息
        loadDepute();
    }

    /**
     * 控件绑定ID
     */
    private void findViewById() {


        listView = (ListView) view.findViewById(R.id.listview_tradeOperate_killOrder);

        allMoneyTitleTextView = (TextView) view.findViewById(R.id.tv_moneyIn_totalAssets_title);
        allMoneyTextView = (TextView) view.findViewById(R.id.tv_moneyIn_totalAssets);
        buyMoneyTitleTextView = (TextView) view.findViewById(R.id.tv_moneyIn_availableAmount_title);
        buyMoneyTextView = (TextView) view.findViewById(R.id.tv_moneyIn_frozenMoney);
        sellMoneyTitleTextView = (TextView) view.findViewById(R.id.tv_moneyIn_totalMarketValue_title);
        sellMoneyTextView = (TextView) view.findViewById(R.id.tv_moneyIn_canUseMoney);

        ringView = (RingView) view.findViewById(R.id.ringView_trade_percent);

        waitingDialog = new WaitingDialog(getActivity());
    }


    /**
     * 设置图形
     *
     * @param buyMoney
     * @param sellMoney
     */
    private void setCharts(double sellMoney, double buyMoney) {


        allMoneyTitleTextView.setText("总挂单");
        buyMoneyTitleTextView.setText("买单");
        sellMoneyTitleTextView.setText("卖单");


        double all = buyMoney + sellMoney;

        allMoneyTextView.setText(MathUtil.toBigDecimal(all, 2).toString());
        buyMoneyTextView.setText(MathUtil.toBigDecimal(buyMoney, 2).toString());
        sellMoneyTextView.setText(MathUtil.toBigDecimal(sellMoney, 2).toString());


        //设置比例
        ringView.setRadio((float) sellMoney, (float) all);

    }

    /**
     * 初始化listview
     */
    public void initListView(boolean isFirst) {


        array = new ArrayList<>();

        DateFormat df = new SimpleDateFormat("HH:mm:ss");

        double buyMoney = 0;
        double sellMoney = 0;

        for (DeputyModel_out item : DeputeList) {
            TradeKillOrderModel model = new TradeKillOrderModel();
            model.setName(item.getProductName());
            model.setCode(item.getProductCode());
            model.setTime(item.getTimeStr());
            model.setPrice(String.valueOf(item.getPrice()));
            model.setCommission(String.valueOf(item.getCount()));
            model.setTrade(String.valueOf(item.getCount() - item.getDeputingCount()));
            model.setType(item.getTradeType());
            model.setId(item.getId());
            array.add(0, model);
            if (model.getType().equals("买")) {
                buyMoney += (item.getPrice() * item.getDeputingCount());
            } else {
                sellMoney += (item.getDeputingCount() * item.getPrice());
            }
        }


        setCharts(sellMoney, buyMoney);


        if (isFirst) {
            adapter = new TradeKillOrderAdapter(getActivity(), array);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(onNoDoubleItemClickListener);
        } else {
            if (adapter != null) {
                adapter.updateData(array);
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
        public void onNoDoubleClick(AdapterView<?> parent, View view, int p, long id) {
            position = p;
            //弹出撤单的提示对话框
            initDialog();
        }
    };


    /**
     * 对话框的监听事件
     */
    MyAlertDialog.DialogCallBack dialogCallBack = new MyAlertDialog.DialogCallBack() {
        @Override
        public void onEnSure() {
            //关闭对话框
            int id = array.get(position).getId();
            cancelDepute(id);

            alertDialog.dismiss();
        }

        @Override
        public void onCancel() {
            //关闭对话框
            alertDialog.dismiss();
        }
    };


    /**
     * 撤单提示对话框
     */
    public void initDialog() {

        String[] keys = getResources().getStringArray(R.array.tradekillOrder_dialog);

        int avaiableCount = Integer.valueOf(array.get(position).getCommission()) - Integer.valueOf(array.get(position).getTrade());
        String[] values = {
                "撤" + array.get(position).getType() + "单",
                array.get(position).getCode(),
                array.get(position).getName(),
                array.get(position).getPrice(),
                String.valueOf(avaiableCount)

        };

        alertDialog = new DetailDialog(getActivity(), "撤单信息", "确认", "取消", keys, values, dialogCallBack);
        alertDialog.show();
    }

    /**
     * 获取委托单数据
     */
    private void loadDepute() {
//        waitingDialog.show(getActivity().getResources().getString(R.string.tradeOperate_killOrder_waitingLoading));

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        FuncCall<LoadDeputeModel_in, ListDeputeModel_out> funcCall = new FuncCall<>();
        funcCall.FuncResultHandler = loadDeputeSuccHandler;
        funcCall.FuncErrHandler = getLoadDeputeFailHandler;
        funcCall.Call("GetDepute", new LoadDeputeModel_in(), ListDeputeModel_out.class, errorDict);
        if (errorDict != null && errorDict.elements().hasMoreElements()) {
            //获得失败
            AlertMsg("获得下单数据失败!");

        }
    }

    /**
     * 获得单数据成功
     */
    private Handler loadDeputeSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ListDeputeModel_out deutes = (ListDeputeModel_out) msg.obj;
            DeputeList = deutes.getItemCollection();


            //加载数据
            initListView(true);

            //用户金额变化
            UserInfoLoader userInfoLoader = new UserInfoLoader();
            userInfoLoader.load();
        }
    };

    /**
     * 获得单数据失败
     */
    private Handler getLoadDeputeFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //显示提示对话框
            AlertMsg("获得下单数据失败!");

        }
    };


    /**
     * 撤单
     *
     * @param id
     */
    private void cancelDepute(int id) {
        waitingDialog.show(getActivity().getResources().getString(R.string.tradeOperate_killOrder_waitingKill));

        ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
        CancelDeputeModel_in model_in = new CancelDeputeModel_in();
        model_in.setDeputeId(id);
        FuncCall<CancelDeputeModel_in, ResultModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = cancelFailHandler;
        funcCall.FuncResultHandler = cancelSuccHandler;
        funcCall.Call("CancelDepute", model_in, ResultModel_out.class, dict);
        if (dict != null && dict.keySet().size() > 0) {

            //显示提示对话框
            AlertMsg("访问撤单接口失败!");
        }
    }

    /**
     * 撤单成功
     */
    private Handler cancelSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ResultModel_out ret = (ResultModel_out) msg.obj;
            //显示提示对话框
            AlertMsg(ret.getMsg());

            if (ret.getIsSucc()) {
                loadDepute();
            }
        }
    };
    /**
     * 撤单失败
     */
    private Handler cancelFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            //显示提示对话框
            AlertMsg(errorModel_out.getErrorMsg());


        }
    };


    /**
     * 弹出提示对话框
     *
     * @param msg
     */
    private void AlertMsg(String msg) {

        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
        MyAlertDialog dialog = new MyAlertDialog(getActivity());
        dialog.tipDialog(msg);
    }


    /**
     * 监控用户订单信息发生变化
     */
    private Handler deputeChangeHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            //刷新订单
            loadDepute();
        }
    };


}
