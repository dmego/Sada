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

import cn.dmego.dao.IDictDao;
import cn.dmego.domain.Dict;


/**  
* @Name: DictionaryDaoImpl
* @Description: 数据子弹Dao接口实现类（继承公共Dao接口实现类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-24（创建日期）
*/

@SuppressWarnings("unchecked")
@Repository(IDictDao.DAO_NAME)
public class DictDaoImpl extends CommonDaoImpl<Dict> implements IDictDao {

	/**  
	* @Name: findDictionaryListByDistinct
	* @Description: 查询数据字典,去掉重复值
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-24（创建日期）
	* @Parameters: 无
	* @Return: String：List<Dict>:存放数据字典的集合
	*/
	public List<Dict> findDictionaryListByDistinct() {
		//返回的List集合
		List<Dict> dictList = new ArrayList<Dict>();
		//使用hql 语句直接将投影查询的字段放置到对象中
		String hql = "select distinct new cn.dmego.domain.Dict(o.keyword) from Dict o";
		dictList = (List<Dict>) this.getHibernateTemplate().find(hql);
		return dictList;
	}

	

	@Override
	public List<Dict> findByParentName(final String parentName) {
		return getHibernateTemplate().execute(new HibernateCallback<List<Dict>>() {

			@Override
			public List<Dict> doInHibernate(Session session) throws HibernateException {
				String hql = " FROM Dict where parentId in (select id from Dict where name=?)";
				Query query = session.createQuery(hql);
				query.setParameter(0, parentName);
				return query.list();
				
			}
		});
	}



	@Override
	public Dict getByCode(final String code) {
		return getHibernateTemplate().execute(new HibernateCallback<Dict>() {

			@Override
			public Dict doInHibernate(Session session) throws HibernateException {
				String hql = " FROM Dict where code=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, code);
				return (Dict) query.uniqueResult();
				
			}
		});
	}
	
}
