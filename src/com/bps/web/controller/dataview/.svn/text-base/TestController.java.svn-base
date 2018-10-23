package com.bps.web.controller.dataview;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bps.task.NoticeTask;

@Controller
@RequestMapping("/test1")
public class TestController {
	@Resource
	private NoticeTask noticeTask;
	
	@RequestMapping(value = "tasktest")
	public Map<String, Object> tasktest(HttpSession session){
		Map<String, Object> resultMap=new HashMap<String, Object>();
		System.out.println("come into dd");
		noticeTask.run();
//		copyBeforeTwoDayDataTask.run();
//		delTaskMarkTask.run();
//		totleMoneyService.reckonNoPiHe();
//		totleMoneyService.reckonPiHe();
//		totleMoneyService.reckonIncomeAmount();
//		bpsbusinessAmountService.reckonJinDu();
//		bpstollInfoService.reckonShiChang();
//		toBeRecycledService.insert();
//		toBeRecycledService.insert();
//		copyBeforeTwoDayDataTask.run();
//		reportService.deleteNowTimeReprot();
//		delTaskMarkTask.run();
//		dianYingIBreportTask.run();
//		bpsIBreportTask.run();
		//totleMoneyService.reckonIncomeAmount();
		//dayMeterService.insert();
		//dianyingtollInfoService.reckonShiChang();
		//dianyingbusinessAmountDao.findBeforeDayDataInsertNewTable("2017-05-04");
//		dianYingIBreportTask.startmysql();
//		List<BpsUser> userlist=bpsUserDao.findAll(new BpsUser());
//		for(BpsUser bu:userlist){
//			String toll_info_Value=redisUtil.getJedis().hget(redisUtil.DIANYING_TOLL_INFO_HOURS, bu.getUser_name());
//			 System.out.println(toll_info_Value);
//		}
//		
//		tollInfoService.reckonShiChang();
//		iBreportTask.run();
//		for(int i=0;i<10000;i++){
//			redisUtil.getJedis().set("hello", i+"");
//			 String toll_info_Value=redisUtil.getJedis().get("hello");
//			 RedisUtil.jedis.disconnect();
//			 System.out.println(toll_info_Value);
//		}
//		resultMap.put("msg", success);
		return resultMap;
	}
	
	
}
