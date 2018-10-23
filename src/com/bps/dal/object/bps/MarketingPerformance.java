package com.bps.dal.object.bps;

public class MarketingPerformance implements Cloneable {
	private String center; //所属中心
	private String group;	//所属组别
	private String userName; //坐席工号
	private String userRealName; //坐席姓名
	private String ywType;	//业务类型
	
	private Double approveMoney;	//批核收入
	private Long mainAcceptNum;	//主营成功受理量
	private Double mainApproveMoney;	//主营成功批核金额
	private Double approveMoney18;	//18期批核金额
	private Double approveMoney24;	//24期批核金额
	private Double approveMoney36;	//36期批核金额
	private Double crossEPPApproveMoney;	//交叉EPP批核金额
	private Double crossBillApproveMoney;	//交叉账单批核金额
	private Double crossEPPCApproveMoney;	//交叉EPPC批核金额
	private Double crossBigEPPCApproveMoney;	//交叉大额EPPC批核金额
	private Long autoBindEPPNum;	//自动绑定EPP量
	private Long autoBindBillNum;	//自动绑定账单分期量
	private Long communicateTotleTime;	//接通通话总时长
	
	public MarketingPerformance(){
		super();
	}
	
	public MarketingPerformance(BpsUserInfo user){
		this.userName = user.getUserName();
		this.userRealName = user.getUserRealName();
	}
	
	
	
	public Double getApproveMoney() {
		return approveMoney;
	}

	public void setApproveMoney(Double approveMoney) {
		this.approveMoney = approveMoney;
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



	public Long getMainAcceptNum() {
		return mainAcceptNum;
	}



	public void setMainAcceptNum(Long mainAcceptNum) {
		this.mainAcceptNum = mainAcceptNum;
	}



	public Double getMainApproveMoney() {
		return mainApproveMoney;
	}



	public void setMainApproveMoney(Double mainApproveMoney) {
		this.mainApproveMoney = mainApproveMoney;
	}



	public Double getApproveMoney18() {
		return approveMoney18;
	}



	public void setApproveMoney18(Double approveMoney18) {
		this.approveMoney18 = approveMoney18;
	}



	public Double getApproveMoney24() {
		return approveMoney24;
	}



	public void setApproveMoney24(Double approveMoney24) {
		this.approveMoney24 = approveMoney24;
	}



	public Double getApproveMoney36() {
		return approveMoney36;
	}



	public void setApproveMoney36(Double approveMoney36) {
		this.approveMoney36 = approveMoney36;
	}



	public Double getCrossEPPApproveMoney() {
		return crossEPPApproveMoney;
	}



	public void setCrossEPPApproveMoney(Double crossEPPApproveMoney) {
		this.crossEPPApproveMoney = crossEPPApproveMoney;
	}



	public Double getCrossBillApproveMoney() {
		return crossBillApproveMoney;
	}



	public void setCrossBillApproveMoney(Double crossBillApproveMoney) {
		this.crossBillApproveMoney = crossBillApproveMoney;
	}



	public Double getCrossEPPCApproveMoney() {
		return crossEPPCApproveMoney;
	}



	public void setCrossEPPCApproveMoney(Double crossEPPCApproveMoney) {
		this.crossEPPCApproveMoney = crossEPPCApproveMoney;
	}



	public Double getCrossBigEPPCApproveMoney() {
		return crossBigEPPCApproveMoney;
	}



	public void setCrossBigEPPCApproveMoney(Double crossBigEPPCApproveMoney) {
		this.crossBigEPPCApproveMoney = crossBigEPPCApproveMoney;
	}



	public Long getAutoBindEPPNum() {
		return autoBindEPPNum;
	}



	public void setAutoBindEPPNum(Long autoBindEPPNum) {
		this.autoBindEPPNum = autoBindEPPNum;
	}



	public Long getAutoBindBillNum() {
		return autoBindBillNum;
	}



	public void setAutoBindBillNum(Long autoBindBillNum) {
		this.autoBindBillNum = autoBindBillNum;
	}



	public Long getCommunicateTotleTime() {
		return communicateTotleTime;
	}



	public void setCommunicateTotleTime(Long communicateTotleTime) {
		this.communicateTotleTime = communicateTotleTime;
	}



	@Override  
    public Object clone() {  
		MarketingPerformance rep = null;  
        try{  
        	rep = (MarketingPerformance)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }
}
