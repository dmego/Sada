package cn.dmego.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.dmego.domain.Dict;
import cn.dmego.dao.IDictDao;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IDictService;
import cn.dmego.utils.TreeUtil;

/**
 * @Name: UserSerciveImpl
 * @Description: 数据字典Service接口实现类
 * @Author: 曾凯（作者）
 * @Version: V1.00 （版本号）
 * @Create Date: 2018-04-13（创建日期）
 */
@SuppressWarnings("rawtypes")
@Service(IDictService.SERVICE_NAME)
@Transactional(readOnly = true)
public class DictServiceImpl extends BaseServiceImpl implements IDictService {

	/** 数据字典表Dao */
	@Resource(name = IDictDao.DAO_NAME)
	IDictDao dictDao;

	// 资产模板信息map
	private static Map<String, String> templateMap = new HashMap<String, String>();
	// 方法映射信息map
	private static Map<String, String> methodMap = new HashMap<String, String>();

	/**
	 * @Name: findDictionaryListByKeyword
	 * @Description: 通过数字字典的keyword,查询符合条件的列表
	 * @Author: 曾凯（作者）
	 * @Version: V1.00 （版本号）
	 * @Create Date: 2018-04-24（创建日期）
	 * @Parameters: 无
	 * @Return: List<Dictionary>：符合条件的列表
	 */
	public List<Dict> findDictionaryListByKeyword(String keyword) {

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode> findAllDictList() {
		List<TreeNode> tnlist = null;
		String hql = "from Dict order by levelCode asc";
		List<Dict> dicts = (List<Dict>) this.find(hql);
		Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();

		for (Dict dict : dicts) {
			TreeNode node = new TreeNode();
			node.setText(dict.getName());
			node.setId(dict.getId());
			node.setParentId(dict.getParentId());
			node.setLevelCode(dict.getLevelCode());
			nodelist.put(node.getId(), node);
		}
		// 构造树形结构
		tnlist = TreeUtil.getNodeList(nodelist);
		return tnlist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode> findOrgList() {
		// 先查询出组织单位的id值
		Dict orgDict = getDictByCode("ORG");
		String orgId = orgDict.getId();
		// 然后查询出以组织单位id值为父id的所有单位
		List<TreeNode> tnlist = null;
		String hql = "from Dict where parentId= '" + orgId + "' order by levelCode asc";
		List<Dict> dicts = (List<Dict>) this.find(hql);
		Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
		for (Dict dict : dicts) {
			TreeNode node = new TreeNode();
			node.setText(dict.getName());
			node.setId(dict.getId());
			node.setParentId(dict.getParentId());
			node.setLevelCode(dict.getLevelCode());
			nodelist.put(node.getId(), node);
		}
		// 构造树形结构
		tnlist = TreeUtil.getOrgNodeList(nodelist);
		return tnlist;
	}
	
	@Override
	public Dict getOrgByInviteCode(String inviteCode) {
		// 先查询出组织单位的id值
		Dict orgDict = getDictByCode("ORG");
		String orgId = orgDict.getId();
		// 然后查询出以组织单位id值为父id的所有单位
		List<TreeNode> tnlist = null;
		String hql = "from Dict where parentId= '" + orgId + "' order by levelCode asc";
		List<Dict> dicts = (List<Dict>) this.find(hql);
		for (Dict dict : dicts) {
			if(dict.getValue().equals(inviteCode)){//如果存在该邀请码的组织,返回该组织
				return dict;
			}
		}
		return null;
	}
	

	@Override
	public Dict getDictByCode(String code) {
		String hql = "from Dict where code= '" + code + "'";
		return dictDao.get(hql);
	}

	@Override
	public boolean checkCodeByIdAndCode(String id, String code) {
		String hql = "from Dict where code= '" + code + "'";
		Dict dict1 = dictDao.get(hql);

		Dict dict2 = dictDao.get(Dict.class, id);
		// 如果不存在该code的dict,或者该code是编辑的该字典原本的code
		if (dict1 == null || (dict2 != null && dict2.getCode().equals(code))) {
			return true; // 可以使用该code
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dict> getDictListByParentCode(String code) {
		Dict dictParent = getDictByCode(code);
		String parentId = dictParent.getId();
		String hql = "from Dict where parentId= '" + parentId + "'";
		List<Dict> dicts = (List<Dict>) this.find(hql);
		return dicts;
	}

	@Override
	public List<Dict> getAssetTypeList() {
		return dictDao.findByParentName("资产类型");

	}


}
