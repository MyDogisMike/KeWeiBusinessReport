package com.bps.dal.dao.bps.impl;


import java.util.List;
import java.util.Map;

import com.bps.bean.SelectObj;
import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;

@SuppressWarnings("unchecked")
public class BpsRwHistoryDaoImp extends IbatisBaseDao implements BpsRwHistoryDao{

	@Override
	public List<BpsUserInfo> getUserInfoByTime(String sqlId, Object params) {
		return (List<BpsUserInfo>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}


	@Override
	public Map<String, String> getAllCenter(String sqlId) {
		return getSqlMapClientTemplate().queryForMap(sqlId, null, "bmId", "bmName");
	}

	@Override
	public Map<String, String> getGroupByCenterId(String sqlId, Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "bmId", "bmName");
	}
	
	@Override
	public Long getWhDateNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}


	@Override
	public Long getCountUserInfoByTime(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}


	@Override
	public List<SelectObj> getAllCenterToSelect(String sqlId) {
		return (List<SelectObj>)getSqlMapClientTemplate().queryForList(sqlId, null);
	}


	@Override
	public List<SelectObj> getGroupByCenterIdToSelect(String sqlId,
			Object params) {
		return (List<SelectObj>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}


	@Override
	public List<String> getUserByGroupIdToSelect(String sqlId, Object params) {
		return (List<String>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}


	@Override
	public void insertReportSave(String sqlId, Object params) {
		getSqlMapClientTemplate().insert(sqlId, params);
	}


	@Override
	public List<DefinedReportInfo> getDefinedReportInfo(String sqlId,
			Object params) {
		return (List<DefinedReportInfo>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}


	@Override
	public void updateDefinedReportInfo(String sqlId, Object params) {
		getSqlMapClientTemplate().update(sqlId, params);
	}


	@Override
	public String getUserRole(String sqlId, Object params) {
		return (String) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}


	@Override
	public String getTextById(String sqlId, Object params) {
		return (String) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}


	@Override
	public List<String> getHaveDataUser(String sqlId, Object params) {
		return (List<String>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}

}
