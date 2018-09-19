package cn.dmego.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class Encrypt {
	/*
	 * 散列算法一般用于生成数据的摘要信息，是一种不可逆的算法，一般适合存储密码之类的数据，
	 * 常见的散列算法如MD5、SHA等。一般进行散列时最好提供一个salt（盐），比如加密密码“admin”，
	 * 产生的散列值是“21232f297a57a5a743894a0e4a801fc3”，
	 * 可以到一些md5解密网站很容易的通过散列值得到密码“admin”，
	 */
	
	//高强度加密算法,不可逆
	public static String md5(String password, String salt){
		return new Md5Hash(password,salt,2).toString();
	}
	
}
