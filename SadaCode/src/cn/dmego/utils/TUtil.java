package cn.dmego.utils;

import java.lang.reflect.ParameterizedType;

public class TUtil {
	/**  
	* @Name: getActualType
	* @Description: 泛型转换
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-13（创建日期）
	* @Parameters: Class: 
	* @Return: Class:
	*/
	@SuppressWarnings("rawtypes")
	public static Class getActualType(Class entity) {
		ParameterizedType parameterizedType = (ParameterizedType) entity.getGenericSuperclass();
		Class entityClass = (Class) parameterizedType.getActualTypeArguments()[0];
		return entityClass;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
