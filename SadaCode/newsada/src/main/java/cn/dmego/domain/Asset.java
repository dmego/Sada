/**
 * 
 */
package cn.dmego.domain;

import org.apache.struts2.json.annotations.JSON;

import cn.dmego.utils.DateUtil;

/**
 * @Name: Asset
 * @Description: 资产实体类
 * @Author: 刘西宁
 * @Version: V1.0.0
 * @Create Date:2018-04-16
 */

public class Asset {
	private String id;
	private String name;// 资产名称
	private String fileName;// 文件存储名称
	private String filePath;// 文件存储路径
	private String fileType;// 文件真实类型（png,jpg,pdf,mp4...）
	private String tag;// 标签（用 分隔）
	private String assetType;// 用户的资产类型（例如身份证、驾驶证、房产证）
	private String commonType;// 通用类型（例如图片、视频、文档）
	private String keyInfo;// 关键信息
	private String createDate;//创建时间
	private String updateDate;//更新时间
	private String assetMd5;//资产文件MD5
	private User user;// 创建者

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(String keyInfo) {
		this.keyInfo = keyInfo;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	@JSON(serialize = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCommonType() {
		return commonType;
	}

	public void setCommonType(String commonType) {
		this.commonType = commonType;
	}

	

	public String getAssetMd5() {
		return assetMd5;
	}

	public void setAssetMd5(String assetMd5) {
		this.assetMd5 = assetMd5;
	}

	public void format() {
		setCreateDate(DateUtil.stampToStr(Long.parseLong(getCreateDate()) * 1000 + ""));
		setUpdateDate(DateUtil.stampToStr(Long.parseLong(getUpdateDate()) * 1000 + ""));
		if (name == null) {
			name = "";
		}
		if (fileName == null) {
			fileName = "";
		}
		if (filePath == null) {
			filePath = "";
		}
		if (fileType == null) {
			fileType = "";
		}
		if (tag == null) {
			tag = "";
		}
		if (assetType == null) {
			assetType = "";
		}
		if (commonType == null) {
			commonType = "";
		}
		if (keyInfo == null) {
			keyInfo = "";
		}
	}

}