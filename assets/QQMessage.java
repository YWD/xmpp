package com.itheima.im61.bean;

import com.itheima.im61.util.MyTime;

public class QQMessage extends ProtocalObj{
     public String type = QQMessageType.MSG_TYPE_CHAT_P2P;// 类型的数据 chat login
     public  long from = 0;// 发送者 account
     public String fromNick = "";// 昵称
     public int fromAvatar = 1;// 头像
     public  long to = 0; // 接收者 account
     public  String content = ""; // 消息的内容  约不?
     public String sendTime = MyTime.geTime(); // 发送时间

}
