package dianshi.matchtrader.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.ViewPagerFragmentAdapter;
import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.util.KeyBoardUtils;


public class TradeOperateFragment extends BaseFragment {


    View view;
    ViewPager viewPager;
    RadioButton rbtn_in;
    RadioButton rbtn_out;
    RadioButton rbtn_killOrder;
    RadioButton rbtn_position;
    RadioButton rbtn_query;

    //碎片管理器
    FragmentManager fragmentManager;
    ViewPagerFragmentAdapter adapter;

    TradeMoneyInFragment tradeMoneyInFragment;
    TradeMoneyOutFragment tradeMoneyOutFragment;
    TradeKillOrderFragment tradeKillOrderFragment;
    TradePositionFragment tradePositionFragment;
    TradeQueryFragment tradeQueryFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trade_operate, container, false);

        //初始化RadioGroup
        initRadioGroup();
//        //初始化ViewPager
        initViewPager();


        return view;
    }


    /**
     * 显示某个碎片
     *
     * @param index
     */
    private void setTabSelection(int index) {
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //隐藏碎片
        hideFragment(transaction);
        //改变radiogroup的值
        switchRadioButtonChecked(index);


        switch (index) {
            case 0:
                if (tradeMoneyInFragment == null) {
                    tradeMoneyInFragment = new TradeMoneyInFragment();
                    transaction.add(R.id.framelayout_tradeOperate, tradeMoneyInFragment);
                } else {
                    transaction.show(tradeMoneyInFragment);
                }
                break;
            case 1:

                if (tradeMoneyOutFragment == null) {
                    tradeMoneyOutFragment = new TradeMoneyOutFragment();
                    transaction.add(R.id.framelayout_tradeOperate, tradeMoneyOutFragment);
                } else {
                    transaction.show(tradeMoneyOutFragment);
                }

                break;
            case 2:

                if (tradeKillOrderFragment == null) {
                    tradeKillOrderFragment = new TradeKillOrderFragment();
                    transaction.add(R.id.framelayout_tradeOperate, tradeKillOrderFragment);
                } else {
                    transaction.show(tradeKillOrderFragment);
                }
                break;
            case 3:

                if (tradePositionFragment == null) {
                    tradePositionFragment = new TradePositionFragment();
                    transaction.add(R.id.framelayout_tradeOperate, tradePositionFragment);
                } else {
                    transaction.show(tradePositionFragment);
                }
                break;
            case 4:

                if (tradeQueryFragment == null) {
                    tradeQueryFragment = new TradeQueryFragment();
                    transaction.add(R.id.framelayout_tradeOperate, tradeQueryFragment);
                } else {
                    transaction.show(tradeQueryFragment);
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
        if (tradeMoneyInFragment != null) {
            transaction.hide(tradeMoneyInFragment);
        }
        if (tradeMoneyOutFragment != null) {
            transaction.hide(tradeMoneyOutFragment);
        }
        if (tradeKillOrderFragment != null) {
            transaction.hide(tradeKillOrderFragment);
        }
        if (tradePositionFragment != null) {
            transaction.hide(tradePositionFragment);
        }
        if (tradeQueryFragment != null) {
            transaction.hide(tradeQueryFragment);
        }
        transaction.commit();

    }

    /**
     * 初始化ViewPager
     */
    public void initViewPager() {

        viewPager = (ViewPager) view.findViewById(R.id.viewPager_tradeOperate);

        ArrayList<Fragment> array = new ArrayList<Fragment>();

        array.add(new TradeMoneyInFragment());
        array.add(new TradeMoneyOutFragment());
        array.add(new TradeKillOrderFragment());
        array.add(new TradePositionFragment());
        array.add(new TradeQueryFragment());

        adapter = new ViewPagerFragmentAdapter(getActivity().getSupportFragmentManager(), array);
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //代表viewpager当前页的raduiButton选中
                switchRadioButtonChecked(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    /**
     * 切换radioButton们的选中状态
     *
     * @param position
     */
    public void switchRadioButtonChecked(int position) {

        RadioButton[] rbtns = {rbtn_in, rbtn_out, rbtn_killOrder, rbtn_position, rbtn_query};

        for (int i = 0; i < rbtns.length; i++) {
            if (i == position) {
                rbtns[i].setChecked(true);
            } else {
                rbtns[i].setChecked(false);
            }
        }

        if (KeyBoardUtils.isOpen(getActivity())) {
            KeyBoardUtils.closeKeyboard(getActivity());
        }

    }


    /**
     * 初始化RadioGroup
     */
    public void initRadioGroup() {
        rbtn_in = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_in);
        rbtn_out = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_out);
        rbtn_killOrder = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_killOrder);
        rbtn_position = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_position);
        rbtn_query = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_query);


        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_tradeOperate);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId) {
                    case R.id.rbtn_tradeOperate_in:
                        index = 0;
                        break;
                    case R.id.rbtn_tradeOperate_out:
                        index = 1;
                        break;
                    case R.id.rbtn_tradeOperate_killOrder:
                        index = 2;
                        break;
                    case R.id.rbtn_tradeOperate_position:
                        index = 3;
                        break;
                    case R.id.rbtn_tradeOperate_query:
                        index = 4;
                        break;
                }


                if (viewPager != null) {
                    viewPager.setCurrentItem(index);

                } else {
                    setTabSelection(index);
                }
//                viewPager.setCurrentItem(index);
            }
        });


    }


}
