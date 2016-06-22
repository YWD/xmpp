package com.itheima.im61.util;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

public class NickUtil {

	/**
	 * 处理昵称
	 * 
	 * @param account
	 * @param nick
	 * @return
	 */
	public static String getNick(String account, String nick) {
		String temp = "";
		if (nick == null || "".equals(nick)) {
			temp = account.substring(0, account.indexOf("@"));
		} else {
			temp = nick;
		}
		return temp;
	}

	/**
	 * 获取拼音
	 * 
	 * @param nick
	 * @return
	 */
	public static String getNickPinYin(String nick) {
		String result = PinyinHelper.convertToPinyinString(nick, "", PinyinFormat.WITHOUT_TONE).toUpperCase();
		return result;
	}

	/**
	 * 过滤非法字符
	 * @param account
	 * @return
	 */
	public static String filterAccount(String account) {

		// to="wengweng61@itheima.com/Smack"
		// from="cang@itheima.com/Spark 2.6.3

		return account.substring(0, account.indexOf("@")) + "@itheima.com";
		//
	}
}
