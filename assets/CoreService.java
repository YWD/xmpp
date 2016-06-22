package com.itheima.im61.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import com.itheima.im61.ImApp;
import com.itheima.im61.bean.QQMessage;
import com.itheima.im61.bean.QQMessageType;
import com.itheima.im61.core.QQConnection.OnMessageReceiveListener;
import com.itheima.im61.dao.QQMessageDao;
import com.itheima.im61.util.MyTime;
import com.itheima.im61.util.ThreadUtils;

public class CoreService extends Service {

	ImApp app;

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("  核心服务已创建 。。。");
		// Notification
		Toast.makeText(getBaseContext(), "核心服务已创建", 0).show();
		app = (ImApp) getApplication();
		app.getConn().addOnMessageReceiveListener(listener);
		app.setCoreService(this);//设置核心服务
	}

	OnMessageReceiveListener listener = new OnMessageReceiveListener() {

		@Override
		public void onReceive(final QQMessage msg) {

			ThreadUtils.runUnThread(new Runnable() {

				@Override
				public void run() {
					if (QQMessageType.MSG_TYPE_CHAT_P2P.equals(msg.type)) {
						Toast.makeText(getBaseContext(), "好友消息:" + msg.content, 0).show();
						try {
							com.itheima.im61.dao.QQMessage dbMsg=new com.itheima.im61.dao.QQMessage();
							dbMsg.content=msg.content;
							dbMsg.from=msg.from;
							dbMsg.type=msg.type;
							dbMsg.fromAvatar=msg.fromAvatar;
							dbMsg.fromNick=msg.fromNick;
							dbMsg.sendtime=MyTime.geTime(msg.sendTime);
							dbMsg.session_id=msg.from+"";
							dbMsg.to=msg.to;
							//保存在sqlite
							app.getMessageDao().insert(dbMsg);
							
							Uri uri=Uri.parse("content://"+QQMessageDao.class.getSimpleName());
							getContentResolver().notifyChange(uri, null);//所有的接收者
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			});

		}
	};

	/**
	 * 发送
	 * 
	 * @param msg
	 */
	public void sendMessage(QQMessage msg) {
		try {
			app.getConn().sendMessage(msg);
			
			com.itheima.im61.dao.QQMessage dbMsg=new com.itheima.im61.dao.QQMessage();
			dbMsg.content=msg.content;
			dbMsg.from=msg.from;
			dbMsg.type=msg.type;
			dbMsg.fromAvatar=msg.fromAvatar;
			dbMsg.fromNick=msg.fromNick;
			dbMsg.sendtime=MyTime.geTime(msg.sendTime);
			dbMsg.session_id=msg.to+"";
			dbMsg.to=msg.to;
			//保存在sqlite
			app.getMessageDao().insert(dbMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		app.getConn().removeOnMessageReceiveListener(listener);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
