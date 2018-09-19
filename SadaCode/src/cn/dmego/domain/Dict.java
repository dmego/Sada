package cn.dmego.domain;

/**  
* @Name: Dictionary
* @Description: 数据字典Bean
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-24（创建日期）
*/

public class Dict {
	private String id;
	private String name;
	private String code;
	private String levelCode;
	private String parentId;
	private String remark;
	private String value;
	private String deleted; //删除标记(0启用，1禁用)
	private String createtime;
	private String updatetime;
	
	public Dict() {
		
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
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	
	
	

	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "Dict [id=" + id + ", name=" + name + ", code=" + code
				+ ", levelCode=" + levelCode + ", parentId=" + parentId
				+ ", remark=" + remark + ", value=" + value + ", deleted="
				+ deleted + ", createtime=" + createtime + ", updatetime="
				+ updatetime + ", parentName=" + parentName + "]";
	}
	
	
	

	
}
