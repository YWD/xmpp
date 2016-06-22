package com.itheima.im61.test;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

import org.junit.Test;

import com.itheima.im61.dao.Contact;
import com.itheima.im61.dao.DaoMaster;
import com.itheima.im61.dao.DaoSession;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class PinyinTest extends AndroidTestCase {

	@Test
	public void test() {
		String name = "最近看段子吗？有去岛国的带点正版回来";

		// PinyinHelper.convertToPinyinString(字符串, 每个字产生分隔符, 要不要声调);
		String result = PinyinHelper.convertToPinyinString(name, "", PinyinFormat.WITHOUT_TONE).toUpperCase();
		System.out.println(result);
	}

	@Test
	public void test2() {
		// 指明数据
		SQLiteDatabase db = new DaoMaster.DevOpenHelper(mContext, "im.db", null).getWritableDatabase();
		// 获取 DaoSession的管理者
		DaoMaster master = new DaoMaster(db);
		// 获取DaoSession
		DaoSession session = master.newSession();
		Contact c = session.getContactDao().load(100L);
		System.out.println(c);
//		c.setNick("隔避老王");
//		session.getContactDao().update(c);

	}

}
