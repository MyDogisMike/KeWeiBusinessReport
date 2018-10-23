package com.bps.dal.dao.dianying.impl;

import java.util.List;

import com.bps.dal.dao.dianying.DianYingDao;
import com.bps.dal.dao.ibatis.IbatisCommonDao;
import com.bps.dal.object.dianying.DianYingUser;
import com.bps.exception.BaseException;

public class DianYingDaoImpl   extends IbatisCommonDao<DianYingUser, Long>  implements
DianYingDao {

	/* (non-Javadoc)
	 * @see com.bps.dal.dao.dianying.DianYingDao#getUserList(int, int)
	 */
	@Override
	public List<DianYingUser> getUserList()
			throws BaseException {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("VoiceFileDaoImpl_getUserList");
	}

	/* (non-Javadoc)
	 * @see com.bps.dal.dao.dianying.DianYingDao#getUserSex(java.lang.String)
	 */
	@Override
	public String getUserSex(String agentId) throws BaseException {
		// TODO Auto-generated method stub
		return (String)getSqlMapClientTemplate().queryForObject("VoiceFileDaoImpl_getUserSex",agentId);
	}
	
}
