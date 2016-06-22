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
import com.itheima.im61.bean.QQContact;

public class ContactAdapter extends ArrayAdapter<QQContact> {

	public ContactAdapter(Context context, List<QQContact> objects) {
		super(context, 0, objects);
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
		QQContact contact = getItem(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(getContext(), R.layout.item_contact, null);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
 
		}

		ImApp app = (ImApp) ((Activity) getContext()).getApplication();
		if (contact.account ==app.getAccount()) {
			// 赋值
			holder.title.setText("[自己]");
		} else {
			holder.title.setText(contact.nick);
		}
		holder.desc.setText(contact.account + "@qq.com");
		return convertView;
	}

}
