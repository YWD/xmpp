package com.itheima.im61;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.itheima.im61.bean.QQMessage;
import com.itheima.im61.bean.QQMessageType;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	DataOutputStream writer = null;
	DataInputStream reader = null;
	public void login(View view)
	{
		
		
		
		//101~199   test
		new Thread() {
			public void run() {
				// ip:�����ַ 192.168.22.57
				// port:�������ı�� 5222
				try {
					final QQMessage msg=new QQMessage();
					msg.type=QQMessageType.MSG_TYPE_LOGIN;
					msg.content="101#test";
					Socket client = new Socket("192.168.22.57", 5225);
					try {
						if (writer == null) {
							writer = new DataOutputStream(client.getOutputStream());
							reader = new DataInputStream(client.getInputStream());
						}
						//���͵�¼ ��Ϣ
						writer.writeUTF(msg.toXml());
						
						
						new  Thread(){
							public void run() {
								while(true)//�ȴ��߳�
								{
									
									try {
										String xml=reader.readUTF();//��ȡ
										System.out.println(xml);
									} catch (Exception e) {
										e.printStackTrace();
									}
									 
								}
							};
						}.start();
						
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();
		
		
		
	}
	
	public void connect(View view) {
		new Thread() {
			public void run() {
				// ip:�����ַ 192.168.22.57
				// port:�������ı�� 5222
				try {
					Socket client = new Socket("192.168.22.57", 5222);

					DataOutputStream writer = null;
					DataInputStream reader = null;
					try {
						if (writer == null) {
							writer = new DataOutputStream(client.getOutputStream());
							reader = new DataInputStream(client.getInputStream());
						}
						writer.writeUTF("���ǿͻ���");
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();
	}
}
