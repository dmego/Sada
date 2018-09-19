package cn.dmego.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dmego.dao.IDictDao;
import cn.dmego.dao.IUserDao;
import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.Result;
import cn.dmego.service.IUserService;
import cn.dmego.utils.StringUtil;

/**  
* @Name: UserSerciveImpl
* @Description: 用户Service接口实现类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@SuppressWarnings("rawtypes")
@Service(IUserService.SERVICE_NAME)
@Transactional(readOnly=true)
public class UserServiceImpl extends BaseServiceImpl implements IUserService{
	
	/**注入用户表Dao*/
	@Resource(name=IUserDao.DAO_NAME)
	IUserDao userDao;
	
	/**注入数据字典表Dao*/
	@Resource(name=IDictDao.DAO_NAME)
	IDictDao dictionaryDao;

	/**  
	* @Name: findUserByUserName
	* @Description: 根据用户名查找用户
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-25（创建日期）
	* @Parameters: name:用户名
	* @Return: User:返回用户对象
	*/
	public User findUserByUserName(String username) {
		String hql = "from User where name='" + username + "'";
		User user = (User) this.get(hql);
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAvatarByUserId(String userId) {
		 Map<String,Object> params=new HashMap<String,Object>();
	     params.put("id",userId);
	     User user = (User) this.get(User.class, userId);
	     return user.getUserPic();
	}
	
	@Override
	public boolean checkNickByIdAndNick(String userId, String nickName) {
		String hql = "from User where nickName = '" + nickName + "'";
		User user1 = userDao.get(hql);
		User user2 = userDao.get(User.class, userId);
		if(user1 == null || (user2 != null && user2.getNickName().equals(nickName))){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkNameByIdAndName(String userId, String userName) {
		String hql = "from User where name = '" + userName + "'";
		User user1 = userDao.get(hql);
		User user2 = userDao.get(User.class, userId);
		if(user1 == null || (user2 != null && user2.getName().equals(userName))){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkName(String userName) {
		String hql = "from User where name = '" + userName + "'";
		User user = userDao.get(hql);
		if(user == null){
			return true;
		}
		return false;
	}
	
	//根据用户ID 取出用户所拥有的菜单
	@SuppressWarnings("unchecked")
	@Override
	public List<Popedom> findAuthListByUserId(String id) {
		//先根据用户ID取出用户
		User user = (User) this.get(User.class, id);
		Set<Role> roles = user.getRoles();
		Hashtable<String, String> ht = new Hashtable<String, String>();
		List<Popedom> popedomList = new ArrayList<Popedom>();
		if(roles != null && roles.size()>0){
			for (Role role : roles) {
				//一个用户可以对应多个角色
				ht.put(role.getId(), role.getName());
				//根据角色ID查询出该角色拥有的权限
				Set<Popedom> popedoms = role.getPopedoms();
				if(popedoms != null && popedoms.size()>0){
					for (Popedom popedom : popedoms) {
						popedomList.add(popedom);
					}
				}
				
			}
		}
		return popedomList;
	}
	
	/**
	 * 当前登录用户与选中的用户具有的角色进行比较,返回是否有权限操作
	 */
	public boolean compRole(Set<Role> selectRoles, Set<Role> loginRoles) {
		int selectSort = 0;
		for (Role role : selectRoles) {
			int temp = StringUtil.StrToInt(role.getSort());
			if(temp > selectSort){
				selectSort = temp;
			}
		}
		int loginSort = 0;
		for (Role role : loginRoles) {
			int temp = StringUtil.StrToInt(role.getSort());
			if(temp > loginSort){
				loginSort = temp;
			}
		}
		//如果当前用户角色权限比选中用户的角色大或相等
		if(loginSort >= selectSort){
			return true;
		}
		return false;
	}
}
