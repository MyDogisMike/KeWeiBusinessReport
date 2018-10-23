package com.bps.web.controller.base;

import java.util.HashMap;

import com.bps.bean.OrderObj;
import com.bps.bean.PageObj;
import com.bps.web.util.AbstractValidator;

/**
 * date 2011-9-16 Company taobao
 * 
 * @author Figo
 * @version 1.0
 * 
 */
public class BaseVo extends AbstractValidator {

	private Long id;

	private PageObj page;
	private OrderObj order;
	private HashMap<String, Object> condition;

	public HashMap<String, Object> getCondition() {
		return condition;
	}

	public void setCondition(HashMap<String, Object> condition) {
		this.condition = condition;
	}

	public BaseVo() {
		super();
	}

	public boolean validate() {
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PageObj getPage() {
		return page;
	}

	public void setPage(PageObj page) {
		this.page = page;
	}

	public OrderObj getOrder() {
		return order;
	}

	public void setOrder(OrderObj order) {
		this.order = order;
	}

}
