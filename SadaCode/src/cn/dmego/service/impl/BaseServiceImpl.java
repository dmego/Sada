package cn.dmego.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dmego.dao.ICommonDao;
import cn.dmego.pojo.PageInfo;
import cn.dmego.service.IBaseService;
import cn.dmego.utils.GenericsUtils;

@Service(IBaseService.SERVICE_NAME)
@Transactional(readOnly=true)
public class BaseServiceImpl<T> implements IBaseService<T>{
	
	/**泛型(T)转换*/
	@SuppressWarnings({ "unchecked" })
	Class<T> entityClass = GenericsUtils.getSuperClassGenricType(BaseServiceImpl.class, 0);

	/**注入基本Dao*/
	@Resource(name=ICommonDao.DAO_NAME)
	ICommonDao<T> commonDao;
	

	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public Serializable save(T entity) {
		return commonDao.save(entity);
	}

	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void update(T entity) {
		commonDao.update(entity);
		
	}

	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void delete(T entity) {
		commonDao.delete(entity);
		
	}

	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveOrupdate(Object entity) {
		commonDao.saveOrupdate(entity);
		
	}

	@Override
	public List<T> list(Class<T> clazz) {
		return commonDao.list(clazz);
	}
	
	@Override
	public T get(Class<T> clazz, Serializable id) {
		return commonDao.get(clazz, id);
	}

	@Override
	public T get(String hql) {
		return commonDao.get(hql);
	}

	@Override
	public T get(String hql, Map<String, Object> params) {
		return commonDao.get(hql, params);
	}

	@Override
	public List<T> find(String hql) {
		return commonDao.find(hql);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> findMap(String hql) {
		return commonDao.findMap(hql);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		return commonDao.find(hql, params);
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		return commonDao.find(hql, page, rows);
	}

	@Override
	public Long count(String hql) {
	    Long count = commonDao.count(hql);
	    if (count == null) {
            return (long) 0;
        }
        return count;
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		return commonDao.count(hql, params);
	}

	 public int executeHql(String hql) {

	        return commonDao.executeHql(hql);
	    }

	    public int executeHql(String hql, Map<String, Object> params) {
	        return commonDao.executeHql(hql, params);
	    }

	    public <T> List<T> getListByCriteria(DetachedCriteria criteria, PageInfo page) {

	        return commonDao.getListByCriteria(criteria, page);
	    }

	    public List<?> getListByCriteria(DetachedCriteria criteria, Integer startPage, Integer pageSize) {

	        return commonDao.getListByCriteria(criteria, startPage, pageSize);
	    }

	    public int getCountByCriteria(DetachedCriteria criteria) {

	        return commonDao.getCountByCriteria(criteria);
	    }

	    public boolean isExist(String hql, Map<String, Object> param) {

	        return count(hql, param) > 0;
	    }

	    public <T> List<T> findByCriteria(DetachedCriteria criteria) {

	        return commonDao.findByCriteria(criteria);

	    }
	
}
