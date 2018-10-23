package com.bps.service.bps;

import java.util.List;
import java.util.Map;

import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.dal.object.bps.NewDataDistributeReport;

public interface NewDataDistributeReportService {
	public boolean createReport(String beginTime, String endTime, String saveUrl, String reportName);
	
	public List<NewDataDistributeReport> getUserNewDataDistributeReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag);
	
	public boolean createDefinedReport(DefinedReportInfo reportInfo, String saveUrl);
	
}
