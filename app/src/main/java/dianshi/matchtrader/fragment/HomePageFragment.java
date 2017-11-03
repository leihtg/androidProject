package dianshi.matchtrader.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.dianshi.matchtrader.server.GlobalSingleton;

import java.lang.reflect.Field;
import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.activity.CustomerServiceActivity;
import dianshi.matchtrader.activity.MainActivity;
import dianshi.matchtrader.activity.MoneyLogRecordActivity;
import dianshi.matchtrader.activity.MoneyOutActivity;
import dianshi.matchtrader.activity.NewProductPostDeputeListActivity;
import dianshi.matchtrader.activity.NewProductPostListActivity;
import dianshi.matchtrader.activity.NewProductPostResultListActivity;
import dianshi.matchtrader.activity.OfficialWebsiteActivity;
import dianshi.matchtrader.activity.OperateLogActivity;
import dianshi.matchtrader.activity.TradeSumRecordActivity;
import dianshi.matchtrader.adapter.MyGridAdapter;
import dianshi.matchtrader.model.HomeGridModel;
import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.util.RegexpValidatorUtils;
import dianshi.matchtrader.util.ToastUtils;
import dianshi.matchtrader.view.BannnerView;
import dianshi.matchtrader.view.MyGridView;


public class HomePageFragment extends BaseFragment implements ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {


    /**
     * 轮播图
     */
    private BannnerView bannnerView;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray;

    /**
     * gridView
     */
    private MyGridView gridView;

    public HomePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //configImageLoader();
        //initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        //初始化gridview
        initGridView(view);
        //初始化轮播图
        initBanner(view);
        //初始化actionBar
        initActionBar();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 初始化actionBar
     */
    public void initActionBar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.actionbarTitleChange(R.string.app_name);
    }


    /**
     * 初始化GridView
     *
     * @param view
     */
    private void initGridView(View view) {


        String[] imgStrs = null;
        int[] imgIds = null;

        //判断是否有出金

        if (GlobalSingleton.IsHaveMoneyOut) {

            imgStrs = new String[]{"日志", "出金", "成交", "官网", "客服", "开户", "新品申购", "申购委托", "申购结果"};
            imgIds = new int[]{R.mipmap.school, R.mipmap.money_out,
                    R.mipmap.money_in, R.mipmap.web_page,
                    R.mipmap.service, R.mipmap.create_account, R.mipmap.homepage_product_post, R.mipmap.homepage_product_post_depute,
                    R.mipmap.homepage_product_post_result};
        } else {
            imgStrs = new String[]{"操作日志", "资金明细", "成交汇总", "官网", "客服", "开户", "新品申购", "申购委托", "申购结果"};

            imgIds = new int[]{R.mipmap.school, R.mipmap.money_out,
                    R.mipmap.money_in, R.mipmap.web_page,
                    R.mipmap.service, R.mipmap.create_account, R.mipmap.homepage_product_post, R.mipmap.homepage_product_post_depute,
                    R.mipmap.homepage_product_post_result};

        }


        ArrayList<HomeGridModel> array = new ArrayList<>();
        for (int i = 0; i < imgStrs.length; i++) {
            HomeGridModel model = new HomeGridModel();
            model.setImageId(imgIds[i]);
            model.setFunctionStr(imgStrs[i]);
            array.add(model);
        }


        gridView = (MyGridView) view.findViewById(R.id.home_grid);
        gridView.setAdapter(new MyGridAdapter(this.getContext(), array));
        gridView.setOnItemClickListener(this);


    }

    /**
     * 初始化轮播图
     *
     * @param view
     */
    private void initBanner(View view) {
        bannnerView = (BannnerView) view.findViewById(R.id.home_banner);

        //载入图片资源ID
        imgIdArray = new int[]{R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4, R.mipmap.banner5};

        //加载显示的图片
        bannnerView.setShowPics(imgIdArray, true, true);
        //设置指示当前页的小点图片
        bannnerView.setTipPics(R.mipmap.icon_point_pre, R.mipmap.icon_point);
        //设置滚动时间
//        bannnerView.setTime(4);


    }

    /**
     * gridView 的子项单击事件
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

            case 0://操作日志
                intent = new Intent(getActivity(), OperateLogActivity.class);
                break;
            case 1://出金或者资金明细
                if (GlobalSingleton.IsHaveMoneyOut) {
                    intent = new Intent(getActivity(), MoneyOutActivity.class);
                } else {
                    intent = new Intent(getActivity(), MoneyLogRecordActivity.class);
                }

                break;
            case 2://成交明细
                intent = new Intent(getActivity(), TradeSumRecordActivity.class);
                break;
            case 3://官网
                if (GlobalSingleton.HomePage == null || GlobalSingleton.HomePage.equals("") || !RegexpValidatorUtils.isNetUrl(GlobalSingleton.HomePage)) {
                    ToastUtils.showToast(getActivity(), "敬请期待~");
                } else {
                    intent = new Intent(getActivity(), OfficialWebsiteActivity.class);
                }

                break;
            case 4://客服
                intent = new Intent(getActivity(), CustomerServiceActivity.class);
                break;
            case 5://开户
                if (GlobalSingleton.RefOpenUrl == null || GlobalSingleton.RefOpenUrl.equals("") || !RegexpValidatorUtils.isNetUrl(GlobalSingleton.RefOpenUrl)) {
                    ToastUtils.showToast(getActivity(), "敬请期待~");
                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(GlobalSingleton.RefOpenUrl));
                }
                break;

            case 6://新品申购
                intent = new Intent(getActivity(), NewProductPostListActivity.class);
                break;
            case 7://申购委托
                intent = new Intent(getActivity(), NewProductPostDeputeListActivity.class);
                break;
            case 8://申购结果
                intent = new Intent(getActivity(), NewProductPostResultListActivity.class);
                break;
        }

        if (intent != null)
            startActivity(intent);

    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.mipmap.icon_point);
            } else {
                tips[i].setBackgroundResource(R.mipmap.icon_point_pre);
            }
        }
    }
}
