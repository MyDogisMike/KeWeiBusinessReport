package com.bps.bean;
import java.io.Serializable;
import java.util.HashMap;

public class PageObj implements Serializable {
	/**
	 * @author andalee
	 */
	private static final long serialVersionUID = -5438536035904550737L;
	private String limit = "10";
	private String start = "0";

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public HashMap<String, String> toMap() {

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("limit", limit);

		map.put("start", start);

		return map;
	}
}
