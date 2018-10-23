package com.bps.dal.object.bps;
/**
 * 每日上标及跟进库存报表
 * @author Tom
 *
 */
public class SuperscriptReport implements Cloneable{
	private String beginTime;	//开始时间
	private String endTime;	//结束时间
	private String center; //所属中心
	private String group;	//所属组别
	private String userName; //坐席工号
	private String userRealName; //坐席姓名
	private String ywType;	//业务类型
	private Long whDateNum; //外呼数据量
	private Long bidAMainBusi;	//A标（主营）
	private Long bidACrossEPP;	//A标（交叉EPP
	private Long bidACrossBill;	//A标（交叉账单分期）
	private Long bidACrossEPPC;	//A标（交叉EPPC）
	private Long bidACrossBigEPPC;	//A标（交叉大额EPPC）
	private Long bidBMainBusi;	//B标（主营）
	private Long bidBCrossEPP;	//B标（交叉EPP
	private Long bidBCrossBill;	//B标（交叉账单分期）
	private Long bidBCrossEPPC;	//B标（交叉EPPC）
	private Long bidBCrossBigEPPC;	//B标（交叉大额EPPC）
	private Long bidF;	//F标
	private Long bidF00MainBusi;	//F00标（主营）
	private Long bidF00CrossEPP;	//F00标（交叉EPP
	private Long bidF00CrossBill;	//F00标（交叉账单分期）
	private Long bidF00CrossEPPC;	//F00标（交叉EPPC）
	private Long bidF00CrossBigEPPC;	//F00标（交叉大额EPPC）
	private Long bidF01MainBusi;	//F01标（主营）
	private Long bidF01CrossEPP;	//F01标（交叉EPP
	private Long bidF01CrossBill;	//F01标（交叉账单分期）
	private Long bidF01CrossEPPC;	//F01标（交叉EPPC）
	private Long bidF01CrossBigEPPC;	//F01标（交叉大额EPPC）
	private Long bidF02MainBusi;	//F02标（主营）
	private Long bidF02CrossEPP;	//F02标（交叉EPP
	private Long bidF02CrossBill;	//F02标（交叉账单分期）
	private Long bidF02CrossEPPC;	//F02标（交叉EPPC）
	private Long bidF02CrossBigEPPC;	//F02标（交叉大额EPPC）
	private Long bidG01MainBusi;	//G01标（主营）
	private Long bidG01CrossEPP;	//G01标（交叉EPP
	private Long bidG01CrossBill;	//G01标（交叉账单分期）
	private Long bidG01CrossEPPC;	//G01标（交叉EPPC）
	private Long bidG01CrossBigEPPC;	//G01标（交叉大额EPPC）
	private Long bidG02MainBusi;	//G02标（主营）
	private Long bidG02CrossEPP;	//G02标（交叉EPP
	private Long bidG02CrossBill;	//G02标（交叉账单分期）
	private Long bidG02CrossEPPC;	//G02标（交叉EPPC）
	private Long bidG02CrossBigEPPC;	//G02标（交叉大额EPPC）
	private Long bidMMainBusi;	//M标（主营）
	private Long bidMCrossEPP;	//M标（交叉EPP
	private Long bidMCrossBill;	//M标（交叉账单分期）
	private Long bidMCrossEPPC;	//M标（交叉EPPC）
	private Long bidMCrossBigEPPC;	//M标（交叉大额EPPC）
	private Long dgjDataNum;	//待跟进数据量
	private Long gqDataNum;	//未正常结案过期量
	private Long wwhDataNum;	//未外呼数据量
	
	public SuperscriptReport(){
		super();
	}
	
