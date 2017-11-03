package com.dianshi.matchtrader.Klines;

import android.graphics.Color;
import android.util.Log;

import com.dianshi.matchtrader.kLineModel.DrawTypeModel;
import com.dianshi.matchtrader.kLineModel.DrawWordModel;
import com.dianshi.matchtrader.model.KLineModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * K线图
 */
public class KChart extends ChartBase {

    /**
     * 构造方法
     */
    public KChart(){
        this.DecimalPlace = 2;
        //设置K线图
        this.setMainSection(true);
        this.setDisplayName("K线");
    }


    /**
     * 重写chartBase的计算方法
     * @param startNum
     */
    @Override
    public void compute(int startNum) {
        synchronized (this){
            if(startNum < 0 || startNum > _ResultList.size()){
                startNum = 0;
            }

            //移除K线高开低收的价格值列表
            removeComputeList(startNum);
            //移除绘制类型列表
            removeDrawTypeCollection(startNum);
            //移除绘制文本列表
            removeDrawWordCollection(startNum);

            //得到所有K线数据
            List<KLineModel> kList = this.getParent().getKlineSafeCollection().GetAll();

            //遍历K线列表
            for (int i = startNum; i< kList.size();i++){
                KLineModel item = kList.get(i);


                //ComputeList--K线高开低收的价格值
                ConcurrentHashMap<String,Double> dict = new ConcurrentHashMap<>();
                //获取K线高开低收的价格值
                dict.put("high",item.getHigh());
                dict.put("open",item.getOpen());
                dict.put("low",item.getLow());
                dict.put("close",item.getClose());
                ComputeList.add(i,dict);


                //DrawTypeCollection--K线高开低收键名,绘制线类型,绘制颜色
                DrawTypeModel drawType = new DrawTypeModel(); //绘制类型model初始化
                drawType.setName("DrawKLine");  //类型名称
                drawType.setDrawColor(Color.WHITE);  //颜色
                List<String> keyList = new ArrayList<>();   //存储键值名称
                keyList.add("high");
                keyList.add("open");
                keyList.add("low");
                keyList.add("close");
                drawType.setKeyCollection(keyList);
                List<DrawTypeModel> dtCollection = new ArrayList<>();  //model装入列表
                dtCollection.add(drawType);
                DrawTypeCollection.add(i,dtCollection);

                //DrawWordCollection--K线高开低收的名称,价格值,绘制颜色
                List<DrawWordModel> wordCollection = new ArrayList<>();
                DrawWordModel hWord = new DrawWordModel();
                hWord.setDrawColor(Color.WHITE);
                hWord.setName("高");
                hWord.setValue(item.getHigh());
                wordCollection.add(hWord);
                DrawWordModel oWord = new DrawWordModel();
                oWord.setValue(item.getOpen());
                oWord.setName("开");
                oWord.setDrawColor(Color.WHITE);
                wordCollection.add(oWord);
                DrawWordModel lWord = new DrawWordModel();
                lWord.setDrawColor(Color.WHITE);
                lWord.setName("低");
                lWord.setValue(item.getLow());
                wordCollection.add(lWord);
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
