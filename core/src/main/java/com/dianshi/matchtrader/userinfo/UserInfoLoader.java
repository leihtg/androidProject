package com.dianshi.matchtrader.userinfo;

import android.os.Handler;
import android.os.Message;

import com.dianshi.matchtrader.model.CustomerInfoModel_out;
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载用户的积分
 */
public class UserInfoLoader
{
    public Handler loadSuccHandler;
    private boolean isLoadUserMoney= false;
    private boolean isError = false;
    private String errorMsg = "";
    public void load()
    {

        loadUserMoney();
    }

    /**
     * 加载用户金额
     */
    private void loadUserMoney(){
        ConcurrentHashMap<String,String> dict = new ConcurrentHashMap<>();

        FuncCall<ModelInBase,CustomerInfoModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = loadMoneyFailHandler;
        funcCall.FuncResultHandler = loadMoneySuccHandler;
        funcCall.Call("CustomerInfo",new ModelInBase(),CustomerInfoModel_out.class,dict);

        if(dict != null && dict.keySet().size()>0){
            isLoadUserMoney = true;
            isError = true;
            errorMsg = "加载用户金额失败";
            checkLoad();
        }

    }



    /**
     * 加载用户信息成功
     * {"UserName":"dianshi11","Type":"个人","PhoneNum":"15737954894","RoleName":"总公司","Money":"1000000000414.38","Jifen":"0.00","FrozenMoney":"0.00","Timestamp":"481038b7-c415-4378-9fb2-126f5bf8992d"}
     */
    private Handler loadMoneySuccHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            isLoadUserMoney = true;
            CustomerInfoModel_out model_out = (CustomerInfoModel_out)msg.obj;
            GlobalSingleton.CreateInstance().UserInfoPool.AddUserInfo(model_out);
            checkLoad();
        }
    };
    /**
     * 加载商品信息失败
     */
    private Handler loadMoneyFailHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            isLoadUserMoney = true;
            isError = true;
            errorMsg = errorModel_out.getErrorMsg();
            checkLoad();

        }
    };


    /**
     * 商品和商品分类加载成功或失败都发送给同一个handler---loadSuccHandler
     */
    private void checkLoad(){

        if(isLoadUserMoney ){
            if(loadSuccHandler != null){
                Message msg = new Message();
                msg.obj = errorMsg;
                msg.arg1 = isError ? -1:1;
                loadSuccHandler.sendMessage(msg);
            }
        }
    }
}
