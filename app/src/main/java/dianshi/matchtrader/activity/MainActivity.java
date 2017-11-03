package dianshi.matchtrader.activity;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.AppManager;
import com.dianshi.matchtrader.model.NoticeMsgType;
import com.dianshi.matchtrader.model.ProductCategoryModel_out;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.dianshi.matchtrader.userinfo.UserInfoLoader;
import com.dianshi.matchtrader.userinfo.UserInfoPool;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.MainNavListAdapter;
import dianshi.matchtrader.adapter.MainSpinnerAdapter;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.fragment.ChooseListFragment;
import dianshi.matchtrader.fragment.HomePageFragment;
import dianshi.matchtrader.fragment.NoticeListFragment;
import dianshi.matchtrader.fragment.PriceListFragment;
import dianshi.matchtrader.fragment.TradeOperateFragment;
import dianshi.matchtrader.toolbar.BaseActivity;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    //首页碎片
    private HomePageFragment homePageFragment;
    //行情碎片
    private PriceListFragment priceListFragment;
    //自选碎片
    private ChooseListFragment chooseListFragment;
    //公告碎片
    private NoticeListFragment noticeListFragment;
    //交易碎片
    private TradeOperateFragment tradeOperateFragment;
    private FragmentManager fragmentManager;
    //单项按钮组
    private RadioGroup radioGroup;
    RadioButton rbtn_home, rbtn_price, rbtn_select, rbtn_notice, rbtn_trade;


    Context context;
    //标题栏
    Toolbar toolbar;
    //标题栏中的标题
    TextView txt_title;
    //标题栏中的下拉列表
    Spinner spinner;

    //自定义接口声明
    OnSpinnerClickedListener onSpinnerClickedListener;
    //本地存储文件
    SharedPreferences sharedPreferences;
    //页面跳转
    Intent intent = null;


    /**
     * 保证操作的是同一个fragment对象，防止fragment重叠
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (homePageFragment == null && fragment instanceof HomePageFragment) {
            homePageFragment = (HomePageFragment) fragment;
        } else if (priceListFragment == null && fragment instanceof PriceListFragment) {
            priceListFragment = (PriceListFragment) fragment;
        } else if (chooseListFragment == null && fragment instanceof ChooseListFragment) {
            chooseListFragment = (ChooseListFragment) fragment;
        } else if (noticeListFragment == null && fragment instanceof NoticeListFragment) {
            noticeListFragment = (NoticeListFragment) fragment;
        } else if (tradeOperateFragment == null && fragment instanceof TradeOperateFragment) {
            tradeOperateFragment = (TradeOperateFragment) fragment;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        int product_id = intent.getIntExtra("product_id", -1);
        if (product_id == -1) {
            //清理保存的商品数据
            sharedPreferences = getSharedPreferences(APPFinal.ShAERD_FILE_PRODUCT, MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化toolBar
        initToolBar();

        //默认显示第一个碎片
        setTabSelection(0);

        //初始化radiogroup
        initRadioGroup();

        //初始化DrawerLayout
        initDrawerLayout();

        //处理跳转到本页面的传过来的数据
        initIntent();

        //初始化侧边栏
        initListView();


        context = MainActivity.this;
        //检测是否有远程登录
        GlobalSingleton.CreateInstance().ServerPushHandler.setRemotingLoginHandler(exitHandler);
        //检测是否在做开盘准备
        GlobalSingleton.CreateInstance().ServerPushHandler.setClearHandler(exitHandler);
        //检测是否有商品变化
        GlobalSingleton.CreateInstance().ServerPushHandler.setProductChangeHandler(exitHandler);
        //检测是否有公告
        GlobalSingleton.CreateInstance().ServerPushHandler.regNewsHandler(newPushHandler);
        //检测用户金额
        GlobalSingleton.CreateInstance().ServerPushHandler.regMoneyHandler(moneyChangeHandler);

    }

    private Handler exitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NoticeMsgType.Clear:
                    resetConnectDialog("系统做开盘准备，请退出重新登录！");
                    break;
                case NoticeMsgType.RemotingLogin:
                    resetConnectDialog("您的账号已在另外一台设备登录,如非本人操作,则密码可能已泄漏,建议修改密码");
                    break;
                case NoticeMsgType.ProductChange:
                    resetConnectDialog("系统产品已经发生变化!请退出重新登录！");
                    break;
            }
        }
    };

    MyAlertDialog resetConnectDialog;

    /**
     * 弹出重连对话框
     *
     * @param msg
     */
    public void resetConnectDialog(String msg) {

        if (resetConnectDialog == null) {
            resetConnectDialog = new MyAlertDialog(AppManager.getAppManager().currentActivity());

        }

        resetConnectDialog.ensureDialog("下线通知", msg, "", "重新登录", "退出程序", new MyAlertDialog.DialogCallBack() {
            @Override
            public void onEnSure() {
                resetConnectDialog.dismiss();
                resetConnectDialog = null;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GlobalSingleton.CreateInstance().isRestartConnect = true;
                        GlobalSingleton.CreateInstance().getTCPSingleton().Connect();
                    }
                }).start();

            }

            @Override
            public void onCancel() {
                resetConnectDialog.dismiss();
                resetConnectDialog = null;
                //退出程序
                AppManager.getAppManager().AppExit();


            }
        });
    }


    /**
     * 推送新公告
     */
    private Handler newPushHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //发送通知栏
            initNotication();

        }

    };

    /**
     * 通知设置和发送通知
     */
    public void initNotication() {
        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("notification", "notification");
        // 创建一个PendingIntent，和Intent类似，不同的是由于不是马上调用，需要在下拉状态条出发的activity，所以采用的是PendingIntent,即点击Notification跳转启动到哪个Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("有新公告来啦")
                .setContentTitle("新公告通知")
                .setContentText("有公告刚刚发布，正在刷新中,请单击查看")
                .setContentIntent(pendingIntent)
                .setNumber(1)
                .getNotification();
        // 需要注意build()是在API
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        manager.notify(0, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示


    }


    /**
     * 处理跳转到本页面的传过来的数据
     * 交易详情页面-----交易
     * 公告通知-------公告
     */
    public void initIntent() {

        intent = getIntent();
        if (intent == null) {
            return;
        }
        String extra = intent.getStringExtra("upActivity");
        int product_id = intent.getIntExtra("product_id", -1);


        //上一个页面是StockDetailActivity，要显示交易碎片
        if (extra != null && extra.equals("StockDetailActivity")) {

            //存储商品信息
            sharedPreferences = getSharedPreferences(APPFinal.ShAERD_FILE_PRODUCT, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("product_id", product_id);
            editor.commit();


            setTabSelection(4);

        }

        String notification = intent.getStringExtra("notification");


        //从通知跳过来，要显示公告
        if (notification != null && notification.equals("notification")) {
            setTabSelection(3);
        }


    }


    /**
     * 初始化toolBar
     */
    public void initToolBar() {
        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置toolbar不显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //获得toolbar中自定义的标题控件对象
        txt_title = (TextView) toolbar.findViewById(R.id.txt_toolbar_title);
        spinner = (Spinner) toolbar.findViewById(R.id.spinner_toolbar);

        //得到分类列表
        final ArrayList<ProductCategoryModel_out> array = (ArrayList<ProductCategoryModel_out>) GlobalSingleton.CreateInstance().ProductPool.getProductCategory();
        ProductCategoryModel_out model = new ProductCategoryModel_out();
        model.setName("全部商品");
        model.setId(-1);

        if (array != null) {

            if (array.size() == 0) {//没有分类时保证添加的数据
                array.add(model);
            }
            if (array.get(0).getId() != -1) {
                array.add(0, model);
            }

        }


        //建立Adapter并且绑定数据源
        MainSpinnerAdapter adapter = new MainSpinnerAdapter(MainActivity.this, array);
        spinner.setAdapter(adapter);


        //spinner设置监听事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //得到当前选中分类的ID
                onSpinnerClickedListener.onSpinnerClick(array.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * 自定义一个接口，用来接收选择的分类ID
     *
     * @author zqy
     */
    public interface OnSpinnerClickedListener {
        /**
         * @param position
         */
        public void onSpinnerClick(int position);
    }


    /**
     * 暴露接口的公有方法
     *
     * @param onSpinnerClickedListener
     */
    public void setOnSpinnerClickedListener(OnSpinnerClickedListener onSpinnerClickedListener) {
        this.onSpinnerClickedListener = onSpinnerClickedListener;
    }


    /**
     * 初始化DrawerLayout
     */
    public void initDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //刷新用户余额
                UserInfoLoader userInfoLoader = new UserInfoLoader();
                userInfoLoader.load();
                userInfoLoader.loadSuccHandler = moneyChangeHandler;

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


        };
        mDrawerToggle.syncState();
        drawer.setDrawerListener(mDrawerToggle);
    }

    private TextView tv_userAccount;


    /**
     * 初始化侧边栏
     */
    public void initListView() {
        ListView listview = (ListView) findViewById(R.id.listview_main_nav);

        //头布局控件绑定的赋值
        View headView = LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header_main, null);
        TextView tv_userName = (TextView) headView.findViewById(R.id.tv_nav_userName);
        TextView tv_userAccountName = (TextView) headView.findViewById(R.id.tv_nav_accountName);

        tv_userAccount = (TextView) headView.findViewById(R.id.tv_nav_userAccount);
        tv_userName.setText("(" + GlobalSingleton.CreateInstance().UserInfoPool.getUserName() + ")");
        tv_userAccountName.setText(GlobalSingleton.CreateInstance().TrueName);
        ;

        //添加数据
        ArrayList<String> array = new ArrayList<>();
        String[] navlist = getResources().getStringArray(R.array.nav_list);
        for (String s : navlist) {
            array.add(s);
        }

        //listview添加头布局
        listview.addHeaderView(headView);
        //listview绑定adapter
        MainNavListAdapter adapter = new MainNavListAdapter(MainActivity.this, array);
        listview.setAdapter(adapter);
        //listview添加监听事件
        listview.setOnItemClickListener(this);


    }

    /**
     * 用户信息发生变化
     */
    private Handler moneyChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                ShowLastMoney();
            }
        }
    };

    private void ShowLastMoney() {

        UserInfoPool userInfoPool = GlobalSingleton.CreateInstance().UserInfoPool;
        double money = userInfoPool.getMoney() == null ? 0 : userInfoPool.getMoney();
        BigDecimal bigDecimal = BigDecimal.valueOf(money);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        tv_userAccount.setText(bigDecimal.toString());


    }

    /**
     * 改变actionBar中控件信息
     *
     * @param index
     */
    public void actionbarTitleChange(int index) {
        spinner.setVisibility(View.GONE);
        txt_title.setVisibility(View.VISIBLE);

        switch (index) {
            case 0://首页
                txt_title.setText(GlobalSingleton.TradeCenterName);
                break;
            case 1://行情
                txt_title.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                break;
            case 2://自选
                txt_title.setText(R.string.chooseNav);
                break;
            case 3://公告
                txt_title.setText(R.string.noticeNav);
                break;
            case 4://交易
                txt_title.setText(R.string.tradeNav);
                break;
        }

    }


    /**
     * 返回键监听
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }


        if (AppManager.getAppManager().isOnlyCurrentActivity()) {
            //显示对话框
            initDialog();
        } else {
            super.onBackPressed();
        }
    }


    /**
     * 初始化radioGroup
     */
    public void initRadioGroup() {
        radioGroup = (RadioGroup) findViewById(R.id.navRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int index = 0;
                switch (i) {
                    case R.id.home_tab:
                        index = 0;
                        break;
                    case R.id.price_tab:
                        index = 1;
                        break;
                    case R.id.select_tab:
                        index = 2;
                        break;
                    case R.id.notice_tab:
                        index = 3;
                        break;
                    case R.id.trade_tab:
                        index = 4;
                        break;
                }
                //显示相应碎片
                setTabSelection(index);
            }
        });
    }

    /**
     * 改变radioGroup的值
     */
    private void radioGroupchange(int index) {
        rbtn_home = (RadioButton) findViewById(R.id.home_tab);
        rbtn_price = (RadioButton) findViewById(R.id.price_tab);
        rbtn_select = (RadioButton) findViewById(R.id.select_tab);
        rbtn_notice = (RadioButton) findViewById(R.id.notice_tab);
        rbtn_trade = (RadioButton) findViewById(R.id.trade_tab);
        RadioButton[] rbtns = {rbtn_home, rbtn_price, rbtn_select, rbtn_notice, rbtn_trade};
        for (int i = 0; i < rbtns.length; i++) {
            if (i == index) {
                rbtns[i].setChecked(true);
            } else {
                rbtns[i].setChecked(false);
            }

        }
    }


    /**
     * 显示某个碎片
     *
     * @param index
     */
    private void setTabSelection(int index) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //隐藏碎片
        hideFragment(transaction);
        //改变radiogroup的值
        radioGroupchange(index);
        //改变标题
        actionbarTitleChange(index);

        switch (index) {
            case 0:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    transaction.add(R.id.content, homePageFragment);
                } else {
                    transaction.show(homePageFragment);
                }
                break;
            case 1:
                if (priceListFragment == null) {
                    priceListFragment = new PriceListFragment();
                    transaction.add(R.id.content, priceListFragment);
                } else {
                    transaction.show(priceListFragment);
                }

                break;
            case 2:
                if (chooseListFragment == null) {
                    chooseListFragment = new ChooseListFragment();
                    transaction.add(R.id.content, chooseListFragment);
                } else {
                    transaction.show(chooseListFragment);
                }
                break;
            case 3:
                if (noticeListFragment == null) {
                    noticeListFragment = new NoticeListFragment();
                    transaction.add(R.id.content, noticeListFragment);
                } else {
                    transaction.show(noticeListFragment);
                }
                break;
            case 4:
                if (tradeOperateFragment == null) {
                    tradeOperateFragment = new TradeOperateFragment();
                    transaction.add(R.id.content, tradeOperateFragment);
                } else {
                    transaction.show(tradeOperateFragment);
                }
                break;
        }

        transaction.commit();
    }

    /**
     * 隐藏碎片
     *
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }
        if (priceListFragment != null) {
            transaction.hide(priceListFragment);
        }
        if (noticeListFragment != null) {
            transaction.hide(noticeListFragment);
        }
        if (chooseListFragment != null) {
            transaction.hide(chooseListFragment);
        }
        if (tradeOperateFragment != null) {
            transaction.hide(tradeOperateFragment);
        }

    }


    /**
     * 初始化对话框
     */
    public void initDialog() {

        //显示提示对话框
        final MyAlertDialog dialog = new MyAlertDialog(MainActivity.this);
        dialog.ensureDialog("确定退出吗？", new MyAlertDialog.DialogCallBack() {
            @Override
            public void onEnSure() {
                //将用户ID统计登出
                MobclickAgent.onProfileSignOff();
                System.runFinalizersOnExit(true);
                //退出程序
                AppManager.getAppManager().AppExit();

            }

            @Override
            public void onCancel() {
                //关闭对话框
                dialog.dismiss();
            }
        });

    }


    /**
     * 行单击事件监听
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;

        switch (position - 1) {
            case 0://个人信息
                intent = new Intent(MainActivity.this, UserInfoActivity.class);
                break;
            case 1://操作日志
                intent = new Intent(MainActivity.this, OperateLogActivity.class);
                break;
            case 2://重置密码页面
                intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                break;
            case 3://免责声明
                intent = new Intent(MainActivity.this, DisclaimOfLiabilityActivity.class);
                break;
            case 4://关于
                intent = new Intent(MainActivity.this, CustomerServiceActivity.class);
                break;
            case 5://关于软件
                intent = new Intent(MainActivity.this, AboutSoftWareActivity.class);
                break;
            case 6://退出
                //弹出删除提示对话框
                initDialog();
                break;

        }

        if (intent != null) {
            startActivity(intent);
        }

        //关闭侧滑导航栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }


}
