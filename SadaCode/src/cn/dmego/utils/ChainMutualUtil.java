package cn.dmego.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

import cn.dmego.domain.ChainMsg;

/**
 * @Name: ChainMutualUtil
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月8日
 * 
 */
public class ChainMutualUtil {
//	private static String baseUrl = "http://192.168.119.140:8080/";
//	private static String username = "haha";
//	private static String chainCodeName = "myChaincode";
	private static String baseUrl;
	private static String username;
	private static String chainCodeName;
	private static String jwtToken;
//	private static Long EXPIRATIONTIME = 840000L;
	private static Long EXPIRATIONTIME;
	private static Long jwtTokenEndTime;
	private static Logger logger = Logger.getLogger(ChainMutualUtil.class);
	private static int configNum = 0;
	private static int constructNum = 0;
	private static int installNum = 0;
	private static int instantiateNum = 0;
	private static String isOpen = "no";
	

	static{
		InputStream in = ChainMutualUtil.class.getClassLoader().getResourceAsStream("chainConfig.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			baseUrl = pro.get("chain.baseUrl").toString();
			username = pro.get("chain.username").toString();
			chainCodeName = pro.get("chain.chainCodeName").toString();
			EXPIRATIONTIME = Long.parseLong(pro.get("chain.EXPIRATIONTIME").toString());
			constructNum=Integer.parseInt(pro.get("chain.constructNum").toString());
			installNum=Integer.parseInt(pro.get("chain.installNum").toString());
			instantiateNum=Integer.parseInt(pro.get("chain.instantiateNum").toString());
			isOpen = pro.get("chain.isOpen").toString();
			configNum++;
		} catch (IOException e) {
			logger.error("读取chain配置文件失败");
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
		
		preCheck("invoke");
		
	}
	
	
	private static void checkConfig(){
		if(configNum == 0){
			loadConfig();
		}
	}

	/**
	 * constructNum = 0;
	private static int reconstructNum = 0;
	private static int installNum = 0;
	private static int instantiateNum = 0;
	 */
	private static void preCheck(String method){
		checkConfig();
		if("no".equals(isOpen)){
			return ;
		}
		switch (method) {
		case "install":
			if(constructNum == 0){
				construct(null);
			}
			break;
		case "instantiate":
			if(installNum == 0){
				install(null);
			}
			break;
		case "upgrade":
			if(constructNum == 0){
				construct(null);
			}
			break;
		case "invoke":
			if(instantiateNum == 0){
				instantiate(null);
			}
			break;
		case "query":
			if(instantiateNum == 0){
				instantiate(null);
			}
			break;

		default:
			break;
		}
	}

	 
	    /**  
	    * @Name: loadConfif  
	    * @Description: TODO
		* @Author: 刘西宁
		* @Version: V1.00
		* @Parameters:
	    * @Return void    返回类型  
	    * @throws  
		* @Create Date:2018年6月15日 
	    */  
	    
	private static void loadConfig() {
		InputStream in = ChainMutualUtil.class.getClassLoader().getResourceAsStream("chainConfig.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			baseUrl = pro.get("chain.baseUrl").toString();
			username = pro.get("chain.username").toString();
			chainCodeName = pro.get("chain.chainCodeName").toString();
			EXPIRATIONTIME = Long.parseLong(pro.get("chain.EXPIRATIONTIME").toString());
			constructNum=Integer.parseInt(pro.get("chain.constructNum").toString());
			installNum=Integer.parseInt(pro.get("chain.installNum").toString());
			instantiateNum=Integer.parseInt(pro.get("chain.instantiateNum").toString());
			isOpen = pro.get("chain.isOpen").toString();
			configNum++;
		} catch (IOException e) {
			logger.error("读取chain配置文件失败");
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

	private static String getJwtToken() {
		Long dateTime = new Date().getTime();
		if (jwtToken == null || jwtTokenEndTime == null || dateTime >= jwtTokenEndTime) {
			// 过期，重新获取
			setJwtToken();
		}
		return jwtToken;
	}

	private static void setJwtToken() {
		String resp = enroll(null);
		if (resp != null) {
			jwtToken = resp;
			jwtTokenEndTime = new Date().getTime() + EXPIRATIONTIME;
			logger.info("注册用户获取jwtToken成功！");
		} else {
			logger.error("注册用户获取jwtToken失败!");
		}

	}

	
	
	
	public static String enroll(ChainMsg chainMsg) {
		checkConfig();
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (chainMsg == null || chainMsg.getUsername() == null) {
			param.put("username", ChainMutualUtil.username);
		} else {
			param.put("username", chainMsg.getUsername());
		}

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Content-Type", "application/json");
		reqHeader.put("Accept", "text/plain");

		String response = HttpConnUtil.sendPost(baseUrl + "enroll", param, reqHeader);
		if (response.contains("Success")) {
			int jwtIndex = response.indexOf("jwt");
			return response.substring(jwtIndex + 4);
		}
		return null;
	}

	public static String construct(ChainMsg chainMsg) {
		checkConfig();
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		Map<String, Object> param = new HashMap<String, Object>();

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Content-Type", "application/json");
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());

		String response = HttpConnUtil.sendPost(baseUrl + "api/construct", param, reqHeader);
//		if(response.contains("successfully")){
			constructNum++;
//		}
		return response;

	}

	public static String reconstruct(ChainMsg chainMsg) {
		checkConfig();
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		Map<String, Object> param = new HashMap<String, Object>();

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Content-Type", "application/json");
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());

		String response = HttpConnUtil.sendPost(baseUrl + "api/reconstruct", param, reqHeader);
		return response;

	}

	public static String install(ChainMsg chainMsg) {
		preCheck("install");
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (chainMsg == null || chainMsg.getChaincodeName() == null) {
			param.put("chaincodeName", chainCodeName);
		} else {
			param.put("chaincodeName", chainMsg.getChaincodeName());
		}

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Content-Type", "application/json");
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());

		String response = HttpConnUtil.sendPost(baseUrl + "api/install", param, reqHeader);
//		if(response.contains("successfully")){
			installNum++;
//		}
		return response;

	}

