package cn.dmego.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.JstreeNode;
import cn.dmego.pojo.Result;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IPopedomService;
import cn.dmego.service.IRoleService;
import cn.dmego.service.IUserService;
import cn.dmego.utils.VauleUtil;

/**  
* @Name: RoleAction
* @Description: 角色管理Action类（继承基础Action）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-25（创建日期）
*/
@SuppressWarnings("serial")
@Controller("roleAction")
@Scope(value="prototype")
public class RoleAction extends BaseAction<Role>{

	Role role = this.getModel();
	
	/**注入角色Service*/
	@Resource(name=IRoleService.SERVICE_NAME)
	IRoleService roleService;
	
	/**注入权限Service*/
	@Resource(name=IPopedomService.SERVICE_NAME)
	IPopedomService authService;
	
	/**注入用户Service*/
	@Resource(name=IUserService.SERVICE_NAME)
	IUserService userService;
	
	/**  
	* @Name: home
	* @Description: 用户管理的首页显示
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-13（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/userIndex.jsp
	*/
	@RequiresPermissions("HOME_ROLE") //有访问角色主页权限才能执行该方法
	public String home(){
	
		return "home";
	}
	
	//保存或更新角色
	public String save(){
		System.out.println(role.toString());
		//获取权限字符串
		String auths = role.getAuths();
		//将权限字符串组织成Set<Popedom>集合
		Set<Popedom> popedoms = authService.getSetByStr(auths);
		role.setPopedoms(popedoms);
		roleService.saveOrupdate(role);
		Result result = new Result(true);
		VauleUtil.putValueStack(result);	
		return "jsonMsg";
	}
	
	public String checkCode(){
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		boolean flag = roleService.checkCodeByIdAndCode(id,code);
		if(flag == false){//如果存在该编码，返回 false,
			map.put("valid", false);
		}else{
			map.put("valid", true);
		}
		VauleUtil.putValueStack(map);
		return "jsonMsg";
	}
	
	
	public String getById(){
		String id = request.getParameter("id");
		Role role = this.getRole(id);
		VauleUtil.putValueStack(role);
		return "jsonMsg";
	}
	
	@SuppressWarnings("unchecked")
	public Role getRole(String id) {
        return (Role) roleService.get(Role.class, id);
    }
	
	@SuppressWarnings("unchecked")
	public String roleList(){
		List<Role> roles = roleService.list(Role.class);
		VauleUtil.putValueStack(roles);
		return "jsonMsg";
	}
	
	public String edit(){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return "edit";
	}
	
	
	@SuppressWarnings("unchecked")
	public String del(){
		String id = request.getParameter("id");
		Role role = (Role) userService.get(Role.class, id);
		Result result = null;
		try{
			//级联删除用户角色表
			roleService.delete(role);
			result = new Result(true);
		}catch(Exception e){
			result = new Result(false);
		}
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	
	
	//根据当前选择的角色动态生成权限树
	@SuppressWarnings("unchecked")
	public String authTree(){
		String id = request.getParameter("id");
		//获取当前登录的用户
		User user = (User) request.getSession().getAttribute("user");
		User JOUser = (User) userService.get(User.class, user.getId());
		Set<Role> roles = JOUser.getRoles();
		Role selectRole = new Role();
		String status = null;
		//如果id == 0，说明是新增角色操作
		if(id.equals("0")){
			status = "add";		
		}else{ //否则是修改角色操作
			status = "upd";
			//获取当前选择的角色
			selectRole = (Role) roleService.get(Role.class, id);
		}
		List<JstreeNode> jstreeNodes = authService.buildAuthJsTree(roles,selectRole,status);
		VauleUtil.putValueStack(jstreeNodes);
		return "jsonMsg";
	}
	
	//当前登录用户的角色与选中的角色进行比较,返回是否有权限操作
	@SuppressWarnings("unchecked")
	public String compRole(){
		//获取选中的角色
		String roleId = request.getParameter("id");
		Role selectRole = (Role) roleService.get(Role.class, roleId);
		//获取当前用户的Id
		User user = (User) request.getSession().getAttribute("user");
		User JOuser = (User) userService.get(User.class, user.getId());
		Set<Role> loginRoles = JOuser.getRoles();
		boolean flag = roleService.compRole(selectRole,loginRoles);
		Result result = new Result(flag);
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	
	 //判断当前选中的角色下是否有用户
	@SuppressWarnings("unchecked")
	public String hasUser(){
		//获取选中的角色
		String roleId = request.getParameter("id");
		Role selectRole = (Role) roleService.get(Role.class, roleId);
		Set<User> users = selectRole.getUsers();
		Result result = new Result();
		if(users != null && users.size() > 0 ){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
 	
	//通过角色id加载该角色的权限树
	public String listTree(){
		String roleId = request.getParameter("id");
		List<TreeNode> list = roleService.getTreeDataByRoleId(roleId);
		VauleUtil.putValueStack(list);
		return "jsonMsg";
	}
	
	
	
	
	public String roleAdd(){
		
		return "roleAdd";
	}

}
