package com.bps.dal.object.bps;
/**
 * 话务报表
 * @author Tom
 *
 */
public class TelephoneReport implements Cloneable{
	private String beginTime;	//开始时间
	
	private String endTime;	//结束时间
	
	private String center; //所属中心
	
	private String group;	//所属组别
	
	private String userName; //坐席工号
	
	private String userRealName; //坐席姓名
	
	private String ywType;	//业务类型
	
	private Long whDateNum; //外呼数据量
	
	private Long acceptDateNum; //成功受理数据量
	
	private Long totalCalls; //总接通次数
	
	private Double totalMissCallsTime; //未接通呼叫总时长
	
	private Double totalCallsTime; //接通通话总时长
	
	private Double totalSuccessCallsTime; //成功数据通话总时长

	public TelephoneReport(){
		super();
	}
	
	public TelephoneReport(BpsUserInfo user){
		this.userName = user.getUserName();
		this.userRealName = user.getUserRealName();
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public Long getWhDateNum() {
		return whDateNum;
	}

	public void setWhDateNum(Long whDateNum) {
		this.whDateNum = whDateNum;
	}

	public Long getAcceptDateNum() {
		return acceptDateNum;
	}

	public void setAcceptDateNum(Long acceptDateNum) {
		this.acceptDateNum = acceptDateNum;
	}

	public Long getTotalCalls() {
		return totalCalls;
	}

	public void setTotalCalls(Long totalCalls) {
		this.totalCalls = totalCalls;
	}

	public Double getTotalMissCallsTime() {
		return totalMissCallsTime;
	}

	public void setTotalMissCallsTime(Double totalMissCallsTime) {
		this.totalMissCallsTime = totalMissCallsTime;
	}

	public Double getTotalCallsTime() {
		return totalCallsTime;
	}

	public void setTotalCallsTime(Double totalCallsTime) {
		this.totalCallsTime = totalCallsTime;
	}

	public Double getTotalSuccessCallsTime() {
		return totalSuccessCallsTime;
	}

	public void setTotalSuccessCallsTime(Double totalSuccessCallsTime) {
		this.totalSuccessCallsTime = totalSuccessCallsTime;
	}

	
	@Override  
    public Object clone() {  
		TelephoneReport rep = null;  
        try{  
        	rep = (TelephoneReport)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }

	@Override
	public String toString() {
		return beginTime + "," + endTime
				+ "," + center + "," + group
				+ "," + userName + "," + userRealName + "," + ywType
				+ "," + whDateNum + "," + acceptDateNum
				+ ","+ totalCalls + "," + totalMissCallsTime + "," + totalCallsTime
				+ "," + totalSuccessCallsTime;
	}  
}