	public static String instantiate(ChainMsg chainMsg) {
		preCheck("instantiate");
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (chainMsg == null || chainMsg.getChaincodeName() == null) {
			param.put("chaincodeName", chainCodeName);
		} else {
			param.put("chaincodeName", chainMsg.getChaincodeName());
		}
		if (chainMsg == null || chainMsg.getFunction() == null) {
			param.put("function", "init");
		} else {
			param.put("function", chainMsg.getFunction());
		}
		if (chainMsg == null || chainMsg.getArgs() == null) {
			param.put("args", new String[] {});
		} else {
			param.put("args", chainMsg.getArgs());
		}

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Content-Type", "application/json");
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());

		String response = HttpConnUtil.sendPost(baseUrl + "api/instantiate", param, reqHeader);
//		if(response.contains("Successfully")){
			instantiateNum++;
//		}
		return response;

	}

	public static String upgrade(ChainMsg chainMsg) {
		preCheck("upgrade");
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (chainMsg == null || chainMsg.getChaincodeName() == null) {
			param.put("chaincodeName", chainCodeName);
		} else {
			param.put("chaincodeName", chainMsg.getChaincodeName());
		}
		if (chainMsg == null || chainMsg.getFunction() == null) {
			param.put("function", "init");
		} else {
			param.put("function", chainMsg.getFunction());
		}
		if (chainMsg == null || chainMsg.getArgs() == null) {
			param.put("args", new String[] {});
		} else {
			param.put("args", chainMsg.getArgs());
		}

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Content-Type", "application/json");
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());

		String response = HttpConnUtil.sendPost(baseUrl + "api/upgrade", param, reqHeader);
		return response;

	}

	public static String invoke(ChainMsg chainMsg) {
		preCheck("invoke");
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (chainMsg == null || chainMsg.getChaincodeName() == null) {
			param.put("chaincodeName", chainCodeName);
		} else {
			param.put("chaincodeName", chainMsg.getChaincodeName());
		}
		if (chainMsg == null || chainMsg.getFunction() == null) {
			param.put("function", "invoke");
		} else {
			param.put("function", chainMsg.getFunction());
		}
		if (chainMsg == null || chainMsg.getArgs() == null) {
			param.put("args", new String[] { "save", "id1", "n1", "k1", "o1", "md1" });
		} else {
			param.put("args", chainMsg.getArgs());
			logger.debug("chainMsg:" + chainMsg);
			logger.debug("chainMsg args len:" + chainMsg.getArgs().length);
		}

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Content-Type", "application/json");
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());

		String response = HttpConnUtil.sendPost(baseUrl + "api/invoke", param, reqHeader);
		return response;

	}

	public static String query(ChainMsg chainMsg) {
		preCheck("query");
		if("no".equals(isOpen)){
			return "chain netword is close";
		}
		// ChaincodeName=myChaincode&function=invoke&args=query%2Cb
		// 'api/query?ChaincodeName=myChaincode&function=invoke&args=query%2Cid1'
		String url = baseUrl + "api/query?args=query%2C" + chainMsg.getId() + "&ChaincodeName=";
		if (chainMsg == null || chainMsg.getChaincodeName() == null) {
			url += chainCodeName;
		} else {
			url += chainMsg.getChaincodeName();
		}
		url += "&function=";
		if (chainMsg == null || chainMsg.getFunction() == null) {
			url += "invoke";
		} else {
			url += chainMsg.getFunction();
		}

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());
		String response = HttpConnUtil.sendGet(url, reqHeader);
		return response;

	}

	public static String queryBlockInfo(ChainMsg chainMsg) {
		preCheck("query");
		if ("no".equals(isOpen)) {
			return "chain netword is close";
		}
		// ChaincodeName=myChaincode&function=invoke&args=query%2Cb
		// 'api/query?ChaincodeName=myChaincode&function=invoke&args=query%2Cid1'
		String url = baseUrl + "api/queryblockinfo?args=query%2Ca&ChaincodeName=";
		if (chainMsg == null || chainMsg.getChaincodeName() == null) {
			url += chainCodeName;
		} else {
			url += chainMsg.getChaincodeName();
		}
		url += "&function=";
		if (chainMsg == null || chainMsg.getFunction() == null) {
			url += "invoke";
		} else {
			url += chainMsg.getFunction();
		}

		Map<String, String> reqHeader = new HashMap<String, String>();
		reqHeader.put("Accept", "*/*");
		reqHeader.put("Authorization", "Bearer " + getJwtToken());
		String response = HttpConnUtil.sendGet(url, reqHeader);
		return response;

	}

	public static String reloadConfif(){
		loadConfig();
		return "reload config successfully";
	}
	
	public static void main(String[] args) throws InterruptedException {
		// String enrollStr = getJwtToken();
		// System.out.println(enrollStr);
		// System.out.println(jwtToken);
		// System.out.println(jwtTokenEndTime);
		// System.out.println(jwtTokenEndTime - new Date().getTime());
		// Thread.sleep(1000);
		// String constructstr = construct();
		// System.out.println(constructstr);
		// Thread.sleep(1000);
		// String installstr = install();
		// System.out.println(installstr);
		// Thread.sleep(1000);
		// String instantiatestr = instantiate();
		// System.out.println(instantiatestr);
		// Thread.sleep(1000);
		// String[] args1 = new String[6];
		// args1[0] = "save";
		// args1[1] = "id1";
		// args1[2] = "name1";
		// args1[3] = "k1";
		// args1[4] = "o1";
		// args1[5] = "md5ss1";
		// invoke(args1);
		// Thread.sleep(1000);
		// query("id1");
		// invoke(null);
//		ChainMsg c = new ChainMsg();
//		c.setId("id1");
//		System.out.println(query(c));
		
	}

}
