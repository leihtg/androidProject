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
public class MA5Chart extends ChartBase {
    /**
     * 构造方法
     */
    public MA5Chart() {
        DecimalPlace = 2;
        //绘制在K线图中
        setMainSection(true);
        //展示的名称
        setDisplayName("MA5");
    }


    /**
     * 重写父类的初始化参数函数
     */
    @Override
    public void initParameter() {
        List<Parameter> pList = new ArrayList<>();
        Parameter pMa5 = new Parameter();

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
                KLineColor.ma5Color
        };


        List<OutResult> rList = new ArrayList<>();


        //目前显示的K线是3个
        for (int i = 0; i < 1; i++) {
            OutResult out = new OutResult();
            BigDecimal bigDecimal = BigDecimal.valueOf(get_ParameterList().get(i).getValue());
            bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);

            out.setColor(array[i]);
            out.setDisplayName("MA" + bigDecimal.toString());
            out.setName("MA" + bigDecimal.toString());
            out.setMaNumber(bigDecimal.intValue());
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


                //遍历均线参数列表
                for (int i = 0; i < get_ParameterList().size(); i++) {
                    if (get_ParameterList().get(i).getValue() > 0) {
                        //得到均线输出的参数李彪
                        OutResult outResult = get_ResultList().get(i);

                        //添加value值
                        double value = kline.getClose();

                        if (index >= 4) {
                            double sumClose = 0.0;
                            for (int j = index - 4; j <=index; j++) {
                                sumClose = sumClose + kCollection.get(j).getClose();
                            }
                            value = sumClose / 5;

                        }



                        dict.put(outResult.getName(), value);


                        //DrawTypeCollection--柱形图中绘制类型的名称,成交量键名,绘制颜色
                        DrawTypeModel drawTypeModel = new DrawTypeModel();
                        drawTypeModel.setDrawColor(KLineColor.ma5Color);
                        drawTypeModel.setMaNumber(outResult.getMaNumber());
                        drawTypeModel.setName("BrokenLine");
                        List<String> drawList = new ArrayList<>();
                        drawList.add(outResult.getName());
                        drawTypeModel.setKeyCollection(drawList);
                        dtCollection.add(drawTypeModel);


                        //DrawWordCollection--柱形图中成交量的名称,价格值,绘制颜色
                        DrawWordModel word = new DrawWordModel();
                        word.setValue(value);
                        word.setName(outResult.getName());
                        word.setDrawColor(KLineColor.ma5Color);
                        word.setMaNumber(outResult.getMaNumber());
                        wordCollection.add(word);

                    }

                }


                DrawTypeCollection.add(dtCollection);
                ComputeList.add(dict);
                DrawWordCollection.add(wordCollection);
            }



        }
    }

}
