package com.bps.service.daymeter.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.bps.exception.BaseException;
import com.bps.service.daymeter.DataViewService;
import com.bps.util.RedisUtil;


public class DataViewServiceImpl implements DataViewService{
	
	@Override
	public Map<String, Object> getAll(String center,String area,String group,String date,String user_name) throws BaseException{
		return null;
	}

}

