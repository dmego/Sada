package cn.dmego.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.JstreeNode;
import cn.dmego.pojo.TreeNode;

/**  
* @Name: IPopedomService
* @Description: 权限Service接口类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-25（创建日期）
*/
@SuppressWarnings("rawtypes")
public interface IPopedomService extends IBaseService{
	public static final String SERVICE_NAME = "cn.dmego.service.impl.PopedomServiceImpl";

	List<TreeNode> findAllAuthList();

	boolean checkCodeByIdAndCode(String id, String code);

	Popedom getAuthsByCode(String code);

	List<JstreeNode> buildAuthJsTree(Set<Role> roles,Role selectRole,String status);

	Set<Popedom> getSetByStr(String auths);

	Set<String> getAuthCodeSetByUserId(String userId);

	List<Popedom> getAuthSetByUserId(String id);

}
