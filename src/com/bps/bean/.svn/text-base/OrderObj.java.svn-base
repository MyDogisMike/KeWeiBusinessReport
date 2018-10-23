package com.bps.bean;


import java.io.Serializable;
import java.util.HashMap;

import com.bps.util.CommonUtil;

public class OrderObj implements Serializable{
	/**
	 * @author andalee
	 */
	private static final long serialVersionUID = -6176906309779683281L;
	private String sort = "";
	private String dir = "asc";

	public OrderObj() {

	}

	public OrderObj(String sort) {
		this.sort = sort;
	}

	public OrderObj(String sort, String dir) {
		this.sort = sort;
		if (!CommonUtil.isEmpty(dir)) {
			this.dir = dir;
		}
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public HashMap<String, String> toMap() {

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("sort", sort);

		map.put("dir", dir);

		return map;
	}
}
