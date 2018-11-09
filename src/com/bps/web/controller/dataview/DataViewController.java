package com.bps.web.controller.dataview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.object.PageResult;
import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.MarketingPerformance;
import com.bps.dal.object.bps.MarketingProcess;
import com.bps.dal.object.bps.NewNumberMonitor;
import com.bps.service.bps.MarketingPerformanceService;
import com.bps.service.bps.MarketingProcessService;
import com.bps.service.bps.NewNumberMonitorService;
import com.bps.service.dianying.VoiceFileService;
import com.bps.util.DateUtil;
import com.bps.bean.SelectObj;


@Controller
@RequestMapping("/dataview")
public class DataViewController {
	@Autowired
	private VoiceFileService voiceFileService;
	@Autowired
	private NewNumberMonitorService newNumberMonitorService;
	@Autowired
	private MarketingPerformanceService marketingPerformanceService;
	@Autowired 
	MarketingProcessService marketingProcessService;
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	
	private static final Logger logger = Logger.getLogger("logs");
	
	private final Semaphore semaphore = new Semaphore(50, true);
	 
	 @RequestMapping(value={"checkAction"})
	 public String checkAction(HttpServletRequest request,HttpSession session) throws Exception {
	       String target_par = request.getParameter("target");
	       String roleInfo = request.getParameter("roleInfo");
	       String roleId = request.getParameter("role_id");
	       String target="message";
	       String ipaddress = session.getServletContext().getInitParameter("IpAddress");
	       String[] ipaddresses=ipaddress.split("_");//合法IP数组
	      // String targetip = request.getRemoteAddr();//来源IP
	       String referer = request.getHeader("referer");//请求来源url
	      // System.out.println("获取到的referer:"+referer);
	       request.setAttribute("roleInfo", roleInfo);
	       String roleName = bpsRwHistoryDao.getUserRole("getUserRole", roleId);
	       if("组长".equals(roleName)){
	    	   request.setAttribute("isGroup", "yes");
	       }else{
	    	   request.setAttribute("isGroup", "no");
	       }
	       
	       if(referer!=null){
	    	   int b=referer.indexOf("/ui/");
		       String c=referer.substring(7, b);
		       boolean returnvlue=ArrayUtils.contains(ipaddresses,c);
		       if(returnvlue){
		    	   if(!"".equals(target_par) && target_par!=null){
			    	   target=target_par;
			       }
		       }
	       }
	      else{
	    	  target=target_par;
	      }
	       //System.out.println(target);
		   return target; 
	    }
	 
