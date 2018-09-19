package cn.dmego.test;

import java.util.Date;

import org.junit.Test;

import cn.dmego.utils.DateUtil;

public class TestDateUtil {
	
	@Test
	public void testdate(){
		Date time = new Date();
		System.out.println(time);
		System.out.println(DateUtil.dateToStamp(time));
		String stamp = DateUtil.dateToStamp(time);
		System.out.println(DateUtil.stampToStr(stamp));
		System.out.println(DateUtil.getCurrDateTimeStr());
	}
}
