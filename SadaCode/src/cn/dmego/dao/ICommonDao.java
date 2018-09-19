package cn.dmego.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;

import cn.dmego.pojo.PageInfo;
/**  
* @Name: ICommonDao
* @Description: 公共Dao接口类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
public interface ICommonDao<T> {
	public static final String DAO_NAME = "cn.dmego.dao.impl.CommonDaoImpl";
	
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
