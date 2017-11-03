package com.dianshi.matchtrader.Klines;

import android.graphics.Color;
import android.util.Log;

import com.dianshi.matchtrader.kLineModel.DrawTypeModel;
import com.dianshi.matchtrader.kLineModel.DrawWordModel;
import com.dianshi.matchtrader.kLineModel.OutResult;
import com.dianshi.matchtrader.kLineModel.Parameter;
import com.dianshi.matchtrader.model.KLineModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * K线中的均线图
 */
public class MACDChart extends ChartBase {
    /**
     * 构造方法
     */
    public MACDChart() {
        DecimalPlace = 2;
        //绘制在K线图中
        setMainSection(false);
        //展示的名称
        setDisplayName("MACD(12,26,9)");
    }


    /**
     * 重写父类计算函数
     *
     * @param startNum
     */
    @Override
    public void compute(int startNum) {


        //同步非常重要
        synchronized (this) {


            if ((startNum < 0) || (startNum >= ComputeList.size() - 1)) {
                startNum = 0;
            }
            //移除startNum之前的所有数据的列表
            removeComputeList(startNum);
            removeDrawTypeCollection(startNum);
            removeDrawWordCollection(startNum);
            //得到所有K线数据
            List<KLineModel> kCollection = this.getParent().getKlineSafeCollection().GetAll();


            List<Double> EMA12List = new ArrayList<>();
            List<Double> EMA26List = new ArrayList<>();
            List<Double> DEAList = new ArrayList<>();

            //遍历K线列表
            for (int index = startNum; index < kCollection.size(); index++) {

                KLineModel kline = kCollection.get(index);

                //定义和实例化好三个列表
                ConcurrentHashMap<String, Double> dict = new ConcurrentHashMap<>();
                List<DrawTypeModel> dtCollection = new ArrayList<>();
                List<DrawWordModel> wordCollection = new ArrayList<>();

                //若Y=EMA(X，N)，则Y=［2*X+(N-1)*Y’］/(N+1)
                /**
                 * EMA（12）= 前一日EMA（12）×11/13＋今日收盘价×2/13
                 EMA（26）= 前一日EMA（26）×25/27＋今日收盘价×2/27
                 DIF=今日EMA（12）- 今日EMA（26）
                 DEA= 前一日DEA×8/10＋今日DIF×2/10
                 BAR（MACD）=2×(DIFF－DEA)

                 DIF:EMA(CLOSE,12)-EMA(CLOSE,26);

                 DEA:EMA(DIF,9);

                 MACD:(DIF-DEA)*2;
                 */


                        //添加value值
                        double value = kline.getClose();
                        double EMA12value = 0;
                        double EMA26value = 0;
                        double DEA =0;


                        if (index > 0) {
                            //上一个值
                            double Y12 = EMA12List.get(index-1);
                            double Y26 = EMA26List.get(index-1);
                            EMA12value = (2*value +(12-1)*Y12)/(12+1);
                            EMA26value = (2*value +(26-1)*Y26)/(26+1);
                            double DIFF = EMA12value -EMA26value;
                            double DEAY =DEAList.get(index-1);


                            DEA = (2*DIFF +(9-1)*DEAY)/(9+1);



                            value = (DIFF-DEA)*2;

                        }else{
                            EMA12value = kline.getClose();
                            EMA26value = kline.getClose();

                            double DIFF = EMA12value -EMA26value;
                            DEA = DIFF;

                            value = (DIFF-DEA)*2;

                        }






                        EMA12List.add(EMA12value);
                        EMA26List.add(EMA26value);
                        DEAList.add(DEA);

                        //添加value值
                        dict.put("vol",value);
                        dict.put("zero",0d);


                        //设置绘制类型,名称,颜色,键名集合
                        DrawTypeModel drawTypeModel = new DrawTypeModel();
                        drawTypeModel.setDrawColor(Color.WHITE);
                        drawTypeModel.setName("drawmacd");
                        List<String> drawList = new ArrayList<>();
                        drawList.add("MACD");
                        drawTypeModel.setKeyCollection(drawList);
                        dtCollection.add(drawTypeModel);

                        //设置绘制类型,名称,均线数据
                        DrawWordModel drawWordModel = new DrawWordModel();
                        drawWordModel.setDrawColor(Color.WHITE);
                        drawWordModel.setName("MACD");
                        drawWordModel.setValue(value);
                        wordCollection.add(drawWordModel);

                DrawTypeCollection.add(dtCollection);
                ComputeList.add(dict);
                DrawWordCollection.add(wordCollection);
            }

        }
    }
}
