package cn.dmego.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dmego.dao.IAssetDao;
import cn.dmego.dao.IDictDao;
import cn.dmego.dao.ITagDao;
import cn.dmego.domain.Asset;
import cn.dmego.domain.ChainMsg;
import cn.dmego.domain.Dict;
import cn.dmego.domain.Tag;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IAssetService;
import cn.dmego.utils.AssetIdentifyUtil;
import cn.dmego.utils.ChainMutualUtil;
import cn.dmego.utils.DateUtil;
import cn.dmego.utils.InfoPage;
import cn.dmego.utils.PagingBean;
import cn.dmego.utils.StringUtil;
import cn.dmego.utils.SysConstant;
import cn.dmego.utils.TreeUtil;

/**
 * @Name: AssetServlceImpl
 * @Description: 资产Service实现类
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年4月18日
 * 
 */
@SuppressWarnings("rawtypes")
@Service(IAssetService.SERVICE_NAME)
@Transactional(readOnly = true)
public class AssetServiceImpl extends BaseServiceImpl implements IAssetService {
	@Resource(name = IAssetDao.DAO_NAME)
	IAssetDao assetDao;
	@Resource(name = ITagDao.DAO_NAME)
	ITagDao tagDao;

	/** 数据字典表Dao */
	@Resource(name = IDictDao.DAO_NAME)
	IDictDao dictDao;

	// 资产模板信息map
	public static Map<String, String> templateMap = new HashMap<String, String>();
	// 方法映射信息map
	public static Map<String, String> methodMap = new HashMap<String, String>();

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public String save(Asset asset) {
		String tag = asset.getTag();
		if (tag != null && tag.length() > 0) {
			String[] tagsp = tag.split(" ");
			for (String tagi : tagsp) {
				if (tagi == null || tagi.length() == 0) {
					continue;
				}
				Tag tagBean = tagDao.findByParam(tagi, asset.getUser().getId());
				if (tagBean != null) {
					tagBean.setNum(tagBean.getNum() + 1);
					tagBean.setUpdateDate(DateUtil.nowDateToStamp());
					tagDao.update(tagBean);
				} else {
					tagBean = new Tag();
					tagBean.setName(tagi);
					tagBean.setNum(1);
					tagBean.setCreateDate(DateUtil.nowDateToStamp());
					tagBean.setUpdateDate(DateUtil.nowDateToStamp());
					tagBean.setUserId(asset.getUser().getId());
					tagDao.save(tagBean);
				}
			}

		}
		asset.setCreateDate(DateUtil.nowDateToStamp());
		asset.setUpdateDate(DateUtil.nowDateToStamp());
		assetDao.save(asset);

		return "success";
	}

	@Override
	public Map<String, Object> getAssetByParam(String commonType, String tag,
			String name, String userId, int page) {
		List<String> params = new ArrayList<String>();
		String whereStr = " ";
		if (commonType != null && !commonType.isEmpty()) {
			whereStr += " and commonType=? ";
			params.add(commonType);
			System.out.println("commonType : " + commonType);
		}
		if (tag != null && !tag.isEmpty()) {
			whereStr += " and tag like ? ";
			params.add("%" + tag + "%");
			System.out.println("tag : " + commonType);
		}
		if (name != null && !name.isEmpty()) {
			whereStr += " and name like ? ";
			params.add("%" + name + "%");
			System.out.println("name : " + commonType);
		}
		// 限定用户
		if (userId != null) {
			whereStr += " and userId=? ";
			params.add(userId);
		}
		int totalCount = assetDao.getCountByParam(whereStr, params).intValue();
		PagingBean pagingBean = new PagingBean(totalCount, page,
				SysConstant.ASSET_PAGE_SIZE);
		List<Asset> list = assetDao.getListByParam(whereStr, params,
				pagingBean.getCurrentPage() * SysConstant.ASSET_PAGE_SIZE,
				SysConstant.ASSET_PAGE_SIZE, " updateDate desc ");
		Iterator<Asset> iterator = list.iterator();
		while (iterator.hasNext()) {
			Asset asset = (Asset) iterator.next();
			asset.format();
		}
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("list", list);
		map.put("pagingBean", pagingBean);
		if (pagingBean.getTotalPage() == 1) {
			map.put("hasPage", "0");
		} else {
			map.put("hasPage", "1");
		}
		return map;
	}

	@Override
	public Asset getAssetById(String id) {
		Asset asset = assetDao.getById(id);
		asset.format();
		return asset;
	}

