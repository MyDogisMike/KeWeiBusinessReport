package com.bps.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.bps.util.CommonUtil;
import com.bps.util.Config;

public class MyServletContextListener implements ServletContextListener {
	private static final Logger logger = Logger
			.getLogger(MyServletContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.debug("系统关闭"
				+ CommonUtil.getNowTimeString("yyyy/MM/dd HH:mm:ss"));

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext servlet_context = arg0.getServletContext();
		String base = servlet_context.getContextPath();
		servlet_context.setAttribute("base", base);
		String staticServer = Config.getString("resource.path");
		servlet_context.setAttribute("staticServer", staticServer);

		String resourceAdminPath = Config.getString("resource.admin.path");
		servlet_context.setAttribute("resourceAdminPath", resourceAdminPath);

		String sysDebug = Config.getString("sys.debug");
		servlet_context.setAttribute("sysDebug", sysDebug);


		
		logger.debug("系统启动"
				+ CommonUtil.getNowTimeString("yyyy/MM/dd HH:mm:ss"));
	}
}
