package com.itheima.im61.test;

import java.util.List;

import org.junit.Test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.itheima.im61.R;
import com.itheima.im61.dao.DaoMaster;
import com.itheima.im61.dao.DaoSession;
import com.itheima.im61.dao.QQContact;
import com.itheima.im61.dao.QQContactDao;

public class TestQQContactDao extends AndroidTestCase {

	@Test
	public void testAdd() {

		QQContact contact = new QQContact();
		contact.setAccount(101L);
		contact.setNick("男主角");
		contact.setAvatar(R.drawable.ic_launcher);

		// SqliteOpenHelper
		// |--DevOpenHelper
		// 获取 DaoSession
		SQLiteDatabase db = new DaoMaster.DevOpenHelper(mContext, "im.db", null).getWritableDatabase();
		DaoMaster master = new DaoMaster(db);// 哪个数据库的？
		DaoSession session = master.newSession();
		QQContactDao dao = session.getQQContactDao();
		// dao.insert(contact); 添加
		// QQContact contact2=dao.load(1L);
		// contact2.setNick("男主角-优衣库");
		// dao.update(contact2);//修改

		// QQContact item = dao.load(1L);
		// System.out.println(item.getNick() + " " + item.getAccount() + " " +
		// item.getAvatar());

		dao.deleteByKey(1L);
		List<QQContact> list = dao.loadAll();// 查询
		for (QQContact item : list) {
			System.out.println(item.getNick() + " " + item.getAccount() + " " + item.getAvatar());
		}

	}

	@Test
	public void testFromXMLString() {
	}

}
