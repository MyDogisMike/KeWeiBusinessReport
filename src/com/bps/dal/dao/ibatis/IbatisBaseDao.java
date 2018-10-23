package com.bps.dal.dao.ibatis;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.bps.exception.BaseException;
import com.bps.exception.DaoException;
import com.bps.util.CommonUtil;

/**
 * ibatis操作公共类
 * 
 * @author andalee
 * 
 */
@SuppressWarnings("unchecked")
public abstract class IbatisBaseDao extends SqlMapClientDaoSupport {
	/**
	 * 获取异常信息
	 * 
	 * @param e
	 * @return
	 */
	protected BaseException getException(Exception e) {
		BaseException new_e = null;
		if (BaseException.class.isInstance(e)) {
			new_e = (BaseException) e;
			e.printStackTrace();
		} else {
			new_e = new DaoException("数据库异常", e.getMessage());
			e.printStackTrace();
		}
		if (CommonUtil.isEmpty(new_e.getLayer())) {
			new_e.setLayer("DAO层");
		}
		return new_e;
	}

	/**
	 * map转化为小写形式
	 * 
	 * @param inMap
	 * @return
	 */
	private Map<String, Object> mapToLowerCase(Map<String, Object> inMap) {
		return CommonUtil.mapToLowerCase(inMap);
	}

	/**
	 * 根据参数
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected List queryForList(String sql_id, Object params) {
		return getSqlMapClientTemplate().queryForList(sql_id, params);
	}

	/**
	 * 根据sql取得当前的
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected List queryForLowerList(String sql_id, Object params) {
		List result_list = this.queryForList(sql_id, params);
		if (result_list != null) {
			for (int i = 0; i < result_list.size(); i++) {
				Object obj = result_list.get(i);
				if (obj != null) {
					if (obj instanceof Map) {
						Map<String, Object> result_map = (Map<String, Object>) obj;
						result_list.set(i, mapToLowerCase(result_map));
					} else {// 其他情况不做处理
						break;
					}
				}
			}
		}
		return result_list;
	}

	/**
	 * 取得list<map>的小写对象
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected List<Map<String, Object>> queryForLowerMapObjectList(
			String sql_id, Object params) {
		return (List<Map<String, Object>>) queryForLowerList(sql_id, params);
	}

	/**
	 * 取得list<map>对象
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected List<Map<String, Object>> queryForMapObjectList(String sql_id,
			Object params) {
		return (List<Map<String, Object>>) queryForList(sql_id, params);
	}

	/**
	 * 查询并取得object对象
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected Object queryForObject(String sql_id, Object params) {
		return getSqlMapClientTemplate().queryForObject(sql_id, params);
	}

	/**
	 * 查询并取得Integer对象
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected Integer queryForInteger(String sql_id, Object params) {
		Object res = queryForObject(sql_id, params);
		return (Integer) res;
	}

	/**
	 * 查询并取得Long对象
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected Long queryForLong(String sql_id, Object params) {
		Object res = queryForObject(sql_id, params);
		return (Long) res;
	}

	/**
	 * 查询并取得Float对象
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected Float queryForFloat(String sql_id, Object params) {
		Object res = queryForObject(sql_id, params);
		return (Float) res;
	}

	/**
	 * 查询并取得Map对象
	 * 
	 * @param sql_id
	 * @param params
	 * @return
	 */
	protected Map<String, Object> queryForMapObject(String sql_id, Object params) {
		Object res = queryForObject(sql_id, params);
		return (Map<String, Object>) res;
	}

	/**
	 * 运行插入语句
	 * 
	 * @param sql_id
	 * @param params
	 */
	protected Object executeInsert(String sql_id, Object params) {
		return getSqlMapClientTemplate().insert(sql_id, params);
	}

	/**
	 * 运行更新语句
	 * 
	 * @param sql_id
	 * @param params
	 */
	protected void executeUpdate(String sql_id, Object params) {
		getSqlMapClientTemplate().update(sql_id, params);
	}

	/**
	 * 运行删除语句
	 * 
	 * @param sql_id
	 * @param params
	 */
	protected void executeDelete(String sql_id, Object params) {
		getSqlMapClientTemplate().delete(sql_id, params);
	}
    
	public static void main(String[] args) {
	}
}