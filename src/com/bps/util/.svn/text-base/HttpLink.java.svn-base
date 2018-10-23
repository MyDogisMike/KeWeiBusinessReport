package com.bps.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class HttpLink {
	public static String ccagSenderSN="";
	
	 /*
	  * 组装用于发送到核心的http请求提交方式为Post
	  * 参数声明：
	  *	body_url: http请求正文即向后台传递的参数 ,参数名作为map的key参数值作为map的value
	  */
	 public static String[] urlPostFactory(HttpServletRequest request,boolean logMark){
		 String[] returnvalue=new String[2];
		 
		 Map map=request.getParameterMap();  
	     Set keSet=map.entrySet();
	     String url="";
		 int i=0;
		 
		 for(Iterator itr=keSet.iterator();itr.hasNext();){ 
			 Map.Entry me=(Map.Entry)itr.next();  
			 String key = (String) me.getKey();
			 String value=((String[])me.getValue())[0];
			 
			 try {
					value = URLEncoder.encode(value,"UTF-8");
					if(value.indexOf("%26")>=0){
						value=value.replace("%26", "");
					 }
					if(value.indexOf("%27")>=0){
						value=value.replace("%27", "");
					 }
			 } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			 }
			 
			 if(i==0){
				 url+=key+"="+value;
			 }else{
				 url+="&"+key+"="+value;
			 }
			 i++;
	      }
		 returnvalue[0]="1";
		 returnvalue[1]=url;
		 return returnvalue;
	 }
	 
	 
	public static String readContentFromPost(){
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();    
        ServletContext servletContext = webApplicationContext.getServletContext(); 
		String head_url = servletContext.getInitParameter("Head_Url");// 页面通知客户端下载更新话术文件接口的请求头：http://host:port/index.htm
		int linkTime = Integer.parseInt(servletContext.getInitParameter("Link_Time"));//连接主机超时
		int readTime = Integer.parseInt(servletContext.getInitParameter("Read_Time"));// 从主机读取数据超时
		int time_span = Integer.parseInt(servletContext.getInitParameter("Time_Span"));// 页面通知客户端下载更新话术文件接口的time_span
		String returndata="";//获得后台返回最终数据
	    // Post请求的url，与get不同的是不需要带参数
	    URL postUrl=null;
	    HttpURLConnection connection=null;
	    DataOutputStream out=null;
	    BufferedReader reader=null;
		try {
			postUrl = new URL(head_url);
			connection = (HttpURLConnection)postUrl.openConnection();
	    	// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true
	    	connection.setDoOutput(true);
	    	connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);// Post 请求不能使用缓存
			connection.setInstanceFollowRedirects(true);
			// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
			// 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setConnectTimeout(linkTime);//设置连接主机超时（单位：毫秒）
			connection.setReadTimeout(readTime);//设置从主机读取数据超时（单位：毫秒）
			// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
		    // 要注意的是connection.getOutputStream会隐含的进行connect。
			connection.connect();
		    out =new DataOutputStream(connection.getOutputStream());
		    //System.out.println("--->获得输出流结束也是向输出流写入开始时间,向行方发送报文开始："+DateControl.getCurrentDate("yyyy-MM-dd HH:mm:ss:SSS"));
		    // 配置正文即配置向后台传递的参数，内容跟get的URL中'?'后的参数字符串一致
		    //String content="key=j0r53nmbbd78x7m1pqml06u2&type=1&toemail=386160945@qq.com&activatecode="+URLEncoder.encode("讯鸟软件","utf-8");
		    //DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
		    String timestamp=DateUtil.getNowDate("yyyyMMddHHmmsss");
		    String body_url="action=update_voicepatter&time_span="+time_span+"&jsoncallback=jsoncallback&timestamp="+timestamp;
		    out.writeBytes(body_url);
		    //System.out.println("--->向输出流写入结束,请求报文发送结束,行方收到请求："+DateControl.getCurrentDate("yyyy-MM-dd HH:mm:ss:SSS"));
		    out.flush();
		    out.close();// flush and close
		    reader =new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));//如果收到的中文乱码用InputStreamReader方法可以设置编码 如：new InputStreamReader(connection.getInputStream(),"utf-8") 
		    String line= "";//接收后台返回数据
		    while((line= reader.readLine())!=null){
		    	returndata+=line;
	    	}
		    reader.close();
		    //System.out.println("--->读取数据完毕，交易完成,收到行方返回："+DateControl.getCurrentDate("yyyy-MM-dd HH:mm:ss:SSS"));
		    //System.out.println();
		    connection.disconnect();
		} catch (MalformedURLException e) { 
			e.printStackTrace();
			String returnvalue="{\"ec\":\"TS000004\",\"em\":\"不支持的协议\"}";
			return returnvalue;
        } 
		catch (IOException e) {
			e.printStackTrace();
			String returnvalue="{\"ec\":\"TS000004\",\"em\":\"服务未响应\"}";
			return returnvalue;
		}finally{
			try {
				if(out!=null){
					out.flush();
					out.close();
				}
				if(reader!=null) reader.close();
				if(connection!=null) connection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returndata;
	 }
	
}
