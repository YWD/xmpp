package com.itheima.im61.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.im61.ImApp;
import com.itheima.im61.R;
import com.itheima.im61.bean.QQMessage;
import com.itheima.im61.bean.QQMessageType;
import com.itheima.im61.core.QQConnection;
import com.itheima.im61.core.QQConnection.OnMessageReceiveListener;
import com.itheima.im61.service.CoreService;
import com.itheima.im61.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 登录
 * 
 * @author itheima
 * 
 */
public class LoginActivity extends Activity {

	@ViewInject(R.id.account)
	EditText account;
	@ViewInject(R.id.pwd)
	EditText pwd;

	String username;
	String password;

	@OnClick(R.id.login)
	public void login(View view) {
		// Toast.makeText(getBaseContext(), "我被点了。。", 0).show();

		username = account.getText().toString().trim();
		password = pwd.getText().toString().trim();
		ThreadUtils.runInThread(new Runnable() {

			@Override
			public void run() {

				try {
					QQMessage msg = new QQMessage();
					msg.type = QQMessageType.MSG_TYPE_LOGIN;
					msg.content = username + "#" + password;
					conn.sendMessage(msg);// IO writeUTF
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	QQConnection conn;
	protected void onDestroy() {
		super.onDestroy();
		conn.removeOnMessageReceiveListener(listener);
	};

	OnMessageReceiveListener  listener=new OnMessageReceiveListener() {

		@Override
		public void onReceive(final QQMessage msg) {//子线程

			ThreadUtils.runUnThread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println(msg.toXml());

					if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {

						ImApp app = (ImApp) getApplication();
						// 保存账号
						app.setConn(conn);
						// 保存连接 长连接
						try {
							app.setAccount(Long.parseLong(account.getText().toString().trim()));
						} catch (Exception e) {
							// TODO: handle exception
						}
					
						//保存好友数据
						app.setBuddylistJson(msg.content);
						// 登录成功
						Toast.makeText(getBaseContext(), "登录成功！", 0).show();
						startActivity(new Intent(getBaseContext(), ContactActivity.class));
						startService(new Intent(getBaseContext(), CoreService.class));
						finish();
					} else {
						Toast.makeText(getBaseContext(), "账号或者密码错误!", 0).show();
						// 登录失败
					}

					
				}
			})	;					}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);

		// 连接
		ThreadUtils.runInThread(new Runnable() {

			@Override
			public void run() {
				try {
					// 创建与服务端之间的连接
					conn = new QQConnection("192.168.159.1", 9090);
					conn.addOnMessageReceiveListener(listener);
					conn.connect();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
}
