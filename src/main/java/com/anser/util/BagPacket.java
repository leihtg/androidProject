package com.anser.util;

import java.io.InputStream;
import java.io.OutputStream;

import com.anser.contant.Contant;
import com.anser.contant.ReceiveData;

/**
 * 报文包 Created by leihuating on 2018/1/15.
 */

public class BagPacket {
	// 报文里面各类型字节数
	private static final byte ver_len = 2;// 2字节
	private static final byte type_len = 4;
	private static final byte total_len = 4;

	public int version;
	public int type;
	public int length;

	public static int getHeadLen() {
		return ver_len + type_len + total_len;
	}

	/**
	 * 拆包
	 *
	 * @return
	 */
	public static BagPacket splitBag(byte[] buf) {
		if (buf.length < getHeadLen()) {
			throw new RuntimeException("length buf is:" + buf.length);
		}
		BagPacket bp = new BagPacket();
		bp.version = BitConvert.convertToInt(buf, 0, ver_len);
		bp.type = BitConvert.convertToInt(buf, ver_len, type_len);
		bp.length = BitConvert.convertToInt(buf, ver_len + type_len, total_len);
		if (bp.version != Contant.VERSION) {
			// 版本控制,防止乱输入导致length过大
			throw new IllegalArgumentException("Version:" + bp.version + ",should be:" + Contant.VERSION);
		}
		return bp;
	}

	/**
	 * 装包
	 *
	 * @param dataLen
	 * @param type
	 * @return
	 */
	public static byte[] AssembleBag(int dataLen, int type) {
		byte[] ret = new byte[getHeadLen()];

		int version = Contant.VERSION;
		byte[] vBytes = BitConvert.convertToBytes(version, ver_len);
		byte[] tBytes = BitConvert.convertToBytes(type, type_len);
		byte[] sBytes = BitConvert.convertToBytes(dataLen, total_len);

		System.arraycopy(vBytes, 0, ret, 0, ver_len);
		System.arraycopy(tBytes, 0, ret, ver_len, type_len);
		System.arraycopy(sBytes, 0, ret, ver_len + type_len, total_len);

		return ret;
	}

	/**
	 * 发送数据
	 * 
	 * @param data
	 * @param type
	 * @param os
	 * @return
	 */
	public static boolean sendData(OutputStream os, byte[] data, int type) {
		boolean ret = true;
		try {
			byte[] head = AssembleBag(data.length, type);
			os.write(head);
			os.write(data);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 读取一个完整的包
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static ReceiveData readData(InputStream is) throws Exception {
		ReceiveData rd = null;
		byte[] head = new byte[getHeadLen()];
		// 读取头
		receiveByLen(is, head);
		BagPacket bp = splitBag(head);
		byte[] body = new byte[bp.length];
		// 读包体
		receiveByLen(is, body);
		rd = new ReceiveData();
		rd.dataType = bp.type;
		rd.data = new String(body, "UTF-8");

		System.out.println("query body:"+bp.length);
		return rd;
	}

	/**
	 * 读取固定长度,如果数据不够则阻塞
	 *
	 * @param bytes
	 * @throws Exception
	 */
	public static void receiveByLen(InputStream is, byte[] bytes) throws Exception {
		try {
			int len = bytes.length, readLen = 0, r;
			while (readLen < len) {
				r = is.read(bytes, readLen, len - readLen);
				if (r == -1) {// 对方关闭了输出流
					break;
				}
				readLen += r;
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
