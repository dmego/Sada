package cn.dmego.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.dmego.domain.Dict;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.Result;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IAssetService;
import cn.dmego.service.IDictService;
import cn.dmego.service.IRoleService;
import cn.dmego.service.IUserService;
import cn.dmego.utils.DateUtil;
import cn.dmego.utils.Encrypt;
import cn.dmego.utils.StringUtil;
import cn.dmego.utils.VauleUtil;

/**
 * @Name: UserAction
 * @Description: 用户Action类（继承基础Action）
 * @Author: 曾凯（作者）
 * @Version: V1.00 （版本号）
 * @Create Date: 2018-04-13（创建日期）
 */
@SuppressWarnings("serial")
@Controller("userAction")
@Scope(value = "prototype")
public class UserAction extends BaseAction<User> {

	User user = this.getModel();

	/** 注入用户Service */
	@Resource(name = IUserService.SERVICE_NAME)
	IUserService userService;

	/** 注入角色Service */
	@Resource(name = IRoleService.SERVICE_NAME)
	IRoleService roleService;

	/** 注入数据字典Service */
	@Resource(name = IDictService.SERVICE_NAME)
	IDictService dictService;

	/** 注入数据字典Service */
	@Resource(name = IAssetService.SERVICE_NAME)
	IAssetService assetService;
	
	/**
	 * @Name: home
	 * @Description: 用户管理的首页显示
	 * @Author: 曾凯（作者）
	 * @Version: V1.00 （版本号）
	 * @Create Date: 2018-04-13（创建日期）
	 * @Parameters: 无
	 * @Return: String：跳转到system/userIndex.jsp
	 */

	public String home() {
		return "home";
	}

	/*
	 * 
	 * 个人中心
	 */
	public String my(){
//		User user = (User) ActionContext.getContext().getSession().get("user");
//		user.setDbirthday(DateUtil.stampToStr(user.getBirthday()));
//		Dict dict = (Dict) dictService.get(Dict.class, user.getSexId());
//		user.setSexName(dict.getName());
//		
//		HashMap<String, Integer> assetStatisticMap = assetService.getStatistic(user.getId());
//		Map request = (Map) ActionContext.getContext().get("request");
//		request.put("assetStatisticPhoto", assetStatisticMap.get("图片"));
//		request.put("assetStatisticDoc", assetStatisticMap.get("文档"));
//		request.put("assetStatisticVideo", assetStatisticMap.get("视频"));
////		String count = assetService.getCount();
////		ActionContext.getContext().getSession().put("userCount", count);
		return "my";
	}
	
	@SuppressWarnings("unchecked")
	public String userList() {
		List<User> users = userService.list(User.class);
		VauleUtil.putValueStack(users);
		return "jsonMsg";
	}

	public String edit() {
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return "edit";
	}

	public String avatar() {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		return "avatar";
	}

	public String getAvatar() {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		String path = userService.getAvatarByUserId(userId);
		VauleUtil.putValueStack(path);
		return "jsonMsg";
	}

