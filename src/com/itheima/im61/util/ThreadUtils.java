package com.itheima.im61.util;

import android.os.Handler;

public class ThreadUtils {
	/**
	 * ���������߳�
	 * 
	 * @param r
	 */

	public static void runInThread(Runnable r) {
		new Thread(r).start();
	};

	private static Handler hanlder = new Handler();

	/**
	 * ���������߳�
	 * @param r
	 */
	public static void runUnThread(Runnable r) {
		hanlder.post(r);// new Message -->sendMessage-->handleMessaeg() r.run();
	};
}