	 	@ResponseBody
		@RequestMapping(value = {"getNewNumberMonitor"}, method = RequestMethod.POST)
		public PageResult<NewNumberMonitor> getNewNumberMonitor(QueryParams params){
	 		params.setSkipRow(params.getRows()*(params.getPage()-1));
	 		try {
				if(semaphore.tryAcquire(5, TimeUnit.SECONDS)){
//					System.out.println("进来了");
//					System.out.println(params);
//					System.out.println(Thread.currentThread().getName());
					
					PageResult<NewNumberMonitor> result = newNumberMonitorService.getNewNumberMonitor(params);
					semaphore.release();
//					System.out.println("哈哈");
					return result;
				}
			} catch (Exception e) {
				System.out.println("失败");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  查询新数监控报表报表错误===>  "+e.toString());
				return null;
			}
			System.out.println("失败");
	 		return null;
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"getMarketingPerformance"}, method = RequestMethod.POST)
		public PageResult<MarketingPerformance> getMarketingPerformance(QueryParams params){
	 		params.setSkipRow(params.getRows()*(params.getPage()-1));
	 		try {
				if(semaphore.tryAcquire(5, TimeUnit.SECONDS)){
//					System.out.println("进来了");
//					System.out.println(params);
//					System.out.println(Thread.currentThread().getName());
					
					PageResult<MarketingPerformance> result = marketingPerformanceService.getMarketingPerformance(params);
					semaphore.release();
//					System.out.println("哈哈");
					return result;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  查询营销业绩监控报表错误===>  "+e.toString());
				return null;
			}
			System.out.println("失败");
	 		return null;
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"getMarketingProcess"}, method = RequestMethod.POST)
		public PageResult<MarketingProcess> getMarketingProcess(QueryParams params){
	 		params.setSkipRow(params.getRows()*(params.getPage()-1));
	 		try {
				if(semaphore.tryAcquire(5, TimeUnit.SECONDS)){
//					System.out.println("进来了");
//					System.out.println(params);
//					System.out.println(Thread.currentThread().getName());
					
					PageResult<MarketingProcess> result = marketingProcessService.getMarketingProcess(params);
					semaphore.release();
//					System.out.println("哈哈");
					return result;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  查询营销过程监控报表错误===>  "+e.toString());
				return null;
			}
			System.out.println("失败");
	 		return null;
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"isLeisure"}, method = RequestMethod.POST)
		public boolean isLeisure(){
//	 		System.out.println("进来了isLeisure");
//	 		System.out.println(semaphore.availablePermits());
	 		return semaphore.availablePermits()>0;
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"getCenterList"}, method = RequestMethod.POST)
		public List<SelectObj> getCenterList(){
	 		return bpsRwHistoryDao.getAllCenterToSelect("getAllCenterToSelect");
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"getGroupList"}, method = RequestMethod.POST)
		public List<SelectObj> getGroupList(String centerId){
	 		return bpsRwHistoryDao.getGroupByCenterIdToSelect("getGroupByCenterIdToSelect", centerId);
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"getUserList"}, method = RequestMethod.POST)
		public List<String> getUserList(String groupId){
	 		return bpsRwHistoryDao.getUserByGroupIdToSelect("getUserByGroupIdToSelect", groupId);
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"exportNewNumberMonitorExcel"}, method = RequestMethod.POST)
		public String exportNewNumberMonitorExcel(QueryParams params, HttpServletResponse response){
	 		params.setSkipRow(-1); //表示不分页查询，查询全部数据
//	 		System.out.println(params);
	 		
	 		try {
				if(semaphore.tryAcquire(5, TimeUnit.SECONDS)){
					newNumberMonitorService.exportNewNumberMonitorExcel(params, response);
					semaphore.release();
					return "success";
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  导出新数监控报表错误===>  "+e.toString());
				return "error";
			}
			System.out.println("失败");
	 		return "wait";
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"exportMarketingPerformanceExcel"}, method = RequestMethod.POST)
		public String exportMarketingPerformanceExcel(QueryParams params, HttpServletResponse response){
	 		params.setSkipRow(-1); //表示不分页查询，查询全部数据
//	 		System.out.println(params);
	 		
	 		try {
				if(semaphore.tryAcquire(5, TimeUnit.SECONDS)){
					marketingPerformanceService.exportMarketingPerformanceExcel(params, response);
					semaphore.release();
					return "success";
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  导出营销业绩监控报表错误===>  "+e.toString());
				return "error";
			}
			System.out.println("失败");
	 		return "wait";
	 	}
	 	
	 	@ResponseBody
		@RequestMapping(value = {"exportMarketingProcessExcel"}, method = RequestMethod.POST)
		public String exportMarketingProcessExcel(QueryParams params, HttpServletResponse response){
	 		params.setSkipRow(-1); //表示不分页查询，查询全部数据
//	 		System.out.println(params);
	 		
	 		try {
				if(semaphore.tryAcquire(5, TimeUnit.SECONDS)){
					marketingProcessService.exportMarketingProcessExcel(params, response);
					semaphore.release();
					return "success";
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  导出营销过程监控报表错误===>  "+e.toString());
				return "error";
			}
			System.out.println("失败");
	 		return "wait";
	 	}
	 
	 
		@ResponseBody
		@RequestMapping(value = {"kewei"}, method = RequestMethod.POST)
		public Map<String, Object> keweiView( 
				@RequestParam("center") String center_par,
				@RequestParam("area") String area_par,
				@RequestParam("group") String group_par,
				@RequestParam("date") String date_par,
				@RequestParam("isreport") String isreport_par,
				@RequestParam("username") String username_par,
				@RequestParam("currentUser") String currentUser_par,
				@RequestParam("currentTime") String currentTime_par){
			String username="";
			String date="";
			String currentUser="";
			String currentTime="";
		       if(!"null".equals(username_par) && !"".equals(username_par)){
		    	   byte[] encode=Base64.decodeBase64(username_par.getBytes());
		    	   username=new String(encode);
		       }
		       if(!"null".equals(date_par) && !"".equals(date_par)){
		    	   byte[] encode=Base64.decodeBase64(date_par.getBytes());
		    	   date=new String(encode);
		       }
		       if(!"null".equals(currentUser_par) && !"".equals(currentUser_par)){
		        	byte[] encode=Base64.decodeBase64(currentUser_par.getBytes());
		        	currentUser=new String(encode);
		        }
		        if(!"null".equals(currentTime_par) && !"".equals(currentTime_par)){
		        	byte[] encode=Base64.decodeBase64(currentTime_par.getBytes());
		        	currentTime=new String(encode);
		        }
		        
			Map<String, Object> resultMap=new HashMap<String, Object>();
			String returnstr=voiceFileService.getFFile("werq", "F");
			
			return resultMap;
		}
}
