package cn.dmego.dao;

import java.util.LinkedHashMap;
import java.util.List;

import cn.dmego.domain.Tag;

public interface ITagDao extends ICommonDao<Tag> {

	public static final String DAO_NAME = "cn.dmego.dao.impl.TagDaoImpl";

	/**
	 * @Name: findByParam
	 * @Description: TODO
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return Tag 返回类型
	 * @throws @Create
	 *             Date:2018年5月4日
	 */

	public Tag findByParam(String name, String userId);


}
