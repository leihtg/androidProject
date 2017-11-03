package com.dianshi.matchtrader.Klines;

import com.dianshi.matchtrader.kLineModel.Parameter;
import com.dianshi.matchtrader.model.KLineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Kline集合的操作
 * 加同步锁
 */
public class KlineSafeCollection {
    //K线的有关数据
    private List<KLineModel> KlineCollection = new ArrayList<>();
    //参数的数据
    private List<Parameter> ParameterCollection = new ArrayList<>();



    /**
     * 加同步锁的情况下,得到参数里面的数据
     * 是否是单例?
     */
    public  List<Parameter> GetParameterCollection(){
        List<Parameter> list = new ArrayList<>();

        synchronized (this){
            for (Parameter item : ParameterCollection){
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 加同步锁的情况下,得到K线集合里面所有的数据
     * 是否是单例?
     */
    public List<KLineModel> GetAll(){
        List<KLineModel> list = new ArrayList<>();

        synchronized (KlineCollection){
            for (KLineModel item : KlineCollection){
                list.add(item);
            }
        }
        return list;
    }


    /**
     * 加同步锁的情况下,得到K线集合里面指定位置以后所有的数据
     * @param num
     * @return
     */
    public List<KLineModel> GetAll(int num){
        List<KLineModel> result = new ArrayList<>();
        synchronized (KlineCollection){
            for (int i = num;i<KlineCollection.size();i++){
                KLineModel item = new KLineModel();
                KLineModel newItem = new KLineModel();

                newItem.setClose(item.getClose());
                newItem.setHigh(item.getHigh());
                newItem.setLow(item.getLow());
                newItem.setOpen(item.getOpen());
                newItem.setTime(item.getTime());
                newItem.setVolume(item.getVolume());

                result.add(newItem);
            }
        }
        return  result;
    }


    /**
     * 加同步锁的情况下,给K线集合赋值
     * @param list
     */
    public void Set(List<KLineModel> list){
        synchronized (KlineCollection){
            KlineCollection.clear();
            if (list != null){
                for (KLineModel item: list){
                    KLineModel newItem = new KLineModel();
                    newItem.setVolume(item.getVolume());
                    newItem.setTime(item.getTime());
                    newItem.setOpen(item.getOpen());
                    newItem.setLow(item.getLow());
                    newItem.setHigh(item.getHigh());
                    newItem.setClose(item.getClose());
                    KlineCollection.add(newItem);
                }
            }

        }
    }

    /**
     * 加同步锁的情况下,给K线参数集合赋值
     * @param list
     */
    public void SetParameterCollection(List<Parameter> list){
        synchronized (this){
            ParameterCollection.clear();

            if (list != null){
                for (Parameter item: list){
                    Parameter newItem = new Parameter();
                    newItem.setName(item.getName());//名称 ma
                    newItem.setType(item.getType());//类型,比如是ma5--5,ma10-10
                    newItem.setDefaultValue(item.getDefaultValue());//默认的是...
                    newItem.setMax(item.getMax());
                    newItem.setMin(item.getMin());
                    newItem.setValue(item.getValue());
                    ParameterCollection.add(newItem);
                }
            }



        }
    }

}
