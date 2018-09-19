package cn.dmego.web.shiro;

import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.service.IPopedomService;
import cn.dmego.service.IRoleService;
import cn.dmego.service.IUserService;
import cn.dmego.utils.DateUtil;
import cn.dmego.utils.Encrypt;

/**
 * @Name: UserRealm
 * @Description: 自定义realm
 * @Author: 曾凯（作者）
 * @Version: V1.00 （版本号）
 * @Create Date: 2018-05-06（创建日期）
 */
public class UserRealm extends AuthorizingRealm {

	/** 注入用户Service */
	@Resource(name = IUserService.SERVICE_NAME)
	IUserService userService;

	/** 注入角色Service */
	@Resource(name = IRoleService.SERVICE_NAME)
	IRoleService roleService;

	/** 注入权限Service */
	@Resource(name = IPopedomService.SERVICE_NAME)
	IPopedomService authService;

	// 设置realm的名称
	public void setName(String name) {
		super.setName("userRealm");
	}

	/**
	 * @Name: doGetAuthenticationInfo
	 * @Description: 用于用户认证的方法
	 * @Author: 曾凯（作者）
	 * @Version: V1.00 （版本号）
	 * @Create Date: 2018-04-13（创建日期）
	 * @Parameters: token:用户输入的用户名和密码
	 * @Return:
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		// token 是用户输入的用户密与密码
		if (authcToken == null)
			throw new AuthenticationException("parameter token is null");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		{
			User user = userService.findUserByUserName(token.getUsername());
			if(user == null){
				throw new UnknownAccountException();	
			}
			// 对密码进行MD5加密
			String password = String.copyValueOf(token.getPassword());
			String md5password = Encrypt.md5(password, user.getSalt());
			
			if (user.getDeleted().equals("1")) {// 用户被禁用
				throw new DisabledAccountException();
			}
			if (!md5password.equals(user.getPassword())) {// 校验密码是否一致
				throw new IncorrectCredentialsException();
			}
			// 这样前端页面可取到数据
			// 获取用户所拥有的角色code集合
			Set<String> roleSortSet = roleService.getRoleSortSetByUserId(user.getId());
			for (String sort : roleSortSet) {
				int isort  = Integer.parseInt(sort);
				if(isort == 1){user.setRoleSort(1);}
				else if(isort == 2){user.setRoleSort(2);}
				else{user.setRoleSort(3);}
			}
			user.setLogintime(DateUtil.getCurrDateTimeStr());// 写入登录时间
			SecurityUtils.getSubject().getSession().setAttribute("user", user);
			SecurityUtils.getSubject().getSession().setAttribute("roleSort", user.getRoleSort());
			SecurityUtils.getSubject().getSession()
					.setAttribute("userId", user.getId());
			// 注意此处的返回值没有使用加盐方式,如需要加盐，可以在密码参数上加
			return new SimpleAuthenticationInfo(user.getId(),
					token.getPassword(), token.getUsername());
		}
	}

	/**
	 * @Name: doGetAuthorizationInfo
	 * @Description: 用于用户授权的方法
	 * 
	 *               授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用 shiro 权限控制有三种 1、通过xml配置资源的权限
	 *               2、通过shiro标签控制权限 3、通过shiro注解控制权限
	 *
	 * @Author: 曾凯（作者）
	 * @Version: V1.00 （版本号）
	 * @Create Date: 2018-04-13（创建日期）
	 * @Parameters: principals: 主身份信息
	 * @Return:
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// 因为非正常退出，即没有显式调用 SecurityUtils.getSubject().logout()
		// (可能是关闭浏览器，或超时)，但此时缓存依旧存在(principals)，所以会自己跑到授权方法里。
		if (!SecurityUtils.getSubject().isAuthenticated()) {
			doClearCache(principals);
			SecurityUtils.getSubject().logout();
			return null;
		}
		if (principals == null) {
			throw new AuthorizationException("parameters principals is null");
		}
		// 获取已认证的用户名（登录名）
		String userId = (String) super.getAvailablePrincipal(principals);
		if (StringUtils.isEmpty(userId)) {
			return null;
		}
		// 获取用户所拥有的角色code集合
		Set<String> roleCodeSet = roleService.getRoleCodeSetByUserId(userId);
		// 获取用户所拥护的权限code集合
		Set<String> authCodeSet = authService.getAuthCodeSetByUserId(userId);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(roleCodeSet);
		authorizationInfo.setStringPermissions(authCodeSet);
		return authorizationInfo;
	}

	// 清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject()
				.getPrincipals();
		super.clearCache(principals);
	}

}
