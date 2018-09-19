package cn.dmego.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**  
* @Name: User
* @Description: 系统用户Bean
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@SuppressWarnings("serial")
public class User implements Serializable{
	private String id;
	private String name;
	private String nickName;
	private String password;
	private String salt;//盐值
	private String orgId; //所属组织单位
	private String sexId;
	private String birthday;
	private String email;
	private String mobile;
	private String userPic;
	private String adminIs;
	private String remark;
	private String deleted;
	private String createtime;
	private String updatetime;
	
	//用户角色多对多关系
	private Set<Role> roles = new  HashSet<Role>();
	//用户资产一对多关系
	private Set<Asset> assets = new HashSet<Asset>();
	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	 @JSON(serialize=false)
	 public Set<Asset> getAssets() {
	 	return assets;
	 }
	 public void setAssets(Set<Asset> assets) {
	 	this.assets = assets;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getSexId() {
		return sexId;
	}
	public void setSexId(String sexId) {
		this.sexId = sexId;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	
	public String getAdminIs() {
		return adminIs;
	}
	public void setAdminIs(String adminIs) {
		this.adminIs = adminIs;
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
	private String sexCode;
	private String sexName;
	private String orgName; //组织单位名称
	private String dbirthday;
	private String roleName; //角色名称
	private String logintime;
	private String invitation;//组织邀请码
	private String userIs;
	private int roleSort;
	
	public int getRoleSort() {
		return roleSort;
	}
	public void setRoleSort(int roleSort) {
		this.roleSort = roleSort;
	}
	public String getLogintime() {
		return logintime;
	}
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getDbirthday() {
		return dbirthday;
	}
	public void setDbirthday(String dbirthday) {
		this.dbirthday = dbirthday;
	}
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public String getInvitation() {
		return invitation;
	}
	public void setInvitation(String invitation) {
		this.invitation = invitation;
	}
	public String getUserIs() {
		return userIs;
	}
	public void setUserIs(String userIs) {
		this.userIs = userIs;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", nickName=" + nickName
				+ ", password=" + password + ", sexId=" + sexId + ", birthday="
				+ birthday + ", email=" + email + ", mobile=" + mobile
				+ ", userPic=" + userPic + ", adminIs=" + adminIs + ", remark="
				+ remark + ", deleted=" + deleted + ", createtime="
				+ createtime + ", updatetime=" + updatetime + "]";
	}
	
	
	
	
}
