package dianshi.matchtrader.view;

import java.util.ArrayList;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import android.content.Context;

import android.graphics.Color;
import android.graphics.PorterDuff;

import android.os.Environment;
import android.os.Handler;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;


/**
 * 自定义banner轮播图
 *
 * @author DR
 */

public class BannnerView extends RelativeLayout {


    /**
     * 上下文对象
     */

    Context context;

    /**
     * 装载轮播图片列表
     */

    private ArrayList<ImageView> array_showPics;

    /**
     * 装载下方提示当前页所在哪一面的图片列表
     */

    private ArrayList<ImageView> array_tipPics;

    /**
     * 要加载的图片网址
     */

    private ArrayList<String> array_imgUrls;


    /**
     * 网络图片的三级缓存存储地址
     */

    private String diskCachePath = Environment.getExternalStorageDirectory()
            + "/AA/";


    /**
     * 表示图片正在显示的图片id
     */

    private int checkedPic;

    /**
     * 表示图片未在显示的图片id
     */

    private int uncheckedPic;
    /**
     * 当前播放图片的下标
     */

    private int pageCurrentIndex = 0;
    /**
     * 网络还未加载出来时的默认图片id
     */

    private int dafaultLoadFailedPic;

    /**
     * 网络加载失败的默认图片id
     */

    private int dafaultLoadingPic;
    /**
     * 图片自动播放的延迟时间 设置为2秒
     */

    private int dafaultPlayTime = 4;

    /**
     * 是否可循环无限滑动
     */

    private boolean isLoopSlide = false;
    /**
     * 是否可自动循环播放
     */

    private boolean isAutoLoopPlay = false;
    /**
     * 是否提示图片位置的小点图片只有两个状态，选中和未选中
     */

    private boolean isOnlyTwoTipPics = false;

    /**
     * 展示轮播图片Viewpager
     */

    private ViewPager viewPager;
    /**
     * 展示显示当前轮播图片位置LinearLayout
     */

    private LinearLayout layout_tipPics;
    /**
     * 定时周期执行图片播放的线程池
     */

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 图片的三级缓存机制
     */

    BitmapUtils bitmapUtils;

    /**
     * 构造方法
     *
     * @param context 上下文对象
     */

    public BannnerView(Context context) {
        super(context);
        this.context = context;
        bitmapUtils = new BitmapUtils(context, diskCachePath);
    }

    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param attrs   自定义属性
     */

