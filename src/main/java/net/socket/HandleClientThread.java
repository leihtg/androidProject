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
			while (isConnect) {
				// 读取头
				byte[] head = new byte[BagPacket.getHeadLen()];
				receiveByLen(head);
				BagPacket bp = BagPacket.splitBag(head);
				// 读包体
				byte[] body = new byte[bp.length];
				receiveByLen(body);

				ReceiveData rd = new ReceiveData();
				rd.type = bp.type;
				rd.data = new String(body, "UTF-8");

//				System.out.println("recv:" + rd.type + ",json:" + rd.data);

				mqueue.put(rd);
			}
		} catch (Exception e) {
			isConnect = false;
			e.printStackTrace();
		}

	}

	/**
	 * 读取长度
	 *
	 * @param bytes
	 */
	private void receiveByLen(byte[] bytes) {
		try {
			int len = bytes.length, readLen = 0, r;
			InputStream is = client.getInputStream();
			while (readLen < len) {
				r = is.read(bytes, readLen, len - readLen);
				if (r == -1) {// 对方关闭了输出流
					isConnect = false;
					break;
				}
				readLen += r;
			}
		} catch (Exception e) {
			isConnect = false;
			e.printStackTrace();
		}
	}

	Thread writeThread = new Thread() {

		@Override
		public void run() {
			while (isConnect) {
				try {
					ReceiveData take = mqueue.take();
					// 发送数据
					wirteResponse(take);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
			byte[] data = json.getBytes("UTF-8");
			byte[] head = BagPacket.AssembleBag(data.length, rd.type);

			OutputStream os = client.getOutputStream();
			os.write(head);
			os.write(data);
			os.flush();

			BagPacket sb = BagPacket.splitBag(head);
			System.out.println(String.format("return: type[%d],length[%d]\n", sb.type, sb.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
