package cn.dmego.pojo;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

public class QueryConfig {
	private String id;
	private String userid;
	private String queryId;
	private String pageName;
	private String columnsName;
	@Transient
	public List<String> getColumns() {
		if (StringUtils.isBlank(this.columnsName) == true) {
			return null;
		}
		return Arrays.asList(this.columnsName.split(","));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}


	public String getColumnsName() {
		return columnsName;
	}

	public void setColumnsName(String columnsName) {
		this.columnsName = columnsName;
	}


}
