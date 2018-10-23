package com.bps.web.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

public class WebExecutor {
	public static WebUtil webUtil = new WebUtil();

	private void WebExecutor() {

	}

	public static ModelMap execute(Logger logger, AbstractValidator validator,
			Biz biz) {

		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("webutil",webUtil);
		ResultCode resultCode = new ResultCode();
		resultCode.init();
		logParams(logger, validator);

		if (validator.validate()) {
			try {
				Result result = biz.process();
				logResult(logger, result);
				transfer(result, modelMap);
			} catch (Exception e) {
				logger.error("unknow exception", e);
				resultCode.setParams((Integer) ResultCode.UNKNOW_ERROR_CODE[0],
						(String) ResultCode.UNKNOW_ERROR_CODE[1],
						ValidatorUtil.ERROR_SYSTEM);
			}

		} else {
			logger
					.error(ValidatorUtil.KEY_STATUS + " "
							+ ValidatorUtil.STATUS_FAIL + " "
							+ ValidatorUtil.KEY_ERROR_MSG + " "
							+ validator.getErrMsg());
		}

		return modelMap;
	}

	private static void logResult(Logger logger, Result result) {
		if (result.isSuccess()) {
			logger.info("[RESULT]" + ValidatorUtil.KEY_STATUS + ":"
					+ ValidatorUtil.STATUS_SUCCESS);
		} else {
			logger.error("[RESULT]" + ValidatorUtil.KEY_STATUS + ":"
					+ ValidatorUtil.STATUS_FAIL + ValidatorUtil.KEY_ERROR_MSG
					+ ":" + result.getErrMsg());
		}

	}

	private static void transfer(Result result, ModelMap modelMap) {
		modelMap.put("code", result.isSuccess() ? 1 : 0);
		Map map = result.getModels();
		Set keySet = map.keySet();
		for (Object key : keySet) {
			modelMap.addAttribute((String) key, map.get(key));
		}
	}

	private static void logParams(Logger logger, Object o) {
		StringBuffer sb = new StringBuffer();
		Class c = o.getClass();
		Field[] fields = c.getDeclaredFields();
		sb.append("PARAMS[");
		for (Field field : fields) {
			try {

				sb.append(field.getName());
				sb.append(":");
				sb.append(invokeMethod(o, field.getName(), null));
				sb.append(" , ");

			} catch (IllegalArgumentException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		sb.append("]");
		logger.info(sb.toString());
	}

	public static Object invokeMethod(Object o, String methodName, Object[] args)
			throws Exception {
		Class classes = o.getClass();
		methodName = methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
		Method method = null;
		try {
			method = classes.getMethod("get" + methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}

		return method.invoke(o);
	}

	public interface Biz {
		Result process();
	}
}
