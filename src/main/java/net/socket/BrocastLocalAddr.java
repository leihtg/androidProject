package net.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

import com.anser.contant.Contant;

/**
 * 广播本地地址
 * 
 * @author leihuating
 *
 */
public class BrocastLocalAddr extends Thread {
	@Override
	public void run() {
		try (DatagramSocket ds = new DatagramSocket(Contant.BROCAST_PORT);) {
			ds.setBroadcast(true);
			while (true) {
				byte[] buf = new byte[2048];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				ds.receive(packet);
				// 接收的数据
				buf = Arrays.copyOf(packet.getData(), packet.getLength());
				if (null != buf) {
					switch (buf[0]) {
					case Contant.REQ_HOST_MSG:
						InetAddress clientAddr = packet.getAddress();
						System.out.println("connect from:" + clientAddr);
						packet = new DatagramPacket(new byte[] { Contant.FIND_HOST_ADDR_MSG }, 1, clientAddr,
								Contant.BROCAST_PORT);
						// 发送一个消息带给对方本地ip
						ds.send(packet);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
