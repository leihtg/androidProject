package com.anser.business;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import com.anser.annotation.BusinessType;
import com.anser.contant.MsgType;

/**
 * 文件查询业务
 * 
 * @author leihuating
 * @time 2018年1月18日 下午1:15:02
 */
public class FileQuery {
	@BusinessType(msgType = MsgType.FETCH_DIR)
	public void call(@BusinessType(msgType = 3) Date a, String s) {

	}

	ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(100);

	public void add() {
		new Thread() {

			@Override
			public void run() {
				int i = 0;
				while (i++ < 1000) {
					try {
						queue.put(String.valueOf(i));
						System.out.println("offer: " + i + "\t" + queue.size());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}.start();
	}

	public void take() {
		new Thread() {

			@Override
			public void run() {
				try {
					while (true) {
						String take = queue.take();
						System.out.println("take: " + take);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	public static void main(String[] args) throws Exception {
		FileQuery f = new FileQuery();
		f.add();
		Thread.sleep(1000);
		f.take();
	}

	static void printType(AnnotatedType[] a) {
		for (AnnotatedType aa : a) {
			System.out.println(aa.getType());
		}
	}

	static void print(Annotation[] a) {
		for (Annotation aa : a) {
			System.out.println(aa);
		}
	}
}
