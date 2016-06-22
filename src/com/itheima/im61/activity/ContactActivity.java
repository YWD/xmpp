package com.itheima.im61.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itheima.im61.ImApp;
import com.itheima.im61.R;
import com.itheima.im61.adapter.ContactAdapter;
import com.itheima.im61.dao.ContactDao;
import com.itheima.im61.util.NickUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ContactActivity extends Activity {
	// 与服务器交互都是通过监听器获得服务器发来的数据===================
	// while(true){
	//
	// }

	@ViewInject(R.id.listview)
	ListView listview;

	private ImApp app;

	private ContentObserver observer = new ContentObserver(new Handler()) {

		// 低版本
		public void onChange(boolean selfChange) {
			requery();
		};

		// 高版本
		public void onChange(boolean selfChange, android.net.Uri uri) {
			requery();
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		Uri uri = Uri.parse("content://" + ContactDao.class.getSimpleName());
		getContentResolver().registerContentObserver(uri, true, observer);

		ViewUtils.inject(this);
		listview.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Cursor cursor = adapter.getCursor();
				cursor.moveToPosition(position);// 选 中点击对应的行
				String toAccount = cursor.getString(cursor
						.getColumnIndex("ACCOUNT"));
				String toNick = cursor.getString(cursor.getColumnIndex("NICK"));
				Intent intent = new Intent(getBaseContext(), ChatActivity.class);

				intent.putExtra("toAccount", toAccount);
				intent.putExtra("toNick", toNick);
				startActivity(intent);
			}
		});

		// 取得好友列表
		app = (ImApp) getApplication();
		requery();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adapter.getCursor().close();
		getContentResolver().unregisterContentObserver(observer);
	}

	private ContactAdapter adapter = null;

	@SuppressLint("NewApi")
	private void requery() {

		if (adapter == null) {

			String sql = "select * from  CONTACT order by  SORT  ASC;";
			Cursor cursor = app.getSqliteDatabase().rawQuery(sql,
					new String[] {});
			adapter = new ContactAdapter(this, cursor, true);// true c.requry
																// 重新取数据+刷新列表
			listview.setAdapter(adapter);
		} else {
			//
			String sql = "select * from  CONTACT order by  SORT  ASC;";
			Cursor newCursor = app.getSqliteDatabase().rawQuery(sql,
					new String[] {});
			Cursor oldCursor = adapter.swapCursor(newCursor);// 旧换新
			oldCursor.close();
			// adapter.getCursor().requery();
		}
	}

}
/**
 * 服务启动时，获取服务器数据更新数据库
 */
