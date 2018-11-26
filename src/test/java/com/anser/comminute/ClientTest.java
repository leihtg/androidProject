/**
 * 
 */
package com.anser.comminute;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.anser.contant.Contant;
import com.anser.contant.DataType;
import com.anser.enums.ActionType;
import com.anser.model.FileTransfer_in;
import com.anser.util.BagPacket;
import com.google.gson.Gson;

/**
 * @author leihuating
 * @time 2018年1月22日 下午3:47:19
 */
public class ClientTest {
	static Gson gson = new Gson();

	static void uploadFile() {
		try {
			Socket client = new Socket("localhost", Contant.SERVER_PORT);
//			File file = new File("D:\\soft\\python-3.6.3.exe");
			File file = new File("D:\\soft\\mingw-get-setup.exe");

			OutputStream os = client.getOutputStream();
			InputStream is = client.getInputStream();

			FileInputStream fis = new FileInputStream(file);
			byte[] buf = new byte[2048];
			int i = 0;
			long length = file.length(), pos = 0;
			while ((i = fis.read(buf)) != -1) {
				FileTransfer_in fi = new FileTransfer_in();

				fi.setPos(pos);
				fi.setLength(length);
				fi.setPath("myCloud");
				fi.setUuid("uuid");
				fi.setBusType(ActionType.UP_LOAD);
				fi.setBuf(Arrays.copyOf(buf, i));

				byte[] bytes = gson.toJson(fi).getBytes("UTF-8");
				byte[] b = BagPacket.AssembleBag(bytes.length, DataType.CallFunc);
				os.write(b);
				os.write(bytes);
				os.flush();

				pos += i;
				System.out.println(pos * 100 / length);
			}
			client.shutdownOutput();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
//		uploadFile();
		class A{
			void p() {
				System.out.println("a");
			}
			void hide() {
				System.out.println("hide A");
			}
		}
		class B extends A{
			void p() {
				System.out.println("b");
			}
			void hide(String s) {
				System.out.println("hide B");
			}
		}
		A b=new B();
		((A)b).p();
		((B)b).hide("d");
	}
}
