package com.bps.exception;

import java.util.HashMap;

@SuppressWarnings("serial")
public class DaoException extends BaseException {

	public DaoException() {
		super("数据库异常");

	}

	public DaoException(String msg) {
		super(msg);

	}

	public DaoException(String msg, String detail) {
		super(msg, detail);

	}

	public DaoException(String msg, HashMap<String, Object> info) {
		super(msg, info);

	}
}
