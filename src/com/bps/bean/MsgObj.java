package com.bps.bean;

public class MsgObj {
	private boolean success = true;
	private String msg;
	private Object data;

	public MsgObj(String msg) {
		this.msg = msg;
	}

	public MsgObj(Object data) {
		this.data = data;
	}

	public MsgObj(String msg, boolean success) {
		this.msg = msg;
		this.success = success;
	}

	public MsgObj(Object data, boolean success) {
		this.data = data;
		this.success = success;
	}

	public MsgObj(String msg, Object data) {
		this.msg = msg;
		this.data = data;
	}

	public MsgObj(String msg, Object data, boolean success) {
		this.msg = msg;
		this.data = data;
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
