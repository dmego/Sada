package cn.dmego.sadaApi.service;

import org.hyperledger.fabric.sdk.Channel;

import cn.dmego.sadaApi.model.Org;


public interface IChaincodeService {
	
	public static final String SERVICE_NAME = "cn.dmego.sadaApi.service.ChaincodeServiceImpl";
	
	public Channel reconstructChannel() throws Exception;

	public String enrollAndRegister(String uname);

	public String constructChannel() throws Exception;

	public String installChaincode(String chaincodeName);

	public String instantiateChaincode(String chaincodeName, String chaincodeFunction, String[] chaincodeArgs);

	public String upgradeChaincode(String chaincodeName, String chaincodeFunction, String[] chaincodeArgs);
	
	public String invokeChaincode(String name, String chaincodeFunction, String[] chaincodeArgs);

	public String queryChaincode(String name, String chaincodeFunction, String[] chaincodeArgs);

	public void blockchainInfo(Org sampleOrg, Channel channel);
	
	public String getBlockInfo();

}
