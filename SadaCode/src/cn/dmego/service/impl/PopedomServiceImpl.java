package cn.dmego.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dmego.dao.IDictDao;
import cn.dmego.dao.IPopedomDao;
import cn.dmego.domain.Dict;
import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.JstreeNode;
import cn.dmego.pojo.NodeState;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IPopedomService;
import cn.dmego.utils.TreeUtil;


/**  
* @Name: PopedomSerciveImpl
* @Description: 权限Service接口实现类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-25（创建日期）
*/
@SuppressWarnings("rawtypes")
@Service(IPopedomService.SERVICE_NAME)
@Transactional(readOnly=true)
public class PopedomServiceImpl extends BaseServiceImpl implements IPopedomService{
	
	/**注入功能权限表Dao*/
	@Resource(name=IPopedomDao.DAO_NAME)
	IPopedomDao authDao;
	
	/**注入数据字典表Dao*/
	@Resource(name=IDictDao.DAO_NAME)
	IDictDao dictionaryDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode> findAllAuthList() {
		List<TreeNode> tnlist = null;
		String hql = "from Popedom order by levelCode asc";
		List<Popedom> auths = this.find(hql);
		Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
		for (Popedom auth : auths) {
            TreeNode node = new TreeNode();
            node.setText(auth.getName());
            node.setId(auth.getId());
            node.setParentId(auth.getParentId());
            node.setLevelCode(auth.getLevelCode());
            node.setIcon(auth.getIcon());
            nodelist.put(node.getId(), node);
        }
		// 构造树形结构
        tnlist = TreeUtil.getNodeList(nodelist);
        return tnlist;
	}

	@Override
	public boolean checkCodeByIdAndCode(String id, String code) {
		String hql = "from Popedom where code= '" + code + "'";
		Popedom auth1 = authDao.get(hql);
		Popedom auth2 = authDao.get(Popedom.class, id);
		 //如果不存在该code的dict,或者该code是编辑的该字典原本的code
		if(auth1 == null || (auth2 != null && auth2.getCode().equals(code))){
			return true; //可以使用该code
		}
		return false;
	}

	@Override
	public Popedom getAuthsByCode(String code) {
		String hql = "from Popedom where code= '" + code + "'";
		return authDao.get(hql);
	}

	//根据当前用户拥护的角色动态生成新增角色时可选的权限树
	@SuppressWarnings("unchecked")
	public List<JstreeNode> buildAuthJsTree(Set<Role> roles,Role selectRole,String status) {
		Set<Popedom> popedoms = null;
		if(selectRole != null && status.equals("upd")){
			//获取当前选择的角色拥有的权限集合
			Role JORole = (Role) this.get(Role.class, selectRole.getId());
			popedoms = JORole.getPopedoms();
		}
		//取得所有权限节点
		List<Popedom> authList = this.list(Popedom.class);
		List<JstreeNode> jstreeNodes = new ArrayList<JstreeNode>();
 		for (Role role : roles) {
			if(role.getCode().equals("SUADMIN")){ //如果当前用户有超级管理员权限 
				//构建jstree数据并返回
				for (Popedom popedom : authList) {
					if(popedom != null){
						JstreeNode node = new JstreeNode();
						node.setId(popedom.getId());
						//如果parentid为null,则设置node节点的父节点为"#"
						node.setParent(popedom.getParentId()!= null ? popedom.getParentId(): "#");
						node.setText(popedom.getName());
						node.setType(popedom.getFunctype());
						node.setState(new NodeState(false));
						if(status.equals("upd")){ //只有在修改角色信息的时候才会为节点赋予状态
							
							
							//根据当前用户的角色所拥护的权限，为节点树的节点动态赋予状态
							for (Popedom popedom2 : popedoms) {
								if(popedom2.getCode().equals(popedom.getCode())){
									node.setState(new NodeState(true));
								}
							}
						}
						jstreeNodes.add(node);
					}
				}
				break; 
			}else if(role.getCode().equals("SEADMIN")){ //如果当前用户有高级管理员权限
				//返回的权限树中不包括资产管理与系统管理
				//构建jstree数据并返回
				for (Popedom popedom : authList) {
					//先获取权限的levelcode,如果levelcode的前6位不满足要求，continue
					//根据数据权限表不满足要求的是000006 开头的资产管理与 000008 开头的系统管理
					if(popedom != null){
						String levelCode = popedom.getLevelCode();
						if(levelCode.substring(0, 6).equals("000006") || levelCode.substring(0, 6).equals("000008")){
							continue;
						}else{
							JstreeNode node = new JstreeNode();
							node.setId(popedom.getId());
							//如果parentid为null,则设置node节点的父节点为"#"
							node.setParent(popedom.getParentId()!= null ? popedom.getParentId(): "#");
							node.setText(popedom.getName());
							node.setType(popedom.getFunctype());
							node.setState(new NodeState(false));
							if(status.equals("upd")){ //只有在修改角色信息的时候才会为节点赋予状态
								//根据当前用户的角色所拥护的权限，为节点树的节点动态赋予状态
								for (Popedom popedom2 : popedoms) {
									if(popedom2.getCode().equals(popedom.getCode())){
										node.setState(new NodeState(true));
									}
								}
							}
							jstreeNodes.add(node);
						}
					}
				}		
			}
		}
		return jstreeNodes;
	}
	
	
	//将权限字符串组织成Set<Popedom>集合
	@SuppressWarnings("unchecked")
	public Set<Popedom> getSetByStr(String auths) {
		//先将字符串通过","分隔成数组
		String [] ids = auths.split(",");
		Set<Popedom> popedoms = new HashSet<Popedom>();
		for (String id : ids) {
			Popedom popedom = (Popedom) this.get(Popedom.class, id);
			popedoms.add(popedom);
		}
		return popedoms;
	}

	//根据用户ID获取权限code集合
	@SuppressWarnings("unchecked")
	public Set<String> getAuthCodeSetByUserId(String userId) {
		Set<String> authCodeSet = new HashSet<String>();
		User JOUser = (User) this.get(User.class, userId);
		Set<Role> roles = JOUser.getRoles();
		for (Role role : roles) {
			Role JORole = (Role) this.get(Role.class, role.getId());
			Set<Popedom> popedoms = JORole.getPopedoms();
			for (Popedom popedom : popedoms) {
				authCodeSet.add(popedom.getCode());
			}
		}
		return authCodeSet;
	}
	
	//根据用户ID获取权限 List 集合
	@SuppressWarnings("unchecked")
	public List<Popedom> getAuthSetByUserId(String id) {
		Set<Popedom> authSet = new HashSet<Popedom>();
		User JOUser = (User) this.get(User.class, id);
		Set<Role> roles = JOUser.getRoles();
		for (Role role : roles) {
			Role JORole = (Role) this.get(Role.class, role.getId());
			Set<Popedom> popedoms = JORole.getPopedoms();
			for (Popedom popedom : popedoms) {
				if(popedom.getFunctype().equals("1")){ 
					authSet.add(popedom);
				}//如果是菜单
			}
		}
		//自定义Comparator对象，自定义排序  通过levelCode排序
        Comparator compareByLevelCode = new Comparator<Popedom>() {  
			@Override
			public int compare(Popedom po1, Popedom po2) {
				int c = po1.getLevelCode().compareTo(po2.getLevelCode());
				return c;
			}  
        };
        List<Popedom> authList = new ArrayList<Popedom>(authSet);
        authList.sort(compareByLevelCode);		
		return authList;
	}
	
}
