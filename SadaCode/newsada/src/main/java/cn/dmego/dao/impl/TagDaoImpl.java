package cn.dmego.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.dmego.dao.ITagDao;
import cn.dmego.domain.Tag;


@SuppressWarnings("unchecked")
@Repository(ITagDao.DAO_NAME)
public class TagDaoImpl extends CommonDaoImpl<Tag> implements ITagDao {

	  
	    /* (非 Javadoc)  
	    *   
	    *   
	    * @param name
	    * @param userId
	    * @return  
	    * @see cn.dmego.dao.ITagDao#findByParam(java.lang.String, java.lang.String)  
	    */  
	    
	@Override
	public Tag findByParam(final String name, final String userId) {
		return getHibernateTemplate().execute(new HibernateCallback<Tag>() {

			@Override
			public Tag doInHibernate(Session session) throws HibernateException {
				String hql = "from Tag where name=? and userId=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, name);
				query.setParameter(1, userId);
				return (Tag) query.uniqueResult();
			}
		});
	}
	
}
