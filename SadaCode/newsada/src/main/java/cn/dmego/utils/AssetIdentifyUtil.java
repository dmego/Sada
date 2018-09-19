package cn.dmego.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Name: AssetIdentifyUtil
 * @Description: 资产文件关键信息识别工具类
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年4月17日
 * 
 */
@SuppressWarnings("unused")
public class AssetIdentifyUtil {
	//private static final String URL = "http://127.0.0.1:8080";
	private static final String URL = "http://47.94.252.154:8080";
	public static Map<String, String> getKeyInfo(String fileName, String type, String assetTemplate) {
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		HashMap<String, String> params = new HashMap<String,String>();
		HashMap<String, String> files = new HashMap<String,String>();
		params.put("type", type);
		files.put("pic", fileName);
		String resultStr = HttpClientUtil.sendPostWithFile(URL, params, files);
		
		JSONObject jo = JSON.parseObject(resultStr);
		if(jo.containsKey("error") && jo.getInteger("error") == 1) {
			keyInfoMap.put("error", "识别出错");
			return keyInfoMap;
		}

		String[] keys = assetTemplate.split("#");
		
		for (String key : keys) {
			if(jo.containsKey(key)) {
				keyInfoMap.put(key, jo.getString(key));
			}
		}
		return keyInfoMap;
	}


}
