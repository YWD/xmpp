package com.itheima.im61.adapter;

import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.im61.ImApp;
import com.itheima.im61.R;
import com.itheima.im61.util.MyTime;

public class ChatMessageAdapter extends CursorAdapter {
	ImApp app;
	private Context context;

	public ChatMessageAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context = context;
		Activity activity = (Activity) this.context;
		app = (ImApp) activity.getApplication();
	}

	// 返回行视图种
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {

		Cursor cursor = getCursor();
		cursor.moveToPosition(position);
		String from = cursor.getString(cursor.getColumnIndex("FROM_ID"));
		if ((app.getAccount() + "@itheima.com").equals(from)) // 发送
		{
			// 发送
			return 0;
		} else {
			return 1;
		}
	}

	// 发送 0
	// 接收 1

	// 5.0
	class ViewHolder {
		ImageView head;
		TextView time;
		TextView content;
	}

	// 返回行视图 显示指定下标的数据
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 数据
		
		int type = getItemViewType(position);
		if (type == 0) {
			convertView = setSendMessage(convertView, position);
			return convertView;
		} else {
			convertView = setReceiveMessage(convertView, position);
			return convertView;
		}
	}

	/**
	 * 优化显示发送消息
	 * 
	 * @param convertView
	 * @param msg
	 * @return
	 */

	public View setSendMessage(View convertView, int position) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_chat_send, null);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		
		Cursor cursor=getCursor();
		cursor.moveToPosition(position);//选 中指定行的数据
		String body=cursor.getString(cursor.getColumnIndex("BODY"));
		Long time=cursor.getLong(cursor.getColumnIndex("TIME"));
		holder.time.setText(MyTime.geTime(time));
		holder.content.setText(body);
		return convertView;
	}

	/**
	 * 优化显示接收消息
	 * 
	 * @param convertView
	 * @param msg
	 * @return
	 */

	public View setReceiveMessage(View convertView,int position) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_chat_receive, null);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		
		Cursor cursor=getCursor();
		cursor.moveToPosition(position);//选 中指定行的数据
		String body=cursor.getString(cursor.getColumnIndex("BODY"));
		Long time=cursor.getLong(cursor.getColumnIndex("TIME"));
		holder.time.setText(MyTime.geTime(time));
		holder.content.setText(body);
		return convertView;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub

	}

}
