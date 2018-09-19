package cn.dmego.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {
	public static String sendPostWithFile(String url,HashMap<String,String> params,HashMap<String,String> files) {
	   
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse httpResponse = null;
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2000000).setSocketTimeout(200000000).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			
			Set<Entry<String, String>> entrySetF = files.entrySet();
			for (Entry<String, String> entry : entrySetF) {
				String fileKey = entry.getKey();
				String filePath = entry.getValue();
				File file = new File(filePath);
				if(file.exists()) {
					multipartEntityBuilder.addBinaryBody(fileKey,file);
				}
				
			}
			
			Set<Entry<String, String>> entrySetP = params.entrySet();
			for (Entry<String, String> entry : entrySetP) {
				multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
			}
			HttpEntity httpEntity = multipartEntityBuilder.build();
			httpPost.setEntity(httpEntity);
			
			httpResponse = httpClient.execute(httpPost);
			HttpEntity responseEntity = httpResponse.getEntity();
			int statusCode= httpResponse.getStatusLine().getStatusCode();
			BufferedReader reader = null;
			StringBuffer buffer = null;
			if(statusCode == 200){
				reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
				buffer = new StringBuffer();
				String str = "";
				while((str = reader.readLine()) != null) {
					buffer.append(str);
				}
			}
			
			httpClient.close();
			if(httpResponse!=null){
				httpResponse.close();
			}
			if(buffer != null) {
				return buffer.toString();
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	public static void main(String[] args) {
		HashMap<String, String> params = new HashMap<>();
		HashMap<String, String> files = new HashMap<>();
		params.put("type", "idCard");
		files.put("pic", "C:/Users/zengk/Desktop/小雨.png");
		String result = sendPostWithFile("http://47.94.252.154:8080", params, files);
		//{"name": "\u5c0f\u897f", "error": 0, "idnum": "130103199406055330", "nation": "\u6c49", "birth": "1994\u5e7406\u670805\u65e5", "address": "\u6cb3\u5317\u7701\u77f3\u5bb6\u5e84\u5e02\u5317\u4e8c\u73af\u4e1c17\u53f78\u680b5\u5355\u5143103\u5ba4", "sex": "\u7537"}
		JSONObject jo = JSON.parseObject(result);
		Set<Entry<String, Object>> entrySet = jo.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}
}
