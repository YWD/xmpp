package com.itheima.im61.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import com.itheima.im61.ImApp;
import com.itheima.im61.R;
import com.itheima.im61.dao.Contact;
import com.itheima.im61.dao.ContactDao;
import com.itheima.im61.dao.Sms;
import com.itheima.im61.dao.SmsDao;
import com.itheima.im61.util.NickUtil;
import com.itheima.im61.util.ThreadUtils;

/**
 * ͬ������ͨ�����泤������Ӽ������������շ���˵�����
 * 
 * @author itheima
 * 
 */
public class CoreService extends Service {

	// ---------------

	private ImApp app = null;
	private Roster roster = null;

	// 1.C���U������Dɾ��
	private RosterListener rosterListener = new RosterListener() {

		@Override
		public void presenceChanged(Presence presence) {

		}

		//
		@Override
		public void entriesAdded(Collection<String> addresses) {

			for (String account : addresses) {
				// ��ȡ�����õ���
				RosterEntry person = roster.getEntry(account);// ȡ���޸ĺ����ϵ������
				insertOrUpdate(person);
			}

		}

		@Override
		public void entriesUpdated(Collection<String> addresses) {
			for (String account : addresses) {
				// ��ȡ�޸ĺõ���
				RosterEntry person = roster.getEntry(account);// ȡ���޸ĺ����ϵ������
				insertOrUpdate(person);
			}
		}

		// ɾ��
		@Override
		public void entriesDeleted(Collection<String> addresses) {
			for (String account : addresses) {
				String sql = "delete from CONTACT where ACCOUNT=?;";
				app.getSqliteDatabase().execSQL(sql, new String[] { account });
			}
			Uri uri = Uri.parse("content://" + ContactDao.class.getSimpleName());
			getContentResolver().notifyChange(uri, null);
		}

	};

	private MessageListener listener = new MessageListener() {
		@Override
		public void processMessage(Chat chat, final Message msg) {
			ThreadUtils.runUnThread(new Runnable() {

				@Override
				public void run() {
					String from = msg.getFrom();
					String to = msg.getTo();
					msg.setFrom(NickUtil.filterAccount(from));
					msg.setTo(NickUtil.filterAccount(to));
					msg.setSubject(NickUtil.getNick(msg.getFrom(), null));

					Sms sms = new Sms();
					sms.setFrom_id(msg.getFrom());
					sms.setFrom_nick(NickUtil.getNick(msg.getFrom(), null));
					sms.setFrom_avatar(R.drawable.ic_launcher);
					sms.setBody(msg.getBody());
					sms.setType(msg.getType().toString());
					sms.setTime(System.currentTimeMillis());
					// ���ͻ��ǽ���
					sms.setSession_id(msg.getFrom());
					sms.setSession_name(NickUtil.getNick(msg.getFrom(), null));
					// ������Ϣ
					app.getSmsDao().insert(sms);
					Uri uri = Uri.parse("content://" + SmsDao.class.getSimpleName());
					getContentResolver().notifyChange(uri, null);

					System.out.println(msg.toXML());
					Toast.makeText(getBaseContext(), "������Ϣ:" + msg.getBody(), 0).show();

				}
			});

		}
	};

	private HashMap<String, Chat> chats = new HashMap<String, Chat>();

	/**
	 * ʹ�÷���������Ϣ
	 * 
	 * @param msg
	 * @throws XMPPException
	 */
	public void sendMesage(Message msg) throws XMPPException {
		Chat chat = chats.get(msg.getTo());
		if (chat == null) {
			// ���� ������ ����
			chat = app.getConn().getChatManager().createChat(msg.getTo(), null);
			chat.addMessageListener(listener);

			//��Ҫ����ɣ�
		}
		chat.sendMessage(msg);
		Sms sms = new Sms();
		sms.setFrom_id(msg.getFrom());
		sms.setFrom_nick(NickUtil.getNick(msg.getFrom(), null));
		sms.setFrom_avatar(R.drawable.ic_launcher);
		sms.setBody(msg.getBody());
		sms.setType(msg.getType().toString());
		sms.setTime(System.currentTimeMillis());
		// ���ͻ��ǽ���
		sms.setSession_id(msg.getTo());
		sms.setSession_name(NickUtil.getNick(msg.getTo(), null));
		// ������Ϣ
		app.getSmsDao().insert(sms);

		Uri uri = Uri.parse("content://" + SmsDao.class.getSimpleName());
		getContentResolver().notifyChange(uri, null);

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Notification
		Toast.makeText(this, "Im���ķ����", 0).show();
		System.out.println("Im���ķ����");

		app = (ImApp) getApplication();
		app.setCoreServer(this);
		ThreadUtils.runInThread(new Runnable() {

			@Override
			public void run() {

				// ����������Ĵ���
				ChatManager cm = app.getConn().getChatManager();

				cm.addChatListener(new ChatManagerListener() {

					// Chat ����
					// createdLocally true ���ұ�����
					// false ����������
					@Override
					public void chatCreated(Chat chat, boolean createdLocally) {

						if (!createdLocally) {
							String from = chat.getParticipant();
							from = NickUtil.filterAccount(from);
							Chat c = chats.get(from);
							if (c == null) {
								chat.addMessageListener(listener);
								chats.put(from, chat);
							}
						}

					}
				});

				roster = app.getConn().getRoster();
				// ��Ӽ�����:�������޸�����Ҫ��ͬ��=================================================
				roster.addRosterListener(rosterListener);
				//����������ȡ�����б����浽���ݿ�
				Collection<RosterEntry> all = roster.getEntries();
				List<RosterEntry> allPersons = new ArrayList<RosterEntry>(all);
				for (RosterEntry person : allPersons) {
					System.out.println(person.getUser() + "  " + person.getName());

					insertOrUpdate(person);

				}
				// ͬ����ϵ��

				// ͬ�������¼
			}

		});
	}

	/**
	 * �¼�/���ǳ�
	 * 
	 * @param person
	 */
	public void insertOrUpdate(RosterEntry person) {
		// ͬ�� �ж��Ƿ��
		Contact c = new Contact();
		c.setAccount(person.getUser());// @itheima.com
		String nick = NickUtil.getNick(person.getUser(), person.getName());
		c.setNick(nick);
		c.setAvatar(R.drawable.ic_launcher);
		c.setSort(NickUtil.getNickPinYin(nick));

		// select * from CONTACT where
		// ACCOUNT='shadowwalker@itheima.com';
		// update CONTACT set NICK='Ů��2' where
		// ACCOUNT='shadowwalker@itheima.com';
		List<Contact> list = app.getContactDao().queryRaw("where  ACCOUNT=?", c.getAccount());
		if (list.size() == 0) {
			app.getContactDao().insert(c);
		} else {
			String id = list.get(0).getId() + "";
			System.out.println(id);
			Contact dbContact = app.getContactDao().load(list.get(0).getId());
			dbContact.setNick(c.getNick());
			dbContact.setSort(c.getSort());
			dbContact.setAvatar(c.getAvatar());
			app.getContactDao().insertOrReplace(dbContact);// ����id���и���
		}

		Uri uri = Uri.parse("content://" + ContactDao.class.getSimpleName());
		getContentResolver().notifyChange(uri, null);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
