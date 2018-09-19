package cn.dmego.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Name: ChainMutualUtil
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月8日
 * 
 */
public class ChainMutualUtil {
	public static String isOpen = "no";
	public static String userName;
	public static String adminName;
	public static String channelName;
	public static String chainCodeName;
	public static String chainCodePath;
	public static String chainCodeVersion;

	static {
		InputStream in = ChainMutualUtil.class.getClassLoader().getResourceAsStream("hyperledger.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			isOpen = pro.get("isOpen").toString();
			userName = pro.get("userName").toString();
			adminName=pro.get("adminName").toString();
			channelName=pro.get("channelName").toString();
			chainCodeName = pro.get("chainCodeName").toString();
			chainCodePath = pro.get("chainCodePath").toString();
			chainCodeVersion = pro.get("chainCodeVersion").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


}
