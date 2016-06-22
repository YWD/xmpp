package com.itheima.im61.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.itheima.im61.ImApp;
import com.itheima.im61.R;
import com.itheima.im61.adapter.ContactAdapter;
import com.itheima.im61.bean.QQContact;
import com.itheima.im61.bean.QQContactList;
import com.itheima.im61.bean.QQMessage;
import com.itheima.im61.bean.QQMessageType;
import com.itheima.im61.core.QQConnection.OnMessageReceiveListener;
import com.itheima.im61.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ContactActivity extends Activity {

	@ViewInject(R.id.listview)
	ListView listview;

	List<QQContact> list = new ArrayList<QQContact>();

	ImApp app;

	OnMessageReceiveListener listener = new OnMessageReceiveListener() {

		@Override
		public void onReceive(final QQMessage msg) {
		
			ThreadUtils.runUnThread(new Runnable() {
				
				@Override
				public void run() {
					if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {
						String json = msg.content;
						Gson gson = new Gson();
						// 新数据
						QQContactList temp = gson.fromJson(json, QQContactList.class);
						// 刷新
						list.clear();
						list.addAll(temp.buddyList);

						//刷新列表
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
					}
					
				}
			});

		}
	};
	ContactAdapter adapter;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		app.getConn().removeOnMessageReceiveListener(listener);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 循环数据
		setContentView(R.layout.activity_contact);

		ViewUtils.inject(this);
		

		app = (ImApp) getApplication();
		app.getConn().addOnMessageReceiveListener(listener);
		String json = app.getBuddylistJson();
		System.out.println(json);
		// 07-21 06:55:06.105: I/System.out(3844):
		// {"buddyList":[{"account":101,"nick":"QQ 1","avatar":0}]}

		// Gson
		Gson gson = new Gson();
		QQContactList temp = gson.fromJson(json, QQContactList.class);
		list.addAll(temp.buddyList);
		// for (int i = 0; i < 20; i++) {
		// QQContact contact = new QQContact();
		// contact.account = 2010L;
		// contact.nick = "优衣库" + i;
		// contact.avatar = 0;
		// list.add(contact);
		// }

		adapter = new ContactAdapter(this, list);
		listview.setAdapter(adapter);
		
		
		//添加监听器
		listview.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				//获取点击数据 
				QQContact contact=list.get(position);
				
				Intent intent=new Intent(getBaseContext(),ChatActivity.class);
				intent.putExtra("account", contact.account);//Long
				intent.putExtra("nick", contact.nick);//String
				startActivity(intent);
			}
		});
	}
}
