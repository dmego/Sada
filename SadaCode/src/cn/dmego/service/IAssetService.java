package cn.dmego.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dmego.domain.Asset;
import cn.dmego.domain.Dict;
import cn.dmego.domain.Tag;
import cn.dmego.pojo.TreeNode;

/**
 * @ClassName: IAssetService
 * @Description: 资产Service接口
 * @author liuxining
 * @date 2018年4月16日
 * 
 */

public interface IAssetService {
	public static final String SERVICE_NAME = "cn.dmego.service.impl.AssetServiceImpl";

	/**
	 * @Name: save
	 * @Description: TODO
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return void 返回类型
	 * @throws @Create
	 *             Date:2018年4月19日
	 */

	public String save(Asset asset);

	/**
	 * 
	 * @param userId
	 * @param userId
	 * @Name: getAssetByParam
	 * @Description: 根据参数查询资产列表
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return List<Asset> 返回类型
	 * @throws @Create
	 *             Date:2018年4月26日
	 */
	public Map<String, Object> getAssetByParam(String type, String tag, String name, String userId, int page);

	/**
	 * @Name: getAssetById
	 * @Description: 根据id查询资产信息
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return String 返回类型
	 * @throws @Create
	 *             Date:2018年4月28日
	 */

	public Asset getAssetById(String id);

	/**
	 * @Name: getUserTag
	 * @Description: TODO
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return List<Tag> 返回类型
	 * @throws @Create
	 *             Date:2018年5月4日
	 */

	List<Tag> getUserTag(String userId);

	/**
	 * @Name: deleteAsset
	 * @Description: TODO
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return void 返回类型
	 * @throws @Create
	 *             Date:2018年5月10日
	 */

	public String deleteAsset(String id);

	/**  
	    * @Name: findTreeByUserId  
	    * @Description: 根据用户Id返回所有的资产树
		* @Author: 曾凯
		* @Version: V1.00
		* @Parameters:
	    * @Return List<TreeNode>   返回类型  
	    * @throws  
		* @Create Date:2018年6月17日 
	    */ 
	public List<TreeNode> findTreeByUserId(String id);

	/**
	 * @Name: compareKeyWord
	 * @Description: TODO
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return String 返回类型
	 * @throws @Create
	 *             Date:2018年6月17日
	 */

	public HashMap<String, String> compareKeyInfo(String nKeyInfo, String chainKeyInfo, String assetType);

	 
	    /**  
	    * @Name: getKeyInfo  
	    * @Description: 获取资产文件关键信息
		* @Author: 刘西宁
		* @Version: V1.00
		* @Parameters:
	    * @Return Map<String,String>    返回类型  
	    * @throws  
		* @Create Date:2018年6月17日 
	    */  
	    
	public Map<String, String> getKeyInfo(String fileName, String code);

		 
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
		    
	public List<Asset> getAll();
	
	public HashMap<String,Integer> getStatistic(String userId);

}
