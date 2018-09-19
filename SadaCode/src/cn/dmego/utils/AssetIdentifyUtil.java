package cn.dmego.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

import cn.dmego.dao.ICommonDao;
import cn.dmego.dao.impl.DictDaoImpl;
import cn.dmego.domain.Dict;
import cn.dmego.service.IDictService;
import cn.dmego.service.impl.DictServiceImpl;

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
	// ********************************************************************************
	private static final String CLASSNAME = "cn.dmego.utils.AssetIdentifyUtil";
	// TODO 改为从配置文件中读取
	private static final String APP_ID = "11178647";
	private static final String API_KEY = "S1gD6HqemqIcauGfZYpbVY3u";
	private static final String SECRET_KEY = "2VNiNALi5GkxQhgVnEvyVZbfKefxCKnq";
	private static AipOcr client = null;

	static {
		client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

		// 可选 初始化即加载所有的识别方法和关键信息模板
		// // 获取类型方法映射信息
		// List<Dictionary> methodList =
		// dictionaryService.findAssetIdentifyMethod();
		// for (Dictionary dictionary : methodList) {
		// methodMap.put(dictionary.getKeyword(), dictionary.getDdlname());
		// }
		// List<Dictionary> templateList =
		// dictionaryService.findAssetIdentifyTemplate();
		// for (Dictionary dictionary : templateList) {
		// templateMap.put(dictionary.getKeyword(), dictionary.getDdlname());
		// }

		// 可选配置
		// 建立连接的超时时间（单位：毫秒）
		client.setConnectionTimeoutInMillis(2000);
		// 通过打开的连接传输数据的超时时间（单位：毫秒）
		client.setSocketTimeoutInMillis(60000);

	}

	/**
	 * 
	 * @Name: getKeyInfo
	 * @Description: 获取文件关键信息总方法
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return 关键信息map 如果出错返回null
	 * @throws @Create
	 *             Date:2018年4月17日
	 */

	public static Map<String, String> getKeyInfo(String fileName, String methodName, String assetTemplate) {
		Map<String, String> res = null;
		try {
			Class clazz = Class.forName(CLASSNAME);
			Method method = clazz.getDeclaredMethod(methodName, String.class);
			res = (Map<String, String>) method.invoke(null, fileName);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		if (res == null) {
			return null;
		}
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		if (res.get("error") != null) {
			// 识别错误
			keyInfoMap.put("error", res.get("error"));
			return keyInfoMap;
		}
		System.out.println("----------------");
		System.out.println(res);
		System.out.println("----------------");

		// 有序LinkedHashMap
		String[] keys = assetTemplate.split("#");
		Set<String> keySet = res.keySet();

		for (String key : keys) {
			String value = "";
			if (keySet.contains(key)) {
				value = res.get(key);
			}
			keyInfoMap.put(key, value);
		}
		return keyInfoMap;
	}

	private static JSONObject basicGeneral(String fileName) {
		JSONObject res = client.basicGeneral(fileName, new HashMap<String, String>());
		return res;
	}

	/**
	 * 
	 * @Name: getIdcard
	 * @Description: 获取身份证信息
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return String 返回类型
	 * @throws @Create
	 *             Date:2018年4月17日
	 */
	private static Map<String, String> idcardFront(String fileName) {
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		HashMap<String, String> options = new HashMap<String, String>();
		JSONObject res = client.idcard(fileName, "front", options);
		String status = res.getString("image_status");
		if (!("normal".equals(status))) {
			keyInfoMap.put("error", status);
			return keyInfoMap;
		}
		JSONObject words = res.getJSONObject("words_result");
		Set<String> keySet = words.keySet();

		for (String key : keySet) {
			String value = words.getJSONObject(key).getString("words");
			keyInfoMap.put(key, value);
		}
		String birthDay = keyInfoMap.get("出生");
		if (birthDay != null && birthDay.length() == 8) {
			keyInfoMap.put("出生",
					birthDay.substring(0, 4) + "年" + birthDay.substring(4, 6) + "月" + birthDay.substring(6) + "日");
		}
		return keyInfoMap;
	}

	private static Map<String, String> idcardBack(String fileName) {
		JSONObject res = client.idcard(fileName, "front", new HashMap<String, String>());
		JSONObject words = res.getJSONObject("words_result");
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		Set<String> keySet = words.keySet();
		for (String key : keySet) {
			String value = words.getJSONObject(key).getString("words");
			keyInfoMap.put(key, value);
		}
		return keyInfoMap;
	}

	//飞去
	private static Map<String, String> custom(String fileName) {
		System.out.println(fileName);
		HashMap<String, String> options = new HashMap<String, String>();

		String templateSign = "0d07427e38c36d76829a1fcb1bae8c05";

		// 参数为本地图片路径
		JSONObject res = client.custom(fileName, templateSign, options);
		System.out.println(res);
		JSONArray ret = res.getJSONObject("data").getJSONArray("ret");
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < ret.length(); i++) {
			JSONObject item = ret.getJSONObject(i);
			String key = item.getString("word_name");
			String value = item.getString("word");
			keyInfoMap.put(key, value);
		}
		return keyInfoMap;

	}

	private static Map<String, String> bankCard(String fileName) {
		JSONObject res = client.bankcard(fileName, new HashMap<String, String>());
		JSONObject result = res.getJSONObject("result");
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		keyInfoMap.put("银行卡号", result.getString("bank_card_number"));
		keyInfoMap.put("银行名称", result.getString("bank_name"));
		Integer type = result.getInt("bank_card_type");
		if (type == 0) {
			keyInfoMap.put("银行卡类型", "不能识别");
		} else if (type == 1) {
			keyInfoMap.put("银行卡类型", "借记卡");
		} else if (type == 2) {
			keyInfoMap.put("银行卡类型", "信用卡");
		} else {
			keyInfoMap.put("银行卡类型", "识别错误");
		}
		return keyInfoMap;

	}

	private static Map<String, String> driveringLicense(String fileName) {
		JSONObject res = client.drivingLicense(fileName, new HashMap<String, String>());
		JSONObject words = res.getJSONObject("words_result");
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		Set<String> keySet = words.keySet();
		for (String key : keySet) {
			String value = words.getJSONObject(key).getString("words");
			keyInfoMap.put(key, value);
		}
		String beginDate = keyInfoMap.get("有效期限");
		String endDate = keyInfoMap.get("至");
		String resultDate = "";
		if (beginDate != null && beginDate.length() == 8 && endDate != null && endDate.length() == 8) {
			resultDate += beginDate.substring(0, 4) + "年" + beginDate.substring(4, 6) + "月" + beginDate.substring(6) + "日";
			resultDate += "至";
			resultDate += endDate.substring(0, 4) + "年" + endDate.substring(4, 6) + "月" + endDate.substring(6) + "日";
		}
		keyInfoMap.put("有效期限", resultDate);
		
		return keyInfoMap;
	}

	private static Map<String, String> vehicleLicense(String fileName) {
		JSONObject res = client.vehicleLicense(fileName, new HashMap<String, String>());
		JSONObject words = res.getJSONObject("words_result");
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		Set<String> keySet = words.keySet();
		for (String key : keySet) {
			String value = words.getJSONObject(key).getString("words");
			keyInfoMap.put(key, value);
		}
		return keyInfoMap;
	}

	private static Map<String, String> plateLicense(String fileName) {
		JSONObject res = client.plateLicense(fileName, new HashMap<String, String>());
		JSONObject result = res.getJSONObject("words_result");
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		keyInfoMap.put("颜色", result.getString("color"));
		keyInfoMap.put("车牌号", result.getString("number"));
		return keyInfoMap;
	}

	private static Map<String, String> businessLicense(String fileName) {
		JSONObject res = client.businessLicense(fileName, new HashMap<String, String>());
		JSONObject words = res.getJSONObject("words_result");
		
		Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
		Set<String> keySet = words.keySet();
		for (String key : keySet) {
			String value = words.getJSONObject(key).getString("words");
			keyInfoMap.put(key, value);
		}
		return keyInfoMap;
	}

	private static Map<String, String> word2Text(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			// 文件不存在
			System.out.println("文件不存在");
			return null;
		}
		if (fileName.endsWith("doc")) {
			return doc2Text(fileName);
		}
		return docx2Text(fileName);
	}

	/**
	 * @Name: docx2Text
	 * @Description: TODO
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return JSONObject 返回类型
	 * @throws @Create
	 *             Date:2018年4月20日
	 */

	private static Map<String, String> docx2Text(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(new File(fileName));
			XWPFDocument xdoc = new XWPFDocument(fis);
			XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
			String text = extractor.getText();
			extractor.close();
			xdoc.close();
			fis.close();
			System.out.println(text);
			Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
			keyInfoMap.put("text", text);
			return keyInfoMap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @Name: doc2Text
	 * @Description: TODO
	 * @Author: 刘西宁
	 * @Version: V1.00
	 * @Parameters:
	 * @Return JSONObject 返回类型
	 * @throws @Create
	 *             Date:2018年4月20日
	 */

	private static Map<String, String> doc2Text(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(new File(fileName));
			HWPFDocument doc = new HWPFDocument(fis);
			// String documentText = doc.getDocumentText();
			// System.out.println("documentText:" + documentText);
			StringBuilder text = doc.getText();
			doc.close();
			fis.close();
			System.out.println("text:" + text.toString());
			Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
			keyInfoMap.put("text", text.toString());
			return keyInfoMap;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}

	private static Map<String, String> commonFile2text(String fileName) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				return null;

			}
			FileInputStream fis = new FileInputStream(file);
			Scanner scan = new Scanner(fis);
			String text = "";
			while (scan.hasNextLine()) {
				text += scan.nextLine() + "\n";
			}
			scan.close();
			fis.close();
			System.out.println(text);
			Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
			keyInfoMap.put("text", text);
			return keyInfoMap;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}

