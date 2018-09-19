package cn.dmego.dao.impl;

import org.springframework.stereotype.Repository;

import cn.dmego.dao.IRoleDao;

import cn.dmego.domain.Role;


/**  
* @Name: UserDao
* @Description: 用户Dao接口实现类（继承公共Dao接口实现类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/

@Repository(IRoleDao.DAO_NAME)
public class RoleDaoImpl extends CommonDaoImpl<Role> implements IRoleDao {

}
