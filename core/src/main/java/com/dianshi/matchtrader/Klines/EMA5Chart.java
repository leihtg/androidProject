package com.dianshi.matchtrader.Klines;

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
public class EMA5Chart extends ChartBase {
    /**
     * 构造方法
     */
    public EMA5Chart() {
        DecimalPlace = 2;
        //绘制在K线图中
        setMainSection(true);
        //展示的名称
        setDisplayName("EMA");
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
        pMa5.setName("均线");//名称
        pMa5.setValue(5);//值 和 默认值相同
        pList.add(pMa5);


        //参数列表设置
        set_ParameterList(pList);


        //均线的颜色列表
        int[] array = {
                KLineColor.ma5Color, KLineColor.ma10Color, KLineColor.ma30Color
        };


        List<OutResult> rList = new ArrayList<>();


        //目前显示的K线是3个
        for (int i = 0; i < 1; i++) {
            OutResult out = new OutResult();
            out.setColor(array[i]);
            BigDecimal bigDecimal = BigDecimal.valueOf(get_ParameterList().get(i).getValue());
            bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);

            out.setDisplayName("EMA" + bigDecimal.toString());
            out.setName("EMA" + bigDecimal.toString());
            out.setMaNumber(get_ParameterList().get(i).getType());
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

                //若Y=EMA(X，N)，则Y=［2*X+(N-1)*Y’］/(N+1)

                //遍历均线参数列表
                for (int i = 0; i < get_ParameterList().size(); i++) {

                    if (get_ParameterList().get(i).getValue() > 0) {

                        //得到均线输出的参数
                        OutResult outResult = get_ResultList().get(i);

                        //添加value值
                        double value = kline.getClose();

                        //n天后才有MAn均线
                        if (index > 0) {
                            //上一个值
                            double Y = ComputeList.get(index-1).get(outResult.getName());
                            value = (2*value +(outResult.getMaNumber()-1)*Y)/(outResult.getMaNumber()+1);
                        }




                        //添加value值
                        dict.put(outResult.getName(), value);

                        //设置绘制类型,名称,颜色,键名集合
                        DrawTypeModel drawTypeModel = new DrawTypeModel();
                        drawTypeModel.setDrawColor(outResult.getColor());
                        drawTypeModel.setName("BrokenLine");
                        List<String> drawList = new ArrayList<>();
                        drawList.add(outResult.getName());
                        drawTypeModel.setKeyCollection(drawList);
                        dtCollection.add(drawTypeModel);

                        //设置绘制类型,名称,均线数据
                        DrawWordModel drawWordModel = new DrawWordModel();
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
