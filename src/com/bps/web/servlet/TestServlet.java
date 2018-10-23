package com.bps.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

public class TestServlet extends HttpServlet {

	
	private ApplicationContext context;
	
	/**
	 * Constructor of the object.
	 */
	public TestServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sType = request.getParameter("type");
		int type = 1;
		if(sType==null || sType.equalsIgnoreCase("")){
			type = 1;
		}else{
			type = Integer.parseInt(sType);
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void init() throws ServletException {
//		context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
//		persistence = (PersistenceIF)context.getBean("persistence");
//		Collection c = persistence.loadAll(Tracks.class);
//		int count  = 0;
			
	}
	
	
	

}
