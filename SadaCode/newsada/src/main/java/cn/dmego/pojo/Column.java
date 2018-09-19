package cn.dmego.pojo;

import javax.persistence.Transient;

import cn.dmego.filter.ConditionOperator;

/**
 * 列
 
 */
public class Column {
	/**
     * 实体名
     */
    private String key;

    /**
     * id
     */
    private String id;

    /**
     * 显示值
     */
    private String header;

    /**
     * 类型 ro ch dyn
     */
    private String type;

    /**
     * 类型 ro ch dyn
     */
    private String classType;

    /**
     * 对齐方式 left right center
     */
    private String align;

    /**
     * 是否允许排序
     */
    private Boolean allowSort;

    /**
     * 是否显示图片
     */
    private Boolean showPic;
    /**
     * 是否作为查询条件
     */
    private Boolean allowSearch;

    /**
     * 是否在列表显示
     */
    private Boolean hidden;

    /**
     * 是否显示提示信息
     */
    private Boolean enableTooltip;

    /**
     * 数据列颜色
     */
    private String color;

    /**
     * 查询字符串 like eq
     */
    private String operator;

    /**
     * 显示列的长度
     */
    private String width;

    /**
     * 列的格式化显示（服务器端调用）
     */
    private String dateFormat;

    /**
     * 数据的格式化显示（dhtmlxGrid调用）
     */
    private String numberFormat;

    /**
     * 客户端 重写该列的方法
     */
    private String fnRender;

    /**
     * 是否是服务器端的条件列
     */
    private Boolean isServerCondition;

    /**
     * 是否是服务器端的条件值
     */
    private String value;

    /**
     * 是否将该列导出
     */
    private Boolean isExport;

    /**
     * 导出列的数据后缀 如%
     */
    private String suffix;

    /**
     * 关联字典
     */
    private String dict;

    /**
     * 截取长度
     */
    private Integer maxLen;

    /**
     * 提示文本
     */
    private String tooltip;

    /**
     * 只用于数据导出，不用显示
     */
    private Boolean isJustExport;

    /**
     * 副级表头
     */
    private String subHeader;

    /**
     * render简化fnRender配置 eq对应 redner="type=eq,1=可用,0=停用" 默认type可不配
     * render="type=link,url=/base/user/userAddUpdate,param=id,target=_self"
     * type="link" render=
     * "type=window,url=/base/user/userAddUpdate,param=id,winId=id,winHeader=value,width=600,height=400,isModel=true"
     * winId winHeader isModel可默认不配
     */
    private String render;

    /**
     * 是否增加单引号 针对字符串而言
     */
    private Boolean isQuote;

    /**
     * 是否将该列导出
     */
    private Boolean isImport;

    /**
     * 列索引
     */
    private Integer index;

    /**
     * 通用导入
     */
    private String foreignClass;

    @Transient
    private Boolean isShow;

    public Column() {

        type = "ro";
        width = "50";
        align = "center";
        hidden = false;
        allowSort = false;
        allowSearch = false;
        color = "";
        classType = "java.lang.String";
        operator = "eq";
        isServerCondition = false;
        enableTooltip = false;
        isExport = true;
        isJustExport = false;
        subHeader = "#rspan";
        isQuote = true;
        isImport = true;
    }
    
    
    
    public Boolean getShowPic() {
		return showPic;
	}
	public void setShowPic(Boolean showPic) {
		this.showPic = showPic;
	}

	public String getForeignClass() {

        return foreignClass;
    }

