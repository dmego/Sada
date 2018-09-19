package cn.dmego.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.dmego.dao.ICommonDao;
import cn.dmego.pojo.PageInfo;
import cn.dmego.utils.GenericsUtils;

/**  
* @Name: CommonImpl
* @Description: 公共Dao接口实现类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@Repository(ICommonDao.DAO_NAME)
public class CommonDaoImpl<T> extends HibernateDaoSupport implements ICommonDao<T>{

	/**泛型(T)转换*/
	@SuppressWarnings({ "unchecked" })
	Class<T> entityClass = GenericsUtils.getSuperClassGenricType(CommonDaoImpl.class, 0);
	//Class entityCla = TUtil.getActualType(this.getClass());
	
	@Resource(name="sessionFactory")
	public void setDi(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
    public Session getCurrentSession() {
        return this.getSessionFactory().getCurrentSession();
    }
	
	
	/**保存*/
	@Override
	public Serializable save(T entity) {
		Serializable save = this.getCurrentSession().save(entity);
		this.getCurrentSession().flush(); 
		return save;
	}
	
	/**更新*/
	@Override
	public void update(T entity) {
		this.getCurrentSession().update(entity);
		this.getCurrentSession().flush();
	}

	@Override
	public void delete(T entity) {
		this.getCurrentSession().delete(entity);
		this.getCurrentSession().flush();
	}
	
	@Override
	public void saveOrupdate(Object entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.getCurrentSession().flush();	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(Class<T> clazz) {
		 Criteria ct = this.getCurrentSession().createCriteria(clazz);
	      return ct.list();
	}

	@Override
	public T get(Class<T> clazz, Serializable id) {
		return (T) this.getCurrentSession().get(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get(String hql) {
	   Query query = this.getCurrentSession().createQuery(hql);
		List<T> ls = query.list();
        if (ls != null && ls.size() > 0) {
            return ls.get(0);
        }
        return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public T get(String hql, Map<String, Object> params) {
		Query query = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        List<T> ls = query.list();
        if (ls != null && ls.size() > 0) {
            return ls.get(0);
        }
        return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql) {
		 Query query = this.getCurrentSession().createQuery(hql);
	     return query.list();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> findMap(String hql) {
		 Query query = this.getCurrentSession().createQuery(hql);
	     query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
	     return query.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		 Query query = this.getCurrentSession().createQuery(hql);
	        if (params != null && !params.isEmpty()) {
	            for (String key : params.keySet()) {
	                Object obj = params.get(key);
	                if (obj instanceof Collection<?>) {
	                    query.setParameterList(key, (Collection<?>) obj);
	                } else if (obj instanceof Object[]) {
	                    query.setParameterList(key, (Object[]) obj);
	                } else {
	                    query.setParameter(key, obj);
	                }
	            }
	        }
	        return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, int page, int rows) {
		 Query query = this.getCurrentSession().createQuery(hql);
	     return query.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	@Override
	public Long count(String hql) {
		 Query query = this.getCurrentSession().createQuery(hql);
	     return (Long) query.uniqueResult();
	}
	@Override
	public Long count(String hql, Map<String, Object> params) {
		 Query query = this.getCurrentSession().createQuery(hql);
	        if (params != null && !params.isEmpty()) {
	            for (String key : params.keySet()) {
	                query.setParameter(key, params.get(key));
	            }
	        }
	     return (Long) query.uniqueResult();
	}
	
	public int executeHql(String hql) {

        Query query = this.getCurrentSession().createQuery(hql);
        return query.executeUpdate();
    }



    public int executeHql(String hql,Map<String,Object> params){
        Query query = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                Object obj = params.get(key);
                if (obj instanceof Collection<?>) {
                    query.setParameterList(key, (Collection<?>) obj);
                } else if (obj instanceof Object[]) {
                    query.setParameterList(key, (Object[]) obj);
                } else {
                    query.setParameter(key, obj);
                }
            }
        }
        return query.executeUpdate();
    }


    @SuppressWarnings("unchecked")
	public <T> List<T> getListByCriteria(DetachedCriteria criteria, PageInfo page) {

        if (page == null) {
            return criteria.getExecutableCriteria(getCurrentSession()).setProjection(null)
                    .setResultTransformer(CriteriaSpecification.ROOT_ENTITY).list();
        } else {
            return criteria.getExecutableCriteria(getCurrentSession()).setProjection(null)
                    .setResultTransformer(CriteriaSpecification.ROOT_ENTITY).setFirstResult((page.getPageNum() - 1) * page.getPageSize())
                    .setMaxResults(page.getPageSize()).list();
        }
    }

    public List<?> getListByCriteria(DetachedCriteria criteria, Integer startPage, Integer pageSize) {

        if (startPage != null && pageSize != null) {
            return criteria.getExecutableCriteria(getCurrentSession()).setProjection(null)
                    .setResultTransformer(CriteriaSpecification.ROOT_ENTITY).setFirstResult(startPage).setMaxResults(pageSize).list();
        } else {
            return criteria.getExecutableCriteria(getCurrentSession()).setProjection(null)
                    .setResultTransformer(CriteriaSpecification.ROOT_ENTITY).list();
        }
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> findByCriteria(DetachedCriteria criteria) {

        return criteria.getExecutableCriteria(getCurrentSession()).setProjection(null)
                .setResultTransformer(CriteriaSpecification.ROOT_ENTITY).list();
    }

    public int getCountByCriteria(DetachedCriteria criteria) {

        return ((Long) criteria.getExecutableCriteria(getCurrentSession()).setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }


}
