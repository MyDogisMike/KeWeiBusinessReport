package com.bps.dal.object.dianying;

import java.io.Serializable;
import com.bps.annotation.DBField;
import com.bps.annotation.DBTable;
import com.bps.dal.object.BaseEntity;
//表名字                                                             主键                                                                默认排序列                                            排序方式
@DBTable(tableName = "user_list",primaryKeyName="user_id", defaultSort = "user_id", defaultDir = "desc")
public class DianYingUser  extends BaseEntity implements Serializable{
   private static final long serialVersionUID = 1L;
   @DBField
   private Long user_id;//用户ID
   @DBField
   private String user_name;//用户名
   @DBField
   private String user_real_name;//真实姓名
   @DBField
   private Integer bm_id;//部门编号
   @DBField
   private String bm_path;//部门路径
   @DBField
   private String dept_id;//技能组编号
   @DBField
   private String dept_name;//技能组名称
   @DBField
   private String bm_name;//账号所在部门名称
   @DBField
   private String center;//中心
   @DBField
   private String user_role_id;//账号绑定系统角色编号
   @DBField
   private String user_role;//账号绑定系统角色名称
   @DBField
   private String user_sex;//性别
   
   public DianYingUser(){}
   
	public DianYingUser(Long userId, String userName, String userRealName,
			Integer bmId, String bmPath, String deptId, String deptName,
			String bmName, String center, String userRoleId, String userRole,
			String userSex) {
		super();
		user_id = userId;
		user_name = userName;
		user_real_name = userRealName;
		bm_id = bmId;
		bm_path = bmPath;
		dept_id = deptId;
		dept_name = deptName;
		bm_name = bmName;
		this.center = center;
		user_role_id = userRoleId;
		user_role = userRole;
		user_sex = userSex;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long userId) {
		user_id = userId;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String userName) {
		user_name = userName;
	}

	public String getUser_real_name() {
		return user_real_name;
	}

	public void setUser_real_name(String userRealName) {
		user_real_name = userRealName;
	}

	public Integer getBm_id() {
		return bm_id;
	}

	public void setBm_id(Integer bmId) {
		bm_id = bmId;
	}

	public String getBm_path() {
		return bm_path;
	}

	public void setBm_path(String bmPath) {
		bm_path = bmPath;
	}

	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String deptId) {
		dept_id = deptId;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String deptName) {
		dept_name = deptName;
	}

	public String getBm_name() {
		return bm_name;
	}

	public void setBm_name(String bmName) {
		bm_name = bmName;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getUser_role_id() {
		return user_role_id;
	}

	public void setUser_role_id(String userRoleId) {
		user_role_id = userRoleId;
	}

	public String getUser_role() {
		return user_role;
	}

	public void setUser_role(String userRole) {
		user_role = userRole;
	}

	public String getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(String userSex) {
		user_sex = userSex;
	}

	@Override
	public String toString() {
		return "DianYingUser [bm_id=" + bm_id + ", bm_name=" + bm_name
				+ ", bm_path=" + bm_path + ", center=" + center + ", dept_id="
				+ dept_id + ", dept_name=" + dept_name + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", user_real_name="
				+ user_real_name + ", user_role=" + user_role
				+ ", user_role_id=" + user_role_id + ", user_sex=" + user_sex
				+ "]";
	}
	
	
}