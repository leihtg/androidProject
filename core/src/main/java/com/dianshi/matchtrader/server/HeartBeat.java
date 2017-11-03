package com.dianshi.matchtrader.server;

import android.os.Handler;
import android.os.Message;

import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.Date;

/**
 * 服务器心跳机制
 * Created by Administrator on 2016/4/22.
 */
public class HeartBeat {

    private Handler heartBeartHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Date curDate  =  new  Date(System.currentTimeMillis());
            globalSingleton.LastHeartBeat = curDate;
            isBearting  = true;
        }
    };
    private Handler heartStopHandler;
    private int sleepSecond;
    private boolean isBearting = false;
    private GlobalSingleton globalSingleton;

    /**
     * 实例化
     * @param sleepSecond
     * @param stopHandler
     */
    public HeartBeat(int sleepSecond,  Handler stopHandler){
        this.sleepSecond = sleepSecond;
        isBearting = true;
        this.heartStopHandler = stopHandler;
        globalSingleton = GlobalSingleton.CreateInstance();
        globalSingleton.getTCPSingleton().HeartBeatHandler = heartBeartHandler;
        globalSingleton.getTCPSingleton().HeartStopHandler = heartStopHandler;
        Date curDate  =  new  Date(System.currentTimeMillis());
        globalSingleton.LastHeartBeat = curDate;
    }

    /**
     * 心跳开始
     */
    public void Start(){
        new Thread(){
            @Override
            public void run() {
                Beat();
            }
        }.start();
    }

    /**
     * 心跳终止
     */
    public void Abort()
    {
        isBearting = false;
    }

    /**
     * 发送心跳跳动
     */
    public void Beat(){
        while (isBearting){
            try {
                globalSingleton.getTCPSingleton().HeartBeat();
                Thread.sleep(sleepSecond * 1000);
            }catch (Exception e){

            }
        }
    }


}
