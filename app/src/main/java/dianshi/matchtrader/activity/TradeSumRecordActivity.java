package dianshi.matchtrader.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dianshi.matchtrader.model.PageListDoneTotalModel_out;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.model.DoneTotalModel_out;
import com.dianshi.matchtrader.model.NoticeMsgType;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.TradeSumRecordAdapter;

import com.dianshi.matchtrader.model.PageListBaseModel_in;

import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.view.PullRefreshView;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
public class TradeSumRecordActivity extends ToolBarActivity {

    Context context;

    //数据列表
    ListView listView;
    //整体下拉刷新控件
    PullRefreshView pullRefreshView;
    //listview的适配器
    TradeSumRecordAdapter adapter;
    //listview展示的数据列表
    ArrayList<DoneTotalModel_out> array;

    //加载的页码
    int page;
    //每一页加载的数目
    int PageSize = 10;
    //数据的总条数
    int allCount;
    //listview脚标中的表示正在加载的图片
    ImageView img_loading;
    //listview脚标中的表示加载状态的文字
    TextView tv_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_sum_record);
        context = this;
        //初始化标题
        initHeader();
        //初始化下拉刷新
        initPullRefresh();
        //初始化Listview
        initListView();


//        GlobalSingleton.CreateInstance().ServerPushHandler.regPositionHandler(refreshHandler);
//        GlobalSingleton.CreateInstance().ServerPushHandler.regDeputeHandler(refreshHandler);
    }

    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NoticeMsgType.SelfDeputeChange) {
                getNetInfo();
            } else if (msg.what == NoticeMsgType.SelfPostionChange) {
                getNetInfo();
            }
        }
    };

    /**
     * 初始化下拉刷新
     */
    public void initPullRefresh() {

        pullRefreshView = (PullRefreshView) findViewById(R.id.pullRefreshView_tradeSumRecord);
        pullRefreshView.setOnRefreshListener(new PullRefreshView.PullRefreshListener() {
            @Override
            public void onRefresh(PullRefreshView view) {
                getNetInfo();
            }
        });
    }

    private void initHeader() {
        TextView tv_first = (TextView) findViewById(R.id.tv_header_tv_first);
        TextView tv_second = (TextView) findViewById(R.id.tv_header_tv_second);
        TextView tv_third = (TextView) findViewById(R.id.tv_header_tv_third);
        TextView tv_four = (TextView) findViewById(R.id.tv_header_tv_four);

        tv_first.setText(R.string.trade_sum_name);
        tv_second.setText(R.string.trade_sum_totalTaxRate);
        tv_third.setText(R.string.trade_sum_amount);
        tv_four.setText(R.string.trade_sum_averagePrice);
    }

    /**
     * 设置toolbar标题
     *
     * @param tv
     */
    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv.setText(R.string.tradeSumRecord_tradeSumRecord);
    }

    public void initListView() {

        listView = (ListView) findViewById(R.id.listview_tradeSumRecord);

        //获取网络数据
        getNetInfo();

        //监听滚动事件
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {

                            img_loading.setVisibility(View.VISIBLE);
                            tv_loading.setVisibility(View.VISIBLE);
                            //给图片添加动态效果
                            Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_progressbar);
                            img_loading.setAnimation(anim);

                            //加载下一页
                            page++;
                            //申请下一页的数据
                            getNetInfo();
                        }

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });
    }


    /**
     * 网络获取
     */
    private void getNetInfo() {
        //如果是最后一页
        if (!pullRefreshView.isRefreshing() && listView.getFooterViewsCount() > 0 && allCount == (listView.getCount() - listView.getFooterViewsCount() - listView.getHeaderViewsCount())) {
            Toast.makeText(context, R.string.pullRefresh_lastPage, Toast.LENGTH_SHORT).show();
            img_loading.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);
            return;
        }


        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        FuncCall<ModelInBase, PageListDoneTotalModel_out> funcCall = new FuncCall<>();
        PageListBaseModel_in model_in = new PageListBaseModel_in();
        model_in.setPage(page);
        model_in.setPageSize(PageSize);

        funcCall.FuncResultHandler = loadSuccHandler;
        funcCall.FuncErrHandler = loadFailHandler;

        //DoneTotal
        funcCall.Call("HistoryDoneTotal", model_in, PageListDoneTotalModel_out.class, errorDict);

        if (errorDict.keys().hasMoreElements()) {
            //显示提示对话框
            AlertMessage("获得数据失败!");
        }
    }

    private Handler loadSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            PageListDoneTotalModel_out lists = (PageListDoneTotalModel_out) msg.obj;

            //获取数据
            allCount = lists.getAllCount();

            //第一次加载绑定适配器
            if (page == 0) {

                array = (ArrayList<DoneTotalModel_out>) lists.getEntityCollection();
                adapter = new TradeSumRecordAdapter(context, array);
                //绑定适配器
                listView.setAdapter(adapter);
                if (listView.getFooterViewsCount() < 1) {
                    View footer = LayoutInflater.from(context).inflate(R.layout.footer_listview, null);
                    listView.addFooterView(footer);
                    img_loading = (ImageView) footer.findViewById(R.id.img_loadMore);
                    tv_loading = (TextView) footer.findViewById(R.id.tv_loadMore);
                }


            } else {


                //将每次获取的新数据都追加到原ArrayList的后面,节省流量
                ArrayList<DoneTotalModel_out> array_new = (ArrayList<DoneTotalModel_out>) lists.getEntityCollection();
                for (DoneTotalModel_out model : array_new) {
                    array.add(model);
                }
                //适配器刷新
                adapter.refresh(array);
            }

            //如果是处于正在刷新的状态
            if (pullRefreshView != null && pullRefreshView.isRefreshing()) {
                //结束下拉刷新
                pullRefreshView.finishRefresh();
            }

            //加载完毕隐藏加载更多脚标内容
            img_loading.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);


        }
    };

    private Handler loadFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //显示提示对话框
            AlertMessage("获得数据失败!");


        }
    };

    /**
     * 弹出提示对话框
     *
     * @param message
     */
    public void AlertMessage(String message) {
        //如果是处于正在刷新的状态
        if (pullRefreshView != null && pullRefreshView.isRefreshing()) {
            //结束下拉刷新
            pullRefreshView.finishRefresh();
        }
        //显示提示对话框
        MyAlertDialog dialog = new MyAlertDialog(context);
        dialog.tipDialog(message);
    }
}