	@SuppressWarnings("unchecked")
	public String save() {
		System.out.println(user.toString());
		// 更新性别ID
		Dict dict = dictService.getDictByCode(user.getSexCode());
		user.setSexId(dict.getId());
		// 出生日期字符串转时间戳
		String birthday = DateUtil.strToStamp(user.getDbirthday());
		user.setBirthday(birthday);
		// 设置默认头像
		user.setUserPic("/upload/admin/avatar.png");		
		// 根据roleName查询出角色,
		Role role = roleService.getRoleByCode(user.getRoleName());
		// 级联保存
		user.getRoles().add(role);
		//如果组织邀请码不为空
		if(user.getInvitation() != null){
			Dict dictOrg = (Dict) dictService.get(Dict.class, user.getOrgId());
			dictOrg.setValue(user.getInvitation());
			dictService.update(dictOrg);
		}
			
		// 如果用户没有ID,新增用户
		if (StringUtils.isBlank(user.getId())) {
			// 生成10个随机数作为盐值
			String salt = StringUtil.getRandomString(10);
			user.setSalt(salt);
			//使用MD5+盐值对密码进行加密
			user.setPassword(Encrypt.md5(user.getPassword(),salt)); 
			// 创建日期
			user.setCreatetime(DateUtil.nowDateToStamp());
			// 保存用户
			String userid = userService.save(user).toString();
			// 用户权限控制
			// userRoleService.setRoleForRegisterUser(userId);
			// 上传头像
			// userService.updateUserAvatar();
		} else { // 更新用户数据
			//先获取到数据库中的原用户数据
			 User oldUser=this.getUser(user.getId());			 
			 //将 user 对象中的数据拷贝到oldUser中去,忽略password与salt属性
			 BeanUtils.copyProperties(user,oldUser,"password","salt");
			 //如果用户密码改变了，此时更新密码
			 if(!oldUser.getPassword().equals(user.getPassword())){
				 //重新生成盐值
				 String salt = StringUtil.getRandomString(10);
				 oldUser.setSalt(salt);
				 oldUser.setPassword(Encrypt.md5(user.getPassword(),salt));
			 }
			 //更新日期
			 oldUser.setUpdatetime(DateUtil.nowDateToStamp());
			 //将oldUser 存入数据库中
			 userService.update(oldUser);
		}
		Result result = new Result(true);
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
		

	// 检查昵称是否唯一
	public String checkNick() {
		String nickName = request.getParameter("nickName");
		String userId = request.getParameter("userId");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		boolean flag = userService.checkNickByIdAndNick(userId, nickName);
		if (flag == false) {// 如果存在该昵称，返回 false,
			map.put("valid", false);
		} else {
			map.put("valid", true);
		}
		VauleUtil.putValueStack(map);
		return "jsonMsg";
	}
	
	// 检查姓名是否唯一
	public String checkName() {
		String userName = request.getParameter("userName");
		String userId = request.getParameter("userId");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		boolean flag = userService.checkNameByIdAndName(userId, userName);	
		if (flag == false) {// 如果存在该昵称，返回 false,
			map.put("valid", false);
		} else {
			map.put("valid", true);
		}
		VauleUtil.putValueStack(map);
		return "jsonMsg";
	}
	
	//检查新注册的用户名是否重复
	public String checkRegistName() {
		String userName = request.getParameter("userName");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		boolean flag = userService.checkName(userName);	
		if (flag == false) {// 如果存在该名称，返回 false,
			map.put("valid", false);
		} else {
			map.put("valid", true);
		}
		VauleUtil.putValueStack(map);
		return "jsonMsg";
	}
	
	public String pagelist() {
		String id = request.getParameter("id");
		request.setAttribute("selectId", id);
		return "pagelist";
	}

	@SuppressWarnings("unchecked")
	public String getById() {
		String id = request.getParameter("id");
		User user = this.getUser(id);
		if(user.getSexId() != null){
			Dict dictSex = (Dict) dictService.get(Dict.class, user.getSexId());	
			user.setSexCode(dictSex.getCode());
		}
		if(user.getOrgId() != null){
			Dict dictOrg = (Dict) dictService.get(Dict.class, user.getOrgId());
			user.setOrgName(dictOrg.getName());
			user.setInvitation(dictOrg.getValue());
		}
		user.setDbirthday(user.getBirthday());		
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			user.setRoleName(role.getCode());
			if(role.getCode().equals("USER")){
				user.setUserIs("1");
			}else if(role.getCode().equals("ORGADMIN")){
				user.setUserIs("0");
			}
		}
		VauleUtil.putValueStack(user);
		return "jsonMsg";
	}
	
