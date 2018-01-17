package net.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 广播
 * 
 * @author leihuating
 * @time 2017年8月1日 下午3:53:25
 */
public class BroadCastSocket {

    public void recvCastbroad() {
        try (DatagramSocket ds = new DatagramSocket(7777);) {
            DatagramPacket dp = null;
            ds.setBroadcast(true);
            byte[] buf = new byte[2048];
            dp = new DatagramPacket(buf, buf.length);
            int i = 1;
            while (true) {
                ds.receive(dp);
                System.out.println(i++ + "\t" + new String(dp.getData(), 0, dp.getLength()));
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBroadCast() {
        byte[] buf = null;
        try (DatagramSocket ds = new DatagramSocket();) {
            // 172.29.112.255
            InetAddress iet = InetAddress.getByName("224.1.1.1");
            ds.setBroadcast(true);
            buf = "客户端请求连接！".getBytes("GBK");
            DatagramPacket dp = new DatagramPacket(buf, buf.length, iet, 7777);
            ds.send(dp);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
