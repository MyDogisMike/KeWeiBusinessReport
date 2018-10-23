/**
 * 
 */
package com.bps.dal.dao.dianying.impl;

import java.util.HashMap;
import java.util.List;

import com.bps.dal.dao.dianying.VoiceFileDao;
import com.bps.dal.dao.ibatis.IbatisCommonDao;
import com.bps.dal.object.dianying.VoiceFile;
import com.bps.exception.BaseException;

/**
 * @author HeJin
 *
 */
public class VoiceFileDaoImpl  extends IbatisCommonDao<VoiceFile, Long> implements VoiceFileDao {

	/* (non-Javadoc)
	 * @see com.bps.dal.dao.dianying.VoiceFileDao#getTuiJianDataList(java.util.HashMap)
	 */
	@Override
	public List<VoiceFile> getVoiceFileList(HashMap<String, Object> params)
			throws BaseException {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("VoiceFileDaoImpl_getVoiceFileInfo", params);
	}

}
