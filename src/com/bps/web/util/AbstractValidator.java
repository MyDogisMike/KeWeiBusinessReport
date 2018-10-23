package com.bps.web.util;

public abstract class AbstractValidator implements ParamValidate{
	StringBuffer errMsgBuf = new StringBuffer();
	boolean isValidated;
	
	private boolean baseParamCheck = true;
	
	
	
	
	public boolean baseParamIsOk() {
		return baseParamCheck;
	}

	public void setParamFailed(boolean baseParamCheck) {
		this.baseParamCheck = baseParamCheck;
	}

	public String getErrMsg(){
		return errMsgBuf.toString();
	}
	
	public void addErrMsg(String paramName,String paramParamMsg){
		errMsgBuf.append(paramName);
		errMsgBuf.append(" ");
		errMsgBuf.append(paramParamMsg);
		errMsgBuf.append(" ");
	}
	
	public boolean isValid(){
		return this.isValidated;
	}
	
	public void fail(){
		isValidated = false;
	}
	
	public void sucess(){
		isValidated = true;
	}
}
