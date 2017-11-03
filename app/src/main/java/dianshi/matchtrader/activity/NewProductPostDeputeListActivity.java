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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.AskRecordModel_out;
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.model.IDModel_in;
import com.dianshi.matchtrader.model.PageListAskRecordModel_out;
import com.dianshi.matchtrader.model.PageListBaseModel_in;
import com.dianshi.matchtrader.model.ResultModel_out;
import com.dianshi.matchtrader.server.FuncCall;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.ProductPostDeputeListAdapter;
import dianshi.matchtrader.customInterface.OnNoDoubleItemClickListener;
import dianshi.matchtrader.dialog.DetailDialog;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.dialog.WaitingDialog;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.view.PullRefreshView;

/**
 * Created by Administrator on 2017/1/3.
 */
public class NewProductPostDeputeListActivity extends ToolBarActivity {

    Context context;
    //数据列表
    ListView listView;
    //整体下拉刷新控件
    PullRefreshView pullRefreshView;
    //listview的适配器
    ProductPostDeputeListAdapter adapter;
    //listview展示的数据列表
    ArrayList<AskRecordModel_out> array;
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

    WaitingDialog waitingDialog;
    DetailDialog detailDialog;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_post_depute_list);

        context = this;

        //初始化下拉刷新
        initPullRefresh();
        //初始化列表标题
        initTitle();
        //初始化Listview
        initListView();

    }

    /**
     * 设置toolbar标题
     *
     * @param tv
     */
    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv.setText(R.string.newProductPostDepute_title);
    }

    private void initTitle() {
        TextView tv_first = (TextView) findViewById(R.id.tv_header_tv_first);
        TextView tv_second = (TextView) findViewById(R.id.tv_header_tv_second);
        TextView tv_third = (TextView) findViewById(R.id.tv_header_tv_third);
        TextView tv_four = (TextView) findViewById(R.id.tv_header_tv_four);

        tv_first.setText(R.string.newProductPostDepute_Name);
        tv_second.setText(R.string.newProductPostDepute_count);
        tv_third.setText(R.string.newProductPostDepute_money);
        tv_four.setText(R.string.newProductPostDepute_note);
    }


    /**
     * 初始化下拉刷新
     */
    public void initPullRefresh() {

        pullRefreshView = (PullRefreshView) findViewById(R.id.pullRefreshView_newProductPostDeputeList);
        pullRefreshView.setOnRefreshListener(new PullRefreshView.PullRefreshListener() {
            @Override
            public void onRefresh(PullRefreshView view) {
                page = 0;
                getNetInfo();
            }
        });
    }


    /**
     * 初始化Listview
     */
    public void initListView() {
        listView = (ListView) findViewById(R.id.listview_newProductPostDeputeList);
        listView.setOnItemClickListener(onNoDoubleItemClickListener);

        //申请网络数据
        page = 0;
        getNetInfo();
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
     * 申请网络数据
     */
    public void getNetInfo() {

        //如果是最后一页
        if (!pullRefreshView.isRefreshing() && listView.getFooterViewsCount() > 0 && allCount == (listView.getCount() - listView.getFooterViewsCount() - listView.getHeaderViewsCount())) {
            Toast.makeText(context, R.string.pullRefresh_lastPage, Toast.LENGTH_SHORT).show();
            img_loading.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);
            return;
        }


        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        PageListBaseModel_in model_in = new PageListBaseModel_in();
        model_in.setPage(page);
        model_in.setPageSize(PageSize);
        FuncCall<PageListBaseModel_in, PageListAskRecordModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = funcErrHandler;
        funcCall.FuncResultHandler = funcResultHandler;
        funcCall.Call("AskRecordingCheck", model_in, PageListAskRecordModel_out.class, errorDict);


        if (errorDict.keys().hasMoreElements()) {
            //显示提示对话框
            AlertMessage(getResources().getString(R.string.newProductPostDepute_errorMsg_getPostDeputeListFail));
        }
    }


    /**
     * 申请申购委托列表接口失败
     */
    private Handler funcErrHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            //显示提示对话框;
            AlertMessage(errorModel_out.getErrorMsg());
        }
    };
    /**
     * 申购列表返回结果
     */
    private Handler funcResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            PageListAskRecordModel_out result = (PageListAskRecordModel_out) msg.obj;

            //获取数据
            allCount = result.getAllCount();

            //第一次加载绑定适配器
            if (page == 0) {
                array = (ArrayList<AskRecordModel_out>) result.getEntityCollection();
                adapter = new ProductPostDeputeListAdapter(context, array);
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
                ArrayList<AskRecordModel_out> array_new = (ArrayList<AskRecordModel_out>) result.getEntityCollection();
                for (AskRecordModel_out model : array_new) {
                    array.add(model);
                }
                //适配器刷新
                adapter.refresh(array);
            }

            //如果是处于正在刷新的状态
            if (pullRefreshView != null && pullRefreshView.isRefreshing()) {                //结束下拉刷新
                pullRefreshView.finishRefresh();
            }

            //加载完毕隐藏加载更多脚标内容
            img_loading.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);
        }
    };


    private void cancelDeputeRecord(int recordId) {

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        IDModel_in model_in = new IDModel_in();
        model_in.setId(recordId);
        FuncCall<IDModel_in, ResultModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = cancelFuncErrHandler;
        funcCall.FuncResultHandler = cancelFuncSuccHandler;
        funcCall.Call("CancelAskRecord", model_in, ResultModel_out.class, errorDict);


        if (errorDict.keys().hasMoreElements()) {
            //显示提示对话框
            AlertMessage(getResources().getString(R.string.newProductPostDepute_errorMsg_cancelPostDeputeFail));
        }
    }

    /**
     * 申请取消委托接口失败
     */
    private Handler cancelFuncErrHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            //显示提示对话框
            AlertMessage(errorModel_out.getErrorMsg());
        }
    };
    /**
     * 申请取消委托接口成功
     */
    private Handler cancelFuncSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ResultModel_out result = (ResultModel_out) msg.obj;
            if (result != null) {
                if (result.getIsSucc()) {

                    if (waitingDialog != null) {
                        waitingDialog.dismiss();
                    }

                    page = 0;
                    pullRefreshView.startRefresh();


                } else {
                    AlertMessage(result.getMsg());
                }


            } else {
                AlertMessage(getResources().getString(R.string.newProductPostDepute_errorMsg_cancelPostDeputeFail));

            }

        }
    };

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
            initDialog(position);
        }
    };


    /**
     * 弹出提示对话框
     *
     * @param message
     */
    public void AlertMessage(String message) {
        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
        //如果是处于正在刷新的状态
        if (pullRefreshView != null && pullRefreshView.isRefreshing()) {
            //结束下拉刷新
            pullRefreshView.finishRefresh();
        }
        //显示提示对话框
        MyAlertDialog dialog = new MyAlertDialog(context);
        dialog.tipDialog(message);
    }

    /**
     * 详细信息对话框
     *
     * @param position
     */
    public void initDialog(final int position) {

        String[] keys = getResources().getStringArray(R.array.newProductPostDepute_dialog);

        String[] values = {
                array.get(position).getProductName(),
                array.get(position).getCode(),
                MathUtil.toBigDecimal(array.get(position).getAskMoney(), 2).toString(),
                String.valueOf(array.get(position).getCount()),
                array.get(position).getStatus() == 0 ? getResources().getString(R.string.newProductPostDepute_status_noSuccess) : getResources().getString(R.string.newProductPostDepute_status_success),
                (array.get(position).getNote() == null || array.get(position).getNote().equals("")) ? "--" : array.get(position).getNote()

        };

        detailDialog = new DetailDialog(context, getResources().getString(R.string.newProductPostDepute_dialog_title), getResources().getString(R.string.dialog_ensure), getResources().getString(R.string.dialog_cancel), keys, values, dialogCallBack);
        detailDialog.show();

    }

    MyAlertDialog.DialogCallBack dialogCallBack = new MyAlertDialog.DialogCallBack()

    {
        @Override
        public void onEnSure() {
            if (detailDialog != null && detailDialog.isShowing()) {
                detailDialog.dismiss();
            }

            if (waitingDialog == null) {
                waitingDialog = new WaitingDialog(context);

            }

            waitingDialog.show(getResources().getString(R.string.newProductPostDepute_loading_cancelPostDepute));
            cancelDeputeRecord(array.get(position).getId());

        }

        @Override
        public void onCancel() {

            if (detailDialog != null && detailDialog.isShowing()) {
                detailDialog.dismiss();
            }
        }
    };


}
