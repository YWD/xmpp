package com.itheima.im61.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.im61.R;

public class ContactAdapter extends CursorAdapter {

	private Context context=null;

	public ContactAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context=context;
	}

	// 5.0
	class ViewHolder {
		ImageView head;
		TextView title;
		TextView desc;
	}

	// 返回行视图 显示指定下标的数据
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 数据
		
		Cursor cursor=getCursor();
		cursor.moveToPosition(position);//选 中数据
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(this.context, R.layout.item_contact, null);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		String nick=cursor.getString(cursor.getColumnIndex("NICK"));
		String account=cursor.getString(cursor.getColumnIndex("ACCOUNT"));
		holder.title.setText(nick);
		holder.desc.setText(account);
		return convertView;
	}

	
	//layout-->view if (convertView == null) {
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return null;
	}

	//setValue
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
	}

}