	public SuperscriptReport(BpsUserInfo user){
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
	public Long getBidAMainBusi() {
		return bidAMainBusi;
	}
	public void setBidAMainBusi(Long bidAMainBusi) {
		this.bidAMainBusi = bidAMainBusi;
	}
	public Long getBidACrossEPP() {
		return bidACrossEPP;
	}
	public void setBidACrossEPP(Long bidACrossEPP) {
		this.bidACrossEPP = bidACrossEPP;
	}
	public Long getBidACrossBill() {
		return bidACrossBill;
	}
	public void setBidACrossBill(Long bidACrossBill) {
		this.bidACrossBill = bidACrossBill;
	}
	public Long getBidACrossEPPC() {
		return bidACrossEPPC;
	}
	public void setBidACrossEPPC(Long bidACrossEPPC) {
		this.bidACrossEPPC = bidACrossEPPC;
	}
	public Long getBidACrossBigEPPC() {
		return bidACrossBigEPPC;
	}
	public void setBidACrossBigEPPC(Long bidACrossBigEPPC) {
		this.bidACrossBigEPPC = bidACrossBigEPPC;
	}
	public Long getBidBMainBusi() {
		return bidBMainBusi;
	}
	public void setBidBMainBusi(Long bidBMainBusi) {
		this.bidBMainBusi = bidBMainBusi;
	}
	public Long getBidBCrossEPP() {
		return bidBCrossEPP;
	}
	public void setBidBCrossEPP(Long bidBCrossEPP) {
		this.bidBCrossEPP = bidBCrossEPP;
	}
	public Long getBidBCrossBill() {
		return bidBCrossBill;
	}
	public void setBidBCrossBill(Long bidBCrossBill) {
		this.bidBCrossBill = bidBCrossBill;
	}
	public Long getBidBCrossEPPC() {
		return bidBCrossEPPC;
	}
	public void setBidBCrossEPPC(Long bidBCrossEPPC) {
		this.bidBCrossEPPC = bidBCrossEPPC;
	}
	public Long getBidBCrossBigEPPC() {
		return bidBCrossBigEPPC;
	}
	public void setBidBCrossBigEPPC(Long bidBCrossBigEPPC) {
		this.bidBCrossBigEPPC = bidBCrossBigEPPC;
	}
	public Long getBidF() {
		return bidF;
	}
	public void setBidF(Long bidF) {
		this.bidF = bidF;
	}
	public Long getBidF00MainBusi() {
		return bidF00MainBusi;
	}
	public void setBidF00MainBusi(Long bidF00MainBusi) {
		this.bidF00MainBusi = bidF00MainBusi;
	}
	public Long getBidF00CrossEPP() {
		return bidF00CrossEPP;
	}
	public void setBidF00CrossEPP(Long bidF00CrossEPP) {
		this.bidF00CrossEPP = bidF00CrossEPP;
	}
	public Long getBidF00CrossBill() {
		return bidF00CrossBill;
	}
	public void setBidF00CrossBill(Long bidF00CrossBill) {
		this.bidF00CrossBill = bidF00CrossBill;
	}
	public Long getBidF00CrossEPPC() {
		return bidF00CrossEPPC;
	}
	public void setBidF00CrossEPPC(Long bidF00CrossEPPC) {
		this.bidF00CrossEPPC = bidF00CrossEPPC;
	}
	public Long getBidF00CrossBigEPPC() {
		return bidF00CrossBigEPPC;
	}
	public void setBidF00CrossBigEPPC(Long bidF00CrossBigEPPC) {
		this.bidF00CrossBigEPPC = bidF00CrossBigEPPC;
	}
	public Long getBidF01MainBusi() {
		return bidF01MainBusi;
	}
	public void setBidF01MainBusi(Long bidF01MainBusi) {
		this.bidF01MainBusi = bidF01MainBusi;
	}
	public Long getBidF01CrossEPP() {
		return bidF01CrossEPP;
	}
	public void setBidF01CrossEPP(Long bidF01CrossEPP) {
		this.bidF01CrossEPP = bidF01CrossEPP;
	}
	public Long getBidF01CrossBill() {
		return bidF01CrossBill;
	}
	public void setBidF01CrossBill(Long bidF01CrossBill) {
		this.bidF01CrossBill = bidF01CrossBill;
	}
	public Long getBidF01CrossEPPC() {
		return bidF01CrossEPPC;
	}
	public void setBidF01CrossEPPC(Long bidF01CrossEPPC) {
		this.bidF01CrossEPPC = bidF01CrossEPPC;
	}
	public Long getBidF01CrossBigEPPC() {
		return bidF01CrossBigEPPC;
	}
	public void setBidF01CrossBigEPPC(Long bidF01CrossBigEPPC) {
		this.bidF01CrossBigEPPC = bidF01CrossBigEPPC;
	}
	public Long getBidF02MainBusi() {
		return bidF02MainBusi;
	}
	public void setBidF02MainBusi(Long bidF02MainBusi) {
		this.bidF02MainBusi = bidF02MainBusi;
	}
	public Long getBidF02CrossEPP() {
		return bidF02CrossEPP;
	}
	public void setBidF02CrossEPP(Long bidF02CrossEPP) {
		this.bidF02CrossEPP = bidF02CrossEPP;
	}
	public Long getBidF02CrossBill() {
		return bidF02CrossBill;
	}
	public void setBidF02CrossBill(Long bidF02CrossBill) {
		this.bidF02CrossBill = bidF02CrossBill;
	}
	public Long getBidF02CrossEPPC() {
		return bidF02CrossEPPC;
	}
	public void setBidF02CrossEPPC(Long bidF02CrossEPPC) {
		this.bidF02CrossEPPC = bidF02CrossEPPC;
	}
	public Long getBidF02CrossBigEPPC() {
		return bidF02CrossBigEPPC;
	}
	public void setBidF02CrossBigEPPC(Long bidF02CrossBigEPPC) {
		this.bidF02CrossBigEPPC = bidF02CrossBigEPPC;
	}
	public Long getBidG01MainBusi() {
		return bidG01MainBusi;
	}
	public void setBidG01MainBusi(Long bidG01MainBusi) {
		this.bidG01MainBusi = bidG01MainBusi;
	}
	public Long getBidG01CrossEPP() {
		return bidG01CrossEPP;
	}
	public void setBidG01CrossEPP(Long bidG01CrossEPP) {
		this.bidG01CrossEPP = bidG01CrossEPP;
	}
	public Long getBidG01CrossBill() {
		return bidG01CrossBill;
	}
	public void setBidG01CrossBill(Long bidG01CrossBill) {
		this.bidG01CrossBill = bidG01CrossBill;
	}
	public Long getBidG01CrossEPPC() {
		return bidG01CrossEPPC;
	}
	public void setBidG01CrossEPPC(Long bidG01CrossEPPC) {
		this.bidG01CrossEPPC = bidG01CrossEPPC;
	}
	public Long getBidG01CrossBigEPPC() {
		return bidG01CrossBigEPPC;
	}
	public void setBidG01CrossBigEPPC(Long bidG01CrossBigEPPC) {
		this.bidG01CrossBigEPPC = bidG01CrossBigEPPC;
	}
	public Long getBidG02MainBusi() {
		return bidG02MainBusi;
	}
	public void setBidG02MainBusi(Long bidG02MainBusi) {
		this.bidG02MainBusi = bidG02MainBusi;
	}
	public Long getBidG02CrossEPP() {
		return bidG02CrossEPP;
	}
	public void setBidG02CrossEPP(Long bidG02CrossEPP) {
		this.bidG02CrossEPP = bidG02CrossEPP;
	}
	public Long getBidG02CrossBill() {
		return bidG02CrossBill;
	}
	public void setBidG02CrossBill(Long bidG02CrossBill) {
		this.bidG02CrossBill = bidG02CrossBill;
	}
	public Long getBidG02CrossEPPC() {
		return bidG02CrossEPPC;
	}
	public void setBidG02CrossEPPC(Long bidG02CrossEPPC) {
		this.bidG02CrossEPPC = bidG02CrossEPPC;
	}
	public Long getBidG02CrossBigEPPC() {
		return bidG02CrossBigEPPC;
	}
	public void setBidG02CrossBigEPPC(Long bidG02CrossBigEPPC) {
		this.bidG02CrossBigEPPC = bidG02CrossBigEPPC;
	}
	public Long getBidMMainBusi() {
		return bidMMainBusi;
	}
	public void setBidMMainBusi(Long bidMMainBusi) {
		this.bidMMainBusi = bidMMainBusi;
	}
	public Long getBidMCrossEPP() {
		return bidMCrossEPP;
	}
	public void setBidMCrossEPP(Long bidMCrossEPP) {
		this.bidMCrossEPP = bidMCrossEPP;
	}
	public Long getBidMCrossBill() {
		return bidMCrossBill;
	}
	public void setBidMCrossBill(Long bidMCrossBill) {
		this.bidMCrossBill = bidMCrossBill;
	}
	public Long getBidMCrossEPPC() {
		return bidMCrossEPPC;
	}
	public void setBidMCrossEPPC(Long bidMCrossEPPC) {
		this.bidMCrossEPPC = bidMCrossEPPC;
	}
	public Long getBidMCrossBigEPPC() {
		return bidMCrossBigEPPC;
	}
	public void setBidMCrossBigEPPC(Long bidMCrossBigEPPC) {
		this.bidMCrossBigEPPC = bidMCrossBigEPPC;
	}
	public Long getDgjDataNum() {
		return dgjDataNum;
	}
	public void setDgjDataNum(Long dgjDataNum) {
		this.dgjDataNum = dgjDataNum;
	}
	public Long getGqDataNum() {
		return gqDataNum;
	}
	public void setGqDataNum(Long gqDataNum) {
		this.gqDataNum = gqDataNum;
	}
	public Long getWwhDataNum() {
		return wwhDataNum;
	}
	public void setWwhDataNum(Long wwhDataNum) {
		this.wwhDataNum = wwhDataNum;
	}
	
	@Override  
    public Object clone() {  
		SuperscriptReport rep = null;  
        try{  
        	rep = (SuperscriptReport)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return rep;  
    }
	
	@Override
	public String toString() {
		return beginTime+","+endTime+","+center+","+group+","+
		userName+","+userRealName+","+ywType+","+whDateNum+","+
		bidAMainBusi+","+bidACrossEPP+","+bidACrossBill+","+bidACrossEPPC+","+
		bidACrossBigEPPC+","+bidBMainBusi+","+bidBCrossEPP+","+bidBCrossBill+","+
		bidBCrossEPPC+","+bidBCrossBigEPPC+","+bidF+","+bidF00MainBusi+","+
		bidF00CrossEPP+","+bidF00CrossBill+","+bidF00CrossEPPC+","+bidF00CrossBigEPPC+","+
		bidF01MainBusi+","+bidF01CrossEPP+","+bidF01CrossBill+","+bidF01CrossEPPC+","+
		bidF01CrossBigEPPC+","+bidF02MainBusi+","+bidF02CrossEPP+","+bidF02CrossBill+","+
		bidF02CrossEPPC+","+bidF02CrossBigEPPC+","+bidG01MainBusi+","+bidG01CrossEPP+","+
		bidG01CrossBill+","+bidG01CrossEPPC+","+bidG01CrossBigEPPC+","+bidG02MainBusi+","+
		bidG02CrossEPP+","+bidG02CrossBill+","+bidG02CrossEPPC+","+bidG02CrossBigEPPC+","+
		bidMMainBusi+","+bidMCrossEPP+","+bidMCrossBill+","+bidMCrossEPPC+","+
		bidMCrossBigEPPC+","+dgjDataNum+","+gqDataNum+","+wwhDataNum;
	}
	
}
