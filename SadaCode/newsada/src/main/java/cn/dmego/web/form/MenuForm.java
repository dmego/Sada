package cn.dmego.web.form;

public class MenuForm {
	private String username; //登录名称
	private String password; //登录密码
	private String registname;
	private String registpass1;
	private String registpass2;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRegistname() {
		return registname;
	}
	public void setRegistname(String registname) {
		this.registname = registname;
	}
	public String getRegistpass1() {
		return registpass1;
	}
	public void setRegistpass1(String registpass1) {
		this.registpass1 = registpass1;
	}
	public String getRegistpass2() {
		return registpass2;
	}
	public void setRegistpass2(String registpass2) {
		this.registpass2 = registpass2;
	}
	
	@Override
	public String toString() {
		return "MenuForm [username=" + username + ", password=" + password
				+ ", registname=" + registname + ", registpass1=" + registpass1
				+ ", registpass2=" + registpass2 + "]";
	}
	
	
}
