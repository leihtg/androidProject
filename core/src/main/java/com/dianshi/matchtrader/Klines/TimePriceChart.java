package com.dianshi.matchtrader.Klines;

import android.graphics.Color;
import android.util.Log;

import com.dianshi.matchtrader.kLineModel.DrawTypeModel;
import com.dianshi.matchtrader.kLineModel.DrawWordModel;
import com.dianshi.matchtrader.kLineModel.OutResult;
import com.dianshi.matchtrader.kLineModel.Parameter;
import com.dianshi.matchtrader.model.KLineModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * K线中的均线图
 */
public class TimePriceChart extends ChartBase {
    /**
     * 构造方法
     */
    public TimePriceChart() {
        DecimalPlace = 2;
        //绘制在K线图中
        setMainSection(true);
        //展示的名称
        setDisplayName("分时图");
    }

    /**
     * 重写父类计算函数
     *
     * @param startNum
     */
    @Override
    public void compute(int startNum) {

        //这里为什么要加同步呢
        synchronized (this) {
            if ((startNum < 0) || (startNum >= ComputeList.size() - 1)) {
                startNum = 0;
            }

            //移除startNum之前的所有数据的列表
            removeComputeList(startNum);
            removeDrawTypeCollection(startNum);
            removeDrawWordCollection(startNum);

            //得到所有K线数据
            List<KLineModel> kList = this.getParent().getKlineSafeCollection().GetAll();


            //遍历K线列表
            for (int i = startNum; i< kList.size();i++){
                KLineModel item = kList.get(i);


                //ComputeList--K线高开低收的价格值
                ConcurrentHashMap<String,Double> dict = new ConcurrentHashMap<>();
                //获取K线收盘的价格值
                dict.put("close",item.getClose());
                ComputeList.add(i,dict);


                //DrawTypeCollection--K线收键名,绘制线类型,绘制颜色
                DrawTypeModel drawType = new DrawTypeModel(); //绘制类型model初始化
                drawType.setName("BrokenLine");  //类型名称
                drawType.setDrawColor(KLineColor.timePriceColor);  //颜色
                List<String> keyList = new ArrayList<>();   //存储键值名称
                keyList.add("close");
                drawType.setKeyCollection(keyList);
                List<DrawTypeModel> dtCollection = new ArrayList<>();  //model装入列表
                dtCollection.add(drawType);
                DrawTypeCollection.add(i,dtCollection);

                //DrawWordCollection--K线收的名称,价格值,绘制颜色
                List<DrawWordModel> wordCollection = new ArrayList<>();
                DrawWordModel cWord = new DrawWordModel();
                cWord.setDrawColor(Color.WHITE);
                cWord.setName("收");
                cWord.setValue(item.getClose());
                wordCollection.add(cWord);
                DrawWordCollection.add(i,wordCollection);
            }

        }
    }
}
