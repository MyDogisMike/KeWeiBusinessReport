package com.bps.service.bps;

import javax.servlet.http.HttpServletResponse;

import com.bps.dal.object.PageResult;
import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.MarketingPerformance;
import com.bps.dal.object.bps.MarketingProcess;

public interface MarketingProcessService {
	public PageResult<MarketingProcess> getMarketingProcess(QueryParams params);
	
	public void exportMarketingProcessExcel(QueryParams params, HttpServletResponse response);
}
