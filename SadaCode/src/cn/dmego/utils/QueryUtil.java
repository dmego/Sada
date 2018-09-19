package cn.dmego.utils;

import cn.dmego.pojo.PageInfo;
import cn.dmego.exception.QueryException;
import cn.dmego.pojo.Column;
import cn.dmego.pojo.Query;
import cn.dmego.pojo.QueryCondition;
import cn.dmego.filter.ConditionOperator;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 query查询的工具类
 */
public class QueryUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(QueryUtil.class);

	/**
	 * 根据类名获取类
	 *
	 * @param className
	 *            类名
	 * @return 类
	 * @throws ClassNotFoundException
	 *             找不到类异常
	 */
	public static Class<?> getClassName(String className)
			throws ClassNotFoundException {
		Class<?> objClass = null;
		if (!StringUtils.isEmpty(className)) {
			objClass = Class.forName(className);
		}
		return objClass;

	}


	/*
	 * 获取分页信息
	 * 
	 * @param queryCondition 查询条件
	 * 
	 * @param query 查询配置
	 * 
	 * @return 分页信息
	 */
	public static PageInfo getPageInfo(QueryCondition queryCondition,
			Query query) {
		PageInfo pageInfo = new PageInfo();
		if (queryCondition.getPageInfo() == null) {
			pageInfo.setPageSize(query.getPagesize());
		} else {
			pageInfo = queryCondition.getPageInfo();
		}
		return pageInfo;
	}


	/**
	 * 获取排序配置,使用ID替换key 解决排序问题
	 *
	 * @param condition
	 *            查询条件（界面）
	 * @param query
	 *            查询配置
	 * @return 排序
	 */
	private static String getSortInfo(QueryCondition condition, Query query) {
		String sortInfo = !StringUtils.isEmpty(condition.getSortInfo()) ? condition
				.getSortInfo() : query.getOrder();
		if (StringUtils.isEmpty(sortInfo))
			return sortInfo;
		String[] arr = sortInfo.split(",");
		for (String str : arr) {
			String[] keyArr = str.split(" ");
			String key = keyArr[0].trim();
			String id = getColumnIdByKey(query, key);
			if (!StringUtils.isEmpty(id))
				sortInfo = sortInfo.replace(key, id);
		}
		return sortInfo;
	}

	private static String getColumnIdByKey(Query query, String key) {
		for (Column column : query.getColumnList()) {
			if (column.getKey().equals(key)) {
				return column.getId();
			}
		}
		return key;
	}
	
	/**
	 * 获取查询操作符
	 *
	 * @param map
	 *            前台参数
	 * @param column
	 *            列配置
	 * @return 操作符
	 */
	private static String getOperatorStr(Map map, Column column) {
		if (map.get("operator") != null
				&& !StringUtils.isEmpty(map.get("operator").toString())) {
			return map.get("operator").toString();
		}
		return column.getOperator();
	}

	/**
	 * 获取操作符
	 *
	 * @param operator
	 *            操作符字符串
	 * @return 操作符
	 */
	private static ConditionOperator getOperator(String operator) {
		return ConditionOperator.getOperator(operator);
	}

	/**
	 * 查询值是否为空
	 *
	 * @param value
	 *            值
	 * @return 是否为空
	 */
	private static boolean isNotEmptyValue(Object value) {
		if (value == null)
			return false;
		if (StringUtils.isEmpty(value.toString())) {
			return false;
		}
		if (value.toString().equals("%%") || value.toString().equals("%")
				|| value.toString().equals(","))
			return false;
		return true;
	}

	/**
	 * 不需要注入值的情况
	 *
	 * @param operator
	 *            操作符
	 * @return 是否需要值
	 */
	private static boolean isNeedValue(ConditionOperator operator) {
		if (operator.equals(ConditionOperator.NOT_NULL)
				|| operator.equals(ConditionOperator.NULL))
			return false;
		if (operator.equals(ConditionOperator.NOT_EXISTS)
				|| operator.equals(ConditionOperator.EXISTS))
			return false;
		return true;
	}

	/**
	 * 根据操作符来处理值，并返回结果
	 *
	 * @param operator
	 *            操作符
	 * @param value
	 *            值
	 * @return 处理后的值
	 * @throws Exception
	 *             JSonHelper.getValue异常
	 */
	public static Object getValueByOperator(ConditionOperator operator,
			Object value, Class clazz) throws Exception {
		if (operator.equals(ConditionOperator.BETWEEN)) {
			return getValueForBetween(value, clazz);
		} else if (operator.equals(ConditionOperator.IN)
				|| operator.equals(ConditionOperator.NOT_IN)) {
			return getValueForInOrNotIn(value, clazz);
		}
		return JSonHelper.getValue(clazz, value);
	}

	/**
	 * 处理between的注入值
	 *
	 * @param value
	 *            原来值
	 * @param clazz
	 *            类型
	 * @return 处理后的值
	 * @throws Exception
	 *             JSonHelper.getValue异常
	 */
	private static Object getValueForBetween(Object value, Class clazz)
			throws Exception {
		String[] values = value.toString().split(",");
		List list = new ArrayList();
		if (StringUtils.isEmpty(values[0]) && StringUtils.isEmpty(values[1])) {
			return null;
		}
		for (String s : values) {
			if (StringUtils.isEmpty(s)) {
				list.add(null);
			} else {
				list.add(JSonHelper.getValue(clazz, s));
			}
		}
		// 只选取了开始时间
		if (values.length == 1) {
			list.add(null);
		}
		return list;
	}

	/**
	 * 处理in not_in的注入值
	 *
	 * @param value
	 *            原来值
	 * @param clazz
	 *            类型
	 * @return 处理后的值
	 * @throws Exception
	 *             JSonHelper.getValue异常
	 */
	private static Object getValueForInOrNotIn(Object value, Class clazz)
			throws Exception {
		String[] values = value.toString().split(",");
		List list = new ArrayList();
		for (String s : values) {
			if (!StringUtils.isEmpty(s)) {
				list.add(JSonHelper.getValue(clazz, s));
			}
		}
		return list.isEmpty() ? null : list;
	}

	/**
	 * 日期查询的处理
	 *
	 * @param operator
	 *            操作符
	 * @param value
	 *            值
	 * @return map 处理后的值 operator返回操作符（日期操作 EQ 改为BETWEEN） value
	 */
	private static Map<String, Object> getValueForDate(
			ConditionOperator operator, Object value) throws Exception {
		Map<String, Object> map = new HashMap<>();
		if (!value.toString().contains(":")
				|| value.toString().contains("00:00:00")) {
			if (operator.equals(ConditionOperator.BETWEEN)) {
				List list = (List) value;
				if (list.get(1) != null) {
					Object nextValue = DateUtil.getLastMilliSecond(list.get(1));
					list.set(1, nextValue);
				}
				value = list;
			} else if ((operator.equals(ConditionOperator.EQ))
					|| (operator.equals(ConditionOperator.GT))
					|| (operator.equals(ConditionOperator.GE))) {
				Object nextValue = DateUtil.getLastMilliSecond(value);
				if (operator.equals(ConditionOperator.EQ)) {
					operator = ConditionOperator.BETWEEN;
					value = Arrays.asList(value, nextValue);
				} else {
					value = nextValue;
				}
			}
		}
		map.put("operator", operator);
		map.put("value", value);
		return map;
	}

	/**
	 * 查询条件值处理结果（值和操作符）
	 *
	 * @param operator
	 *            操作符
	 * @param value
	 *            值
	 * @return map 处理后的值 operator返回操作符（日期操作 EQ 改为BETWEEN） value
	 */
	private static Map<String, Object> getValueForOperator(
			ConditionOperator operator, Object value, Class clazz)
			throws QueryException {
		Object newValue;
		try {
			newValue = getValueByOperator(operator, value, clazz);
		} catch (Exception ex) {
			throw new QueryException("JSonHelper.getValue发生异常,转化的参数为："
					+ value.toString());
		}
		if (newValue == null)
			return null;
		// 对日期格式的处理
		if (Date.class.equals(clazz)
				|| Date.class.equals(clazz.getSuperclass())) {
			try {
				// 时间处理可能改变操作符 比如EQ 变成 BETWEEN
				Map<String, Object> date_map = getValueForDate(operator,
						newValue);
				newValue = date_map.get("value");
				operator = (ConditionOperator) date_map.get("operator");
			} catch (Exception ex) {
				throw new QueryException("DateUtil.getLastMilliSecond日期转化发生异常");
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("operator", operator);
		map.put("value", newValue);
		return map;
	}

	/**
	 * 获取服务器端注入的查询条件
	 *
	 * @param query
	 *            查询配置
	 * @return 服务器端拼接的查询条件
	 */
	private static List<Map<String, Object>> getServerConditions(Query query) {
		List<Column> collist = query.getColumnList();
		List<Map<String, Object>> serverConditions = new ArrayList<>();
		for (Column column : collist) {
			// 控制是否是服务器端查询条件的开关
			if (!column.getIsServerCondition()) {
				continue;
			}
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("value", column.getValue());
			conditionMap.put("operator", column.getOperator());
			conditionMap.put("key", column.getKey());
			serverConditions.add(conditionMap);
		}
		return serverConditions;
	}
	

	/**
	 * 拼接criteria离线查询条件
	 *
	 * @param condition
	 *            查询条件
	 * @param query
	 *            查询配置
	 * @param entityClazz
	 *            映射类
	 * @return 离线查询criteria
	 */
	public static DetachedCriteria generateCriteria(QueryCondition condition,
			Query query, Class entityClazz) throws QueryException {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClazz);

		// 排序处理
		String sortInfo = getSortInfo(condition, query);
		if (!StringUtils.isEmpty(sortInfo)) {
			if (query.getAllowPaging()
					&& !StringUtils.isEmpty(query.getSortKey())) {
				sortInfo = sortInfo + "," + query.getSortKey();
			}
			String[] strArr = sortInfo.split(",");
			for (String str : strArr) {
				String[] order_str = str.split(" ");
				if (order_str.length != 2) {
					throw new QueryException("排序" + str
							+ "的配置有问题,可能未配置asc或desc，请检查");
				}
				criteria = ObjectUtil.getCriteriaWithAlias(criteria,
						order_str[0]);
				if ("DESC".equals(order_str[1].toUpperCase())) {
					criteria.addOrder(Order.desc(order_str[0]));
				} else {
					// 默认为asc排序
					criteria.addOrder(Order.asc(order_str[0]));
				}
			}
		}

		// 查询条件注入
		List<Map<String, Object>> conditions = condition.getConditions();
		// 配置在json上的服务端查询条件
		conditions.addAll(getServerConditions(query));
		// 配置在数据权限上的查询条件
		// conditions.addAll(getFunctionFilterCondition(query));
		if (conditions.isEmpty()) {
			return criteria;
		}
		logger.debug("-------" + query.getId() + "的filter查询条件-----------");
		for (Map<String, Object> map : conditions) {
			String key = map.get("key").toString();
			Object value = map.get("value");
			Column column = query.getColumn(key);
			if (column == null) {
				column = new Column();
				column.setKey(key);
				if (map.get("classType") != null) {
					column.setClassType(map.get("classType").toString());
				}
				if (map.get("operator") != null
						&& StringUtils.isNotBlank(map.get("operator")
								.toString())) {
					column.setOperator(map.get("operator").toString());
				}
			}
			String operatorStr = getOperatorStr(map, column);
			logger.debug("condition:" + key + " " + operatorStr + " " + value);
			ConditionOperator operator = getOperator(operatorStr);
			// 类型处理
			Class clazz;
			try {
				clazz = JSonHelper.getClass(column.getClassType());
			} catch (Exception ex) {
				throw new QueryException("转化类【" + column.getClassType()
						+ "】出现异常！");
			}

			Object newValue = value;
			if (isNeedValue(operator) && isNotEmptyValue(value)) {
				// 值处理
				Map<String, Object> value_map = getValueForOperator(operator,
						value, clazz);
				if (value_map == null)
					continue;
				newValue = value_map.get("value");
				operator = (ConditionOperator) value_map.get("operator");
				criteria = ObjectUtil.getCriteriaWithAlias(criteria, key);
				criteria = operator.getCriteria(criteria, key, newValue);
			} else if (!isNeedValue(operator)) {
				criteria = ObjectUtil.getCriteriaWithAlias(criteria, key);
				criteria = operator.getCriteria(criteria, key, newValue);
			}
		}

		return criteria;
	}

	/**
	 * 根据不同的queryId 查询配置文件返回不同的query对象
	 * 
	 * @param queryId
	 * @return
	 */
	public static Query getQueryByQueryId(String jsontext, String queryId) {
		List<Query> querylist = JSON.parseArray(jsontext, Query.class);
		for (Query query : querylist) {
			String id = query.getId();
	  		if (id.equals(queryId)) {
				List<LinkedMap<String, Object>> listcolu = query.getColumn();
				List<Column> Listco = new ArrayList<Column>();
				Map<String, Column> mapclo = new LinkedMap<String, Column>();
				for (int i = 0; i < listcolu.size(); i++) {
					Map<String, Object> columnmap = listcolu.get(i);
					Column column = (Column) MapObjectUtil.map2Object(
							columnmap, Column.class);
					Listco.add(column);
					String key = column.getKey();
					mapclo.put(key, column);
				}
				query.setColumnList(Listco);
				query.setColumnMap(mapclo);
				return query;
			}

		}
		return null;
	}

}
