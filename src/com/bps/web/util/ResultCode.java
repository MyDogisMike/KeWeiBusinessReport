package com.bps.web.util;


public class ResultCode {
	
	
	public final static Object[] SUCCESS_CODE = {200,""};
	
	public final static Object[] UNKNOW_ERROR_CODE = {500,"系统繁忙"};
	
	public final static Object[] AUTH_ERROR_CODE = {401,"认证失败"};
	
	public final static String RESULT_KEY = "result";
	

	
	public void init(){
		this.code = (Integer)SUCCESS_CODE[0];
		this.msg = (String)SUCCESS_CODE[1];
		this.devMsg = "";
	}
	
	public void setParams(int code,String msg,String devMsg){
		this.code = code;
		this.msg = msg;
		this.devMsg = devMsg;
	}
	
	

	int code;
	String msg;
	String devMsg;
	Object data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDevMsg() {
		return devMsg;
	}
	public void setDevMsg(String devMsg) {
		this.devMsg = devMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	
}
