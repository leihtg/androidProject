package com.dianshi.matchtrader.product;

import com.dianshi.framework.BitConverters;
import com.dianshi.matchtrader.Utils.AppManager;
import com.dianshi.matchtrader.Utils.Util;
import com.dianshi.matchtrader.model.KLineModel;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class KLineProcesser {

    private GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();

    /**
     * 拼接请求网址
     *
     * @param productId
     * @param type
     * @param num
     */
    private String getUrl(int productId, int type, int num) {
        String KLineUrl = globalSingleton.KlineDownLoadSite + "/DataLoader/LoadByNum?productId=" + productId + "&type=" + type + "&num=" + num;

        return KLineUrl;
    }

    /**
     * 加载分时图
     * @return
     */
    public List<KLineModel>  loadByTime(int productId) throws Exception {


        List<KLineModel> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String timer = df.format(calendar.getTime());

        String[] strings = timer.split("-");
        String year = strings[0];
        String month = strings[1];
        String day = strings[2];


        String KLineUrl = globalSingleton.KlineDownLoadSite + "/DataLoader/Loadminute?productId=" + productId + "&year=" + year + "&month=" + month+"&day="+day;

        URL url = new URL(KLineUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);// 设置链接超时的时间
        // 设置请求的头
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

        if (connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream(); // 字节流转换成字符串
            byte[] myBuf  = AppManager.StreamTools.readInputStream(inputStream);//输入流获取转换成字节数组




            result = SplitBuf(myBuf);
        }
        return result;

    }

    public List<KLineModel> loadByNum(int productId, int type, int num) throws Exception {
        List<KLineModel> result = new ArrayList<>();


        //得到K线的网址
        String urlStr = getUrl(productId,type,num);


        URL url = new URL(urlStr);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);// 设置链接超时的时间
        // 设置请求的头
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

        if (connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream(); // 字节流转换成字符串
            byte[] myBuf  = AppManager.StreamTools.readInputStream(inputStream);//输入流获取转换成字节数组
            result = SplitBuf(myBuf);
        }
        return result;
    }

    /// <summary>
    /// 获得标准时间
    ///
    /// </summary>
    /// <param name="time"></param>
    /// <param name="type">
    /// 0:1分钟
    /// 1:5分钟
    /// 2:15分钟
    /// 3:30分钟
    /// 4:1小时
    /// 5:4小时
    /// 6:日线
    /// 7：周线
    /// 8：月线
    /// </param>
    /// <returns></returns>
    public int GetStandTime(int time, int type)
    {
        int result = time;
        if (type <= 5)
        {
            result = GetSimpleTime(time, type);
        }
        else
        {
            switch (type)
            {
                case 6:
                    result = GetDayTime(time);
                    break;
                case 7:
                    result = GetWeekTime(time);
                    break;
                case 8:
                    result = GetMonthTime(time);
                    break;
            }
        }

        return result;
    }

    public int GetSimpleTime(int time, int type)
    {
        int result = time;
        int[] array = { 60, 300, 900,1800, 3600, 14400 };
        if (type >= 0 && type < array.length)
        {
            int v = array[type];
            result = ((int)(time / array[type])) * v;
        }
        return result;
    }


    /**
     * 得到年
     * @param time
     * @return
     */
    public int GetYearTime(int time)
    {
        int result = time;
        Date date = Util.UnixToTime(GetDayTime(time));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfMonth = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.DATE,-1*dayOfMonth+1);

        result = Util.TimeToUnix(calendar.getTime());
        return result;
    }

    /**
     * 得到月
     * @param time
     * @return
     */
    public int GetMonthTime(int time)
    {
        int result = time;
        Date date = Util.UnixToTime(GetDayTime(time));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE,-1*dayOfMonth+1);

        result = Util.TimeToUnix(calendar.getTime());
        return result;
    }

    /**
     * 得到天
     * @param time
     * @return
     */
    public int GetDayTime(int time)
    {
        int result = time;
        Date date = new Date(time*1000);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = df.format(date);
        try {
            date = df.parse(dateStr);
        }catch (Exception ex){

        }
        result = Util.TimeToUnix(date);
        return result;
    }

    public int GetWeekTime(int time)
    {
        int result = time;
        Date date = Util.UnixToTime(GetDayTime(time));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 0)
        {
            dayOfWeek = 7;
        }
        calendar.add(Calendar.DATE,-1*dayOfWeek+1);

        result = Util.TimeToUnix(calendar.getTime());
        return result;
    }





    /**
     * 分离获取数据
     * @param myBuf
     * @return
     */

    private List<KLineModel> SplitBuf(byte[] myBuf)
    {
        List<KLineModel> result = new ArrayList<>();

        for (int i = 0; i< myBuf.length; )
        {
            KLineModel item = new KLineModel();

            item.setOpen(BitConverters.ToSingle(myBuf,i));
            item.setHigh(BitConverters.ToSingle(myBuf,i+4));
            item.setLow(BitConverters.ToSingle(myBuf,i+8));
            item.setClose(BitConverters.ToSingle(myBuf,i+12));
            item.setVolume(BitConverters.ToInt32(myBuf,i+16));
            item.setTime(BitConverters.ToInt32(myBuf,i+20));
            result.add(item);
            i = i + 24;
        }



        return result;


    }
}
