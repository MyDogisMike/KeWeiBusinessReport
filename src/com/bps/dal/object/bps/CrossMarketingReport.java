package com.bps.dal.object.bps;
/**
 * 交叉营销成效报表
 * @author Tom
 *
 */
public class CrossMarketingReport implements Cloneable{
	private String beginTime;	//开始时间
	private String endTime;	//结束时间
	private String center; //所属中心
	private String group;	//所属组别
	private String userName; //坐席工号
	private String userRealName; //坐席姓名
	private String ywType;	//业务类型
	private Long whDateNum; //外呼数据量
	private Double whDistributeMoney;	//外呼数据派发金额
	private Long crossEPPAmount;	//交叉EPP批核量
	private Long crossBillAmount;	//交叉账单批核量
	private Long crossEPPCAmount;	//交叉EPPC批核量
	private Long crossBigEPPCAmount;	//交叉大额EPPC批核量
	private Double crossEPPMoney;	//交叉EPP批核金额
	private Double crossBillMoney;	//交叉账单批核金额
	private Double crossEPPCMoney;	//交叉EPPC批核金额
	private Double crossBigEPPCMoney;	//交叉大额EPPC批核金额
	private Long crossEPPBinding;	//交叉EPP自动绑定量
	private Long crossMGLWill;	//交叉MGL意愿量
	private Long crossInsuranceWill;	//交叉保险意愿量
	private Long crossBillBinding;	//交叉自动绑定账单分期量
	private Double crossEPPMoney3;	//交叉EPP批核金额（3期）
	private Double crossEPPMoney6;	//交叉EPP批核金额（6期）
	private Double crossEPPMoney12;	//交叉EPP批核金额（12期）
	private Double crossEPPMoney18;	//交叉EPP批核金额（18期）
	private Double crossEPPMoney24;	//交叉EPP批核金额（24期）
	private Double crossEPPMoney36;	//交叉EPP批核金额（36期）
	private Double crossBillMoney3;	//交叉账单批核金额（3期）
	private Double crossBillMoney6;
	private Double crossBillMoney12;
	private Double crossBillMoney18;
	private Double crossBillMoney24;
	private Double crossBillMoney36;
	private Double crossEPPCMoney3;	//交叉EPPC批核金额（3期）
	private Double crossEPPCMoney6;
	private Double crossEPPCMoney12;
	private Double crossEPPCMoney18;
	private Double crossEPPCMoney24;
	private Double crossEPPCMoney36;
	private Double crossBigEPPCMoney3;	//交叉大额EPPC批核金额（3期）
	private Double crossBigEPPCMoney6;
	private Double crossBigEPPCMoney12;
	private Double crossBigEPPCMoney18;
	private Double crossBigEPPCMoney24;
	private Double crossBigEPPCMoney36;
	
