package com.bps.dal.object.bps;

public class DefinedReportInfo {
	
	private String id;
	private String baobiaoName;
	private String yxyData;
	private String startTime;
	private String endTime;
	private String zhongxin;
	private String xiaozu;
	private String assignName;
	private String state;
	private String zhixingTime;
	private String filezuName;
	private String filezuRealname;
	private String fileyxyName;
	private String fileyxyRealname;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAssignName() {
		return assignName;
	}
	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}
	public String getBaobiaoName() {
		return baobiaoName;
	}
	public void setBaobiaoName(String baobiaoName) {
		this.baobiaoName = baobiaoName;
	}
	public String getYxyData() {
		return yxyData;
	}
	public void setYxyData(String yxyData) {
		this.yxyData = yxyData;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getZhongxin() {
		return zhongxin;
	}
	public void setZhongxin(String zhongxin) {
		this.zhongxin = zhongxin;
	}
	public String getXiaozu() {
		return xiaozu;
	}
	public void setXiaozu(String xiaozu) {
		this.xiaozu = xiaozu;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZhixingTime() {
		return zhixingTime;
	}
	public void setZhixingTime(String zhixingTime) {
		this.zhixingTime = zhixingTime;
	}
	public String getFilezuName() {
		return filezuName;
	}
	public void setFilezuName(String filezuName) {
		this.filezuName = filezuName;
	}
	public String getFilezuRealname() {
		return filezuRealname;
	}
	public void setFilezuRealname(String filezuRealname) {
		this.filezuRealname = filezuRealname;
	}
	public String getFileyxyName() {
		return fileyxyName;
	}
	public void setFileyxyName(String fileyxyName) {
		this.fileyxyName = fileyxyName;
	}
	public String getFileyxyRealname() {
		return fileyxyRealname;
	}
	public void setFileyxyRealname(String fileyxyRealname) {
		this.fileyxyRealname = fileyxyRealname;
	}
	@Override
	public String toString() {
		return "DefinedReportInfo [assignName=" + assignName + ", baobiaoName="
				+ baobiaoName + ", endTime=" + endTime + ", fileyxyName="
				+ fileyxyName + ", fileyxyRealname=" + fileyxyRealname
				+ ", filezuName=" + filezuName + ", filezuRealname="
				+ filezuRealname + ", id=" + id + ", startTime=" + startTime
				+ ", state=" + state + ", xiaozu=" + xiaozu + ", yxyData="
				+ yxyData + ", zhixingTime=" + zhixingTime + ", zhongxin="
				+ zhongxin + "]";
	}
	
	

}
