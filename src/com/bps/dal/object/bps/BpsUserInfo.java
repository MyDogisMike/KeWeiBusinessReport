package com.bps.dal.object.bps;

public class BpsUserInfo {
	private String userName; //坐席工号
	
	private String userRealName; //坐席姓名
	
	private String groupId;	//所在小组id
	
	private String centerId;	//所在中心ID

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
	

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
	
	public String getYwType(){
		return "test";
	}

	@Override
	public String toString() {
		return "BpsUserInfo [groupId=" + groupId + ", userName=" + userName
				+ ", userRealName=" + userRealName  + "]";
	}

}
