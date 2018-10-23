package com.bps.service.base;

import com.bps.exception.BaseException;
import com.bps.exception.ServiceException;
import com.bps.util.CommonUtil;
/**
 * 
 * @author andalee   20170331
 *
 */
public abstract class BaseService {
	public BaseException getException(Exception e) {
		BaseException new_e;
		if (e instanceof BaseException) {
			new_e = (BaseException) e;
		} else {
			new_e = new ServiceException("服务异常", e.getMessage());
			e.printStackTrace();
		}
		if (CommonUtil.isEmpty(new_e.getLayer())) {
			new_e.setLayer("Service层");
		}
		return new_e;
	}
}
