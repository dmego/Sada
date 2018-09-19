package cn.dmego.test;

import org.junit.Test;

import cn.dmego.utils.Encrypt;
import cn.dmego.utils.StringUtil;

public class TestMD5 {
	
	@Test
	public void testmd5(){
		for(int i = 0; i< 10;i++){
			String salt = StringUtil.getRandomString(10);
			System.out.println(salt);
			System.out.println(Encrypt.md5("123456",salt));
		}
		
	}
}	
