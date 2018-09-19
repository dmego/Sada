package cn.dmego.service;

import java.util.List;
import java.util.Set;

import cn.dmego.domain.Role;
import cn.dmego.pojo.TreeNode;

/**  
* @Name: IRoleService
* @Description: 角色Service接口类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@SuppressWarnings("rawtypes")
public interface IRoleService extends IBaseService {
	public static final String SERVICE_NAME = "cn.dmego.service.impl.RoleServiceImpl";

	Role getRoleByCode(String code);

	boolean checkCodeByIdAndCode(String id, String code);

	boolean compRole(Role selectRole, Set<Role> loginRoles);

	List<TreeNode> getTreeDataByRoleId(String roleId);

	Set<String> getRoleCodeSetByUserId(String userId);
	
	Set<String> getRoleSortSetByUserId(String userId);
}
