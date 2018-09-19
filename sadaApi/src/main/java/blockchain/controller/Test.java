package blockchain.controller;

import java.util.Date;

import blockchain.dto.FunctionAndArgsDto;
import blockchain.dto.UserDto;
import blockchain.service.ChaincodeService;
import blockchain.service.ChaincodeServiceImpl;

public class Test {
	private static final long EXPIRATIONTIME = 900000;
	private static ChaincodeService chaincodeService = new ChaincodeServiceImpl();
	private static UserDto user = new UserDto("maqe");
	
	public static void main(String[] args) {
		System.out.println(user.getUsername());
		//testEroll();
		//TestConstruct();
	}
	
	@org.junit.Test //测试注册管理员
	public void testEroll() {
		String result = chaincodeService.enrollAndRegister(user.getUsername());
		if (result != "Failed to enroll user") {
			String jwtToken = "";
			if (user.getUsername() == null) {
				System.out.println("please enter username in request body");
			}
			String username = user.getUsername();			
			System.out.println(result + "  jwt:" + jwtToken);
		}	
	}
	
	@org.junit.Test //测试创建通道
	public void TestConstruct(){
			String response;
			String result = chaincodeService.enrollAndRegister("maqe");
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
	
	@org.junit.Test //测试安装链码
	public void TestInstall(){
		String result = chaincodeService.enrollAndRegister("maqe");
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
	
	
	@org.junit.Test //测试实例化链码
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
		String uname = "maqe";
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
	@org.junit.Test
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
		String uname = "maqe";
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
	
	@org.junit.Test
	public void testQuery(){	
		String uname =  "maqe";
		if(uname!=null)
		{
		String result = chaincodeService.enrollAndRegister(uname);

		if (result != "Failed to enroll user") {

			String ChaincodeName = "mycc";
			String ChaincodeFunction = "invoke";
			String[] ChaincodeArgs = {"query","id"};
			
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