	public CrossMarketingReport(){
		super();
	}
	public CrossMarketingReport(BpsUserInfo user){
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



	public Long getCrossEPPAmount() {
		return crossEPPAmount;
	}



	public void setCrossEPPAmount(Long crossEPPAmount) {
		this.crossEPPAmount = crossEPPAmount;
	}



	public Long getCrossBillAmount() {
		return crossBillAmount;
	}



	public void setCrossBillAmount(Long crossBillAmount) {
		this.crossBillAmount = crossBillAmount;
	}



	public Long getCrossEPPCAmount() {
		return crossEPPCAmount;
	}



	public void setCrossEPPCAmount(Long crossEPPCAmount) {
		this.crossEPPCAmount = crossEPPCAmount;
	}



	public Long getCrossBigEPPCAmount() {
		return crossBigEPPCAmount;
	}



	public void setCrossBigEPPCAmount(Long crossBigEPPCAmount) {
		this.crossBigEPPCAmount = crossBigEPPCAmount;
	}



	public Double getCrossEPPMoney() {
		return crossEPPMoney;
	}



	public void setCrossEPPMoney(Double crossEPPMoney) {
		this.crossEPPMoney = crossEPPMoney;
	}



	public Double getCrossBillMoney() {
		return crossBillMoney;
	}



	public void setCrossBillMoney(Double crossBillMoney) {
		this.crossBillMoney = crossBillMoney;
	}



	public Double getCrossEPPCMoney() {
		return crossEPPCMoney;
	}



	public void setCrossEPPCMoney(Double crossEPPCMoney) {
		this.crossEPPCMoney = crossEPPCMoney;
	}



	public Double getCrossBigEPPCMoney() {
		return crossBigEPPCMoney;
	}



	public void setCrossBigEPPCMoney(Double crossBigEPPCMoney) {
		this.crossBigEPPCMoney = crossBigEPPCMoney;
	}



	public Long getCrossEPPBinding() {
		return crossEPPBinding;
	}



	public void setCrossEPPBinding(Long crossEPPBinding) {
		this.crossEPPBinding = crossEPPBinding;
	}



	public Long getCrossMGLWill() {
		return crossMGLWill;
	}



	public void setCrossMGLWill(Long crossMGLWill) {
		this.crossMGLWill = crossMGLWill;
	}



	public Long getCrossInsuranceWill() {
		return crossInsuranceWill;
	}



	public void setCrossInsuranceWill(Long crossInsuranceWill) {
		this.crossInsuranceWill = crossInsuranceWill;
	}



	public Long getCrossBillBinding() {
		return crossBillBinding;
	}



	public void setCrossBillBinding(Long crossBillBinding) {
		this.crossBillBinding = crossBillBinding;
	}



	public Double getCrossEPPMoney3() {
		return crossEPPMoney3;
	}



	public void setCrossEPPMoney3(Double crossEPPMoney3) {
		this.crossEPPMoney3 = crossEPPMoney3;
	}



	public Double getCrossEPPMoney6() {
		return crossEPPMoney6;
	}



	public void setCrossEPPMoney6(Double crossEPPMoney6) {
		this.crossEPPMoney6 = crossEPPMoney6;
	}



	public Double getCrossEPPMoney12() {
		return crossEPPMoney12;
	}



	public void setCrossEPPMoney12(Double crossEPPMoney12) {
		this.crossEPPMoney12 = crossEPPMoney12;
	}



	public Double getCrossEPPMoney18() {
		return crossEPPMoney18;
	}



	public void setCrossEPPMoney18(Double crossEPPMoney18) {
		this.crossEPPMoney18 = crossEPPMoney18;
	}



	public Double getCrossEPPMoney24() {
		return crossEPPMoney24;
	}



	public void setCrossEPPMoney24(Double crossEPPMoney24) {
		this.crossEPPMoney24 = crossEPPMoney24;
	}



	public Double getCrossEPPMoney36() {
		return crossEPPMoney36;
	}



	public void setCrossEPPMoney36(Double crossEPPMoney36) {
		this.crossEPPMoney36 = crossEPPMoney36;
	}



	public Double getCrossBillMoney3() {
		return crossBillMoney3;
	}



	public void setCrossBillMoney3(Double crossBillMoney3) {
		this.crossBillMoney3 = crossBillMoney3;
	}



	public Double getCrossBillMoney6() {
		return crossBillMoney6;
	}



	public void setCrossBillMoney6(Double crossBillMoney6) {
		this.crossBillMoney6 = crossBillMoney6;
	}



	public Double getCrossBillMoney12() {
		return crossBillMoney12;
	}



	public void setCrossBillMoney12(Double crossBillMoney12) {
		this.crossBillMoney12 = crossBillMoney12;
	}



	public Double getCrossBillMoney18() {
		return crossBillMoney18;
	}



	public void setCrossBillMoney18(Double crossBillMoney18) {
		this.crossBillMoney18 = crossBillMoney18;
	}



	public Double getCrossBillMoney24() {
		return crossBillMoney24;
	}



	public void setCrossBillMoney24(Double crossBillMoney24) {
		this.crossBillMoney24 = crossBillMoney24;
	}



	public Double getCrossBillMoney36() {
		return crossBillMoney36;
	}



	public void setCrossBillMoney36(Double crossBillMoney36) {
		this.crossBillMoney36 = crossBillMoney36;
	}



	public Double getCrossEPPCMoney3() {
		return crossEPPCMoney3;
	}



	public void setCrossEPPCMoney3(Double crossEPPCMoney3) {
		this.crossEPPCMoney3 = crossEPPCMoney3;
	}



	public Double getCrossEPPCMoney6() {
		return crossEPPCMoney6;
	}



	public void setCrossEPPCMoney6(Double crossEPPCMoney6) {
		this.crossEPPCMoney6 = crossEPPCMoney6;
	}



	public Double getCrossEPPCMoney12() {
		return crossEPPCMoney12;
	}



	public void setCrossEPPCMoney12(Double crossEPPCMoney12) {
		this.crossEPPCMoney12 = crossEPPCMoney12;
	}



	public Double getCrossEPPCMoney18() {
		return crossEPPCMoney18;
	}



	public void setCrossEPPCMoney18(Double crossEPPCMoney18) {
		this.crossEPPCMoney18 = crossEPPCMoney18;
	}



	public Double getCrossEPPCMoney24() {
		return crossEPPCMoney24;
	}



	public void setCrossEPPCMoney24(Double crossEPPCMoney24) {
		this.crossEPPCMoney24 = crossEPPCMoney24;
	}



	public Double getCrossEPPCMoney36() {
		return crossEPPCMoney36;
	}



	public void setCrossEPPCMoney36(Double crossEPPCMoney36) {
		this.crossEPPCMoney36 = crossEPPCMoney36;
	}



	public Double getCrossBigEPPCMoney3() {
		return crossBigEPPCMoney3;
	}



	public void setCrossBigEPPCMoney3(Double crossBigEPPCMoney3) {
		this.crossBigEPPCMoney3 = crossBigEPPCMoney3;
	}



	public Double getCrossBigEPPCMoney6() {
		return crossBigEPPCMoney6;
	}



	public void setCrossBigEPPCMoney6(Double crossBigEPPCMoney6) {
		this.crossBigEPPCMoney6 = crossBigEPPCMoney6;
	}



	public Double getCrossBigEPPCMoney12() {
		return crossBigEPPCMoney12;
	}



	public void setCrossBigEPPCMoney12(Double crossBigEPPCMoney12) {
		this.crossBigEPPCMoney12 = crossBigEPPCMoney12;
	}



	public Double getCrossBigEPPCMoney18() {
		return crossBigEPPCMoney18;
	}



	public void setCrossBigEPPCMoney18(Double crossBigEPPCMoney18) {
		this.crossBigEPPCMoney18 = crossBigEPPCMoney18;
	}



	public Double getCrossBigEPPCMoney24() {
		return crossBigEPPCMoney24;
	}



	public void setCrossBigEPPCMoney24(Double crossBigEPPCMoney24) {
		this.crossBigEPPCMoney24 = crossBigEPPCMoney24;
	}



	public Double getCrossBigEPPCMoney36() {
		return crossBigEPPCMoney36;
	}



	public void setCrossBigEPPCMoney36(Double crossBigEPPCMoney36) {
		this.crossBigEPPCMoney36 = crossBigEPPCMoney36;
	}



	@Override  
    public Object clone() {  
		CrossMarketingReport rep = null;  
        try{  
        	rep = (CrossMarketingReport)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }



	@Override
	public String toString() {
		return beginTime+","+endTime+","+center+","+group+","+
		userName+","+userRealName+","+ywType+","+whDateNum+","+
		whDistributeMoney+","+crossEPPAmount+","+crossBillAmount+","+crossEPPCAmount+","+
		crossBigEPPCAmount+","+crossEPPMoney+","+crossBillMoney+","+crossEPPCMoney+","+
		crossBigEPPCMoney+","+crossEPPBinding+","+crossMGLWill+","+crossInsuranceWill+","+
		crossBillBinding+","+crossEPPMoney3+","+crossEPPMoney6+","+crossEPPMoney12+","+
		crossEPPMoney18+","+crossEPPMoney24+","+crossEPPMoney36+","+crossBillMoney3+","+
		crossBillMoney6+","+crossBillMoney12+","+crossBillMoney18+","+crossBillMoney24+","+
		crossBillMoney36+","+crossEPPCMoney3+","+crossEPPCMoney6+","+crossEPPCMoney12+","+
		crossEPPCMoney18+","+crossEPPCMoney24+","+crossEPPCMoney36+","+crossBigEPPCMoney3+","+
		crossBigEPPCMoney6+","+crossBigEPPCMoney12+","+crossBigEPPCMoney18+","+crossBigEPPCMoney24+","+crossBigEPPCMoney36;
	}

}
