package com.bps.bean;

import java.util.ArrayList;
import java.util.List;

public class GridObj {
	private boolean success = true;
	private List<?> root;
	private Long totalProperty;

	public GridObj() {
		this.setRoot(null);
		this.setTotalProperty(null);
	}

	public GridObj(List<?> root) {
		this.setRoot(root);
		this.setTotalProperty(new Long(this.root.size()));
	}

	public GridObj(List<?> root, Long totalProperty) {
		this.setRoot(root);
		this.setTotalProperty(totalProperty);
	}

	public void setRoot(List<?> root) {
		if (root == null) {
			root = new ArrayList();
		}
		this.root = root;
	}

	public void setTotalProperty(Long totalProperty) {
		if (totalProperty == null) {
			this.totalProperty = 0L;
		} else {
			this.totalProperty = totalProperty;
		}
	}

	public Long getTotalProperty() {
		return totalProperty;
	}

	public List<?> getRoot() {
		return root;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
