package dianshi.matchtrader.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class HttpUtil {

    /**
     * 通过网址获取信息
     *
     * @param website
     * @return
     */
    public static String getStringByWebsite(String website) {
        String datas = "";
        try {
            URL url;
            url = new URL(website);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream);
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                datas += line;
                line = bufferedReader.readLine();
            }
        } catch (MalformedURLException e) {
            System.out.println("路径出错啦");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("连接出错啦");
            e.printStackTrace();
        }
        return datas;

    }
}
