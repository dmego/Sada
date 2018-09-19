package cn.dmego.filter;

public class PopedomFilter {

	    private String key;
	    private String operator;
	    private String value;
	    private String classType;
	    private Integer sort;
	    private String roleId;
	    private String functionId;
	    public String getRoleId() {
	        return roleId;
	    }
	    
	    public void setRoleId(String roleId) {
	        this.roleId = roleId;
	    }

	    public String getFunctionId() {
	        return functionId;
	    }

	    public void setFunctionId(String functionId) {
	        this.functionId = functionId;
	    }

	    public String getKey() {
	        return key;
	    }

	    public void setKey(String key) {
	        this.key = key;
	    }

	    public String getOperator() {
	        return operator;
	    }

	    public void setOperator(String operator) {
	        this.operator = operator;
	    }

	    public String getValue() {
	        return value;
	    }

	    public void setValue(String value) {
	        this.value = value;
	    }

	    public String getClassType() {
	        return classType;
	    }

	    public void setClassType(String classType) {
	        this.classType = classType;
	    }

	    public Integer getSort() {
	        return sort;
	    }

	    public void setSort(Integer sort) {
	        this.sort = sort;
	    }

}
