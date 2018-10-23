package com.bps.dal.object.bps;
/**
 * 新数据派发及成效报表
 * @author Tom
 *
 */
public class NewDataDistributeReport implements Cloneable{
	private String beginTime;	//开始时间
	private String endTime;	//结束时间
	private String center; //所属中心
	private String group;	//所属组别
	private String userName; //坐席工号
	private String userRealName; //坐席姓名
	private String ywType;	//业务类型
	private String dataType;	//数据类别
	private Long distributeAmount;	//新数派发量
	private Double distributeMoney;	//新数派发金额
	private Long whNum;	//新数外呼量
	private Long connectNum;	//新数接通量
	private Long successAcceptAmount;	//新数成功受理量
	private Double successAcceptMoney;	//新数成功受理金额
	private int successApproveAmount;	//新数成功批核量
	private Double successApproveMoney;	//新数成功批核金额
	private Long wrongDataNum;	//错误数据量（M标）
	
	public NewDataDistributeReport(){
		super();
	}
	public NewDataDistributeReport(BpsUserInfo user){
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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getDistributeAmount() {
		return distributeAmount;
	}

	public void setDistributeAmount(Long distributeAmount) {
		this.distributeAmount = distributeAmount;
	}

	public Double getDistributeMoney() {
		return distributeMoney;
	}

	public void setDistributeMoney(Double distributeMoney) {
		this.distributeMoney = distributeMoney;
	}

	public Long getWhNum() {
		return whNum;
	}

	public void setWhNum(Long whNum) {
		this.whNum = whNum;
	}

	public Long getConnectNum() {
		return connectNum;
	}

	public void setConnectNum(Long connectNum) {
		this.connectNum = connectNum;
	}

	public Long getSuccessAcceptAmount() {
		return successAcceptAmount;
	}

	public void setSuccessAcceptAmount(Long successAcceptAmount) {
		this.successAcceptAmount = successAcceptAmount;
	}

	public Double getSuccessAcceptMoney() {
		return successAcceptMoney;
	}

	public void setSuccessAcceptMoney(Double successAcceptMoney) {
		this.successAcceptMoney = successAcceptMoney;
	}

	public int getSuccessApproveAmount() {
		return successApproveAmount;
	}

	public void setSuccessApproveAmount(int successApproveAmount) {
		this.successApproveAmount = successApproveAmount;
	}

	public Double getSuccessApproveMoney() {
		return successApproveMoney;
	}

	public void setSuccessApproveMoney(Double successApproveMoney) {
		this.successApproveMoney = successApproveMoney;
	}

	public Long getWrongDataNum() {
		return wrongDataNum;
	}

	public void setWrongDataNum(Long wrongDataNum) {
		this.wrongDataNum = wrongDataNum;
	}

	@Override  
    public Object clone() {  
		NewDataDistributeReport rep = null;  
        try{  
        	rep = (NewDataDistributeReport)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }

	@Override
	public String toString() {
		return beginTime+","+endTime+","+center+","+group+","+
		userName+","+userRealName+","+ywType+","+dataType+","+
		distributeAmount+","+distributeMoney+","+whNum+","+connectNum+","+
		successAcceptAmount+","+successAcceptMoney+","+successApproveAmount+","+successApproveMoney+","+wrongDataNum;
	}
}
