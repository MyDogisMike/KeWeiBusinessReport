package com.bps.dal.dao.ibatis;

import com.bps.exception.BaseException;
import com.bps.exception.DaoException;
import com.bps.util.CommonUtil;

/**
 * ibatis操作公共类
 * 
 * @author andalee
 * 
 */
@SuppressWarnings("unchecked")
public abstract class IbatisCacheDao {
	/**
	 * 获取异常信息
	 * 
	 * @param e
	 * @return
	 */
	protected BaseException getException(Exception e) {
		BaseException new_e = null;
		if (BaseException.class.isInstance(e)) {
			new_e = (BaseException) e;
		} else {
			new_e = new DaoException("数据库异常", e.getMessage());
			e.printStackTrace();
		}
		if (CommonUtil.isEmpty(new_e.getLayer())) {
			new_e.setLayer("DAO层");
		}
		return new_e;
	}

}