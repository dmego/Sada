package cn.dmego.service.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dmego.dao.IRoleDao;
import cn.dmego.dao.IUserDao;
import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IRoleService;
import cn.dmego.utils.StringUtil;
import cn.dmego.utils.TreeUtil;

/**
 * @Name: UserSerciveImpl
 * @Description: 用户Service接口实现类
 * @Author: 曾凯（作者）
 * @Version: V1.00 （版本号）
 * @Create Date: 2018-04-13（创建日期）
 */
@SuppressWarnings("rawtypes")
@Service(IRoleService.SERVICE_NAME)
@Transactional(readOnly = true)
public class RoleServiceImpl extends BaseServiceImpl implements IRoleService {

	/** 角色表Dao */
	@Resource(name = IRoleDao.DAO_NAME)
	IRoleDao roleDao;

	/**
	 * 根据角色code查询出角色
	 */
	public Role getRoleByCode(String code) {
		String hql = "from Role where code= '" + code + "'";
		return roleDao.get(hql);
	}

	@Override
	public boolean checkCodeByIdAndCode(String id, String code) {
		String hql = "from Role where code= '" + code + "'";
		Role role1 = roleDao.get(hql);
		Role role2 = roleDao.get(Role.class, id);
		// 如果不存在该code的role1,或者该code是编辑的该字典原本的code
		if (role1 == null || (role2 != null && role2.getCode().equals(code))) {
			return true; // 可以使用该code
		}
		return false;
	}

	// 当前登录用户的角色与选中的角色进行比较,返回是否有权限操作
	@Override
	public boolean compRole(Role selectRole, Set<Role> loginRoles) {
		int selectSort = StringUtil.StrToInt(selectRole.getSort());
		int loginSort = 0;
		for (Role role : loginRoles) {
			int temp = StringUtil.StrToInt(role.getSort());
			if (temp > loginSort) {
				loginSort = temp;
			}
		}
		// 如果当前用户角色权限比选中用户的角色大或相等
		if (loginSort >= selectSort) {
			return true;
		}
		return false;

	}

	//通过角色id加载该角色的权限树
	@SuppressWarnings("unchecked")
	public List<TreeNode> getTreeDataByRoleId(String roleId) {
		Role JORole = (Role) this.get(Role.class, roleId);
		Set<Popedom> popedoms = JORole.getPopedoms(); //该角色的权限集合
		Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
		for (Popedom popedom : popedoms) {
			TreeNode node = new TreeNode();
            node.setText(popedom.getName());
            node.setId(popedom.getId());
            node.setParentId(popedom.getParentId());
            node.setLevelCode(popedom.getLevelCode());
            node.setIcon(popedom.getIcon());
            nodelist.put(node.getId(), node);
		}
		 // 构造树形结构
        return TreeUtil.getNodeList(nodelist);
	}

	
	//通过用户ID 返回用户所具有的角色code 集合
	@SuppressWarnings("unchecked")
	public Set<String> getRoleCodeSetByUserId(String userId) {
		User JOUser = (User) this.get(User.class, userId);
		Set<Role> roles = JOUser.getRoles();
		Set<String> roleCodeSet = new HashSet<String>();
		for (Role role : roles) {
			roleCodeSet.add(role.getCode());
		}
		return roleCodeSet;
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> getRoleSortSetByUserId(String userId) {
		User JOUser = (User) this.get(User.class, userId);
		Set<Role> roles = JOUser.getRoles();
		Set<String> roleSortSet = new HashSet<String>();
		for (Role role : roles) {
			roleSortSet.add(role.getSort());
		}
		return roleSortSet;
	}
	
	
	
}
