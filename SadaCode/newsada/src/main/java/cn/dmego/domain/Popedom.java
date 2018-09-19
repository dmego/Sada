package cn.dmego.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * @Name: Popedom
 * @Description: 系统权限Bean
 * @Author: 曾凯（作者）
 * @Version: V1.00 （版本号）
 * @Create Date: 2018-04-25（创建日期）
 */
@SuppressWarnings("serial")
public class Popedom implements Serializable {
	private String id;	
	private String name;
	private String levelCode;
	private String code;
	private String parentId;
	private String url;
	private String icon;
	private String functype; //1.菜单，2.权限
	private String queryId;
	private String py;
	private String pingyin;
	private String remark;
	private String deleted; //删除标记(0启用，1禁用)
	private String createtime;
	private String updatetime;
	
	//角色权限多对多关系
	private Set<Role> roles = new HashSet<Role>();
	
	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getFunctype() {
		return functype;
	}
	public void setFunctype(String functype) {
		this.functype = functype;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public String getPingyin() {
		return pingyin;
	}
	public void setPingyin(String pingyin) {
		this.pingyin = pingyin;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeleted() {
		return deleted == null ? "0" : deleted;
	}
	public void setDeleted(String deleted) {
		deleted=deleted==null? "0" :deleted;
        this.deleted = deleted;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}	
	
/**非持久化对象属性*/
	
	//父节点名称
	private String parentName;
	private List<Popedom> nodes; //该节点的子节点集合
	
	public List<Popedom> getNodes() {
		return nodes;
	}
	public void setNodes(List<Popedom> nodes) {
		this.nodes = nodes;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
}
