package cn.dmego.domain;

/**
 * @Name: MailInfo
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月20日
 * 
 */
public class MailInfo {
	private String to;
	private String subject;
	private String content;
	private String type;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
