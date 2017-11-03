package com.dianshi.matchtrader.server;

import android.os.Handler;
import android.os.Message;

import com.dianshi.matchtrader.model.MapProductDeputeCountModel;
import com.dianshi.matchtrader.model.MapProductDetailsNotice;
import com.dianshi.matchtrader.model.MapProductNotice;
import com.dianshi.matchtrader.model.NoticeMsgType;

import java.util.ArrayList;

/**
 *
 * 服务器消息推送handler
 */
public class ServerPushHandler {
    private Handler remotingLoginHandler = new Handler();
    private Handler clearHandler = new Handler();
    private Handler productChangeHandler = new Handler();
    private ArrayList<Handler> deputeHandlerArray = new ArrayList<>();
    private ArrayList<Handler> positionHandlerArray = new ArrayList<>();
    private ArrayList<Handler> newHandlerArray = new ArrayList<>();
    private ArrayList<Handler> moneyChangeHandlerArray = new ArrayList<>();
    private ArrayList<Handler> productPriceChangeHandlerArray = new ArrayList<>();
    private ArrayList<Handler> productDetailChangeHandlerArray = new ArrayList<>();
    private ArrayList<Handler> productDeputeChangeHandlerArray = new ArrayList<>();


    public void setRemotingLoginHandler(Handler handler){
        this.remotingLoginHandler = handler;
    }
    public void sendRemotingLogin(String ip){
        if(remotingLoginHandler!= null){
            Message msg = new Message();
            msg.what = NoticeMsgType.RemotingLogin;
            msg.obj = ip;
            remotingLoginHandler.sendMessage(msg);
        }
    }


    public void  setClearHandler(Handler handler){
        this.clearHandler = handler;
    }
    public void sendClearNotice(){
        if(clearHandler != null){
            Message msg = new Message();
            msg.what= NoticeMsgType.Clear;
            clearHandler.sendMessage(msg);
        }
    }

    /**
     *服务器发送用户的挂单信息
     * @param handler
     */
    public void regDeputeHandler(Handler handler){
        deputeHandlerArray.add(handler);
    }

    /**
     * 服务器发送用户的挂单信息
     */
    public void sendDeputeChangeNotice(){


        for (Handler item: deputeHandlerArray){
            Message msg = new Message();
            msg.what = NoticeMsgType.SelfDeputeChange;
            item.sendMessage(msg);
        }
    }

    public void regPositionHandler(Handler handler){
        positionHandlerArray.add(handler);
    }

    /**
     * 服务器发送持仓信息
     */
    public void sendPositionChangeNotice(int loadState){
        for (Handler item: positionHandlerArray){

            Message msg = new Message();
            msg.arg1 = loadState;
            msg.what = NoticeMsgType.SelfPostionChange;
            item.sendMessage(msg);
        }
    }

    public void setProductChangeHandler(Handler handler){
        this.productChangeHandler = handler;
    }
    public void sendProductChangeNotice(){
        if(productChangeHandler != null){
            Message msg = new Message();
            msg.what = NoticeMsgType.ProductChange;
            productChangeHandler.sendMessage(msg);
        }
    }

    public void regNewsHandler(Handler handler){
        newHandlerArray.add(handler);
    }
    public void sendNewsNotice(){
        for (Handler item:newHandlerArray ){

            Message msg = new Message();
            msg.what = NoticeMsgType.News;
            item.sendMessage(msg);
        }
    }

    /**
     * 用户的金额发生变化
     * @param handler
     */
    public void regMoneyHandler(Handler handler){
        moneyChangeHandlerArray.add(handler);
    }



    public void sendMoneyChange( int loadState){

        for (Handler item:moneyChangeHandlerArray){
            Message msg = new Message();
            msg.arg1 = loadState;
            msg.what = NoticeMsgType.SelfMoneyChange;
            item.sendMessage(msg);
        }
    }

    /**
     * 商品的价格发生变化
     * @param handler
     */
    public void regProductPriceHandler(Handler handler){
        productPriceChangeHandlerArray.add(handler);
    }
    /**
     * 商品的价格发生变化
     * @param maps
     */
    public void sendProductPriceNotice(MapProductNotice maps){

        for (Handler handler: productPriceChangeHandlerArray){
            Message msg = new Message();
            msg.what = NoticeMsgType.ProductPriceChange;
            msg.obj = maps;
            handler.sendMessage(msg);
        }
    }

    public void regProductDetailHandler(Handler handler){
        productDetailChangeHandlerArray.add(handler);
    }

    /**
     * 商品的五档发生变化
     * @param maps
     */
    public void sendProductDetailNotice(MapProductDetailsNotice maps){

        for (Handler handler: productDetailChangeHandlerArray){
            Message msg = new Message();
            msg.what = NoticeMsgType.ProductDetailChange;
            msg.obj = maps;
            handler.sendMessage(msg);
        }
    }

    public void regProductDeputeHandler(Handler handler){
        productDeputeChangeHandlerArray.add(handler);
    }
    public void sendProductDeputeNotice(MapProductDeputeCountModel maps){

        for (Handler handler: productDeputeChangeHandlerArray){
            Message msg = new Message();
            msg.what = NoticeMsgType.ProductDeputeChange;
            msg.obj = maps;
            handler.sendMessage(msg);
        }
    }
}
