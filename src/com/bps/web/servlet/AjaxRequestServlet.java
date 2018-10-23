package com.bps.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AjaxRequestServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AjaxRequestServlet() {
		super();
	}


	private ApplicationContext context;
	
	
	public void init() throws ServletException {
		if (context == null) {
			context = WebApplicationContextUtils.getWebApplicationContext(this
					.getServletContext());
		}
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("methodName") == null) {
			return;
		}
		
		
		PrintWriter out = response.getWriter();
		String methodName = request.getParameter("methodName");
		String str = "";
		if (methodName == null) {
			return;
		}
		
		
		String encoding = System.getProperty("file.encoding");
		String tempStr = new String(str.getBytes(encoding),"gbk");
		if (tempStr.trim().equalsIgnoreCase("")) {
			out.print("  ");
		} else {
			out.print(tempStr);
		}
		
		
	}
	
	
	/**
	 * 取得get中的参数值,经过转码
	 * @param request
	 * @param key
	 * @return
	 */
	private String getReqParameter(HttpServletRequest request, String key) {
		try {
			String contact = request.getParameter(key);
			contact = new String(contact.getBytes("iso8859-1"), "utf8");
			return contact;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		int year = 2008-1900;
		int month = 11;
		Date startDate = new Date(year,month,1);
		Date endDate = new Date(year,month+1,1);
		System.out.println(startDate);
	}
	

}
