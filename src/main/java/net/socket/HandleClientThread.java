package net.socket;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.anser.contant.Contant;
import com.anser.contant.MsgType;
import com.anser.contant.ReceiveData;
import com.anser.model.FileModel;
import com.anser.model.FileQueryModel_in;
import com.anser.model.FileQueryModel_out;
import com.anser.model.base.ModelInBase;
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
	private volatile boolean isConnect = false;
	private Socket client;

	public HandleClientThread(Socket socket) {
		this.client = socket;
		isConnect = true;
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

				System.out.println("recv:" + rd.type + ",json:" + rd.data);

				// 发送数据
				wirteResponse(rd);
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

	/**
	 * 向socket写入返回信息
	 * @param rd
	 */
	private void wirteResponse(ReceiveData rd) {
		try {
			ModelInBase mi = gson.fromJson(rd.data, ModelInBase.class);
			switch (mi.getBusType()) {
			case MsgType.FETCH_DIR:
				FileQueryModel_in fm = gson.fromJson(rd.data, FileQueryModel_in.class);
				File file = new File(Contant.HOME_DIR, fm.getPath());
				FileQueryModel_out out = new FileQueryModel_out();
				out.setUuid(fm.getUuid());
				out.setList(listFile(file));
				String json = gson.toJson(out);
				byte[] data = json.getBytes("UTF-8");
				byte[] head = BagPacket.AssembleBag(data.length, rd.type);

				OutputStream os = client.getOutputStream();
				os.write(head);
				os.write(data);
				os.flush();

				BagPacket sb = BagPacket.splitBag(head);
				System.out.println(String.format("\nreturn: type[%d],length[%d]", sb.type, sb.length));
				break;

			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<FileModel> listFile(File file) {
		List<FileModel> dirs = new ArrayList<>();
		List<FileModel> files = new ArrayList<>();

		for (File f : file.listFiles()) {
			FileModel m = new FileModel();
			m.setName(f.getName());
			m.setLastModified(f.lastModified());
			m.setLength(f.length());
			m.setDir(f.isDirectory());
			if (f.isDirectory()) {
				dirs.add(m);
			} else {
				files.add(m);
			}
		}
		dirs.addAll(files);
		return dirs;
	}

}
