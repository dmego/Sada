package cn.dmego.web.action;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.Result;
import cn.dmego.service.IPopedomService;
import cn.dmego.service.IRoleService;
import cn.dmego.service.IUserService;
import cn.dmego.utils.DateUtil;
import cn.dmego.utils.Encrypt;
import cn.dmego.utils.LoginUtil;
import cn.dmego.utils.SecurityUtil;
import cn.dmego.utils.StringUtil;
import cn.dmego.utils.VauleUtil;
import cn.dmego.web.form.MenuForm;
import cn.dmego.web.shiro.UserRealm;
/**  
* @Name: menuAction
* @Description: 菜单Action类（继承基础Action）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@SuppressWarnings("serial")
@Controller("menuAction")
@Scope(value="prototype")
public class MenuAction extends BaseAction<MenuForm>{
	MenuForm menuForm = this.getModel();
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuAction.class);
	/**注入用户Service*/
	@Resource(name=IUserService.SERVICE_NAME)
	IUserService userService;
	
	/**注入角色Service*/
	@Resource(name=IRoleService.SERVICE_NAME)
	IRoleService roleService;
	
	/**注入权限Service*/
	@Resource(name=IPopedomService.SERVICE_NAME)
	IPopedomService authService;
	
	@Autowired
	private UserRealm userRealm;
	
	
	/**  
	* @Name: home
	* @Description: 跳转到系统的首页
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-05 （创建日期）
	* @Parameters: 无
	* @Return: String：跳转到menu/home.jsp
	*/
	public String home(){
		
		return "home";
	}
	
	//控制台
	public String control(){
		
		return "control";
	}
	
	//系统首页
	public String index(){
		
		return "index";
	}
	
	/**  
	* @throws Exception 
	 * @Name: login
	* @Description: 系统登录
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-05 （创建日期）
	* @Parameters: 无
	* @Return: String：跳转到menu/login.jsp
	*/
	@SuppressWarnings("unchecked")
	public String login(){
		//已经登录过，直接跳转到主页
		Subject subject = SecurityUtils.getSubject();
		if(subject != null && subject.isAuthenticated()){
			Object authorized = subject.getSession().getAttribute("isAuthorized");
			if(authorized != null && Boolean.valueOf(authorized.toString())){
				return "index"; //跳转到主页
			}
		}	
		//获取到用户名和密码
		String username = menuForm.getUsername();
		String password = menuForm.getPassword();
		if(StringUtils.isEmpty(username)){
			return "login";
		}
		
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		//token.setRememberMe(true);//记住我
		subject = SecurityUtils.getSubject();
		String msg;
		try {
			subject.login(token);//当这一代码执行时，就会自动跳入到userRealm中认证方法
			//通过认证
			if(subject.isAuthenticated()){
				String userId = subject.getPrincipal().toString();
				User JOUser = (User) userService.get(User.class, userId);
				Set<Role> roles = JOUser.getRoles();
				if(!roles.isEmpty()){ //如果该用户分配了角色
					subject.getSession().setAttribute("isAuthorized", true);
					/**记住我*/
					LoginUtil.remeberMe(username,password,request,response);
					return "index";
				}else{//没有授权
					request.setAttribute("msg", 1);
					subject.getSession().setAttribute("isAuthorized", false);
                    LOGGER.error("用户未分配角色，没有权限");
					return "loginError";
				}
			}else{
				return "login";
			}
			
		}catch (IncorrectCredentialsException e) {
			request.setAttribute("msg", "登录密码错误");
            LOGGER.error("登录密码错误");
		} catch (DisabledAccountException e) {
            msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";
            request.setAttribute("msg", "帐号已被禁用");
            LOGGER.error(msg);
        } catch (UnknownAccountException e) {
            msg = "帐号不存在. There is no user with username of " + token.getPrincipal();
            request.setAttribute("msg", "帐号不存在");
            LOGGER.error(msg);
        } catch (UnauthorizedException e) {
            msg = "您没有得到相应的授权！" + e.getMessage();
            request.setAttribute("msg", "您没有得到相应的授权");
            LOGGER.error(msg);
        }
        return "login";
	
	}
	
	//依据当前登录用户的角色，动态加载菜单
	public String showMenu(){
		User user = SecurityUtil.getUser();
		List<Popedom> menuList = authService.getAuthSetByUserId(user.getId());
		VauleUtil.putValueStack(menuList);
		return "jsonMsg";
	}
	
	/**  
	* @Name: regist
	* @Description: 注册用户
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-05 （创建日期）
	* @Parameters: 无
	* @Return: String：跳转到menu/register.jsp
	*/
	@SuppressWarnings("unchecked")
	public String regist(){
		System.out.println(menuForm.toString());
		User user = new User();
		String username = menuForm.getRegistname();
		String password = menuForm.getRegistpass1();
		user.setName(username);
		user.setPassword(password);
		//设置默认昵称
		user.setNickName("新用户");
		//设置非管理员
		user.setAdminIs("0");
		// 设置默认头像
		user.setUserPic("/upload/admin/avatar.png");
		//从登陆页面注册的用户默认角色为普通用户
		// 根据roleName查询出角色,
		Role role = roleService.getRoleByCode("USER");
		// 级联保存
		user.getRoles().add(role);
		// 生成10个随机数作为盐值
		String salt = StringUtil.getRandomString(10);
		user.setSalt(salt);
		//使用MD5+盐值对密码进行加密
		user.setPassword(Encrypt.md5(user.getPassword(),salt)); 
		// 创建日期
		user.setCreatetime(DateUtil.nowDateToStamp());
		// 保存用户
		String userid = userService.save(user).toString();
		//注册成功后跳转到首页，并显示恭喜你，注册成功
		if(userid != null){
			UsernamePasswordToken token = new UsernamePasswordToken(username,password);
			Subject subject = SecurityUtils.getSubject();
			subject = SecurityUtils.getSubject();
			try {
				subject.login(token);//当这一代码执行时，就会自动跳入到userRealm中认证方法
				//通过认证
				if(subject.isAuthenticated()){
					String userId = subject.getPrincipal().toString();
					User JOUser = (User) userService.get(User.class, userId);
					Set<Role> roles = JOUser.getRoles();
					if(!roles.isEmpty()){ //如果该用户分配了角色
						subject.getSession().setAttribute("isAuthorized", true);
						subject.getSession().setAttribute("msg", "msg");
						return "index";
					}else{//没有授权
						request.setAttribute("msg", 1);
						subject.getSession().setAttribute("isAuthorized", false);
	                    LOGGER.error("用户未分配角色，没有权限");
						return "loginError";
					}
				}else{
					return "login";
				}	
			}catch (Exception e) {
				e.printStackTrace();
	        }
	        return "login";
		}
		return "login";
	}
	
	/**  
	* @Name: logout
	* @Description: 退出登录,跳转到登录页面
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-25 （创建日期）
	* @Parameters: 无
	* @Return: String：重定到index.jsp
	*/
	//--------2018-6-7-----------------
	/*
	 *暂停使用该方法，退出注销使用 过滤器 
	 * 在shiro 中配置
	 */
	//---------------------------------
	public String logout(){
		//清空所有Session
		 Subject subject = SecurityUtils.getSubject();
	     subject.logout();
		return "redirect:/login";
	}
		
	//清除ehcache缓存
	public String wipecache(){
		Result result = new Result();
		try {
			userRealm.clearCached();
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
		
}