    public void setForeignClass(String foreignClass) {

        this.foreignClass = foreignClass;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getHeader() {

        return header;
    }

    public void setHeader(String header) {

        this.header = header;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getAlign() {

        return align;
    }

    public void setAlign(String align) {

        this.align = align;
    }

    public Boolean getAllowSort() {

        return allowSort;
    }

    public void setAllowSort(Boolean allowSort) {

        this.allowSort = allowSort;
    }

    public Boolean getAllowSearch() {

        return allowSearch;
    }

    public void setAllowSearch(Boolean allowSearch) {

        this.allowSearch = allowSearch;
    }

    public Boolean getHidden() {

        return hidden;
    }

    public void setHidden(Boolean hidden) {

        this.hidden = hidden;
    }

    public String getColor() {

        return color;
    }

    public void setColor(String color) {

        this.color = color;
    }

    public String getOperator() {

        return operator;
    }

    public void setOperator(String operator) {

        this.operator = operator;
    }

    public String getWidth() {

        return width;
    }

    public String getClassType() {

        return classType;
    }

    public void setClassType(String classType) {

        this.classType = classType;
    }

    public void setWidth(String width) {

        this.width = width;
    }

    public ConditionOperator getOperatorObject() throws RuntimeException {

        if (this.operator == null)
            return null;
        else
            return ConditionOperator.getOperator(this.operator);
    }

    public String getFnRender() {

        return fnRender;
    }

    public void setFnRender(String fnRender) {

        this.fnRender = fnRender;
    }

    public Boolean getIsServerCondition() {

        return isServerCondition;
    }

    public void setIsServerCondition(Boolean isServerCondition) {

        this.isServerCondition = isServerCondition;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    public Boolean getEnableTooltip() {

        return enableTooltip;
    }

    public void setEnableTooltip(Boolean enableTooltip) {

        this.enableTooltip = enableTooltip;
    }

    public Boolean getIsExport() {

        return isExport;
    }

    public void setIsExport(Boolean isExport) {

        this.isExport = isExport;
    }

    public String getSuffix() {

        return suffix;
    }

    public void setSuffix(String suffix) {

        this.suffix = suffix;
    }

    public String getDict() {

        return dict;
    }

    public void setDict(String dict) {

        this.dict = dict;
    }

    public String getDateFormat() {

        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {

        this.dateFormat = dateFormat;
    }

    public String getNumberFormat() {

        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {

        this.numberFormat = numberFormat;
    }

    public Integer getMaxLen() {

        return maxLen;
    }

    public void setMaxLen(Integer maxLen) {

        this.maxLen = maxLen;
    }

    public String getTooltip() {

        return tooltip;
    }

    public void setTooltip(String tooltip) {

        this.tooltip = tooltip;
    }

    public Boolean getIsJustExport() {

        return isJustExport;
    }

    public void setIsJustExport(Boolean isJustExport) {

        this.isJustExport = isJustExport;
    }

    public Boolean getIsShow() {

        return isShow;
    }

    public void setIsShow(Boolean isShow) {

        this.isShow = isShow;
    }

    public String getSubHeader() {

        return subHeader;
    }

    public void setSubHeader(String subHeader) {

        this.subHeader = subHeader;
    }

    public String getRender() {

        return render;
    }

    public void setRender(String render) {

        this.render = render;
    }

    public Boolean getIsQuote() {

        return isQuote;
    }

    public void setIsQuote(Boolean isQuote) {

        this.isQuote = isQuote;
    }

    public Boolean getIsImport() {

        return isImport;
    }

    public void setIsImport(Boolean isImport) {

        this.isImport = isImport;
    }

    public Integer getIndex() {

        return index;
    }

    public void setIndex(Integer index) {

        this.index = index;
    }

	@Override
	public String toString() {
		return "Column [key=" + key + ", id=" + id + ", header=" + header
				+ ", type=" + type + ", classType=" + classType + ", align="
				+ align + ", allowSort=" + allowSort + ", allowSearch="
				+ allowSearch + ", hidden=" + hidden + ", enableTooltip="
				+ enableTooltip + ", color=" + color + ", operator=" + operator
				+ ", width=" + width + ", dateFormat=" + dateFormat
				+ ", numberFormat=" + numberFormat + ", fnRender=" + fnRender
				+ ", isServerCondition=" + isServerCondition + ", value="
				+ value + ", isExport=" + isExport + ", suffix=" + suffix
				+ ", dict=" + dict + ", maxLen=" + maxLen + ", tooltip="
				+ tooltip + ", isJustExport=" + isJustExport + ", subHeader="
				+ subHeader + ", render=" + render + ", isQuote=" + isQuote
				+ ", isImport=" + isImport + ", index=" + index
				+ ", foreignClass=" + foreignClass + ", isShow=" + isShow + "]";
	}

	
    
    
}
