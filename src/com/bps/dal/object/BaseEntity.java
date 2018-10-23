package com.bps.dal.object;

import java.io.Serializable;

import com.bps.bean.OrderObj;
import com.bps.bean.PageObj;

public class BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PageObj page = new PageObj();
	private OrderObj order = null;

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
