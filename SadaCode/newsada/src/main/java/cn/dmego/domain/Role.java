package cn.dmego.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**  
* @Name: Role
* @Description: 系统角色Bean
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-25（创建日期）
*/
@SuppressWarnings("serial")
public class Role implements Serializable{
	private String id;
	private String name;
	private String code;
	private String remark;
	private String sort;
	private String deleted; //删除标记(0启用，1禁用)
	private String createtime;
	private String updatetime;
	
	//用户角色多对多关系
	private Set<User> users = new HashSet<User>();
	//角色权限多对多关系
	private Set<Popedom> popedoms = new HashSet<Popedom>();
	@JSON(serialize=false)
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@JSON(serialize=false)
	public Set<Popedom> getPopedoms() {
		return popedoms;
	}
	public void setPopedoms(Set<Popedom> popedoms) {
		this.popedoms = popedoms;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
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
		
	//非持久化对象
	private String auths; //权限节点字符串，通过","分隔

	public String getAuths() {
		return auths;
	}
	public void setAuths(String auths) {
		this.auths = auths;
	}
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", code=" + code
				+ ", remark=" + remark + ", sort=" + sort + ", deleted="
				+ deleted + ", createtime=" + createtime + ", updatetime="
				+ updatetime + ", users=" + users + ", popedoms=" + popedoms
				+ ", auths=" + auths + "]";
	}
	
	
}
