package com.bps.dal.object;

public class QueryParams {
	public static final int DEFAULT_PAGE_SIZE = 10;

    private int page; // 查询的页码
    private int rows = DEFAULT_PAGE_SIZE; // 每页显示的数据量
    private int skipRow;	//跳过多少条记录row*(page-1)
    private String center;
    private String ywType; 
    private String group;
    private String userName;
    
	public int getSkipRow() {
		return skipRow;
	}
	public void setSkipRow(int skipRow) {
		this.skipRow = skipRow;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getYwType() {
		return ywType;
	}
	public void setYwType(String ywType) {
		this.ywType = ywType;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "QueryParams [center=" + center + ", group=" + group + ", page="
				+ page + ", rows=" + rows + ", salesman=" + userName
				+ ", ywType=" + ywType + "]";
	}
    
}
