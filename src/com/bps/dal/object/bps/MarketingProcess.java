package com.bps.dal.object.bps;

public class MarketingProcess implements Cloneable {
	private String center; //所属中心
	private String group;	//所属组别
	private String userName; //坐席工号
	private String userRealName; //坐席姓名
	private String ywType;	//业务类型
	
	private Long whDataNum;	//外呼数据量
	private Long successAcceptNum;	//成功受理量
	private Double successApproveMoney;	//成功批核金额
	private Long communicateTotleTime;	//接通通话总时长
	
	public MarketingProcess(){
		super();
	}
	
	public MarketingProcess(BpsUserInfo user){
		this.userName = user.getUserName();
		this.userRealName = user.getUserRealName();
	}
	
	public String getCenter() {
		return center;
	}



	public void setCenter(String center) {
		this.center = center;
	}



	public String getGroup() {
		return group;
	}



	public void setGroup(String group) {
		this.group = group;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserRealName() {
		return userRealName;
	}



	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}



	public String getYwType() {
		return ywType;
	}



	public void setYwType(String ywType) {
		this.ywType = ywType;
	}



	public Long getWhDataNum() {
		return whDataNum;
	}



	public void setWhDataNum(Long whDataNum) {
		this.whDataNum = whDataNum;
	}



	public Long getSuccessAcceptNum() {
		return successAcceptNum;
	}



	public void setSuccessAcceptNum(Long successAcceptNum) {
		this.successAcceptNum = successAcceptNum;
	}



	public Double getSuccessApproveMoney() {
		return successApproveMoney;
	}



	public void setSuccessApproveMoney(Double successApproveMoney) {
		this.successApproveMoney = successApproveMoney;
	}


	public Long getCommunicateTotleTime() {
		return communicateTotleTime;
	}

	public void setCommunicateTotleTime(Long communicateTotleTime) {
		this.communicateTotleTime = communicateTotleTime;
	}

	@Override  
    public Object clone() {  
		MarketingProcess rep = null;  
        try{  
        	rep = (MarketingProcess)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }
}
