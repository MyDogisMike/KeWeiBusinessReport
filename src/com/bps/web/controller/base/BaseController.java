package com.bps.web.controller.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import com.bps.bean.GridObj;
import com.bps.bean.MsgObj;
import com.bps.exception.BaseException;
import com.bps.util.JsonUtil;
import com.bps.web.util.Result;
import com.bps.web.util.ResultSupport;

/**
 * base Copyright Copyright 2017/3/31 Company infobird
 * 
 * @author andalee
 * @version 1.0
 */

@Controller
public class BaseController {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	private static final Logger logger = Logger.getLogger(BaseController.class);
	public static JsonUtil jsonUtil = new JsonUtil();

	public Result prapare(BaseVo vo) {
		Result result = new ResultSupport();
		return result;

	}

	public static JsonUtil getJsonUtil() {
		return jsonUtil;
	}

	public static void setJsonUtil(JsonUtil jsonUtil) {
		BaseController.jsonUtil = jsonUtil;
	}

	protected ModelAndView ajax_success() {
		return ajax_success("操作成功");
	}

	protected ModelAndView ajax_success(String msg) {
		return ajax_success(msg, null);
	}

	protected ModelAndView ajax_success(Object obj) {
		return ajax_success("操作成功", obj);
	}

	protected ModelAndView ajax_success(GridObj obj) {
		return ajax(obj);
	}

	protected ModelAndView ajax_success(String msg, Object obj) {
		return ajax(new MsgObj(msg, obj, true));
	}

	protected ModelAndView ajax_fail() {
		return ajax_fail("操作失败");
	}

	protected ModelAndView ajax_fail(String msg) {
		return ajax_fail(msg, null);
	}

	protected ModelAndView ajax_fail(Object obj) {
		return ajax_fail("操作失败", obj);
	}

	protected ModelAndView ajax_fail(String msg, Object obj) {
		return ajax(new MsgObj(msg, obj, false));
	}

	protected ModelAndView ajax(Object obj) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute(Result.DEFAULT_MODEL_KEY, obj);
		return new ModelAndView("commonAjaxView", modelMap);
	}

	protected ModelAndView ajax(ModelMap obj) {
		return new ModelAndView("commonAjaxView", obj);
	}

	protected Object getException(Exception e) {
		String msg = "系统出错";
		if (e instanceof BaseException) {
			BaseException nwe_e = (BaseException) e;
			msg = e.getMessage();
			logger.debug(nwe_e.getLayer());
		} else {
			logger.debug("其他错误");
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return new MsgObj(msg, false);
	}

	/**
	 * 从1,3,4,5,6转变成list
	 * 
	 * @param ids
	 * @return
	 */
	protected List<Long> getListLongFromStr(String ids) {
		List<Long> result = null;
		try {
			if (ids != null) {
				String[] idArr = ids.split(",");
				if (idArr != null && idArr.length > 0) {
					result = new ArrayList<Long>();
					for (int i = 0; i < idArr.length; i++) {
						String idStr = idArr[i];
						try {
							Long id = Long.parseLong(idStr);
							if (id != null) {
								result.add(id);
							}
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
