package com.amqp;

public class UserMessageLog {

	private String id;
	private int uid;
	private String msgType;
	private String msgText;
	private String msgTime;
	private String handleTime;
	private long msgMillis;
	private long handleMillis;

	public UserMessageLog() {
		super();
	}

	public UserMessageLog(String id, int uid, String msgType, String msgText, String msgTime, String handleTime,
			long msgMillis, long handleMillis) {
		super();
		this.id = id;
		this.uid = uid;
		this.msgType = msgType;
		this.msgText = msgText;
		this.msgTime = msgTime;
		this.handleTime = handleTime;
		this.msgMillis = msgMillis;
		this.handleMillis = handleMillis;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}

	public long getMsgMillis() {
		return msgMillis;
	}

	public void setMsgMillis(long msgMillis) {
		this.msgMillis = msgMillis;
	}

	public long getHandleMillis() {
		return handleMillis;
	}

	public void setHandleMillis(long handleMillis) {
		this.handleMillis = handleMillis;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
}
