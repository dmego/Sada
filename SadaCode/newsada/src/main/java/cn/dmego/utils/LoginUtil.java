package cn.dmego.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginUtil {
	/**记住我*/
	public static void remeberMe(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		//1：创建2个Cookie，分别存放用户名和密码
		//Cookie中不能存放中文
		try {
			username = URLEncoder.encode(username, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie usernameCookie = new Cookie("username",username);
		Cookie passwordCookie = new Cookie("password",password);
		//2：判断页面中的复选框是否被选中，选中设置，不选中就不设置
		String remeberMe = request.getParameter("rememberMe");
		//设置有效路径
		usernameCookie.setPath(request.getContextPath()+"/");
		passwordCookie.setPath(request.getContextPath()+"/");
		//选中
		if(remeberMe!=null && remeberMe.equals("yes")){
			//设置有效时间
			usernameCookie.setMaxAge(7*24*60*60);//1周
			passwordCookie.setMaxAge(7*24*60*60);//1周
		}
		else{
			//清空
			usernameCookie.setMaxAge(0);//
			passwordCookie.setMaxAge(0);//
		}
		//3：将Cookie存放到response对象
		response.addCookie(usernameCookie);
		response.addCookie(passwordCookie);
	}

}
