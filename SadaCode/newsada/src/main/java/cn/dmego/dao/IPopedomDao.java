package cn.dmego.dao;

import cn.dmego.domain.Popedom;
import cn.dmego.domain.User;

/**  
* @Name: IPopedomDao
* @Description: 权限Dao接口类（继承公共Dao接口类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
public interface IPopedomDao extends ICommonDao<Popedom>{
	
	public static final String DAO_NAME = "cn.dmego.dao.impl.PopedomDaoImpl";
		
}
