package com.bps.dal.object.bps;
/**
 * 专项营销成效报表
 * @author Tom
 *
 */
public class SpecialMarketingReport implements Cloneable{
	
	private String beginTime;	//开始时间
	private String endTime;	//结束时间
	private String center; //所属中心
	private String group;	//所属组别
	private String userName; //坐席工号
	private String userRealName; //坐席姓名
	private String ywType;	//业务类型
	private Long whDateNum; //外呼数据量
	private Double whDistributeMoney;	//外呼数据派发金额
	private Long connectNum;	//接通量
	private Long successAcceptAmount;	//成功受理量
	private Double successAcceptMoney;	//成功受理金额
	private Long successApproveAmount;	//成功批核量
	private Double successApproveMoney;	//成功批核金额
	private Long approveAmount3;	//3期批核量
	private Double approveMoney3;	//3期批核金额
	private Long approveAmount6;
	private Double approveMoney6;
	private Long approveAmount12;
	private Double approveMoney12;
	private Long approveAmount18;
	private Double approveMoney18;
	private Long approveAmount24;
	private Double approveMoney24;
	private Long approveAmount36;
	private Double approveMoney36;
	private Double approveIncome;	//批核收入
	
	public SpecialMarketingReport(){
		super();
	}
	public SpecialMarketingReport(BpsUserInfo user){
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


	public Double getWhDistributeMoney() {
		return whDistributeMoney;
	}


	public void setWhDistributeMoney(Double whDistributeMoney) {
		this.whDistributeMoney = whDistributeMoney;
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


	public Long getSuccessApproveAmount() {
		return successApproveAmount;
	}


	public void setSuccessApproveAmount(Long successApproveAmount) {
		this.successApproveAmount = successApproveAmount;
	}


	public Double getSuccessApproveMoney() {
		return successApproveMoney;
	}


	public void setSuccessApproveMoney(Double successApproveMoney) {
		this.successApproveMoney = successApproveMoney;
	}


	public Long getApproveAmount3() {
		return approveAmount3;
	}


	public void setApproveAmount3(Long approveAmount3) {
		this.approveAmount3 = approveAmount3;
	}


	public Double getApproveMoney3() {
		return approveMoney3;
	}


	public void setApproveMoney3(Double approveMoney3) {
		this.approveMoney3 = approveMoney3;
	}


	public Long getApproveAmount6() {
		return approveAmount6;
	}


	public void setApproveAmount6(Long approveAmount6) {
		this.approveAmount6 = approveAmount6;
	}


	public Double getApproveMoney6() {
		return approveMoney6;
	}


	public void setApproveMoney6(Double approveMoney6) {
		this.approveMoney6 = approveMoney6;
	}


	public Long getApproveAmount12() {
		return approveAmount12;
	}


	public void setApproveAmount12(Long approveAmount12) {
		this.approveAmount12 = approveAmount12;
	}


	public Double getApproveMoney12() {
		return approveMoney12;
	}


	public void setApproveMoney12(Double approveMoney12) {
		this.approveMoney12 = approveMoney12;
	}


	public Long getApproveAmount18() {
		return approveAmount18;
	}


	public void setApproveAmount18(Long approveAmount18) {
		this.approveAmount18 = approveAmount18;
	}


	public Double getApproveMoney18() {
		return approveMoney18;
	}


	public void setApproveMoney18(Double approveMoney18) {
		this.approveMoney18 = approveMoney18;
	}


	public Long getApproveAmount24() {
		return approveAmount24;
	}


	public void setApproveAmount24(Long approveAmount24) {
		this.approveAmount24 = approveAmount24;
	}


	public Double getApproveMoney24() {
		return approveMoney24;
	}


	public void setApproveMoney24(Double approveMoney24) {
		this.approveMoney24 = approveMoney24;
	}


	public Long getApproveAmount36() {
		return approveAmount36;
	}


	public void setApproveAmount36(Long approveAmount36) {
		this.approveAmount36 = approveAmount36;
	}


	public Double getApproveMoney36() {
		return approveMoney36;
	}


	public void setApproveMoney36(Double approveMoney36) {
		this.approveMoney36 = approveMoney36;
	}


	public Double getApproveIncome() {
		return approveIncome;
	}


	public void setApproveIncome(Double approveIncome) {
		this.approveIncome = approveIncome;
	}


	@Override  
    public Object clone() {  
		SpecialMarketingReport rep = null;  
        try{  
        	rep = (SpecialMarketingReport)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }


	@Override
	public String toString() {
		return beginTime+","+endTime+","+center+","+group+","+
		userName+","+userRealName+","+ywType+","+whDateNum+","+
		whDistributeMoney+","+connectNum+","+successAcceptAmount+","+successAcceptMoney+","+
		successApproveAmount+","+successApproveMoney+","+approveAmount3+","+approveMoney3+","+
		approveAmount6+","+approveMoney6+","+approveAmount12+","+approveMoney12+","+
		approveAmount18+","+approveMoney18+","+approveAmount24+","+approveMoney24+","+
		approveAmount36+","+approveMoney36+","+approveIncome;
	}
}
