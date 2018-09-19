package cn.dmego.dao;

import cn.dmego.domain.Role;
/**  
* @Name: IRoleDao
* @Description: 角色Dao接口类（继承公共Dao接口类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-25（创建日期）
*/
public interface IRoleDao extends ICommonDao<Role>{
	public static final String DAO_NAME = "cn.dmego.dao.impl.RoleDaoImpl";
		
}
