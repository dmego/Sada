package cn.dmego.dao;

import cn.dmego.domain.User;

/**  
* @Name: IUserDao
* @Description: 用户Dao接口类（继承公共Dao接口类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
public interface IUserDao extends ICommonDao<User>{
	
	public static final String DAO_NAME = "cn.dmego.dao.impl.UserDaoImpl";
		
}
