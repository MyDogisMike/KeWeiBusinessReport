package com.bps.dal.dao.dianying;


import java.util.List;

import com.bps.dal.dao.ibatis.CommonDaoInterface;
import com.bps.dal.object.dianying.DianYingUser;
import com.bps.exception.BaseException;


public interface DianYingDao extends CommonDaoInterface<DianYingUser, Long> {
	/**
	 * 获得电营所有正常使用的用户信息
	 * @Project VoiceAPI
	 * @param days
	 * @param mark
	 * @return
	 * @throws BaseException
	 * @author hejin
	 * @date 2018-5-18 上午11:13:50 
	 * @version V 1.0
	 */
	List<DianYingUser>  getUserList()throws BaseException;
	
	/**
	 * 根据坐席ID获得坐席性别
	 * @Project VoiceAPI
	 * @return
	 * @throws BaseException
	 * @author hejin
	 * @date 2018-5-22 下午05:18:19 
	 * @version V 1.0
	 */
	String getUserSex(String agentId)throws BaseException;
}
