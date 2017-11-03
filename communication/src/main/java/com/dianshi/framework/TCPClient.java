package com.dianshi.framework;

import android.os.Handler;
import android.os.Message;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/22.
 */
public class TCPClient {
    private String host;
    private int port;
    private int version;
    private Handler receiveDataHandler;
    private Handler sendFailHandler;
    private Handler connectFailHandler;
    private Handler heartFailHandler;
    private Handler connectSuccHandler;
    private Socket client;

    public TCPClient(String host,int port, int version
            ,Handler receiveDataHandler, Handler sendFailHandler,  Handler connectFailHandler, Handler connectSuccHandler,Handler heartFailHandler){
        this.host = host;
        this.port = port;
        this.version = version;
        this.receiveDataHandler = receiveDataHandler;
        this.sendFailHandler = sendFailHandler;
        this.connectFailHandler = connectFailHandler;
        this.connectSuccHandler = connectSuccHandler;
        this.heartFailHandler = heartFailHandler;
    }

    /**
     *连接服务端
     */
    public  void Connect(){
        isConnectFail  = false;
        try{
            client = new Socket(host,port);
            Message msg = new Message();
            msg.obj = "succ";
            connectSuccHandler.sendMessage(msg);

        }catch (Exception  e){
            e.printStackTrace();
            isConnectFail = true;
            Message msg = new Message();
            msg.obj = "fail";
            connectFailHandler.sendMessage(msg);
        }finally {

            if (!isConnectFail){
                //开启线程接收数据
                thread_receiveData.start();
            }else{
                if (thread_receiveData.isAlive()){
                    thread_receiveData.interrupt();

                }
                ShutDown();
            }
        }
    }




    /**
     * 关闭面向服务端的连接
     */
    public  void ShutDown(){
        if(client!= null){
            if(client.isConnected() && !client.isClosed()){
                try {
                    client.close();
                }catch (Exception e){
                }
            }
        }
    }

    /**
     * 像服务器发送数据
     * @param type
     * @param data
     */
    public  void Send(final int type, final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (client){
                    try {
                        byte[] bodyBuf = data.getBytes("UTF8");
                        byte[] headBuf = BagHead.AssembleBag(bodyBuf,type,version);
                        OutputStream out = client.getOutputStream();
                        out.write(headBuf);
                        out.write(bodyBuf);
                        out.flush();

                    }catch (Exception e){

                        Message msg = new Message();
                        msg.obj = "fail";
                        sendFailHandler.sendMessage(msg);
                        heartFailHandler.sendMessage(msg);
                    }
                }

            }
        }).start();

    }

    //判断是否失败
    boolean isConnectFail;


    /**
     * 接收数据的线程
     */
    Thread thread_receiveData= new Thread() {
        @Override
        public void run() {

            while (!isConnectFail) {
                try {
                    // 读包头-读12个字节
                    byte[] headBytes = new byte[12];
                    receiveByLen(12,headBytes);

                    //该包头中的中含有包体的信息,包体的长度是多少,如果没有发送BagHea为null
                    BagHead bh = BagHead.SplitBagHead(headBytes,version);

                    // 读包体
                    byte[] bodyBuf = new byte[bh.Length];

                    receiveByLen(bh.Length,bodyBuf);

                    String bodyData = new String(bodyBuf,"UTF8");
                    ReceiveData data = new ReceiveData();
                    data.Type = bh.Type;
                    data.Data = bodyData;

                    Message msg = new Message();
                    msg.obj = data;
                    receiveDataHandler.sendMessage(msg);

                    // 根据type进行处理
                } catch (Exception e) {
                    //服务端连接失败
                    isConnectFail = true;
                    // 网络异常
                    Message msg = new Message();
                    msg.obj = "fail";
                    connectFailHandler.sendMessage(msg);
                    return;
                }finally {

                    if (isConnectFail){
                        if (thread_receiveData.isAlive()){
                            thread_receiveData.interrupt();
                        }
                        ShutDown();
                    }else{
                        if (thread_receiveData.isInterrupted()){
                            thread_receiveData.notify();
                        }
                    }
                }
            }



        }
    };




    /**
     * 读取长度
     * @param len
     * @param bytes
     */
    private void receiveByLen(int len, byte[] bytes)
    {


        int readLen = 0;
        while (readLen <len){

            int pieceLen = 1024;
            int perLen = len - readLen <pieceLen ?len-readLen:pieceLen;
            byte[] buf = new byte[perLen];

            int tmpReadLen = 0;
            while(tmpReadLen <=0){
                try {

                    tmpReadLen = client.getInputStream().read(buf, 0, perLen);

                }catch (Exception e){
                    //服务端连接失败
                    isConnectFail = true;
                    Message msg = new Message();
                    msg.obj = "fail";
                    connectFailHandler.sendMessage(msg);
                    break;
                }
            }
            System.arraycopy(buf,0,bytes,readLen,tmpReadLen);
            readLen += tmpReadLen;

        }
    }
}