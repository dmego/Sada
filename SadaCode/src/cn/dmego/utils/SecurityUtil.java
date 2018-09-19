package cn.dmego.utils;

import org.apache.shiro.SecurityUtils;

import cn.dmego.domain.User;

public class SecurityUtil {
	
	public static String getUserId(){
		return SecurityUtils.getSubject().getPrincipal().toString();
	}
    
	public static User getUser(){
		return (User) SecurityUtils.getSubject().getSession().getAttribute("user");
	}
	
}
