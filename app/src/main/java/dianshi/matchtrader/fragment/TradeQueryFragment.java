package dianshi.matchtrader.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.activity.MoneyInRecordActivity;
import dianshi.matchtrader.activity.MoneyLogRecordActivity;
import dianshi.matchtrader.activity.MoneyOutRecordActivity;
import dianshi.matchtrader.activity.TradeDetailRecordActivity;
import dianshi.matchtrader.activity.TradeSumRecordActivity;
import dianshi.matchtrader.adapter.TradeQueryAdapter;
import dianshi.matchtrader.toolbar.BaseFragment;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class TradeQueryFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    View view;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trade_operate_query, null);

        //初始化Listview
        initListview();

        return view;
    }

    /**
     * 初始化Listview
     */
    public void initListview() {
        listView = (ListView) view.findViewById(R.id.listview_tradeOperate_query);


        //添加数据
        ArrayList<String> array = new ArrayList<>();
        String[] navlist = getResources().getStringArray(R.array.nav_tradeOperate_query);
        for (String s : navlist) {
            array.add(s);
        }

        //listview绑定adapter
        TradeQueryAdapter adapter = new TradeQueryAdapter(getActivity(), array);
        listView.setAdapter(adapter);
        //listview设置行单击事件
        listView.setOnItemClickListener(this);
    }


    /**
     * listview的行单击事件监听
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;

        switch (position) {

            case 0://成交明细
                intent = new Intent(getActivity(), TradeDetailRecordActivity.class);
                break;
            case 1://成交汇总
                intent = new Intent(getActivity(), TradeSumRecordActivity.class);
                break;
            case 2://入金记录页面
                intent = new Intent(getActivity(), MoneyInRecordActivity.class);
                break;
            case 3://出金记录
                intent = new Intent(getActivity(), MoneyOutRecordActivity.class);
                break;
            case 4://资金明细
                intent = new Intent(getActivity(), MoneyLogRecordActivity.class);
                break;

        }

        if (intent != null) {
            startActivity(intent);
        }


    }
}
