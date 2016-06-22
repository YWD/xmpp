package com.itheima.im61.util;

import android.os.Handler;

public class ThreadUtils {
	/**
	 * 运行在子线程
	 * 
	 * @param r
	 */

	public static void runInThread(Runnable r) {
		new Thread(r).start();
	};

	private static Handler hanlder = new Handler();

	/**
	 * 运行在主线程
	 * @param r
	 */
	public static void runUnThread(Runnable r) {
		hanlder.post(r);// new Message -->sendMessage-->handleMessaeg() r.run();
	};
}
