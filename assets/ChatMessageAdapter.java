package com.itheima.im61.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.im61.ImApp;
import com.itheima.im61.R;
import com.itheima.im61.bean.QQMessage;

public class ChatMessageAdapter extends ArrayAdapter<QQMessage> {

	ImApp app;

	public ChatMessageAdapter(Context context, List<QQMessage> objects) {
		super(context, 0, objects);

		Activity act = (Activity) context;
		app = (ImApp) act.getApplication();
	}

	// ��������ͼ��
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {

		QQMessage msg = getItem(position);

		if (msg.from == app.getAccount()) // ����
		{
			// ����
			return 0;
		} else {
			return 1;
		}
	}

	// ���� 0
	// ���� 1

	// 5.0
	class ViewHolder {
		ImageView head;
		TextView time;
		TextView content;
	}

	// ��������ͼ ��ʾָ���±������
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ����
		QQMessage msg=getItem(position);
		int type = getItemViewType(position);
		if (type == 0) {
			convertView = setSendMessage(convertView, msg);
			return convertView;
		} else {
			convertView = setReceiveMessage(convertView, msg);
			return convertView;
		}
	}
	
	/**
	 * �Ż���ʾ������Ϣ
	 * @param convertView
	 * @param msg
	 * @return
	 */

	public View setSendMessage(View convertView, QQMessage msg) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView = View.inflate(getContext(), R.layout.item_chat_send, null);
			holder.head=(ImageView) convertView.findViewById(R.id.head);
			holder.time=(TextView) convertView.findViewById(R.id.time);
			holder.content=(TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		}else
		{
			holder=(ViewHolder) convertView.getTag();
			
		}
		holder.time.setText(msg.sendTime);
		holder.content.setText(msg.content);
		return convertView;
	}
	/**
	 * �Ż���ʾ������Ϣ
	 * @param convertView
	 * @param msg
	 * @return
	 */
	
	public View setReceiveMessage(View convertView, QQMessage msg) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView = View.inflate(getContext(), R.layout.item_chat_receive, null);
			holder.head=(ImageView) convertView.findViewById(R.id.head);
			holder.time=(TextView) convertView.findViewById(R.id.time);
			holder.content=(TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		}else
		{
			holder=(ViewHolder) convertView.getTag();
			
		}
		holder.time.setText(msg.sendTime);
		holder.content.setText(msg.content);
		return convertView;
	}

}
