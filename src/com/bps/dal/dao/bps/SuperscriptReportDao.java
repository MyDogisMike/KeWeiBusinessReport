package com.bps.dal.dao.bps;

public interface SuperscriptReportDao {
	
	public Long getBidMainBusi(String sqlId, Object params);
	
	public Long getBidCrossData(String sqlId, Object params);
	
	public Long getDgjDataNum(String sqlId, Object params);
	
	public Long getGqDataNum(String sqlId, Object params);
	
	public Long getWwhDataNum(String sqlId, Object params);
	
	public Long getBidFData(String sqlId, Object params);
}
