/**
 * 
 */
package com.bps.dal.dao.dianying;

import java.util.HashMap;
import java.util.List;

import com.bps.dal.dao.ibatis.CommonDaoInterface;
import com.bps.dal.object.dianying.VoiceFile;
import com.bps.exception.BaseException;

/**
 * @author HeJin
 *
 */
public interface VoiceFileDao extends CommonDaoInterface<VoiceFile, Long>  {
	/**
	 * 
	 * @Project VoiceAPI
	 * @param days
	 * @param mark
	 * @return
	 * @throws BaseException
	 * @author hejin
	 * @date 2018-5-16 下午05:43:43 
	 * @version V 1.0
	 */
	List<VoiceFile>  getVoiceFileList(HashMap<String, Object> params)
			throws BaseException;
}
