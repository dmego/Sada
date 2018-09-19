package cn.dmego.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.type.Type;

import cn.dmego.pojo.PageInfo;

public interface IBaseService<T> {
	public static final String SERVICE_NAME = "cn.dmego.service.impl.BaseServiceImpl";
	Serializable save(T entity);

	void update(T entity);

    void delete(T entity);

	void saveOrupdate(Object entity);	

	List<T> list(Class<T> clazz);
	
	T get(Class<T> clazz, Serializable id);
	
	T get(String hql);
	
	T get(String hql, Map<String, Object> params);
	
	List<T> find(String hql);
	
	@SuppressWarnings("rawtypes")
	List<Map> findMap(String hql);
	
	List<T> find(String hql, Map<String, Object> params);
	
	List<T> find(String hql, int page, int rows);
	
	Long count(String hql);
	 
    Long count(String hql, Map<String, Object> params);
  
    /**
     * 执行hql语句（可带事务）
     *
     * @param hql 查询语句
     * @return
     */
    int executeHql(String hql);
    
    int executeHql(String hql, Map<String, Object> params);
 
    <T> List<T> getListByCriteria(DetachedCriteria criteria, PageInfo page);

    List<?> getListByCriteria(DetachedCriteria criteria, Integer startPage, Integer pageSize);

    int getCountByCriteria(DetachedCriteria criteria);

    <T> List<T> findByCriteria(DetachedCriteria criteria);

}
