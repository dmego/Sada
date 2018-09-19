package cn.dmego.service;

import java.util.List;
import java.util.Map;

import cn.dmego.domain.Dict;
import cn.dmego.pojo.TreeNode;

/**  
* @Name: IDictionaryService
* @Description: 数据字典Service接口类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-24（创建日期）
*/
@SuppressWarnings("rawtypes")
public interface IDictService extends IBaseService{
	public static final String SERVICE_NAME = "cn.dmego.service.impl.DictServiceImpl";

	List<TreeNode> findAllDictList();

	Dict getDictByCode(String code);

	boolean checkCodeByIdAndCode(String id, String code);

	List<Dict> getAssetTypeList();

	List<TreeNode> findOrgList();
	
	List<Dict> getDictListByParentCode(String code);

	Dict getOrgByInviteCode(String inviteCode);

}
