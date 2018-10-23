package com.bps.dal.dao.ibatis;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.UncategorizedSQLException;

import com.bps.annotation.DBField;
import com.bps.annotation.DBTable;
import com.bps.bean.EqualObj;
import com.bps.bean.GridObj;
import com.bps.bean.OrderObj;
import com.bps.bean.PageObj;
import com.bps.exception.BaseException;
import com.bps.exception.DaoException;
import com.bps.util.CommonUtil;
import com.bps.util.StringUtil;

/**
 * DAO操作公共类
 * 
 * @author andalee
 * @date 2017-03-31
 * @time 下午01:50:11
 * @version 1.0
 * @updater
 * @update-time
 * @update-info
 * @param <T>
 * @param <ID>
 */
@SuppressWarnings("unchecked")
public abstract class IbatisCommonDao<T, ID> extends IbatisBaseDao {
	private static final Logger logger = Logger
			.getLogger(IbatisCommonDao.class);

	private String tableName;
	private String primaryKeyName;
	private String defaultSort;
	private String defaultDir;
	//private String flagName;// 删除标志的名称：默认flag

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDefaultSort() {
		return defaultSort;
	}

	public void setDefaultSort(String defaultSort) {
		this.defaultSort = defaultSort;
	}

	public String getDefaultDir() {
		return defaultDir;
	}

	public void setDefaultDir(String defaultDir) {
		this.defaultDir = defaultDir;
	}

