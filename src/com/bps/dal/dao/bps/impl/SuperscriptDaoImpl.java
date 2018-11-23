package com.bps.dal.dao.bps.impl;

import com.bps.dal.dao.bps.SuperscriptReportDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

public class SuperscriptDaoImpl extends IbatisBaseDao  implements SuperscriptReportDao{


	@Override
	public Long getBidMainBusi(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getBidCrossData(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getDgjDataNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getGqDataNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getWwhDataNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getBidFData(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
