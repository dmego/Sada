package cn.dmego.service;

import java.util.List;
import java.util.Set;

import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;

/**  
* @Name: IUserService
* @Description: 用户Service接口类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@SuppressWarnings("rawtypes")
public interface IUserService extends IBaseService{
	public static final String SERVICE_NAME = "cn.dmego.service.impl.UserServiceImpl";

	User findUserByUserName(String username);

	String getAvatarByUserId(String userId);

	boolean checkNickByIdAndNick(String userId, String nickName);
	
	boolean checkNameByIdAndName(String userId, String userName);

	List<Popedom> findAuthListByUserId(String id);

	boolean compRole(Set<Role> selectRoles, Set<Role> loginRoles);

	boolean checkName(String userName);

	
}
