package com.itheima.im61.util;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

public class NickUtil {

	/**
	 * �����ǳ�
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
	 * ��ȡƴ��
	 * 
	 * @param nick
	 * @return
	 */
	public static String getNickPinYin(String nick) {
		String result = PinyinHelper.convertToPinyinString(nick, "", PinyinFormat.WITHOUT_TONE).toUpperCase();
		return result;
	}

	/**
	 * ���˷Ƿ��ַ�
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
