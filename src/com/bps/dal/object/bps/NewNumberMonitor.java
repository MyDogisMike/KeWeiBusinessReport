package com.bps.dal.object.bps;

public class NewNumberMonitor implements Cloneable{
	private String center; //所属中心
	private String group;	//所属组别
	private String userName; //坐席工号
	private String userRealName; //坐席姓名
	private String ywType;	//业务类型
	
	private Long distributeNum;	//新数派发量
	private Double distributeMoney;	//新数派发金额
	private Long outboundNum;	//新数外呼量
	private String percentageComplete;	//新数完成率
	private Long connectNum;	//新数接通量
	private Long successAccept;	//新数成功受理量
	
	public NewNumberMonitor(){
		super();
	}
	
	public NewNumberMonitor(BpsUserInfo user){
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
	public Long getDistributeNum() {
		return distributeNum;
	}
	public void setDistributeNum(Long distributeNum) {
		this.distributeNum = distributeNum;
	}
	public Double getDistributeMoney() {
		return distributeMoney;
	}
	public void setDistributeMoney(Double distributeMoney) {
		this.distributeMoney = distributeMoney;
	}
	public Long getOutboundNum() {
		return outboundNum;
	}
	public void setOutboundNum(Long outboundNum) {
		this.outboundNum = outboundNum;
	}
	public String getPercentageComplete() {
		return percentageComplete;
	}
	public void setPercentageComplete(String percentageComplete) {
		this.percentageComplete = percentageComplete;
	}
	public Long getConnectNum() {
		return connectNum;
	}
	public void setConnectNum(Long connectNum) {
		this.connectNum = connectNum;
	}
	public Long getSuccessAccept() {
		return successAccept;
	}
	public void setSuccessAccept(Long successAccept) {
		this.successAccept = successAccept;
	}
	
	@Override  
    public Object clone() {  
		NewNumberMonitor rep = null;  
        try{  
        	rep = (NewNumberMonitor)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }
	
	@Override
	public String toString() {
		return "NewNumberMonitor [center=" + center + ", connectNum="
				+ connectNum + ", distributeMoney=" + distributeMoney
				+ ", distributeNum=" + distributeNum + ", group=" + group
				+ ", outboundNum=" + outboundNum + ", percentageComplete="
				+ percentageComplete + ", successAccept=" + successAccept
				+ ", userName=" + userName + ", userRealName=" + userRealName
				+ ", ywType=" + ywType + "]";
	}
	
}
