package com.dianshi.matchtrader.server;

import com.dianshi.matchtrader.product.ProductPool;
import com.dianshi.matchtrader.userinfo.UserInfoPool;

import java.util.Date;
import java.util.Random;

/**
 * 存储app信息的单例类
 */
public class GlobalSingleton {
    private static GlobalSingleton instance = new GlobalSingleton();
    public TCPSingleton getTCPSingleton(){
        return TCPSingleton.CreateInstance();
    }


    /**
     * 实例化
     */
    private GlobalSingleton(){
        //初始化商品存储
        ProductPool = new ProductPool();
        //初始化用户信息存储
        UserInfoPool =  new UserInfoPool();
        //初始化服务器推送handler
        ServerPushHandler = new ServerPushHandler();
    }

    public static GlobalSingleton CreateInstance(){
        return instance;
    }


    /**
     * 得到服务器Server对象
     * @return
     */
    public static Server getServerHost(){
        Server srv = new Server();
        if (ServerHost == null || ServerPort==null ||ServerHost.isEmpty()|| ServerPort.isEmpty()){
            return null;
        }
        //获取随机的服务器IP和端口号
        getRandomIPAndPort(ServerHost,ServerPort);

        srv.Host = ServerHost;
        srv.Port = Integer.parseInt(ServerPort);
        return  srv;
    }


    /**
     * ip和端口号的特殊处理
     * 这个方法是在多台服务器存在的情况下,随机连接服务器,目的是避免全部Android移动端连接同一台服务器,增加负担
     * 使用方法: 不同的ip 和端口号使用"*"号连接. ip和端口号的顺序要严格保持一致
     * 例如 :
     * ipStr:192.168.1.1*192.168.1.2
     * portStr:9290*9290
     *
     * @param ipStr IP
     * @param portStr 端口号
     */
    private static void getRandomIPAndPort(String ipStr,String portStr){

        if ( ipStr== null || portStr == null || ipStr.trim().equals("")||portStr.trim().equals("")){
            GlobalSingleton.ServerHost = null;
            GlobalSingleton.ServerPort = null;
            return;
        }

        //只有一个ip
        if (ipStr.contains("*") && portStr.contains("*") ){
            //多个*号分隔的ip
            String[] ipArray = ipStr.split("\\*");
            String[] portArray = portStr.split("\\*");

            int randomNumber  = new Random().nextInt(ipArray.length);
            GlobalSingleton.ServerHost = ipArray[randomNumber];
            GlobalSingleton.ServerPort = portArray[randomNumber];
        }else if(!ipStr.contains("*") && !portStr.contains("*")){

            GlobalSingleton.ServerHost = ipStr;
            GlobalSingleton.ServerPort = portStr;
        }else{
            GlobalSingleton.ServerHost = null;
            GlobalSingleton.ServerPort = null;
        }
    }

    /*用户信息*/
    public int CustomerId;
    public String UserName;
    public String TrueName;
    public String BankUserName;
    public String BankCard;
    public String PasswordMD5;
    public String SignStr;
    /**K线加载的接口*/
    public String KlineDownLoadSite;
    public Boolean IsLogined = false;
    public Boolean IsOnLine = false;
    public Boolean isRestartConnect= false;
    public Date LastHeartBeat;

    /**K线的种类*/
    public int PeriodTypeIndex = 6;


    /**
     * 交易所信息
     */
    public static String SoftName = "Android_亿客交易平台";//软件更新的检查标志
    public static String ProductTypeName  = "藏品";//商品所属类型
    public static int Version = 12306;//服务器版本
    public static boolean IsHaveMoneyOut = false;//是否有出金选项


    public static String ServerHost = "120.76.22.233";//服务器ip
    public static String ServerPort = "9290";//服务器端口号
    public static String HomePage = "http://www.ykds888.com/";//公司官网
    public static String RefOpenUrl = "http://open.ykds888.com/";//开户链接
    public static String CompayName = "浙江亿客电子商务有限公司";//公司名称
    public static String SchoolWebSite  = "";//学堂地址
    public static String Phone = "400-9987-721";//客服电话
    public static String TradeCenterName = "亿客交易平台";//交易所标题
    public static String MoneyOutStyle = "normal";//出金接口模式 cmcc是民生银行接口 normal是普通的出金接口



    public ProductPool ProductPool;
    public UserInfoPool UserInfoPool;
    public ServerPushHandler ServerPushHandler;

}
