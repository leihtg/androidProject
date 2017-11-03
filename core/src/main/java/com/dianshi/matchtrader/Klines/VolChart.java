package com.dianshi.matchtrader.Klines;

import android.graphics.Color;
import android.util.Log;

import com.dianshi.matchtrader.kLineModel.DrawTypeModel;
import com.dianshi.matchtrader.kLineModel.DrawWordModel;
import com.dianshi.matchtrader.model.KLineModel;
import com.dianshi.matchtrader.model.LoadDeputeModel_in;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 成交量柱形图
 */
public class VolChart extends ChartBase {

    /**
     *构造方法
     */
    public VolChart(){
        this.DecimalPlace = 0;
        this.setMainSection(false);
        this.setDisplayName("成交量");

        //柱形图Y轴开始的坐标
        this.HLineCollection.add(0,0d);

        this._MaxMinStep = 3;
        this._MinMinValue = 0;
    }


    /**
     * 重写计算方法
     * @param startNum
     */
    @Override
    public void compute(int startNum) {




        synchronized (this){

            if((startNum <0) || (startNum >= ComputeList.size()-1)){
                startNum = 0;
            }


            //移除K线数据值列表
            removeComputeList(startNum);
            //移除绘制类型列表
            removeDrawTypeCollection(startNum);
            //移除绘制文本列表
            removeDrawWordCollection(startNum);
            List<KLineModel> kCollection = this.getParent().getKlineSafeCollection().GetAll();

            for (int i = startNum;i<kCollection.size();i++){
                KLineModel item = kCollection.get(i);

                //ComputeList--柱形图的阳阴线类型,成交量,还有坐标起点
                ConcurrentHashMap<String,Double> dict = new ConcurrentHashMap<>();
                dict.put("Type",item.getOpen() > item.getClose()?0d:1);//阳线为1,阴线为0
                dict.put("vol",Double.valueOf(item.getVolume()));
                dict.put("zero",0d);


                ComputeList.add(i,dict);

                //DrawTypeCollection--柱形图中绘制类型的名称,成交量键名,绘制颜色
                DrawTypeModel drawType = new DrawTypeModel();
                drawType.setName("DrawVol");
                drawType.setDrawColor(Color.WHITE);
                List<String> keys = new ArrayList<>();
                keys.add("vol");
                drawType.setKeyCollection(keys);
                List<DrawTypeModel> dtCollection = new ArrayList<>();
                dtCollection.add(drawType);
                DrawTypeCollection.add(i,dtCollection);


                //DrawWordCollection--柱形图中成交量的名称,价格值,绘制颜色
                List<DrawWordModel> wordCollection = new ArrayList<>();
                DrawWordModel word = new DrawWordModel();
                word.setValue(item.getVolume());
                word.setName("成交量");
                word.setDrawColor(Color.WHITE);
                wordCollection.add(word);
                DrawWordCollection.add(i,wordCollection);
            }



        }


    }
}
