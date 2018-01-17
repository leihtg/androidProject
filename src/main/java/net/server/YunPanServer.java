package net.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.anser.contant.Contant;

import net.socket.BrocastLocalAddr;
import net.socket.HandleClientThread;

/**
 * 为android 提供服务
 * 
 * @author leihuating
 * @time 2018年1月9日14:40:20
 */
public class YunPanServer extends Thread {
	public static void main(String[] args) {
		YunPanServer server = new YunPanServer();
		BrocastLocalAddr ba = new BrocastLocalAddr();
		server.checkHomeDir();
		ba.start();
		server.start();
	}

	private void checkHomeDir() {
		if (!new File(Contant.HOME_DIR).exists()) {
			new File(Contant.HOME_DIR).mkdirs();
		}
	}

	@Override
	public void run() {

		try (ServerSocket server = new ServerSocket(Contant.SERVER_PORT);) {
			while (true) {
				Socket client = server.accept();
				System.out.println("tcp link: " + client);
				new HandleClientThread(client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
