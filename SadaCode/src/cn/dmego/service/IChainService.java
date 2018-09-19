package cn.dmego.service;

import cn.dmego.domain.Asset;
import cn.dmego.domain.ChainMsg;

/**
 * @Name: IChainService
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月14日
 * 
 */
public interface IChainService {
	public static final String SERVICE_NAME = "cn.dmego.service.impl.ChainServiceImpl";

	String enroll(ChainMsg chainMsg);

	String construct(ChainMsg chainMsg);

	String reconstruct(ChainMsg chainMsg);

	String install(ChainMsg chainMsg);

	String instantiate(ChainMsg chainMsg);

	String invoke(ChainMsg chainMsg);

	String query(ChainMsg chainMsg);

	String upgrade(ChainMsg chainMsg);

	String reloadConfig(ChainMsg chainMsg);

	 
	    /**  
	    * @Name: save  
	    * @Description: TODO
		* @Author: 刘西宁
		* @Version: V1.00
		* @Parameters:
	    * @Return void    返回类型  
	    * @throws  
		* @Create Date:2018年6月19日 
	    */  
	    
	String save(Asset asset);

		 
		    /**  
		    * @Name: queryBlockInfo  
		    * @Description: TODO
			* @Author: 刘西宁
			* @Version: V1.00
			* @Parameters:
		    * @Return String    返回类型  
		    * @throws  
			* @Create Date:2018年6月26日 
		    */  
		    
	String queryBlockInfo(ChainMsg chainMsg);
	
}