	/**
	 * 对象转化为相等的list对象
	 * 
	 * @param from_entity
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private List<EqualObj> getEqualList(T entity)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<EqualObj> result = null;
		if (entity != null) {
			result = new ArrayList<EqualObj>();
			// Set<Entry<String, Object>> newSet =
			// PropertyUtils.describe(entity)
			// .entrySet();
			Field[] fields_curr = entity.getClass().getDeclaredFields();
			Field[] fields;
			// 如果存在父类，拷贝到父类属性
			if (entity.getClass().getGenericSuperclass() != null) {
				Class superClass = entity.getClass().getSuperclass();// 父类
				Field[] fields_super = superClass.getDeclaredFields();// 父类属性
				fields = new Field[fields_curr.length + fields_super.length];
				System.arraycopy(fields_curr, 0, fields, 0, fields_curr.length);
				System.arraycopy(fields_super, 0, fields, fields_curr.length,
						fields_super.length);
			} else {
				fields = fields_curr.clone();
			}
			for (int i = 0; i < fields.length; i++) {
				// for (Entry<String, Object> newEntry : newSet) {
				Field field = fields[i];
				DBField annotation = field.getAnnotation(DBField.class);
				if (annotation != null) {
					String dbkey = annotation.field();
					String key = field.getName();
					if (CommonUtil.isEmpty(dbkey)) {
						dbkey = StringUtil.getSplitString(key);
					}
					Object value = annotation != null ? this.getPropertyValue(
							entity, key) : null;
					if (CommonUtil.isEmpty(dbkey) || CommonUtil.isEmpty(value)) {
						continue;
					}
					String v = value.toString();
					if (value instanceof Date) {
						java.util.Date d = (java.util.Date) value;
						v = CommonUtil.formatDateTime(d);
					}
					if (!dbkey.equals(this.primaryKeyName)) {
						v = "'" + v + "'";
					}
					EqualObj eo = new EqualObj(dbkey, v);
					result.add(eo);
				}
			}
		}
		return result;
	}

	/**
	 * 通过对象取得对象的主键值
	 * 
	 * @param entity
	 * @return
	 * @throws ParamException
	 * @throws DaoException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private ID getPrimaryKey(T entity) throws DaoException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (entity == null) {
			throw new NullPointerException();
		}
		this.initFromAnnotation((Class<T>) entity.getClass());
		Object primaryKeyValueObj = getPropertyValue(entity, primaryKeyName);
		return (ID) primaryKeyValueObj;
	}

	/**
	 * 取得对象的属性值
	 * 
	 * @param entity
	 * @param propertyName
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private Object getPropertyValue(T entity, String propertyName)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object result = null;
		try {
			if (!StringUtils.isEmpty(propertyName)) {
				result = PropertyUtils.getProperty(entity, propertyName);
			}
		} catch (Exception e) {
			this.getException(e);
		}
		return result;
	}

	/**
	 * 设置对象的属性值
	 * 
	 * @param entity
	 * @param propertyName
	 * @param PropertyValue
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private void setPropertyValue(T entity, String propertyName,
			Object PropertyValue) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		boolean isWriteAble = PropertyUtils.isWriteable(entity, propertyName);
		if (isWriteAble) {
			PropertyUtils.setProperty(entity, propertyName, PropertyValue);
		}
	}

	/**
	 * 从map中取得对象实例
	 * 
	 * @param entityClass
	 * @param result
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	private T getInstanceFromMap(Class<T> entityClass,
			Map<String, Object> result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException {
		T entity = entityClass.newInstance();
		if (result != null) {
			Field[] fields_curr = entity.getClass().getDeclaredFields();
			Field[] fields_super;
			Field[] fields;
			// 如果存在父类，拷贝到父类属性
			if (entity.getClass().getGenericSuperclass() != null) {
				Class superClass = entity.getClass().getSuperclass();// 父类
				fields_super = superClass.getDeclaredFields();// 父类属性
				fields = new Field[fields_curr.length + fields_super.length];
				System.arraycopy(fields_curr, 0, fields, 0, fields_curr.length);
				System.arraycopy(fields_super, 0, fields, fields_curr.length,
						fields_super.length);
			} else {
				fields = fields_curr.clone();
			}
			for (int i = 0; i < fields.length; i++) {
				DBField annotation = fields[i].getAnnotation(DBField.class);// 取得注解
				String dbkey = "";
				String key = "";
				if (annotation != null) {
					key = fields[i].getName();
					dbkey = annotation.field();
					if (CommonUtil.isEmpty(dbkey)) {
						dbkey = StringUtil.getSplitString(key);// 将java类中helloWorld转为hello_world
					}
				} else {
					key = fields[i].getName();
					dbkey = StringUtil.getSplitString(key);
				}
				Object value_obj = result.get(dbkey);
				if (value_obj != null) {
					Class<?> vo_class = PropertyUtils.getPropertyType(entity,
							fields[i].getName());
					if (vo_class.isInstance(value_obj)) {
						setPropertyValue(entity, key, value_obj);
					} else {
						logger.debug("字段" + key + "对应存在问题:数据库中类型："
								+ value_obj.getClass().getSimpleName()
								+ ",java中类型：" + vo_class.getSimpleName());
						if (value_obj instanceof BigInteger) {
							BigInteger value_biginteger = (BigInteger) value_obj;
							if (vo_class.isAssignableFrom(Integer.class)) {
								setPropertyValue(entity, key,
										value_biginteger.intValue());
							} else if (vo_class.isAssignableFrom(Long.class)) {
								setPropertyValue(entity, key,
										value_biginteger.longValue());
							} else if (vo_class.isAssignableFrom(Float.class)) {
								setPropertyValue(entity, key,
										value_biginteger.floatValue());
							}
						} else if (value_obj instanceof BigDecimal) {
							BigDecimal value_bigdecimal = (BigDecimal) value_obj;
							if (vo_class.isAssignableFrom(Integer.class)) {
								setPropertyValue(entity, key,
										value_bigdecimal.intValue());
							} else if (vo_class.isAssignableFrom(Long.class)) {
								setPropertyValue(entity, key,
										value_bigdecimal.longValue());
							} else if (vo_class.isAssignableFrom(Float.class)) {
								setPropertyValue(entity, key,
										value_bigdecimal.floatValue());
							}
						} else if (value_obj instanceof Integer) {
							Integer value_int = (Integer) value_obj;
							if (vo_class.isAssignableFrom(Integer.class)) {
								setPropertyValue(entity, key,
										value_int.intValue());
							} else if (vo_class.isAssignableFrom(Long.class)) {
								setPropertyValue(entity, key,
										value_int.longValue());
							} else if (vo_class.isAssignableFrom(Float.class)) {
								setPropertyValue(entity, key,
										value_int.floatValue());
							}
						} else if (value_obj instanceof Boolean) {
							Boolean value_boolean = (Boolean) value_obj;
							if (vo_class.isAssignableFrom(Integer.class)
									|| vo_class.isAssignableFrom(Long.class)
									|| vo_class.isAssignableFrom(Float.class)) {
								if (value_boolean != null) {
									if (value_boolean) {
										setPropertyValue(entity, key, 1);
									} else {
										setPropertyValue(entity, key, 0);
									}
								}
							}
						}
					}
				}
			}
		}
		return entity;
	}

	/**
	 * 从map中取得对象实例
	 * 
	 * @param entityClass
	 * @param result
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	private T getInstanceFromMapBack(Class<T> entityClass,
			Map<String, Object> result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException {
		T entity = entityClass.newInstance();
		for (Iterator<Entry<String, Object>> it = result.entrySet().iterator(); it
				.hasNext();) {
			Entry<String, Object> o = it.next();
			String key = o.getKey();
			Object value_obj = o.getValue();
			if (key != null && value_obj != null) {
				key = key.toLowerCase();
				Class<?> vo_class = PropertyUtils.getPropertyType(entity, key);
				if (vo_class == null) {
					logger.debug("字段" + key + "在java类中没有找到");
					continue;
				}
				if (vo_class.isInstance(value_obj)) {
					setPropertyValue(entity, key, value_obj);
				} else {
					logger.debug("字段" + key + "对应存在问题:数据库中类型："
							+ value_obj.getClass().getSimpleName()
							+ ",java中类型：" + vo_class.getSimpleName());
					if (value_obj instanceof BigInteger) {
						BigInteger value_biginteger = (BigInteger) value_obj;
						if (vo_class.isAssignableFrom(Integer.class)) {
							setPropertyValue(entity, key,
									value_biginteger.intValue());
						} else if (vo_class.isAssignableFrom(Long.class)) {
							setPropertyValue(entity, key,
									value_biginteger.longValue());
						} else if (vo_class.isAssignableFrom(Float.class)) {
							setPropertyValue(entity, key,
									value_biginteger.floatValue());
						}
					} else if (value_obj instanceof BigDecimal) {
						BigDecimal value_bigdecimal = (BigDecimal) value_obj;
						if (vo_class.isAssignableFrom(Integer.class)) {
							setPropertyValue(entity, key,
									value_bigdecimal.intValue());
						} else if (vo_class.isAssignableFrom(Long.class)) {
							setPropertyValue(entity, key,
									value_bigdecimal.longValue());
						} else if (vo_class.isAssignableFrom(Float.class)) {
							setPropertyValue(entity, key,
									value_bigdecimal.floatValue());
						}
					} else if (value_obj instanceof Integer) {
						Integer value_int = (Integer) value_obj;
						if (vo_class.isAssignableFrom(Integer.class)) {
							setPropertyValue(entity, key, value_int.intValue());
						} else if (vo_class.isAssignableFrom(Long.class)) {
							setPropertyValue(entity, key, value_int.longValue());
						} else if (vo_class.isAssignableFrom(Float.class)) {
							setPropertyValue(entity, key,
									value_int.floatValue());
						}
					}
				}
			}
		}
		return entity;
	}

	/**
	 * 从list<map>中取得对象实例list
	 * 
	 * @param entityClass
	 * @param mapList
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	private List<T> getListInstanceFromListMap(Class<T> entityClass,
			List<Map<String, Object>> mapList) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException {
		List<T> resultList = null;
		if (mapList != null && mapList.size() > 0) {
			resultList = new ArrayList<T>();
			for (Map<String, Object> map : mapList) {
				T entity = this.getInstanceFromMap(entityClass, map);
				resultList.add(entity);
			}
		}
		return resultList;
	}

	/**
	 * 根据例子来查找列表
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	private List<T> findByExample(T entity, PageObj page, OrderObj order)
			throws BaseException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException, SQLException {
		if (entity == null) {
			throw new NullPointerException();
		}
		initFromAnnotation((Class<T>) entity.getClass());
		if (CommonUtil.isEmpty(tableName)) {
			throw new DaoException("不支持此操作", "tableName为空");
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);

	//	if (!StringUtils.isEmpty(this.flagName)) {
	//		Object flagValue = this.getPropertyValue(entity, this.flagName);
	//		if (flagValue == null) {// 过滤掉打了删除标记的记录
	//			this.setPropertyValue(entity, this.flagName, 0);
	//		}
	//	}

		List<EqualObj> conditionList = getEqualList(entity);
		params.put("conditionList", conditionList);
		if (order == null) {
			if (!CommonUtil.isEmpty(defaultSort)) {
				order = new OrderObj(defaultSort, defaultDir);
			}
		}
		if (order != null) {
			params.putAll(order.toMap());
		}
		if (page != null) {
			params.putAll(page.toMap());
		}
		List list = this.queryForList("IbatisCommonDao_findByExample", params);
		Class<T> entityClass = (Class<T>) entity.getClass();
		return this.getListInstanceFromListMap(entityClass, list);
	}

	private void initFromAnnotation(Class<T> entityClass) throws DaoException {
		if (this.tableName == null) {
			DBTable annotation = entityClass.getAnnotation(DBTable.class);
			if (annotation != null) {
				this.tableName = annotation.tableName();
				this.primaryKeyName = annotation.primaryKeyName();
				this.defaultDir = annotation.defaultDir();
				this.defaultSort = annotation.defaultSort();
			//	this.flagName = annotation.flagName();
				if (this.tableName == null) {
					this.tableName = StringUtil.getSplitString(entityClass
							.getSimpleName());
				}
				if (CommonUtil.isEmpty(this.tableName)
						|| CommonUtil.isEmpty(this.primaryKeyName)) {
					throw new DaoException("配置出错");
				}
			} else {
				throw new DaoException("配置出错");
			}
		}
	}

	/**
	 * 加载对象
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 */
	public T load(Class<T> entityClass, ID id) throws BaseException {
		try {
			if (CommonUtil.isEmpty(entityClass) || id == null) {
				throw new NullPointerException();
			}
			initFromAnnotation(entityClass);

			List<T> list = this.findByProperty(entityClass, primaryKeyName, id);
			if (list == null || list.size() == 0) {
				throw new DaoException("加载时数据库中不存在记录", "load出错");
			} else if (list.size() > 1) {
				throw new DaoException("加载时数据库中存在1条记录以上记录", "load出错");
			}
			return list.get(0);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 加载对象,若找不到或者记录过多的时候会报错
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 */
	public T load(T entity) throws BaseException {
		try {
			Class<T> entityClass = (Class<T>) entity.getClass();
			ID id = getPrimaryKey(entity);
			return load(entityClass, id);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 加载属性相同对象
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 */
	public T loadEqual(T entity) throws BaseException {
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			List<T> list = this.findByExample(entity);
			if (list == null || list.size() == 0) {
				throw new DaoException("加载时数据库中不存在记录", "loadEqual出错");
			} else if (list.size() > 1) {
				throw new DaoException("加载时数据库中存在1条记录以上记录", "loadEqual出错");
			}
			return list.get(0);
		} catch (UncategorizedSQLException e1) {
			throw new DaoException("加载出错", e1.getMessage());
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 查找一条记录，若找不到则返回null，不会报错
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws BaseException
	 */
	public T findById(Class<T> entityClass, ID id) throws BaseException {
		try {
			if (CommonUtil.isEmpty(entityClass) || id == null) {
				throw new NullPointerException();
			}
			T entity = null;
			initFromAnnotation(entityClass);
			List<T> list = this.findByProperty(entityClass, primaryKeyName, id);
			if (list != null && list.size() > 0) {
				if (list.size() > 1) {
					throw new DaoException("查找相同记录时数据库中存在1条记录以上记录", "find出错");
				} else {// 只有一条记录的情况
					entity = list.get(0);
				}
			}
			return entity;
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 根据例子来查找列表
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 */
	public List<T> findByExample(T entity) throws BaseException {
		try {
			return findByExample(entity, null, null);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 根据例子来查找列表
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 */
	public List<T> findByExample(T entity, OrderObj order) throws BaseException {
		try {
			return findByExample(entity, null, order);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 根据属性来查找对象
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 * @throws BaseException
	 */
	public List<T> findByProperty(Class<T> entityClass, String propertyName,
			Object propertyValue) throws BaseException {
		try {
			if (entityClass == null || CommonUtil.isEmpty(propertyName)
					|| CommonUtil.isEmpty(propertyValue)) {
				throw new NullPointerException();
			}
			T entity = entityClass.newInstance();
			this.setPropertyValue(entity, propertyName, propertyValue);
			return this.findByExample(entity);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 分页查询记录
	 * 
	 * @param entity
	 * @param page
	 * @param order
	 * @return
	 * @throws BaseException
	 */
	public GridObj findByPage(T entity, PageObj page, OrderObj order)
			throws BaseException {
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			if (page == null) {
				page = new PageObj();
			}
			List<T> root = findByExample(entity, page, order);
			Long count = countEqual(entity);
			return new GridObj(root, count);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 查找全部
	 * 
	 * @return
	 * @throws BaseException
	 */
	public List<T> findAll(Class<T> entityClass) throws BaseException {
		try {
			T entity = entityClass.newInstance();
			return findByExample(entity);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 通用的插入操作
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 */
	public Long insert(T entity) throws BaseException {
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			initFromAnnotation((Class<T>) entity.getClass());
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("tableName", tableName);
			// params.put("primaryKeyName", primaryKeyName);
			// setPropertyValue(entity, primaryKeyName, null);
			List<EqualObj> conditionList = getEqualList(entity);
			params.put("conditionList", conditionList);
			Long id = (Long) this.executeInsert("IbatisCommonDao_insert",
					params);
			ID oldId = this.getPrimaryKey(entity);
			if (oldId == null) {
				setPropertyValue(entity, primaryKeyName, null);
			}
			// else {
			// id = oldId;
			// }
			return id;
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 通用更新操作,注意此操作会将对象中属性为空字段更新到数据库中（目前暂时未实现）
	 * 
	 * @param entity
	 * @throws BaseException
	 */
	public boolean update(T entity) throws BaseException {
		boolean flag = true;
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			initFromAnnotation((Class<T>) entity.getClass());
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("tableName", tableName);
			List<EqualObj> conditionList = getEqualList(entity);
			params.put("conditionList", conditionList);
			params.put("primaryKeyName", primaryKeyName);
			Object primaryKeyValueObj = getPropertyValue(entity, primaryKeyName);
			if (primaryKeyValueObj == null) {
				throw new DaoException("数据库异常", "更新操作没有id");
			}
			params.put("primaryKeyValue", primaryKeyValueObj);
			this.executeUpdate("IbatisCommonDao_update", params);
		} catch (Exception e) {
			flag = false;
			throw getException(e);
		}
		return flag;
	}

	/**
	 * 仅更新非空的部分
	 * 
	 * @param entity
	 * @throws BaseException
	 */
	public boolean updateIgnoreNull(T entity) throws BaseException {
		boolean flag = true;
		try {
			flag = this.update(entity);
		} catch (Exception e) {
			flag = false;
			throw getException(e);
		}
		return flag;
	}

	/**
	 * 通用删除
	 * 
	 * @param entityClass
	 * @param id
	 * @throws BaseException
	 */
	public boolean delete(Class<T> entityClass, ID id) throws BaseException {
		boolean flag = true;
		try {
			if (CommonUtil.isEmpty(entityClass) || id == null) {
				throw new NullPointerException();
			}
			initFromAnnotation(entityClass);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("tableName", tableName);
			params.put("primaryKeyName", primaryKeyName);
			params.put("primaryKeyValue", id);
			this.executeDelete("IbatisCommonDao_delete", params);
		} catch (Exception e) {
			flag = false;
			throw getException(e);
		}
		return flag;
	}

	/**
	 * 通用删除
	 * 
	 * @param entityClass
	 * @param id
	 * @throws BaseException
	 */
	public boolean delete(T entity) throws BaseException {
		boolean flag = true;
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			ID id = getPrimaryKey(entity);
			delete((Class<T>) entity.getClass(), id);
		} catch (Exception e) {
			flag = false;
			throw getException(e);
		}
		return flag;
	}

	/**
	 * 删除与此实体相同属性的对象
	 * 
	 * @param entity
	 * @throws BaseException
	 */
	public boolean deleteByFlag(Class<T> entityClass, ID id)
			throws BaseException {
		boolean flag = true;
		try {
			initFromAnnotation(entityClass);
			T entity = entityClass.newInstance();
			this.setPropertyValue(entity, primaryKeyName, id);
		//	this.setPropertyValue(entity, flagName, -1);
			this.update(entity);
		} catch (Exception e) {
			flag = false;
			throw getException(e);
		}
		return flag;
	}

	/**
	 * 删除与此实体相同属性的对象
	 * 
	 * @param entity
	 * @throws BaseException
	 */
	public boolean deleteEqual(T entity) throws BaseException {
		boolean flag = true;
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			initFromAnnotation((Class<T>) entity.getClass());
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("tableName", tableName);
			List<EqualObj> conditionList = this.getEqualList(entity);
			if(conditionList==null || conditionList.size()==0){
				throw getException(new Exception("param is null"));
			}
			params.put("conditionList", conditionList);
			this.executeDelete("IbatisCommonDao_deleteEqual", params);
		} catch (Exception e) {
			flag = false;
			throw getException(e);
		}
		return flag;
	}

	/**
	 * 自动判断增加或者修改
	 * 
	 * @param entity
	 * @throws BaseException
	 */
	public boolean saveOrUpdate(T entity) throws BaseException {
		boolean actSuccess = true;
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			ID id = this.getPrimaryKey(entity);
			if (id == null) {
				this.insert(entity);
			} else {
				this.update(entity);
			}
		} catch (Exception e) {
			actSuccess = false;
			throw getException(e);
		}
		return actSuccess;
	}

	/**
	 * 统计当前跟当前实体属性一致的记录数
	 * 
	 * @param entity
	 * @return
	 * @throws BaseException
	 */
	public Long countEqual(T entity) throws BaseException {
		try {
			if (entity == null) {
				throw new NullPointerException();
			}
			//Object flagValue = this.getPropertyValue(entity, this.flagName);
			//if (flagValue == null && !CommonUtil.isEmpty(this.flagName)) {// 过滤掉打了删除标记的记录
			//	this.setPropertyValue(entity, this.flagName, 0);
			//}
			initFromAnnotation((Class<T>) entity.getClass());
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("tableName", tableName);
			List<EqualObj> conditionList = getEqualList(entity);
			params.put("conditionList", conditionList);
			return this.queryForLong("IbatisCommonDao_countEqual", params);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	/**
	 * 统计属性一致的记录数
	 * 
	 * @param entityClass
	 * @return
	 */
	public Long countByProperty(Class<T> entityClass, String propertyName,
			Object PropertyValue) throws BaseException {
		try {
			T entity = entityClass.newInstance();
			this.setPropertyValue(entity, propertyName, PropertyValue);
			return this.countEqual(entity);
		} catch (Exception e) {
			throw getException(e);
		}
	}

	public List<T> queryForEntityList(Class<T> entityClass, String sql_id,
			Object params) throws BaseException {
		try {
			List list = this.queryForMapObjectList(sql_id, params);
			return this.getListInstanceFromListMap(entityClass, list);
		} catch (Exception e) {
			throw this.getException(e);
		}
	}
}