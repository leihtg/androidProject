package net.socket;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import net.socket.contant.Contant;

/**
 * 为android 提供服务
 * 
 * @author leihuating
 * @time 2018年1月9日14:40:20
 */
public class YunPanServer extends Thread {
	public static void main(String[] args) {
		YunPanServer server = new YunPanServer();
		server.checkHomeDir();
		server.start();
		server.startListen();
	}

	private void checkHomeDir() {
		if (!new File(Contant.HOME_DIR).exists()) {
			new File(Contant.HOME_DIR).mkdirs();
		}
	}

	public void startListen() {
		new Thread(run).start();
	}

	private Runnable run = new Runnable() {

		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(Contant.SERVER_PORT);
				while (true) {
					Socket client = server.accept();
					System.out.println("tcp link :" + client);
					new HandleClientThread(client).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void run() {
		try {
			DatagramSocket ds = new DatagramSocket(Contant.BROCAST_PORT);
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
