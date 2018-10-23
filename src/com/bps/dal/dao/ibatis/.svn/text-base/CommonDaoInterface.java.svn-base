package com.bps.dal.dao.ibatis;

import java.util.List;

import com.bps.bean.GridObj;
import com.bps.bean.OrderObj;
import com.bps.bean.PageObj;
import com.bps.exception.BaseException;
/**
 * 
 * @author andalee   20170331
 *
 * @param <T>
 * @param <ID>
 */
public interface CommonDaoInterface<T, ID> {
	T load(Class<T> entityClass, ID id) throws BaseException;

	T load(T entity) throws BaseException;

	T loadEqual(T entity) throws BaseException;

	T findById(Class<T> entityClass, ID id) throws BaseException;

	List<T> findByExample(T entity) throws BaseException;

	List<T> findByExample(T entity, OrderObj order) throws BaseException;

	List<T> findByProperty(Class<T> entityClass, String propertyName,
			Object propertyValue) throws BaseException;

	GridObj findByPage(T entity, PageObj page, OrderObj order)
			throws BaseException;

	List<T> findAll(Class<T> entityClass) throws BaseException;

	Long insert(T entity) throws BaseException;

	boolean update(T entity) throws BaseException;

	boolean updateIgnoreNull(T entity) throws BaseException;

	boolean delete(Class<T> entityClass, ID id) throws BaseException;

	boolean delete(T entity) throws BaseException;

	boolean deleteByFlag(Class<T> entityClass, ID id) throws BaseException;

	boolean deleteEqual(T entity) throws BaseException;

	boolean saveOrUpdate(T entity) throws BaseException;

	Long countEqual(T entity) throws BaseException;

	Long countByProperty(Class<T> entityClass, String propertyName,
			Object PropertyValue) throws BaseException;

}
