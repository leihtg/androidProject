package dianshi.matchtrader.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.toolbar.BaseActivity;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class IntroduceActivity extends BaseActivity {

    Context context;
    ArrayList<View> array;
    ImageView[] imgPoints;
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        context = IntroduceActivity.this;
        //初始化控件
        initView();
        initViewPager();


    }

    public void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_introduce);
        array = new ArrayList<>();
        int[] imgIds = {R.mipmap.introduce_1, R.mipmap.introduce_2};
        ImageView img_1 = (ImageView) findViewById(R.id.img_introduce_point_1);
        ImageView img_2 = (ImageView) findViewById(R.id.img_introduce_point_2);
        imgPoints = new ImageView[]{img_1, img_2};
        for (int imgId : imgIds) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imgId);
            array.add(imageView);
        }

        //绑定适配器
        viewPager.setAdapter(pagerAdapter);

        //监听滑动事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //改变小点点的位置
                for (int i = 0; i < imgPoints.length; i++) {
                    if (i == position) {
                        imgPoints[i].setImageResource(R.mipmap.icon_point_pre);
                    } else {
                        imgPoints[i].setImageResource(R.mipmap.icon_point);
                    }
                }


                //开启按钮的显示与隐藏
                if (position == array.size() - 1) {
                    btn_start.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(btn_start, "translateY", -20f, 0f).setDuration(1000).start();
                } else {
                    btn_start.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    /**
     * 初始化控件
     */
    public void initView() {


        btn_start = (Button) findViewById(R.id.btn_introduce);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(APPFinal.ShAERD_FILE_FirstStart, Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("isFirstStart", false).commit();

                //跳转到登录和注册页面
                Intent intent = new Intent(IntroduceActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    /**
     * Viewpager的适配器
     */
    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(array.get(position));
            return array.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(array.get(position));//删除页卡
        }
    };
}