    public BannnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        bitmapUtils = new BitmapUtils(context, diskCachePath);
    }

    /**
     * 设置网络加载失败时的默认图片
     *
     * @param imgId
     */

    public void setDefaultLoadFailedImage(int imgId) {
        this.dafaultLoadFailedPic = imgId;
        bitmapUtils.configDefaultLoadFailedImage(imgId);
    }

    /**
     * 设置网络未加载出来时的默认图片
     *
     * @param imgId 图片id
     */


    public void setDefaultLoadingImage(int imgId) {
        this.dafaultLoadingPic = imgId;

        bitmapUtils.configDefaultLoadingImage(imgId);
    }

    /**
     * 添加轮播图上要显示的图片,网络获取
     *
     * @param imageUrls   网络图片的网址路径
     * @param isLoopSlide 是否要
     */

    public void setShowOnlinePics(String[] imageUrls, boolean isLoopSlide,
                                  boolean isAutoLoopPlay) {

        // 设置是否可以循环无限滑动
        this.isLoopSlide = isLoopSlide;
        // 设置是否可以自动定时播放
        this.isAutoLoopPlay = isAutoLoopPlay;

        // 设置图片的数量
        int length;
        if (isLoopSlide) {// 判断是否可循环滑动
            length = imageUrls.length + 2;
        } else {
            length = imageUrls.length;
        }

        // array装载内容，N+2个view 要展示的图片装在1~N, 0装的是最后一张 N+1装的是第一张
        array_showPics = new ArrayList<ImageView>();
        array_imgUrls = new ArrayList<String>();

        for (int i = 0; i < length; i++) {
            ImageView img = new ImageView(context);
            img.setScaleType(ScaleType.FIT_XY);
            array_showPics.add(img);
            // 初始化array_imgUrls
            if (i == 0) {
                array_imgUrls.add(imageUrls[imageUrls.length - 1]);
            } else if (i == length - 1) {

                array_imgUrls.add(imageUrls[0]);
            } else {
                array_imgUrls.add(imageUrls[i - 1]);
            }
        }

        // 初始化Viewpager
        initViewPager();

    }

    /**
     * 添加轮播图上要显示的图片
     *
     * @param imageIds    资源图片id
     * @param isLoopSlide 是要
     */

    public void setShowPics(int[] imageIds, boolean isLoopSlide,
                            boolean isAutoLoopPlay) {

        // 设置是否可以循环无限滑动
        this.isLoopSlide = isLoopSlide;
        // 设置是否可以自动定时播放
        this.isAutoLoopPlay = isAutoLoopPlay;
        // 设置图片的数量
        int length;
        if (isLoopSlide) {// 判断是否可循环滑动
            length = imageIds.length + 2;
        } else {
            length = imageIds.length;
        }
        // array装载内容，N+2个view 要展示的图片装在1~N, 0装的是最后一张 N+1装的是第一张
        array_showPics = new ArrayList<ImageView>();
        for (int i = 0; i < length; i++) {
            ImageView img = new ImageView(context);
            if (i == 0) {
                img.setImageResource(imageIds[imageIds.length - 1]);
            } else if (i == imageIds.length + 1) {
                img.setImageResource(imageIds[0]);
            } else {
                img.setImageResource(imageIds[i - 1]);
            }
            img.setScaleType(ScaleType.FIT_XY);

            array_showPics.add(img);
        }

        // 初始化Viewpager
        initViewPager();

    }

    /**
     * 添加显示轮播图片位置的小图片，只需选中、未选中两种状态的图片
     * 注意：该方法只能在setShowOnlinePics() 和setShowPics()之后调用
     *
     * @param checkedPic   当前页选中的图片
     * @param unCheckedPic 当前页未选中的图片
     */

    public void setTipPics(int checkedPic, int unCheckedPic) {

        this.isOnlyTwoTipPics = true;

        // 设置选中图片
        this.checkedPic = checkedPic;
        // 设置未选中图片
        this.uncheckedPic = unCheckedPic;
        // 设置图片的数量
        int length;
        if (array_showPics == null) {
            return;
        }
        if (isLoopSlide) {// 是否可循环滑动
            length = array_showPics.size() - 2;
        } else {
            length = array_showPics.size();
        }
        // 初始化array_tipPics
        array_tipPics = new ArrayList<ImageView>();
        for (int i = 0; i < length; i++) {
            ImageView img = new ImageView(context);
            if (i == 0) {
                img.setImageResource(checkedPic);
            } else {
                img.setImageResource(uncheckedPic);
            }
            array_tipPics.add(img);
        }

        // 初始化layout_tipPics
        initLayout_tipPics();
    }

    /**
     * 添加显示轮播图片位置的小图片，viewPager的每页都对应一个小图片
     *
     * @param imageIds 小图片的资源集合
     */

    @SuppressWarnings("unchecked")
    private void setTipPics(int[] imageIds) {

        this.isOnlyTwoTipPics = false;

        // 设置图片的数量
        int length;
        if (isLoopSlide) {// 是否可循环滑动
            length = array_showPics.size() - 2;
        } else {
            length = array_showPics.size();
        }
        // 初始化array_tipPics
        array_tipPics = new ArrayList<ImageView>();
        for (int i = 0; i < length; i++) {
            ImageView img = new ImageView(context);
            img.setImageResource(imageIds[i]);
            if (i == 0) {
                img.setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);
            }
            array_tipPics.add(img);
        }

        // 初始化layout_tipPics
        initLayout_tipPics();

    }

    /**
     * 设置自动重播时间
     *
     * @param seconds 延迟时间，以秒为单位
     */

    public void setTime(long seconds) {
        if (isAutoLoopPlay) {

            if (scheduledExecutorService != null) {
                try {
                    scheduledExecutorService.wait();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

            // 计时播放图片
            scheduledExecutorService = Executors
                    .newSingleThreadScheduledExecutor();
            // 此处将初始延迟时间和任务间隔时间设置为同样的时间
            scheduledExecutorService.scheduleWithFixedDelay(
                    new ViewPagerTask(), seconds, seconds, TimeUnit.SECONDS);
        }

    }

    /**
     * 初始化layout_tipPics
     */

    private void initLayout_tipPics() {
        layout_tipPics = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 15, 15, 15);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout_tipPics.setLayoutParams(lp);
        // 设置子View横向排列
        layout_tipPics.setOrientation(LinearLayout.HORIZONTAL);
        // 添加layout_tipPics到父布局
        addView(layout_tipPics);

        // 添加子控件
        for (int i = 0; i < array_tipPics.size(); i++) {
            LinearLayout.LayoutParams lp_img = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_img.setMargins(15, 0, 0, 0);
            array_tipPics.get(i).setLayoutParams(lp_img);
            layout_tipPics.addView(array_tipPics.get(i));
        }

    }

    /**
     * 初始化ViewPager
     */

    private void initViewPager() {

        // 创建ViewPager
        viewPager = new ViewPager(context);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        viewPager.setLayoutParams(lp);
        // 添加viewPager
        addView(viewPager);

        // 添加数据
        viewPager.setAdapter(new ViewPageAdapter());
        // 添加监听事件
        viewPager.setOnPageChangeListener(onPageChangeListener);
        // 如果是循环无限滑动
        if (isLoopSlide) {
            viewPager.setCurrentItem(1, false);
        }

        // 设置自动延时时间间隔
        setTime(dafaultPlayTime);

    }

    /**
     * ViewPager的监听事件
     */

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {

            pageCurrentIndex = arg0;

            // 判断array_tipPics是否为空
            if (array_tipPics != null && array_tipPics.size() != 0) {

                // 只有（1-N）时会改变
                if (arg0 < array_showPics.size() - 1 && arg0 > 0) {
                    System.out.println("--------" + arg0);

                    tipPicsChange(arg0 - 1);
                }
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // 判断是否设置循环无限滑动，如果没有结束本函数
            if (!isLoopSlide) {
                return;
            }
            // arg1=0代表停止滑动
            if (arg1 == 0) {
                // 滑到第一页
                if (arg0 == 0) {
                    // 跳到倒数第二页
                    viewPager.setCurrentItem(array_showPics.size() - 2, true);
                    // 滑到最后一页
                } else if (arg0 == array_showPics.size() - 1) {
                    // 跳到第二页
                    viewPager.setCurrentItem(1, true);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * 改变viewpage下方表示当前页的小红点显示
     *
     * @param currentPage viewpager当前显示的图片下标
     */

    private void tipPicsChange(int currentPage) {

        if (isOnlyTwoTipPics) {
            for (int i = 0; i < array_tipPics.size(); i++) {
                if (i == currentPage) {
                    array_tipPics.get(i).setImageResource(checkedPic);
                } else {
                    array_tipPics.get(i).setImageResource(uncheckedPic);
                }
            }
        } else {
            for (int i = 0; i < array_tipPics.size(); i++) {
                // viewpager当前页对应的位置图片高亮
                if (i == currentPage) {
                    array_tipPics.get(i).setColorFilter(Color.WHITE,
                            PorterDuff.Mode.LIGHTEN);
                } else {
                    // 其他的图片清除滤镜
                    array_tipPics.get(i).clearColorFilter();
                }


            }
        }

    }

    /**
     * ViewPager的适配器
     */


    class ViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return array_showPics.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(array_showPics.get(position));

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 资源文件夹图片获取
            View view = array_showPics.get(position);
            if (array_imgUrls != null) {
                bitmapUtils.display(view, array_imgUrls.get(position));

            }
            container.addView(view);
            return view;

        }

    }

    /**
     * 继承runnable一个新线程，来定时播放图片
     */

    class ViewPagerTask implements Runnable {

        @Override
        public void run() {
            pageCurrentIndex++;
            handler.sendEmptyMessage(0);

        }

        Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                viewPager.setCurrentItem(pageCurrentIndex, true);
            }

            ;
        };

    }

}
