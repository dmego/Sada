package cn.dmego.dao.impl;

import org.springframework.stereotype.Repository;

import cn.dmego.dao.IPopedomDao;
import cn.dmego.domain.Popedom;


/**  
* @Name: IPopedomDao
* @Description: 权限Dao接口实现类（继承公共Dao接口实现类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/

@SuppressWarnings("unchecked")
@Repository(IPopedomDao.DAO_NAME)
public class PopedomDaoImpl extends CommonDaoImpl<Popedom> implements IPopedomDao {

}
