
package com.dianshi.matchtrader.server;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dianshi.framework.ReceiveData;
import com.dianshi.framework.TCPClient;
import com.dianshi.matchtrader.model.CustomerNotice;
import com.dianshi.matchtrader.model.MapProductDeputeCountModel;
import com.dianshi.matchtrader.model.MapProductDetailsNotice;
import com.dianshi.matchtrader.model.MapProductNotice;
import com.dianshi.matchtrader.model.ModelOutBase;
import com.dianshi.matchtrader.model.NoticeItem;
import com.dianshi.matchtrader.model.NoticeType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tcp连接单例类
 */
public class TCPSingleton {
    private static TCPSingleton instance = new TCPSingleton();
    private TCPClient tcpClient;
    private int serverIndex = 0;

    //存放handler
    private ConcurrentHashMap<String, Handler> handlerMsg = new ConcurrentHashMap<String, Handler>();


    /*大量的处理信息的Handler*/
    public Handler HeartStopHandler = null;
    public Handler HeartBeatHandler = null;
    public Handler ReLoginHandler = null;
    public Handler ServiceStopHandler = null;
    public Handler NoticeHandler = null;
    public Handler connectSuccHandler = null;
    public Handler sendFailHandler = null;
    public Handler connectFailHandler = null;

    private TCPSingleton() {

    }

    public static TCPSingleton CreateInstance() {
        return instance;
    }


    public void ChangeServer(int index) {
        this.serverIndex = index;
        Server server = GlobalSingleton.getServerHost();
        tcpClient = new TCPClient(server.Host, server.Port, GlobalSingleton.Version, receiveDataHandler, sendFailHandler, connectFailHandler, connectSuccHandler, HeartStopHandler);
    }




    /**
     * 网络连接
     */
    public void Connect() {
        ChangeServer(0);
        this.tcpClient.Connect();
    }

    public void Close() {
        tcpClient.ShutDown();
    }

