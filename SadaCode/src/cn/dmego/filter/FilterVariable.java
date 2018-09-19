package cn.dmego.filter;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
public class FilterVariable extends FilterFragment {

	public FilterVariable(String propertyName) {
		this(null, propertyName);
	}

	public FilterVariable(String alias, String propertyName) {
		this.propertyName = propertyName;
		setTableAlias(alias);
	}

	public String getString() {
		if (StringUtils.isNotBlank(alias))
			return (new StringBuilder()).append(alias).append(".").append(propertyName).toString();
		else
			return (new StringBuilder()).append(propertyName).toString();
	}
 
	public List getValues() {
		return Collections.EMPTY_LIST;
	}

	public void setTableAlias(String alias) {
		if (StringUtils.isNotBlank(alias))
			this.alias = alias;
	}

	private final String propertyName;
	private String alias;
}