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
		String name = "�������������ȥ�����Ĵ����������";

		// PinyinHelper.convertToPinyinString(�ַ���, ÿ���ֲ����ָ���, Ҫ��Ҫ����);
		String result = PinyinHelper.convertToPinyinString(name, "", PinyinFormat.WITHOUT_TONE).toUpperCase();
		System.out.println(result);
	}

	@Test
	public void test2() {
		// ָ������
		SQLiteDatabase db = new DaoMaster.DevOpenHelper(mContext, "im.db", null).getWritableDatabase();
		// ��ȡ DaoSession�Ĺ�����
		DaoMaster master = new DaoMaster(db);
		// ��ȡDaoSession
		DaoSession session = master.newSession();
		Contact c = session.getContactDao().load(100L);
		System.out.println(c);
//		c.setNick("��������");
//		session.getContactDao().update(c);

	}

}
