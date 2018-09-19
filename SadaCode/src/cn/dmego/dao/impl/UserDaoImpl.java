package cn.dmego.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.dmego.dao.IUserDao;
import cn.dmego.domain.User;

/**  
* @Name: UserDao
* @Description: 用户Dao接口实现类（继承公共Dao接口实现类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/

@Repository(IUserDao.DAO_NAME)
public class UserDaoImpl extends CommonDaoImpl<User> implements IUserDao {
	

}
