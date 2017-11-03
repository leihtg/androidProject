package com.dianshi.matchtrader.Klines;

import android.util.Log;

import com.dianshi.matchtrader.kLineModel.DrawTypeModel;
import com.dianshi.matchtrader.kLineModel.DrawWordModel;
import com.dianshi.matchtrader.kLineModel.OutResult;
import com.dianshi.matchtrader.kLineModel.Parameter;
import com.dianshi.matchtrader.model.KLineModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * KDJ指标
 */
public class KDJChart extends ChartBase {
    /**
     * 构造方法
     */
    public KDJChart() {
        DecimalPlace = 2;
        //绘制在K线图中
        setMainSection(false);
        //展示的名称
        setDisplayName("KDJ(9,3,3)");
    }


    /**
     * 重写父类的初始化参数函数
     */
    @Override
    public void initParameter() {
        List<Parameter> pList = new ArrayList<>();
        Parameter pMa5 = new Parameter();
        Parameter pMa10 = new Parameter();
        Parameter pMa30 = new Parameter();

        pMa5.setType(5);
        pMa5.setDefaultValue(5);//默认值
        pMa5.setMax(600);//最大值
        pMa5.setMin(1);//最小值
        pMa5.setName("K");//名称
        pMa5.setValue(5);//值 和 默认值相同
        pList.add(pMa5);

        pMa10.setType(10);
        pMa10.setDefaultValue(10);
        pMa10.setMax(100);
        pMa10.setMin(1);
        pMa10.setName("D");
        pMa10.setValue(10);
        pList.add(pMa10);

        pMa30.setType(30);
        pMa30.setDefaultValue(30);
        pMa30.setMax(30);
        pMa30.setMin(1);
        pMa30.setName("J");
        pMa30.setValue(30);
        pList.add(pMa30);

        //参数列表设置
        set_ParameterList(pList);


        //均线的颜色列表
        int[] array = {
                KLineColor.ma5Color, KLineColor.ma10Color, KLineColor.ma30Color
        };


        List<OutResult> rList = new ArrayList<>();


        //目前显示的K线是3个
        for (int i = 0; i < 3; i++) {
            OutResult out = new OutResult();
            out.setColor(array[i]);
            out.setDisplayName(get_ParameterList().get(i).getName());
            out.setName(get_ParameterList().get(i).getName());
            rList.add(out);
        }

        //输出参数列表设置
        set_ResultList(rList);
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



            //遍历K线列表
            for (int index = startNum; index < kCollection.size(); index++) {

                KLineModel kline = kCollection.get(index);

                //定义和实例化好三个列表
                ConcurrentHashMap<String, Double> dict = new ConcurrentHashMap<>();
                List<DrawTypeModel> dtCollection = new ArrayList<>();
                List<DrawWordModel> wordCollection = new ArrayList<>();

                //遍历参数列表
                for (int i = 0; i < get_ParameterList().size(); i++) {


                    if (get_ParameterList().get(i).getValue() > 0) {

                        //得到均线输出的参数
                        OutResult outResult = get_ResultList().get(i);

                        //添加value值
                        double value = 0.0;
                        double max = kline.getClose();
                        double min = kline.getClose();
                        double RSV = 0;



                        /*
                         n 日RSV=（Cn－Ln）÷（Hn－Ln）×100 式中，Cn 为第n 日收盘价；Ln 为n 日内的最低价；Hn 为n 日内的最高价
                         当日K 值=2/3×前一日K 值＋1/3×当日RSV 
                         当日D 值=2/3×前一日D 值＋1/3×当日K 值 
                         若无前一日K 值与D 值，则可分别用50 来代替
                         J=3K—2D
                         */
                        //9日最低价

                        if (index > 0) {

                            //满9日计算最大最小值
                            if (index >= 8) {
                                for (int j = index - 8; j <= index; j++) {
                                    KLineModel model = kCollection.get(j);
                                    max = Math.max(max, model.getHigh());
                                    min = Math.min(min, model.getLow());
                                }

                            //不满九日计算最大最小值
                            } else {
                                for (int j = 0; j <= index; j++) {
                                    KLineModel model = kCollection.get(j);
                                    max = Math.max(max, model.getHigh());
                                    min = Math.min(min, model.getLow());
                                }

                            }


                            if (max < min) {
                                double temp = max;
                                max = min;
                                min = temp;
                            }

                            if (max == min){
                                RSV = 100;
                            }else{
                                BigDecimal b1 = BigDecimal.valueOf((kline.getClose() - min));
                                BigDecimal b2 = BigDecimal.valueOf((max - min));
                                if (b2.doubleValue() > 0.0) {
                                    RSV = b1.divide(b2, 8,BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
                                }
                            }

                            if (i == 0) {

                                double KY = ComputeList.get(index - 1).get(outResult.getName());
                                double K = (2*KY + RSV)/3;
                                value = K;
                            } else if (i == 1) {
                                double KY =  ComputeList.get(index - 1).get(get_ParameterList().get(i - 1).getName());
                                double DY = ComputeList.get(index - 1).get(outResult.getName());
                                double K = (2*KY + RSV)/3;
                                double D = (2*DY + K) / 3;
                                value = D;
                            } else {
                                double KY = ComputeList.get(index - 1).get(get_ParameterList().get(i - 2).getName());
                                double DY = ComputeList.get(index - 1).get(get_ParameterList().get(i - 1).getName());
                                double K = (2*KY + RSV)/3;
                                double D = (2*DY + K) / 3;
                                double J = 3 * K - 2 * D;
                                value = J;
                            }
                        } else {
                            value = 1;
                        }


                        //添加value值
                        dict.put(outResult.getName(), value);

                        //设置绘制类型,名称,颜色,键名集合
                        DrawTypeModel drawTypeModel = new DrawTypeModel();
                        drawTypeModel.setDrawColor(outResult.getColor());
                        drawTypeModel.setMaNumber(outResult.getMaNumber());
                        drawTypeModel.setName("BrokenLine");
                        List<String> drawList = new ArrayList<>();
                        drawList.add(outResult.getName());
                        drawTypeModel.setKeyCollection(drawList);
                        dtCollection.add(drawTypeModel);

                        //设置绘制类型,名称,均线数据
                        DrawWordModel drawWordModel = new DrawWordModel();
                        drawWordModel.setMaNumber(outResult.getMaNumber());
                        drawWordModel.setDrawColor(outResult.getColor());
                        drawWordModel.setName(outResult.getName());
                        drawWordModel.setValue(value);
                        wordCollection.add(drawWordModel);
                    }


                }
                DrawTypeCollection.add(dtCollection);
                ComputeList.add(dict);
                DrawWordCollection.add(wordCollection);
            }


        }
    }
}
