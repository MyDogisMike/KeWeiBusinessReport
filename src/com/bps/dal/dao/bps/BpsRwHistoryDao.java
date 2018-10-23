package com.bps.dal.dao.bps;

import java.util.List;
import java.util.Map;

import com.bps.bean.SelectObj;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;

public interface BpsRwHistoryDao {
	/**
	 * 查找指点时间（param)后出现在bps_rw_history表中的用户
	 * @param sqlId
	 * @param params
	 * @return
	 */
	public List<BpsUserInfo> getUserInfoByTime(String sqlId, Object params);
	
	
	/**
	 * 获取所有的中心id和名称
	 * @param sqlId
	 * @return
	 */
	public Map<String, String> getAllCenter(String sqlId);
	
	/**
	 * 根据中心id获取该中心下所有的小组id和名称
	 * @param sqlId
	 * @param params
	 * @return
	 */
	public Map<String, String> getGroupByCenterId(String sqlId, Object params);
	
	/**
	 * 获取所有的中心id和名称（前台界面的select）
	 * @param sqlId
	 * @return
	 */
	public List<SelectObj> getAllCenterToSelect(String sqlId);
	
	/**
	 * 根据中心id获取该中心下所有的小组id和名称（前台界面的select）
	 * @param sqlId
	 * @param params
	 * @return
	 */
	public List<SelectObj> getGroupByCenterIdToSelect(String sqlId, Object params);
	
	/**
	 * 根据小组id获取该小组下所有的用户id（前台界面的select）
	 * @param sqlId
	 * @param params
	 * @return
	 */
	public List<String> getUserByGroupIdToSelect(String sqlId, Object params);
	
	/**
	 * 查询指定时间内某员工的外呼数据
	 * @param sqlId
	 * @param params
	 * @return
	 */
	public Long getWhDateNum(String sqlId, Object params);
	
	public Long getCountUserInfoByTime(String sqlId, Object params);
	
	public void insertReportSave(String sqlId, Object params);
	
	public List<DefinedReportInfo> getDefinedReportInfo(String sqlId, Object params);
	
	public void updateDefinedReportInfo(String sqlId, Object params);
	
	public String getUserRole(String sqlId, Object params);
	
	public String getTextById(String sqlId, Object params);
	
	public List<String> getHaveDataUser(String sqlId, Object params);
	
}
