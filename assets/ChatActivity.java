package com.itheima.im61.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
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
import com.itheima.im61.bean.QQMessage;
import com.itheima.im61.bean.QQMessageType;
import com.itheima.im61.dao.QQMessageDao;
import com.itheima.im61.util.MyTime;
import com.itheima.im61.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ChatActivity extends Activity {

	private String toNick;
	private long toAccount;

	@ViewInject(R.id.title)
	TextView title;
	@ViewInject(R.id.listview)
	ListView listview;
	@ViewInject(R.id.input)
	EditText input;

	@OnClick(R.id.send)
	public void send(View view) {

		String messageBody = input.getText().toString().trim();
		if ("".equals(messageBody)) {
			Toast.makeText(this, "发送消息不能为空", 0).show();
			return;
		}

		input.setText("");
		final QQMessage msg = new QQMessage();
		msg.type = QQMessageType.MSG_TYPE_CHAT_P2P;
		msg.content = messageBody;
		msg.from = app.getAccount();
		msg.to = toAccount;
		
		msg.fromNick = app.getAccount() + "";
		list.add(msg);

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		// 最后一行显示
		if (list.size() > 0) {
			listview.setSelection(list.size() - 1);
		}

		ThreadUtils.runInThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				app.getCoreService().sendMessage(msg);// 创建 子线程
			}
		});

	}

	ImApp app;
	List<QQMessage> list = new ArrayList<QQMessage>();
	ChatMessageAdapter adapter = null;

	private ContentObserver observer = new ContentObserver(new Handler()) {

		// 接收信号后的处理方法： 2.2
		public void onChange(boolean selfChange) {
			requery();

		};

		// 接收信号后的处理方法 4.0
		public void onChange(boolean selfChange, android.net.Uri uri) {
			requery();
		};
	};

	protected void requery() {
		System.out.println("刷新聊天界面");

		list.clear();

		// select * from QQMESSAGE where SESSION_ID ='102' order by SENDTIME
		// ASC;

		List<com.itheima.im61.dao.QQMessage> msgs = app.getMessageDao().queryRaw(" where  SESSION_ID =? order  by SENDTIME ASC", toAccount + "");

		for (com.itheima.im61.dao.QQMessage item : msgs) {
			final QQMessage bean = new QQMessage();
			bean.type = QQMessageType.MSG_TYPE_CHAT_P2P;
			bean.content = item.content;
			bean.from = item.from;
			bean.to = item.to;
			bean.fromNick = item.from + "";
			bean.sendTime=MyTime.geTime(item.sendtime);
			list.add(bean);
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// 查询新数据
		if (list.size() > 0) {
			listview.setSelection(list.size() - 1);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getContentResolver().unregisterContentObserver(observer);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Uri uri = Uri.parse("content://" + QQMessageDao.class.getSimpleName());
		// content://com.itheima.add
		// content://com.itheima.add/1
		// getContentResolver().registerContentObserver(地址：信号 , 子路径是否有效, 内容观察者);
		getContentResolver().registerContentObserver(uri, true, observer);

		// 获取应用
		app = (ImApp) getApplication();
		Intent intent = getIntent();

		toNick = intent.getStringExtra("nick");
		toAccount = intent.getLongExtra("account", 0L);
		System.out.println(toNick);

		setContentView(R.layout.activity_chat);

		ViewUtils.inject(this);
		title.setText("与" + toNick + "聊天中");

		// select * from QQMESSAGE where SESSION_ID ='102' order by SENDTIME
		// ASC;

		List<com.itheima.im61.dao.QQMessage> msgs = app.getMessageDao().queryRaw(" where  SESSION_ID =? order  by SENDTIME ASC", toAccount + "");

		for (com.itheima.im61.dao.QQMessage item : msgs) {
			final QQMessage bean = new QQMessage();
			bean.type = QQMessageType.MSG_TYPE_CHAT_P2P;
			bean.content = item.content;
			bean.from = item.from;
			bean.to = item.to;
			bean.fromNick = item.from + "";
			bean.sendTime=MyTime.geTime(item.sendtime);
			list.add(bean);
		}

		// // 发送 男主角
		// for (int i = 0; i < 10; i++) {
		// QQMessage msg = new QQMessage();
		// msg.type = QQMessageType.MSG_TYPE_CHAT_P2P;
		// msg.content = "今晚约不？？？";
		// msg.from = app.getAccount();
		// msg.to = toAccount;
		// msg.fromNick = "男主角";
		// list.add(msg);
		// }
		// // 接收
		// for (int i = 0; i < 10; i++) {
		// QQMessage msg = new QQMessage();
		// msg.type = QQMessageType.MSG_TYPE_CHAT_P2P;
		// msg.content = "三里屯 优衣库记得带...";
		// msg.from = toAccount;
		// msg.to = app.getAccount();
		// msg.fromNick = "女主角";
		// list.add(msg);
		// }

		// 内容

		adapter = new ChatMessageAdapter(this, list);
		listview.setAdapter(adapter);

		// 最后一行显示
		if (list.size() > 0) {
			listview.setSelection(list.size() - 1);
		}
	}

}
