package com.bps.exception;

public abstract class BaseException extends RuntimeException {
	private String detail;// 错误详情
	private Object data;// 向前台传递的消息
	private String layer;// 所处层次

	public BaseException(String msg) {
		super(msg);
	}

	public BaseException(String msg, String detail) {
		super(msg);
		this.detail = detail;
	}

	public BaseException(String msg, Object data) {
		super(msg);
		this.data = data;
	}

	public String getDetail() {
		return this.detail;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
