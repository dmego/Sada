package cn.dmego.dao;

import java.util.List;

import cn.dmego.domain.Asset;

/**
 * @Name: IAssetDao
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年4月19日
 * 
 */
public interface IAssetDao extends ICommonDao<Asset> {
	String DAO_NAME = "cn.dmego.dao.impl.AssetDaoImpl";

	Asset getById(String id);

	Long getCountByParam(String whereStr, List<String> params);

	List<Asset> getListByParam(String whereStr, List<String> params, Integer start, Integer size, String orderBy);

	 
	    /**  
	    * @Name: getAll  
	    * @Description: TODO
		* @Author: 刘西宁
		* @Version: V1.00
		* @Parameters:
	    * @Return List<Asset>    返回类型  
	    * @throws  
		* @Create Date:2018年6月19日 
	    */  
	    
	List<Asset> getAll();

}
