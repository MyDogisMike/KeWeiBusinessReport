package com.bps.service.bps;

import javax.servlet.http.HttpServletResponse;

import com.bps.dal.object.PageResult;
import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.NewNumberMonitor;

public interface NewNumberMonitorService {
	public PageResult<NewNumberMonitor> getNewNumberMonitor(QueryParams params);
	
	public void exportNewNumberMonitorExcel(QueryParams params, HttpServletResponse response);
}
