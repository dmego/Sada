package cn.dmego.service.impl;


import org.apache.commons.io.IOUtils;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.alibaba.fastjson.JSON;

import cn.dmego.domain.Dict;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.exception.QueryException;
import cn.dmego.pojo.PageInfo;
import cn.dmego.pojo.Query;
import cn.dmego.pojo.QueryCondition;
import cn.dmego.service.IDictService;
import cn.dmego.service.IQueryService;
import cn.dmego.utils.QueryUtil;

/**
 * @Name: UserSerciveImpl
 * @Description: 用户Service接口实现类
 * @Author: 曾凯（作者）
 * @Version: V1.00 （版本号）
 * @Create Date: 2018-04-13（创建日期）
 */
@SuppressWarnings("rawtypes")
@Service(IQueryService.SERVICE_NAME)
@Transactional(readOnly = true)
public class QueryServiceImpl extends BaseServiceImpl implements IQueryService {

	/** 注入数据字典Service */
	@Resource(name = IDictService.SERVICE_NAME)
	IDictService dictService;

	@Override
	public Map<String, Object> loadData(String reqObj) throws Exception {
		Map<String, Object> map = new HashMap<>(); // 用于接收返回数据(配置、分页、数据)
		QueryCondition queryCondition = JSON.parseObject(reqObj,QueryCondition.class);// 将请求的json数据转成对象
		// 获取配置文件
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream inputStream = cl.getResourceAsStream("query-res.json");
		String jsontext = IOUtils.toString(inputStream, "utf8");
		// 根据不同的queryId 查询配置文件返回不同的query配置对象
		Query query = null;
		if (queryCondition.getQueryId() != null) {
			query = QueryUtil.getQueryByQueryId(jsontext,queryCondition.getQueryId());
		}
		Class<?> objClass = QueryUtil.getClassName(query.getClassName());// 获取所属的类
		PageInfo pageInfo = QueryUtil.getPageInfo(queryCondition, query);// 分页信息
		List objList = getDataList(queryCondition, query, pageInfo, objClass,true); // 返回数据
		objList = this.giveValue(objList, objClass,queryCondition.getQueryId()); //根据不同业务需求 替换list中的元素
		
		map.put("query", query);
		map.put("pageInfo", pageInfo);
		map.put("rows", objList);
		return map;
	}

	/**
	 * 获取查询结果数据
	 *
	 * @param queryCondition
	 *            查询条件
	 * @param query
	 *            查询配置
	 * @param pageInfo
	 *            分页信息
	 * @param objClass
	 *            映射类
	 * @param isQuery
	 *            是否是查询 true=查询
	 * @return 返回的数据列表
	 * @throws QueryException
	 *             获取过程中的异常
	 */
	public List getDataList(QueryCondition queryCondition, Query query,
			PageInfo pageInfo, Class objClass, boolean isQuery)
			throws QueryException {
		List objList = null;
		if (objClass != null) {
			DetachedCriteria criteria = QueryUtil.generateCriteria(
					queryCondition, query, objClass);
			pageInfo.setCount(this.getCountByCriteria(criteria));
			if (query.getAllowPaging() && isQuery)
				objList = this.getListByCriteria(criteria, pageInfo);
			else
				objList = this.findByCriteria(criteria);
		}
		return objList;
	}

	// 根据不同业务需求，替换list中不同的元素
	@SuppressWarnings("unchecked")
	public List giveValue(List objList, Class<?> className,String queryId) {
		
		if (className.equals(User.class)) {
			List<User> userList = new ArrayList<User>();
			//先获取性别的字典集合
			List<Dict> sexList = dictService.getDictListByParentCode("SEX");
			List<Dict> orgList = dictService.getDictListByParentCode("ORG");
			for (Object object : objList) {
				User user = (User) object;
				for (Dict dict : sexList) {//遍历并替换性别成名称
					if(dict.getId().equals(user.getSexId())){
						user.setSexId(dict.getName());
					}
				}
				for (Dict dict : orgList) {//遍历并替换组织单位成名称
					if(dict.getId().equals(user.getOrgId())){
						user.setOrgId(dict.getName());
					}
				}
				userList.add(user);
			}
			objList = userList;
			return objList;
		} else if (className.equals(Role.class)) {
			//如果是查询的是用户角色表信息（ 已经绑定该角色的用户）
			if(queryId.equals("userRole_selected_list")){
				if(objList != null && objList.size()>0){
					Role role = (Role)objList.get(0);
					//获取持久化对象
					Role JORole = (Role) this.get(Role.class, role.getId());
					Set<User> users = JORole.getUsers(); 
					List<User> userList = new ArrayList<>(users); //将set集合转为list集合
					return userList;
				}
			}

			return objList;
		}
		return null;
	}

}