//	private static Map<String, String> pdf2Text(String fileName) {
//		File file = new File(fileName);
//		if (!file.exists()) {
//			// 文件不存在
//			System.out.println("文件不存在");
//			return null;
//		}
//		try {
//			// 加载文件
//			PDDocument document = PDDocument.load(file);
//			// 获取总页数
//			int totalPageNum = document.getNumberOfPages();
//
//			PDFTextStripper stripper = new PDFTextStripper();
//			// 设置按顺序输出
//			stripper.setSortByPosition(true);
//			stripper.setStartPage(1);
//			stripper.setEndPage(1);
//			String text = stripper.getText(document);
//			System.out.println(text);
//			document.close();
//			Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
//			keyInfoMap.put("text", text);
//			return keyInfoMap;
//		} catch (IOException e) {
//			System.out.println("出现错误");
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	private static Map<String, String> pdf2Text(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			// 文件不存在
			System.out.println("文件不存在");
			return null;
		}
		try {
			// 加载文件
			PDDocument document = PDDocument.load(file);
			// 获取总页数
			List<PDPage> pages = document.getDocumentCatalog().getAllPages();
			PDPage page = pages.get(0);
			BufferedImage img_temp = page.convertToImage();
	        Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix("png");  
	        ImageWriter writer = (ImageWriter) it.next();   
	        String saveName = fileName.substring(0,fileName.lastIndexOf(".")) + ".png";
	        ImageOutputStream imageout = ImageIO.createImageOutputStream(new FileOutputStream(saveName));  
	        writer.setOutput(imageout);  
	        writer.write(new IIOImage(img_temp, null, null));  
	        
	        //识别
	        HashMap<String, String> options = new HashMap<String, String>();

			String templateSign = "c25a91099e2f808c51e5f0a26d762e35";

			// 参数为本地图片路径
			JSONObject res = client.custom(saveName, templateSign, options);
//			System.out.println(res);
			JSONArray ret = res.getJSONObject("data").getJSONArray("ret");
			Map<String, String> keyInfoMap = new LinkedHashMap<String, String>();
			for (int i = 0; i < ret.length(); i++) {
				JSONObject item = ret.getJSONObject(i);
				String key = item.getString("word_name");
				String value = item.getString("word");
				keyInfoMap.put(key, value);
			}
//			File saveFile = new File(saveName);
//			saveFile.deleteOnExit();
			return keyInfoMap;
	        
	        
		} catch (IOException e) {
			System.out.println("出现错误");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException {
//		String[] s = {"820125012杨鹏展.pdf","820145018宋春萌.pdf","820145021王步文.pdf","任计龙820145016.pdf"};
//		Map<String, String> pdf2Text = pdf2Text("/Users/liuxining/Documents/QQ接收/PDF模板/" + s[0]);
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
		String dateStr = sdf.format(d);
		System.out.println(dateStr);
		//		System.out.println(pdf2Text);
//		Scanner scan = new Scanner(pdf2Text.get("text"));
//		while(scan.hasNext()){
//			String line = scan.nextLine();
//			if(line.contains("学位论文")){
//				while(scan.hasNext()){
//					String nextLine = scan.nextLine();
//					if(!isEmpty(nextLine)){
//						break;
//					}
//				}
//				String title = scan.nextLine();
//				title = title.trim();
//				System.out.println(title);
//				break;	
//			}
//		}
//		while(scan.hasNext()){
//			String line = scan.nextLine();
//			if(line.contains("培 养 单 位")){
//				line = line.substring(line.indexOf(":"));
//				line = line.trim();
//				System.out.println("培 养 单 位:" + line);
//				
//				break;	
//			}
//		}
	}

	 
	private static boolean isEmpty(String nextLine) {
		for(int i = 0;i < nextLine.length();i++){
			if(nextLine.charAt(0) != ' '){
				return false;
			}
		}
		return true;
	}
}
