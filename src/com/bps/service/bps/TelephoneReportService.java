package com.bps.service.bps;

import java.util.List;
import java.util.Map;

import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.dal.object.bps.TelephoneReport;
import com.bps.exception.BaseException;

public interface TelephoneReportService{
	public boolean createReport(String beginTime, String endTime, String saveUrl, String reportName) throws BaseException;
	
	public List<TelephoneReport> getUserTelephoneReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag);
	
	public boolean createDefinedReport(DefinedReportInfo reportInfo, String saveUrl);
}
