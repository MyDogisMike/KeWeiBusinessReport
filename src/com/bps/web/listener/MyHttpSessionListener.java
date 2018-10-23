package com.bps.web.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.bps.util.CommonUtil;

public class MyHttpSessionListener implements HttpSessionListener {
	private static final Logger logger = Logger
			.getLogger(MyHttpSessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		//SystemUtil.addTotalUserCount();
		logger.debug("session创建："
				+ CommonUtil.getNowTimeString("yyyy/MM/dd HH:mm:ss"));
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		//SystemUtil.removeTotalUserCount();
		logger.debug("session销毁:"
				+ CommonUtil.getNowTimeString("yyyy/MM/dd HH:mm:ss"));
	}
}
