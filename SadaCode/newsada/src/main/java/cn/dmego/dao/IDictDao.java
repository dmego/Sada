package cn.dmego.dao;
import java.util.LinkedHashMap;
import java.util.List;

import cn.dmego.domain.Dict;

/**  
* @Name: IDictionaryDao
* @Description: 数据字典Dao接口类（继承公共Dao接口类）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-24（创建日期）
*/
public interface IDictDao extends ICommonDao<Dict>{
	
	public static final String DAO_NAME = "cn.dmego.dao.impl.DictDaoImpl";

	List<Dict> findByParentName(String string);

	Dict getByCode(String code);
	
}
