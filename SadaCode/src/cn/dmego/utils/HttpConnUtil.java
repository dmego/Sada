package cn.dmego.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * @Name: HttpConnUtil
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月8日
 * 
 */
public class HttpConnUtil {
	private static Logger logger = Logger.getLogger(HttpConnUtil.class);

	public static String sendGet(String url,Map<String,String> reqHeader) {
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
//			connection.setRequestProperty("accept", "*/*");
//			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(reqHeader != null){
				Set<Entry<String, String>> entrySet = reqHeader.entrySet();
				for (Entry<String, String> entry : entrySet) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setConnectTimeout(50000);
			connection.setReadTimeout(50000);
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer buffer2 = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null) {
				buffer2.append(line);
			}
			return buffer2.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occur when send http get request!", e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	public static String sendPost(String strURL, Map<String, Object> paramMap, Map<String, String> reqHeader) {
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
//			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
//			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			if(reqHeader != null){
				Set<Entry<String, String>> entrySet = reqHeader.entrySet();
				for (Entry<String, String> entry : entrySet) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(JSON.toJSONString(paramMap));
			out.flush();
			out.close();

			int code = connection.getResponseCode();
			InputStream is = null;
			if (code == 200) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}

			// 读取响应
			int length = (int) connection.getContentLength();// 获取长度
			if (length != -1) {
				byte[] data = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, data, destPos, readLen);
					destPos += readLen;
				}
				String result = new String(data, "UTF-8"); // utf-8编码
				return result;
			}

		} catch (IOException e) {
			logger.error("Exception occur when send http post request!", e);
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}
	
	public static void main(String[] args) {
		Map<String,String> param = new HashMap<String, String>();
		param.put("username", "haha");
//		String resp = sendPost("http://192.168.119.140:8080/enroll", param);
//		System.out.println(resp);
	}

}
