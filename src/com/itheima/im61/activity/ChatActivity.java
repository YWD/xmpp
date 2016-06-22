package com.itheima.im61.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.im61.ImApp;
import com.itheima.im61.R;
import com.itheima.im61.adapter.ChatMessageAdapter;
import com.itheima.im61.dao.SmsDao;
import com.itheima.im61.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ChatActivity extends Activity {

	private String toNick;
	private String toAccount;

	@ViewInject(R.id.title)
	TextView title;
	@ViewInject(R.id.listview)
	ListView listview;
	@ViewInject(R.id.input)
	EditText input;

	ImApp app;

	@OnClick(R.id.send)
	public void send(View view) {

		final String body = input.getText().toString().trim();

		if ("".equals(body)) {
			Toast.makeText(this, "消息不为空", 0).show();
			return;
		}
		input.setText("");

		ThreadUtils.runInThread(new Runnable() {
			@Override
			public void run() {
				try {
					// chat.sendMessage(body);
					Message message = new Message(toAccount, Type.chat);
					message.setFrom(app.getAccount() + "@itheima.com");
					message.setBody(body);
					message.setSubject(toNick);
					app.getCoreServer().sendMesage(message);
				} catch (XMPPException e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getContentResolver().unregisterContentObserver(observer);
		adapter.getCursor().close();
	}

	private ContentObserver observer = new ContentObserver(new Handler()) {
		public void onChange(boolean selfChange) {
			requey();
		};

		public void onChange(boolean selfChange, android.net.Uri uri) {
			requey();
		};
	};

	private ChatMessageAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Uri uri = Uri.parse("content://" + SmsDao.class.getSimpleName());
		getContentResolver().registerContentObserver(uri, true, observer);

		ViewUtils.inject(this);
		app = (ImApp) getApplication();

		Intent intent = getIntent();

		toNick = intent.getStringExtra("toNick");
		toAccount = intent.getStringExtra("toAccount");
		title.setText("与" + toNick + "聊天中 ");

		requey();

	}

	/**
	 * 查询与刷新
	 */
	@SuppressLint("NewApi")
	private void requey() {

		if (adapter == null) {
			// 数据
			String sql = "select * from SMS where SESSION_ID=?  order by TIME ASC; ";
			Cursor c = app.getSqliteDatabase().rawQuery(sql, new String[] { toAccount });
			adapter = new ChatMessageAdapter(this, c, true);
			listview.setAdapter(adapter);

			if (c.getCount() > 0) {
				listview.setSelection(c.getCount() - 1);
			}
		} else {
			String sql = "select * from SMS where SESSION_ID=?  order by TIME ASC; ";
			Cursor newCursor = app.getSqliteDatabase().rawQuery(sql, new String[] { toAccount });
			Cursor oldCursor = adapter.swapCursor(newCursor);
			oldCursor.close();
			if (newCursor.getCount() > 0) {
				listview.setSelection(newCursor.getCount() - 1);
			}
		}

	}

}