	@Override
	public List<Tag> getUserTag(String userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(Tag.class);
		dc.add(Restrictions.eq("userId", userId));
		dc.add(Restrictions.gt("num", 0));
		dc.addOrder(Order.desc("num"));
		return tagDao.findByCriteria(dc);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteAsset(String id) {
		Asset asset = assetDao.getById(id);
		String tag = asset.getTag();
		if (tag != null && tag.length() > 0) {
			String[] tagsp = tag.split(" ");
			for (String tagi : tagsp) {
				if (tagi == null || tagi.length() == 0) {
					continue;
				}
				Tag tagBean = tagDao.findByParam(tagi, asset.getUser().getId());
				if (tagBean != null) {
					tagBean.setNum(tagBean.getNum() - 1);
					tagBean.setUpdateDate(DateUtil.nowDateToStamp());
					tagDao.update(tagBean);
				} 
			}
		}
		
		assetDao.delete(asset);
		return "success";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode> findTreeByUserId(String id) {
		List<TreeNode> tnlist = null;
		String hql = "from Asset where userId= '" + id + "' order by id asc";
		List<Asset> assets = (List<Asset>) this.find(hql);
		Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
		List<TreeNode> typelist = new ArrayList<TreeNode>(); // 资产文件类型节点列表
		for (Asset asset : assets) {
			TreeNode node = new TreeNode();
			List<String> tags = new ArrayList<String>(); // 标签List
			node.setText(asset.getName());
			node.setId(asset.getId());
			tags.add(asset.getAssetType());// 资产类型作为标签
			node.setTags(tags);
			if (asset.getAssetType().equals("身份证")) {
				node.setIcon("fa fa-address-card");
			} else if (asset.getAssetType().equals("学生证")) {
				node.setIcon("fa fa-id-badge");
			}
			else if(asset.getAssetType().equals("论文")){
				node.setIcon("fa fa-file-pdf-o");
				
			}
			/*
			 * 先遍历判断该资产文件类型是否已经在typelist中，
			 * 如果不在，则在nodelist中新增一个node父节点，并设置该资产节点的父节点id 如果在，则设置该资产节点的父节点id
			 */
			if (typelist.size() > 0) { // 如果不为空
				boolean exist = false;
				for (TreeNode treeNode : typelist) {
					if (treeNode.getText().equals(asset.getCommonType())) {
						node.setParentId(treeNode.getId());
						exist = true;
						break;
					}
				}
				if(!exist){
					TreeNode typenode = new TreeNode();
					typenode.setText(asset.getCommonType()); // 资产文件类型为节点名称
					typenode.setId(StringUtil.getRandomString(10)); // 随机生成10个数作为其Id
					typelist.add(typenode); // 将该节点保存到typelist
					nodelist.put(typenode.getId(), typenode);// 将该节点同时保存到nodelist
					node.setParentId(typenode.getId());
					nodelist.put(node.getId(), node);
					break;
				}
			} else {// 否则初始化
				TreeNode typenode = new TreeNode();
				typenode.setText(asset.getCommonType()); // 资产文件类型为节点名称
				typenode.setId(StringUtil.getRandomString(10)); // 随机生成10个数作为其Id
				typelist.add(typenode); // 将该节点保存到typelist
				nodelist.put(typenode.getId(), typenode);// 将该节点同时保存到nodelist
				node.setParentId(typenode.getId());
				nodelist.put(node.getId(), node);
			}
			nodelist.put(node.getId(), node);
		}

		// 构造树形结构
		tnlist = TreeUtil.getNodeList(nodelist);
		return tnlist;
	}

	@Override
	public HashMap<String, String> compareKeyInfo(String nKeyInfoStr,
			String chainKeyInfoStr, String assetType) {

		JSONObject nKeyInfo = JSON.parseObject(nKeyInfoStr);
		JSONObject chainKeyInfo = JSON.parseObject(chainKeyInfoStr);

		// TODO 改为以识别关键信息的那共享
		// 获取该类资产的关键信息模板
		Dict dict = dictDao.getByCode(assetType + "_KEYINFO_TEMPLATE");
		String tKeyWordStr = dict.getValue();
		String[] tKeyWord = tKeyWordStr.split("#");

		HashMap<String, String> compareResult = new HashMap<String, String>();

		// 比对
		for (String key : tKeyWord) {
			String chainValue = chainKeyInfo.getString(key);
			String nValue = nKeyInfo.getString(key);
			if (!nValue.equals(chainValue)) {
				compareResult.put(key, "no");
			} else {
				compareResult.put(key, "yes");
			}
		}

		return compareResult;
	}

	public static void main(String[] args) {
//		String res = "{\"name\":\"haha\",\"id\":\"123\"}";
//		JSON.parse(res);
//		JSONObject parseObject = JSON.parseObject(res);
//		System.out.println(parseObject.getString("id"));
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		Integer integer = map.get("asd");
		
		System.out.println(integer);
	}

	@Override
	public Map<String, String> getKeyInfo(String fileName, String code) {
		String methodName = methodMap.get(code);
		if (methodName == null || methodName.isEmpty()) {
			Dict dict = dictDao.getByCode(code + "_IDENTIFY_METHOD");
			if (dict != null) {
				methodName = dict.getValue();
			}
			methodMap.put(code, methodName);
		}

		String assetTemplate = templateMap.get(code);
		if (assetTemplate == null || assetTemplate.isEmpty()) {
			Dict dict = dictDao.getByCode(code + "_KEYINFO_TEMPLATE");
			if (dict != null) {
				assetTemplate = dict.getValue();
			}
			templateMap.put(code, assetTemplate);

		}

		Map<String, String> keyInfo = AssetIdentifyUtil.getKeyInfo(fileName,
				methodName, assetTemplate);
		return keyInfo;
	}

	@Override
	public List<Asset> getAll() {
		return assetDao.getAll();
	}

	  
	@Override
	public HashMap<String, Integer> getStatistic(String userId) {
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		List<Dict> typeList = dictDao.findByParentName("资产文件类型");
		for (Dict dict : typeList) {
			map.put(dict.getName(), 0);
		}
		
		List<String> params = new ArrayList<String>();
		
		params.add(userId);
		
		List<Asset> assetList = assetDao.getListByParam(" and userId=? ", params, null, null, null);
		for (Asset asset : assetList) {
			Integer count = map.get(asset.getCommonType());
			map.put(asset.getCommonType(), count + 1);
		}
		
		for (Dict dict : typeList) {
			Integer count = map.get(dict.getName());
			map.put(dict.getName(), (int) (((count * 1.0) / assetList.size()) * 100.0));
		}
		
		return map;
	}

}