    /**
     * 发送函数
     *
     * @param sendStr
     * @param timestamp
     * @param receiveMethod
     * @return
     */
    public boolean FuncSend(String sendStr, String timestamp, Handler receiveMethod) {
        boolean result = false;
        try {
            //将handler 放到 handlerMsg存放起来
            handlerMsg.put(timestamp, receiveMethod);
            tcpClient.Send(DataType.CallFunc, sendStr);
            result = true;
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return result;
    }


    /**
     * 心跳检测更新
     */
    public void HeartBeat() {
        try {
            //发送心跳
            tcpClient.Send(DataType.HeartBeat, "Beat");
        } catch (Exception e) {
            //发出通知
            Message msg = new Message();
            msg.obj = "stop";
            HeartStopHandler.sendMessage(msg);
        }
    }


    /**
     * 接收服务器发送的数据
     */
    public Handler receiveDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ReceiveData data = (ReceiveData) msg.obj;
            switch (data.Type) {
                case DataType.HeartBeat://心跳

                    if (HeartBeatHandler != null) {
                        Message hMsg = new Message();
                        HeartBeatHandler.sendMessage(hMsg);
                    }
                    break;
                case DataType.CallFunc://访问接口返回
                    synchronized (handlerMsg) {
                        try {
                            //给handlerMsg 中的handler 发送数据
                            ModelOutBase  model = new Gson().fromJson(data.Data, ModelOutBase.class);
                            Handler method = handlerMsg.get(model.getTimestamp());
                            handlerMsg.remove(model.getTimestamp());
                            Message callMsg = new Message();
                            callMsg.obj = data;
                            method.sendMessage(callMsg);
                        } catch (Exception e) {
                        }
                    }
                    break;
                case DataType.ClientNotice://通知.一般是服务器更新数据
                    dealNotice(data.Data);
                    break;
                case DataType.ManagementEvent://
                    break;
                case DataType.ReLogin://重新登录
                    if (ReLoginHandler != null) {
                        Message reMsg = new Message();
                        reMsg.obj = data.Data;
                        ReLoginHandler.sendMessage(reMsg);
                    }
                    break;
                case DataType.Stoped://停止
                    if (ServiceStopHandler != null) {
                        Message ssMsg = new Message();
                        ServiceStopHandler.sendMessage(ssMsg);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 处理通知
     *
     * @param msg [
     *            {"Key":0,"Value":"{\"CodeList\":[\"Depute\",\"Position\"],\"IsNotice\":true,\"IsRemoteLogin\":false,\"RemoteIP\":null}"},
     *            {"Key":1,"Value":"{\"47\":{\"Id\":47,\"P\":16.32,\"NC\":100,\"SFP\":0.0,\"SFC\":0,\"BFP\":16.32,\"BFC\":100,\"AC\":300,\"MxP\":16.32,\"MiP\":4.08,\"AM\":3672.0,\"DBC\":100,\"DSC\":0,\"BC\":200,\"SC\":1800,\"PL\":[{\"P\":16.32,\"T\":1468052571,\"C\":0}]}}"},
     *            {"Key":2,"Value":"{\"47\":{\"TL\":[],\"BL\":[{\"P\":16.32,\"C\":100}],\"SL\":[],\"Id\":47}}"}
     *            {"Key":3,"Value":"{\"47\":{\"DBC\":100,\"DSC\":0,\"BFC\":100,\"BFP\":16.32,\"SFC\":0,\"SFP\":0.0}}"},
     *            ]
     */
    private void dealNotice(String msg) {

        try {
            List<NoticeItem> noticeArray = new Gson().fromJson(msg,new TypeToken< List<NoticeItem>>(){}.getType());
            if (noticeArray != null) {
                for (NoticeItem item : noticeArray) {
                    int key = item.getKey();
                    switch (key) {
                        case NoticeType.CustomerNotice:
                            dealCustomerNotice(item.getValue());//处理用户信息(下单,持仓,等)
                            break;
                        case NoticeType.ProductNotice:
                            dealProductNotice(item.getValue());//处理商品信息(高开低收 成交量等)
                            break;
                        case NoticeType.ProductDetailNotice:
                            dealProductDetailNotice(item.getValue());//处理商品详情信息(五档信息)
                            break;
                        case NoticeType.DeputeNotice:
                            dealDeputeNotice(item.getValue());//处理挂单信息? 买一,卖一价?
                            break;

                    }
                }
            }

        } catch (Exception ex) {
            int i = 0;
            i += 100;
        }


    }

    /**
     * 处理用户
     *
     * @param msgStr {"CodeList":["Depute","Position"],"IsNotice":true,"IsRemoteLogin":false,"RemoteIP":null}
     */
    private void dealCustomerNotice(String msgStr) {
        GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();
        try {
            CustomerNotice notice = new Gson().fromJson(msgStr, CustomerNotice.class);


            if (msgStr.contains("Depute"))//下单变化
            {
                //更新用户金额
                dealUserMoneyChange();
                //下单信息学发生变化
                globalSingleton.ServerPushHandler.sendDeputeChangeNotice();
            }
            if (msgStr.contains("Position")) {//用户持仓
                //更新用户持仓
                dealProductPosition();
            }

            if (msgStr.contains("Clear")) {
                globalSingleton.ServerPushHandler.sendClearNotice();
            }

            if (msgStr.contains("MoneyChange")) {//用户金额变化
                //更新用户金额.目前服务器不发送这个通知
                dealUserMoneyChange();
            }

            if (msgStr.contains("ProductChange")) {//商品变化
                globalSingleton.ServerPushHandler.sendProductChangeNotice();
            }

            if (msgStr.contains("News")) {
                globalSingleton.ServerPushHandler.sendNewsNotice();
            }

            if (notice.isRemoteLogin()) {//远程登录
                globalSingleton.ServerPushHandler.sendRemotingLogin(notice.getRemoteIP());
            }
        } catch (Exception ex) {
        }
    }

    /**
     * 处理委托下单
     *
     * @param msg{"Key":3,"Value":"{\"47\":{\"DBC\":100,\"DSC\":0,\"BFC\":100,\"BFP\":16.32,\"SFC\":0,\"SFP\":0.0}}"},
     */
    private void dealDeputeNotice(String msg) {
        try {
            MapProductDeputeCountModel maps = new Gson().fromJson(msg, MapProductDeputeCountModel.class);
            GlobalSingleton.CreateInstance().ProductPool.modifyProduct(maps);
            GlobalSingleton.CreateInstance().ServerPushHandler.sendProductDeputeNotice(maps);


        } catch (Exception ex) {

        }
    }

    /**
     * 处理商品五档详情信息
     *
     * @param msg
     */
    private void dealProductDetailNotice(String msg) {
        try {


            /**
             *{"45":{"TL":[{"T":"17:08:00","P":1.72,"C":1,"TP":1}],"BL":[{"P":1.72,"C":32},{"P":1.44,"C":1}],"SL":[],"Id":45}}
             */
            //map中存储的是?
            MapProductDetailsNotice maps = new Gson().fromJson(msg, MapProductDetailsNotice.class);

            //修改商品五档
            GlobalSingleton.CreateInstance().ProductPool.modifyProduct(maps);

            GlobalSingleton.CreateInstance().ServerPushHandler.sendProductDetailNotice(maps);
        } catch (Exception ex) {

        }
    }

    /**
     * 处理商品价格发生变化
     *
     * @param msg
     */
    private void dealProductNotice(String msg) {
        try {
            MapProductNotice maps = new Gson().fromJson(msg, MapProductNotice.class);

            //修改改变后的商品信息
            GlobalSingleton.CreateInstance().ProductPool.modifyProduct(maps);
            //修改改变后的持仓商品信息
            GlobalSingleton.CreateInstance().ProductPool.modifyPosition(maps);
            //发送信息
            GlobalSingleton.CreateInstance().ServerPushHandler.sendProductPriceNotice(maps);
        } catch (Exception ex) {

        }
    }

    /**
     * 处理商品持仓变化
     */
    private void dealProductPosition() {
        //修改改变后的持仓商品信息
        GlobalSingleton.CreateInstance().ProductPool.modifyPosition(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //发送信息
                GlobalSingleton.CreateInstance().ServerPushHandler.sendPositionChangeNotice(msg.arg1);
            }
        });

    }

    /**
     * 处理用户金额变化
     */
    private void dealUserMoneyChange() {

        //修改改变后的用户信息
        GlobalSingleton.CreateInstance().UserInfoPool.modifyUserInfo(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //发送信息
                GlobalSingleton.CreateInstance().ServerPushHandler.sendMoneyChange(msg.arg1);
            }
        });
    }
}

