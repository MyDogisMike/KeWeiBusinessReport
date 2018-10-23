package com.bps.service.bps;

import java.util.List;
import java.util.Map;

import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.CrossMarketingReport;
import com.bps.dal.object.bps.DefinedReportInfo;

public interface CrossMarketingReportService {
	public boolean createReport(String beginTime, String endTime, String saveUrl, String reportName);
	
	public List<CrossMarketingReport> getUserCrossMarketingReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag);
	
	public boolean createDefinedReport(DefinedReportInfo reportInfo, String saveUrl);
}
