package net.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

/**
 * 多播
 * 
 * @author leihuating
 * @time 2017年8月1日 下午3:49:18
 */
public class MutiCastSocket {

    public void recvMutiCastBroad() {
        try (MulticastSocket ms = new MulticastSocket(7778);) {
            // ms.setReuseAddress(true);
            // ms.bind(new InetSocketAddress(7777));
            InetAddress iet = InetAddress.getByName("224.1.1.1");
            ms.joinGroup(iet);
            ms.joinGroup(InetAddress.getByName("224.1.1.2"));
            byte[] buf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            int i = 1;
            while (true) {
                ms.receive(recv);

                System.out.println(recv.getAddress() + "\t" + new String(recv.getData(), 0, recv.getLength()));
                buf = (i++ + "_套接字可能有必要把超过一个的套接字绑定到相同的套接字地址。").getBytes("GBK");

                recv.setData(buf);;
                ms.send(recv);
                // if (i > 10) {
                // ms.leaveGroup(iet);
                // }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void sendMutiCastBroad() {
        try (MulticastSocket ms = new MulticastSocket(8877);) {
            ms.setReuseAddress(true);
            byte[] buf = new byte[2048];
            InetSocketAddress addr = new InetSocketAddress("224.1.1.1", 7777);
            ms.joinGroup(InetAddress.getByName("224.1.1.1"));
            ms.joinGroup(InetAddress.getByName("224.1.1.2"));
            DatagramPacket dp = new DatagramPacket(buf, buf.length, addr);
            int i = 1;
            while (true) {
                dp.setData((i++ + "_将消息发送到多播组时，该主机和端口的所有预定接收者都将接收到消息（在数据包的生存时间范围内，请参阅下文）。套接字不必成为多播组的成员即可向其发送消息。").getBytes("GBK"));
                ms.send(dp);
                dp.setData(buf);
                ms.receive(dp);
                System.out.println(dp.getAddress() + ":" + dp.getPort() + "\t" + new String(dp.getData(), 0, dp.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
