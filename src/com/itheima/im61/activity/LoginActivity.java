package com.itheima.im61.activity;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.im61.ImApp;
import com.itheima.im61.R;
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

	private boolean flag = false;

	@OnClick(R.id.login)
	public void login(View view) {
		// Toast.makeText(getBaseContext(), "我被点了。。", 0).show();
		username = account.getText().toString().trim();
		password = pwd.getText().toString().trim();
		ThreadUtils.runInThread(new Runnable() {

			@Override
			public void run() {

				try {
					// conn.login(账号, 密码);
					Log.d("service", username + ":" + password);
					conn.login(username, password);
					
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}

				ThreadUtils.runUnThread(new Runnable() {

					@Override
					public void run() {
						if (flag) {
							// 保存数据
							ImApp app = (ImApp) getApplication();

							// 保存长连接
							app.setConn(conn);
							// 保存账号
							app.setAccount(username);

							Toast.makeText(getBaseContext(), "登录成功", 0).show();
							startActivity(new Intent(getBaseContext(), ContactActivity.class));
							startService(new Intent(getBaseContext(), CoreService.class));
							finish();
						} else {
							Toast.makeText(getBaseContext(), "登录失败", 0).show();
						}

					}
				});
			}
		});
	}

	/**
	 * 连接
	 */
	private XMPPConnection conn = null;

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
				try {// 192.168.22.63 5222 ConnectionConfiguration:参数配置对象
						// wengweng61@itheima.com;
					ConnectionConfiguration cfg = new ConnectionConfiguration("192.168.159.1", 5222, "yang");
					// 安卓里配置为了省内存基本使用对象配置
					// 连接 XmppConenction:客户端与服务端之间的连接
					conn = new XMPPConnection(cfg);
					// Socket IO
					conn.connect();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
}