	@SuppressWarnings("unchecked")
	public String joinOrg(){
		Result result = null;
		String id = request.getParameter("id");
		String InviteCode = request.getParameter("InviteCode");
		//先查询有没有该邀请码的组织,没有返回 fasle,有则加入组织,然后返回true
		Dict dictOrg = dictService.getOrgByInviteCode(InviteCode);
		if(dictOrg != null){ //如果存在该邀请码的组织
			User user = (User) userService.get(User.class, id);
			user.setOrgId(dictOrg.getId());
			userService.update(user);
			result = new Result(true);
			result.setMessage(dictOrg.getId());
			result.setData(dictOrg.getName());
		}else{
			result = new Result(false);
		}
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	
	
	

	@SuppressWarnings("unchecked")
	public String del() {
		String id = request.getParameter("id");
		User user = (User) userService.get(User.class, id);
		Result result = null;
		try {
			// 级联删除用户角色表
			userService.delete(user);
			result = new Result(true);
		} catch (Exception e) {
			e.printStackTrace();
			result = new Result(false);
		}
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}

	@SuppressWarnings("unchecked")
	public User getUser(String id) {
		return (User) userService.get(User.class, id);
	}

	// 根据当前用户的角色动态加载角色下拉框
	// 权限机制:高级管理员有管理员管理权限,但是只能新增超级管理员及以下角色的用户
	@SuppressWarnings("unchecked")
	public String getUserRole() {
		// 获取当前用户的Id
		User user = (User) request.getSession().getAttribute("user");
		User JOuser = (User) userService.get(User.class, user.getId());
		Set<Role> roles = JOuser.getRoles();
		List<Role> roleList = roleService.list(Role.class);
		// 先从角色List中删除普通用户角色
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getCode().equals("USER")) {
				roleList.remove(i);
			}
		}
		for (Role role : roles) {
			if (role.getCode().equals("SUADMIN")) { // 当前用户有超级管理员角色
				VauleUtil.putValueStack(roleList);
			} else if (role.getCode().equals("SEADMIN")) {// 当前用户有高级管理员角色
				// 从角色List中删除超级管理员角色
				for (int i = 0; i < roleList.size(); i++) {
					if (roleList.get(i).getCode().equals("SUADMIN")) {
						roleList.remove(i);
					}
				}
				VauleUtil.putValueStack(roleList);
			}
		}
		return "jsonMsg";
	}
	
	// 当前登录用户与选中的用户具有的角色进行比较,返回是否有权限操作
	@SuppressWarnings("unchecked")
	public String compRole() {
		// 获取选中用户所具有的角色
		String userId = request.getParameter("id");
		User selectUser = (User) roleService.get(User.class, userId);
		Set<Role> selectRoles = selectUser.getRoles();
		// 获取当前用户的Id
		User user = (User) request.getSession().getAttribute("user");
		User JOuser = (User) userService.get(User.class, user.getId());
		Set<Role> loginRoles = JOuser.getRoles();

		boolean flag = userService.compRole(selectRoles, loginRoles);
		Result result = new Result(flag);
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	public String adminHome() {
		return "adminHome";
	}

/********************************************************************************************
	 2018-6-15号添加的方法
	 Author:曾凯
*********************************************************************************************/
	
	//构造组织单位bootstrap-treeview格式数据并返回
	public String OrgTreeData() {
		List<TreeNode> list = dictService.findOrgList();
		//将数据字典数组压入值栈,通过struts2的json工具将数组转json数组
		VauleUtil.putValueStack(list);
		return "jsonMsg";
	}
	
	/**
	 * 用户管理界面的角色选择
	 * 有两个角色可供选择
	 * 普通用户---USER
	 * 组织管理者---ORGADMIN
	 * @return
	 */
	public String getApUserRole() {
		List<Role> roleList = roleService.list(Role.class);
		List<Role> userRoleList = new ArrayList<Role>();
		for (int i = 0; i < roleList.size(); i++) {
			String code = roleList.get(i).getCode();
			String id = roleList.get(i).getId();
			if (code.equals("USER") || code.equals("ORGADMIN") ) {
				Role role = (Role) roleService.get(Role.class, id);
				userRoleList.add(role);
			}
		}
		VauleUtil.putValueStack(userRoleList);
		return "jsonMsg";
	}

	  
	
	
	
	
	//-----------liuxn 添加 begin
	
	public String mailActivate() throws Exception {
		
//		String resp = userService.mailActivate(user);
//		request.setAttribute("resp", resp);
		return "mailActivate";
	}	
	
	//-----------liuxn 添加 end
	
}
