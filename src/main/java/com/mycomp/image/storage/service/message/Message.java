package com.mycomp.image.storage.service.message;

public class Message {
	private String timeStamp;
	private String event;
	private String message;

	public Message(String timeStamp, String event, String msg) {
		super();
		this.timeStamp = timeStamp;
		this.event = event;
		this.message = msg;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getMsg() {
		return message;
	}

	public void setMsg(String msg) {
		this.message = msg;
	}

}
