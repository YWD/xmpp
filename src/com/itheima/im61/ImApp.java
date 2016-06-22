package com.itheima.im61;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.im61.dao.ContactDao;
import com.itheima.im61.dao.DaoMaster;
import com.itheima.im61.dao.DaoSession;
import com.itheima.im61.dao.SmsDao;
import com.itheima.im61.service.CoreService;

//Application整个应用
public class ImApp extends Application {

	private CoreService coreServer = null;

	public CoreService getCoreServer() {
		return coreServer;
	}

	public void setCoreServer(CoreService coreServer) {
		this.coreServer = coreServer;
	}

	private XMPPConnection conn;

	private String account;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public XMPPConnection getConn() {
		return conn;
	}

	public void setConn(XMPPConnection conn) {
		this.conn = conn;
	}

	private ContactDao contactDao = null;

	/**
	 * 联系人的dao
	 * 
	 * @return
	 */
	public ContactDao getContactDao() {
		return contactDao;
	}

	private SQLiteDatabase sqliteDatabase;

	public SQLiteDatabase getSqliteDatabase() {
		return sqliteDatabase;
	}

	private SmsDao smsDao = null;

	public SmsDao getSmsDao() {
		return smsDao;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("IM应用创建");

		// 指明数据
		sqliteDatabase = new DaoMaster.DevOpenHelper(this, "im.db", null)
				.getWritableDatabase();
		// 获取 DaoSession的管理者
		DaoMaster master = new DaoMaster(sqliteDatabase);
		// 获取DaoSession
		DaoSession session = master.newSession();

		contactDao = session.getContactDao();

		smsDao = session.getSmsDao();
	}
}
