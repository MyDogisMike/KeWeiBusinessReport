package com.bps.dal.object;

import java.io.Serializable;
import java.util.Date;

import com.bps.annotation.DBField;

public class Entity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7905883912321564918L;
	@DBField
	private Long id;
	@DBField
	private Integer flag; // 0正常 -1删除
	@DBField
	private Date createTime; // 创建时间
	@DBField
	private Date modifyTime; // 修改时间
	@DBField
	private String creator; // 创建者（登录名）
	@DBField
	private String modifier; // 修改者（登录名）

	public Entity() {
		// createTime = new Date();
		// modifyTime = new Date();
		// id = new Long("-1");
		// creator = "admin";
	}

	public Entity(boolean b) {
		if (!b) {
			createTime = null;
			modifyTime = null;
			id = null;
			creator = null;
			flag = null;
		}
	}

	/**
	 * @hibernate.id column="id" length="10" unsaved-value="-1"
	 *               generator-class="native" type="java.lang.Long"
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public boolean isNew() {
		return (null == id || -1 == id);
	}

	/**
	 * @return Returns the creator (user name).
	 * 
	 * @hibernate.property column = "creator" type = "string" length = "128"
	 *                     not-null = "true" default="admin"
	 */
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return Returns the createTime.
	 * 
	 * @hibernate.property column = "create_time" type = "java.util.Date"
	 *                     not-null = "true"
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            The createTime to set.
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return Returns the modifyTime.
	 * 
	 * @hibernate.property column = "modify_time" type = "java.util.Date"
	 *                     not-null = "true"
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 *            The modifyTime to set.
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 
	 * @return Returns the modifier (user name).
	 * 
	 * @hibernate.property column = "modifier" type = "string" length="128"
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier
	 *            The modifier to set.
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return Returns string value.
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		String cls = getClass().getName();
		String cln = cls.substring(cls.lastIndexOf('.') + 1);

		buffer.append("[" + cln + "] ");
		buffer.append("ID = " + this.id + ", ");
		buffer.append("CREATE_TIME = " + this.createTime + ", ");
		buffer.append("MODIFY_TIME = " + this.modifyTime + ", ");
		buffer.append("FLAG = " + this.flag + ", ");
		buffer.append("CREATOR = " + this.creator + ", ");
		buffer.append("MODIFIER = " + this.modifier + ", ");
		return buffer.toString();
	}

	public Entity getSuper() {
		return this;
	}

	public void setSuper(Entity entity) {
		setCreator(entity.getCreator());
		setFlag(entity.getFlag());
		setId(entity.getId());
	}

	/**
	 * @Override
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj != null && obj instanceof Entity) {
			Entity newObj = (Entity) obj;
			return getId().longValue() == newObj.getId().longValue()
					&& newObj.getId().longValue() != -1;
		}
		return false;
	}

	/**
	 * @Override
	 */
	public int hashCode() {
		if (getId() != null) {
			return getId().hashCode();
		} else {
			return super.hashCode();
		}
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
