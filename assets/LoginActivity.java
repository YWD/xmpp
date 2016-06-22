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
 * ��¼
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
		// Toast.makeText(getBaseContext(), "�ұ����ˡ���", 0).show();

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
		public void onReceive(final QQMessage msg) {//���߳�

			ThreadUtils.runUnThread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println(msg.toXml());

					if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {

						ImApp app = (ImApp) getApplication();
						// �����˺�
						app.setConn(conn);
						// �������� ������
						try {
							app.setAccount(Long.parseLong(account.getText().toString().trim()));
						} catch (Exception e) {
							// TODO: handle exception
						}
					
						//�����������
						app.setBuddylistJson(msg.content);
						// ��¼�ɹ�
						Toast.makeText(getBaseContext(), "��¼�ɹ���", 0).show();
						startActivity(new Intent(getBaseContext(), ContactActivity.class));
						startService(new Intent(getBaseContext(), CoreService.class));
						finish();
					} else {
						Toast.makeText(getBaseContext(), "�˺Ż����������!", 0).show();
						// ��¼ʧ��
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

		// ����
		ThreadUtils.runInThread(new Runnable() {

			@Override
			public void run() {
				try {
					// ����������֮�������
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
