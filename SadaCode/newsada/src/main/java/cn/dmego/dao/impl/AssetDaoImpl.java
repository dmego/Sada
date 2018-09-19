package cn.dmego.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.dmego.dao.IAssetDao;

import cn.dmego.domain.Asset;
import cn.dmego.service.IAssetService;

/**
 * @Name: AssetDaoImpl
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年4月19日
 * 
 */
@Repository(IAssetDao.DAO_NAME)
public class AssetDaoImpl extends CommonDaoImpl<Asset> implements IAssetDao {

	@Override
	public Asset getById(final String id) {
		return getHibernateTemplate().execute(new HibernateCallback<Asset>() {

			@Override
			public Asset doInHibernate(Session session) throws HibernateException {
				String hql = "from Asset where id=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, id);
				return (Asset) query.uniqueResult();

			}
		});
	}

	@Override
	public Long getCountByParam(final String whereStr, final List<String> params) {
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				String hql = "select count(*) from Asset where 1=1 " + whereStr;
				Query query = session.createQuery(hql);
				for (int i = 0; i < params.size(); i++) {
					query.setParameter(i, params.get(i));
				}
				return (Long) query.uniqueResult();
			}
		});
	}

	@Override
	public List<Asset> getListByParam(final String whereStr, final List<String> params, final Integer start,
			final Integer size,final String orderBy) {
		return getHibernateTemplate().execute(new HibernateCallback<List<Asset>>() {

			@Override
			public List<Asset> doInHibernate(Session session) throws HibernateException {
				String hql = "from Asset where 1=1 " + whereStr;
				if(orderBy != null && orderBy.length() > 0){
					hql += " order by " + orderBy;
				}
				Query query = session.createQuery(hql);
				for (int i = 0; i < params.size(); i++) {
					query.setParameter(i, params.get(i));
				}
				if(start != null && size != null){
					query.setFirstResult(start);
					query.setMaxResults(size);
				}
				return query.list();

			}
		});
	}

	  
	@Override
	public List<Asset> getAll() {
		return getHibernateTemplate().execute(new HibernateCallback<List<Asset>>() {

			@Override
			public List<Asset> doInHibernate(Session session) throws HibernateException {
				String hql = "from Asset";
				Query query = session.createQuery(hql);
				return query.list();
			}
		});
	}

	  

}
