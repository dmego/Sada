package cn.dmego.sadaApi.test;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dmego.sadaApi.dto.FunctionAndArgsDto;
import cn.dmego.sadaApi.dto.UserDto;
import cn.dmego.sadaApi.service.IChaincodeService;
import cn.dmego.sadaApi.service.ChaincodeServiceImpl;

public class TestDemo {
	private static final Logger logger = LoggerFactory.getLogger(ChaincodeServiceImpl.class);
	private static IChaincodeService chaincodeService = new ChaincodeServiceImpl();
	private static String uname;
	private static String PATH = System.getProperty("user.dir")+"/src/main/java/cn/dmego/sadaApi/config";
	
	//静态代码块,加载配置文件
		static{			
			File file = new File(PATH + "/hyperledger.properties");
			Properties pro = new Properties();
			try {
				InputStream ino = FileUtils.openInputStream(file);
				pro.load(ino);
				uname = pro.getProperty("userName");
				ino.close();
			} catch (Exception e) {
				logger.error("读取  hyperledger.properties 配置文件失败");
				e.printStackTrace();
			}
		}
	
	public static void main(String[] args) {
		String path = TestDemo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println(path);
		System.out.println(PATH);

	}
	
	@Test //测试注册管理员
	public void testEroll() {
		String result = chaincodeService.enrollAndRegister("ww");
		System.out.println(result);
	}
	
	@Test //测试创建通道
	public void TestConstruct(){
			String response;
			String result = chaincodeService.enrollAndRegister(uname);
			if (result != "Failed to enroll user") {
				try {
					response = chaincodeService.constructChannel();
					if(response=="Channel created successfully")
					{
						System.out.println("channel created successfully");
					}
					else
					{
						System.out.println("Something went wrong");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}	
	}
	
	@Test //测试安装链码
	public void TestInstall(){
		String result = chaincodeService.enrollAndRegister(uname);
		if (result != "Failed to enroll user") {
			String response=chaincodeService.installChaincode("mycc");
			if(response=="Chaincode installed successfully")
			{
				System.out.println(response);
			}
			else {
				System.out.println(response);
			}
		} else {
			System.out.println("Something went wrong");
		}
	}
	
	
	@Test //测试实例化链码
	public void testInstantiate(){
		FunctionAndArgsDto chaincodeDto = new FunctionAndArgsDto();
		chaincodeDto.setFunction("init");
		chaincodeDto.setChaincodeName("mycc");
		String[] args = {};
		chaincodeDto.setArgs(args);
		
		if ((chaincodeDto.getFunction()) == null) {
			System.out.println("function not present in method body");
		}
		if (chaincodeDto.getArgs() == null) {
			System.out.println("args not present in method body");
		}
		if(uname!=null)
		{
		String result = chaincodeService.enrollAndRegister(uname);
		if (result != "Failed to enroll user") {
			String response=chaincodeService.instantiateChaincode(chaincodeDto.getChaincodeName(), chaincodeDto.getFunction(),
					chaincodeDto.getArgs());
			if(response=="Chaincode instantiated Successfully")
			{
				System.out.println(response);
			}
			else {
				System.out.println(response);
			}
		} else {
			System.out.println("Something went wrong");
		}
	}	
	}
	
	/**
	 * 测试调用链码的方法
	 */
	@Test
	public void testInvoke(){
		FunctionAndArgsDto chaincodeDto = new FunctionAndArgsDto();
		chaincodeDto.setFunction("invoke");
		chaincodeDto.setChaincodeName("mycc");
		String[] args = { "save", "id2","assetName","keyInfo","userName","MD5","assetType","fileName","filePath"};
		chaincodeDto.setArgs(args);
		if ((chaincodeDto.getFunction()) == null) {
			System.out.println("function not present in method body");
		}
		if (chaincodeDto.getArgs() == null) {
			System.out.println("args not present in method body");
		}
		if(uname!=null)
		{
			String result = chaincodeService.enrollAndRegister(uname);
			if (result != "Failed to enroll user") {
			String response=chaincodeService.invokeChaincode(chaincodeDto.getChaincodeName(), chaincodeDto.getFunction(),
					chaincodeDto.getArgs());
			if(response=="Transaction invoked successfully")
			{
				System.out.println(response);
			}
			else
			{
				System.out.println(response);
			}
		} else {
			System.out.println("Something went wrong");
		}

		}
	}
	
	@Test
	public void testQuery(){	
		if(uname!=null)
		{
		String result = chaincodeService.enrollAndRegister(uname);

		if (result != "Failed to enroll user") {

			String ChaincodeName = "mycc";
			String ChaincodeFunction = "invoke";
			String[] ChaincodeArgs = {"query","id2"};
			
			String response = chaincodeService.queryChaincode(ChaincodeName, ChaincodeFunction, ChaincodeArgs);
			if(response !="Caught an exception while quering chaincode")
			{
				System.out.println(response);
			}
			else
			{
				System.out.println(response);
			}
		} else {
			System.out.println("Something went wrong");
		}
		}	
	}
}
