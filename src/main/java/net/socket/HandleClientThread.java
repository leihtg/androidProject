package net.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import com.anser.annotation.GlobalBeanCollection;
import com.anser.contant.ReceiveData;
import com.anser.model.base.ModelInBase;
import com.anser.model.base.ModelOutBase;
import com.anser.util.BagPacket;
import com.google.gson.Gson;

/**
 * 处理客户端发来的请求 长连接
 *
 * @author leihuating
 * @time 2018年1月10日17:27:5
 */
public class HandleClientThread extends Thread {
    private static Gson gson = new Gson();
    static GlobalBeanCollection globalBean = GlobalBeanCollection.getInstance();
    // 消息队列
    LinkedBlockingQueue<ReceiveData> mqueue = new LinkedBlockingQueue<>();
    private volatile boolean isConnect = false;
    private Socket client;

    public HandleClientThread(Socket socket) {
        isConnect = true;
        this.client = socket;
        writeThread.start();
    }

    @Override
    public void run() {
        try {
            InputStream is = client.getInputStream();
            while (isConnect) {
                ReceiveData rd = BagPacket.readData(is);
//                System.out.println("recv:" + rd.dataType + ",json:" + rd.data);
                mqueue.put(rd);
            }
        } catch (Exception e) {
            isConnect = false;
            writeThread.interrupt();
            e.printStackTrace();
        }

    }

    Thread writeThread = new Thread() {

        @Override
        public void run() {
            try {
                while (isConnect) {
                    ReceiveData take = mqueue.take();
                    // 发送数据
                    wirteResponse(take);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * 向socket写入返回信息
     *
     * @param rd
     */
    private void wirteResponse(ReceiveData rd) {
        try {
            ModelInBase mi = gson.fromJson(rd.data, ModelInBase.class);
            if (null == mi)
                return;
            ModelOutBase mout = globalBean.invokeBusi(mi.getBusType(), rd);
            if (null == mout)
                return;
            mout.setUuid(mi.getUuid());
            String json = gson.toJson(mout);

            OutputStream os = client.getOutputStream();
            byte[] data = json.getBytes("UTF-8");
            BagPacket.sendData(os, data, rd.dataType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
