package com.bps.service.bps;

import javax.servlet.http.HttpServletResponse;

import com.bps.dal.object.PageResult;
import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.MarketingPerformance;

public interface MarketingPerformanceService {
	public PageResult<MarketingPerformance> getMarketingPerformance(QueryParams params);
	
	public void exportMarketingPerformanceExcel(QueryParams params, HttpServletResponse response);
}
