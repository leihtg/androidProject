package dianshi.matchtrader.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dianshi.matchtrader.model.NewsModel_out;
import com.dianshi.matchtrader.model.PageListNewsModel_out;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.activity.MainActivity;
import dianshi.matchtrader.activity.NoticeDetailActivity;
import dianshi.matchtrader.adapter.NoticeAdapter;
import dianshi.matchtrader.dialog.MyAlertDialog;

import com.dianshi.matchtrader.model.PageListBaseModel_in;

import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.view.PullRefreshView;


public class NoticeListFragment extends BaseFragment implements AdapterView.OnItemClickListener {


    Context context;
    int page = 0;
    int pageSize = 10;
    int allCount = 0;
    ListView listView;
    View view;
    ArrayList<NewsModel_out> array;

    //整体下拉刷新控件
    PullRefreshView pullRefreshView;

    NoticeAdapter adapter;
    //listview脚标中的表示正在加载的图片
    ImageView img_loading;
    //listview脚标中的表示加载状态的文字
    TextView tv_loading;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice_list, container, false);

        //初始化Toolbar
        initToolbar();

        //初始化下拉刷新
        initPullRefresh();

        //初始化Listview
        initListview();

        //检测是否有公告
        GlobalSingleton.CreateInstance().ServerPushHandler.regNewsHandler(newPushHandler);
        return view;
    }

    /**
     * 有新公告
     */
    private Handler newPushHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            page = 0;
            getNetInfo();

        }

    };


    /**
     * 初始化actionBar
     */
    public void initToolbar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.actionbarTitleChange(R.string.noticeNav);
    }

    /**
     * 初始化下拉刷新
     */
    public void initPullRefresh() {

        pullRefreshView = (PullRefreshView) view.findViewById(R.id.pullRefreshView_noticeList);
        //因为默认的是2 ，这边默认的不同，所以要改动一下
        pullRefreshView.setListviewPosotion(1);

        pullRefreshView.setOnRefreshListener(new PullRefreshView.PullRefreshListener() {
            @Override
            public void onRefresh(PullRefreshView view) {
                page = 0;
                getNetInfo();

            }
        });
    }

    /**
     * 初始化listView
     */
    public void initListview() {
        listView = (ListView) view.findViewById(R.id.listview_noticeList);
        //网络获取
        getNetInfo();
        //设置行单击事件
        listView.setOnItemClickListener(this);
        listView.setSelection(0);


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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //跳转到详情页面
        Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
        intent.putExtra("NewsId", array.get(position).getId());
        startActivity(intent);
    }


    /**
     * 申请网络数据
     */
    private void getNetInfo() {


        //如果是最后一页
        if (!pullRefreshView.isRefreshing() && allCount == listView.getCount() - 1) {
            Toast.makeText(getActivity(), R.string.pullRefresh_lastPage, Toast.LENGTH_SHORT).show();
            img_loading.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);
            return;
        }
        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        PageListBaseModel_in model_in = new PageListBaseModel_in();
        model_in.setPage(page);
        model_in.setPageSize(pageSize);

        FuncCall<PageListBaseModel_in, PageListNewsModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = loadFailHandler;
        funcCall.FuncResultHandler = loadSuccHandler;

        funcCall.Call("NewsList", model_in, PageListNewsModel_out.class, errorDict);

        if (errorDict.keys().hasMoreElements()) {
            AlertMessage("新闻加载失败");
        }
    }

    /**
     * 申请成功
     */
    private Handler loadSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            PageListNewsModel_out pageListNewsModel = (PageListNewsModel_out) msg.obj;
            allCount = pageListNewsModel.getAllCount();


            //第一次加载绑定适配器
            if (page == 0) {
                array = (ArrayList<NewsModel_out>) pageListNewsModel.getEntityCollection();
                //绑定适配器
                adapter = new NoticeAdapter(getActivity(), array);
                listView.setAdapter(adapter);
                View footer = LayoutInflater.from(context).inflate(R.layout.footer_listview, null);
                listView.addFooterView(footer);
                img_loading = (ImageView) footer.findViewById(R.id.img_loadMore);
                tv_loading = (TextView) footer.findViewById(R.id.tv_loadMore);

            } else {
                //将每次获取的新数据都追加到原ArrayList的后面,节省流量
                ArrayList<NewsModel_out> array_new = (ArrayList<NewsModel_out>) pageListNewsModel.getEntityCollection();
                for (NewsModel_out model : array_new) {
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

    /**
     * 申请失败
     */
    private Handler loadFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertMessage("新闻加载失败");
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
