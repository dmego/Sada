package cn.dmego.service;

import java.util.Map;


/**  
* @Name: IQueryService
* @Description: 用户Service接口类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@SuppressWarnings("rawtypes")
public interface IQueryService extends IBaseService{
	public static final String SERVICE_NAME = "cn.dmego.service.impl.QueryServiceImpl";

	Map<String, Object> loadData(String reqObj) throws Exception;
}
