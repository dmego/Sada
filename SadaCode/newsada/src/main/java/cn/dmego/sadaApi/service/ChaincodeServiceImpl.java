package cn.dmego.sadaApi.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.hyperledger.fabric.protos.common.Common.BlockHeader;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.UpgradeProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionEventException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dmego.sadaApi.model.Config;
import cn.dmego.sadaApi.model.ConfigHelper;
import cn.dmego.sadaApi.model.ConnectionUtil;
import cn.dmego.sadaApi.model.HyperUser;
import cn.dmego.sadaApi.model.Org;
import cn.dmego.sadaApi.model.Store;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.fail;

@Service(IChaincodeService.SERVICE_NAME)
@Transactional(readOnly = true)
public class ChaincodeServiceImpl implements IChaincodeService {

	private static final Logger logger = LoggerFactory.getLogger(ChaincodeServiceImpl.class);
	private static String PATH = Config.class.getClassLoader().getResource("") .getPath();
//	private static String PATH = Config.class.getClassLoader().getResource("") .getPath().substring(1);
	private static final Config Conf = Config.getConfig();
	private static String userName;	
	private static String adminName;	
	private static String chainCodePath;
	private static String chainCodeVersion;
	private static String channelName;
	private static int enrollNum = 0;
	int counter = 0;
	private final ConfigHelper configHelper = new ConfigHelper();
	ChaincodeID chaincodeID;
	private Collection<Org> SampleOrgs;
	HFClient client = HFClient.createNewInstance();
	
	//静态代码块,加载配置文件
	static{
		File file = new File(PATH + "/hyperledger.properties");
		Properties pro = new Properties();
		InputStream ino = null;
		try {
			ino = FileUtils.openInputStream(file);
			pro.load(ino);
			userName = pro.get("userName").toString();
			adminName = pro.get("adminName").toString();
			chainCodePath = pro.get("chainCodePath").toString();
			chainCodeVersion = pro.get("chainCodeVersion").toString();
			channelName = pro.get("channelName").toString();
		} catch (Exception e) {
			logger.error("读取  hyperledger.properties 配置文件失败");
			e.printStackTrace();
		}finally{
			try {
				if(ino!= null){
					ino.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static void out(String format, Object... args) {
		System.err.flush();
		System.out.flush();
		System.out.println(format(format, args));
		System.err.flush();
		System.out.flush();
	}

	/**
	 * takes input as chaincode name and returns chaincode id
	 * 生成链码ID
	 * @param chaincodename
	 * @return ChaincodeID
	 */
	public ChaincodeID getChaincodeId(String name) {
		chaincodeID = ChaincodeID.newBuilder().setName(name).setVersion(chainCodeVersion).setPath(chainCodePath)
				.build();
		return chaincodeID;
	}

	/**
	 * checking config at starting
	 * 检查配置文件
	 */
	public void checkConfig() {
		SampleOrgs = Conf.getSampleOrgs();
		if (counter == 0) {
			try {
				client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
				counter++;
			} catch (CryptoException | InvalidArgumentException e) {
				logger.error("ChaincodeServiceImpl | checkConfig | " + e.getMessage());
			}
		}
		for (Org sampleOrg : SampleOrgs) {
			try {
				sampleOrg.setCAClient(
						HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private void waitOnFabric(int additional) {
		// NOOP today
	}

	/**
	 * For loading user from persistence,if user already exists by taking as
	 * input username
	 * 从文件中读取注册的管理员
	 * @param username
	 * @return status as string
	 */
	public String loadUserFromPersistence(String name) {
		File sampleStoreFile = new File(PATH + "/HyperledgerEnroll.properties");
		final Store sampleStore = new Store(sampleStoreFile);
		for (Org sampleOrg : SampleOrgs) {
			final String orgName = sampleOrg.getName();
			HyperUser admin = sampleStore.getMember(adminName, orgName);
			sampleOrg.setAdmin(admin); // The admin of this org.
			// No need to enroll or register all done in End2endIt !
			HyperUser user = sampleStore.getMember(name, orgName);
			sampleOrg.addUser(user); // Remember user belongs to this Org
			sampleOrg.setPeerAdmin(sampleStore.getMember(orgName + "Admin", orgName));
			final String sampleOrgName = sampleOrg.getName();
			final String sampleOrgDomainName = sampleOrg.getDomainName();
			HyperUser peerOrgAdmin;
			try {
				peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName, sampleOrg.getMSPID(),
						ConnectionUtil.findFileSk(Paths.get(Conf.getChannelPath(), "crypto-config/peerOrganizations/",
								sampleOrgDomainName, format("/users/Admin@%s/msp/keystore", sampleOrgDomainName))
								.toFile()),
						Paths.get(Conf.getChannelPath(), "crypto-config/peerOrganizations/", sampleOrgDomainName,
								format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", sampleOrgDomainName,
										sampleOrgDomainName))
								.toFile());
				sampleOrg.setPeerAdmin(peerOrgAdmin);
			} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | IOException e) {
				logger.error("ChaincodeServiceImpl | loadUserFromPersistence | " + e.getMessage());
			}
		}
		return "Successfully loaded member from persistence";
	}

	/**
	 * Reconstruct the channel returns channel that has been reconstructed
	 * 重新创建通道
	 */
	public Channel reconstructChannel() {
		checkConfig();
		try {
			Org sampleOrg = Conf.getSampleOrg("peerOrg1");
			client.setUserContext(sampleOrg.getPeerAdmin());
			Channel newChannel = client.newChannel(channelName);
			for (String orderName : sampleOrg.getOrdererNames()) {
				newChannel.addOrderer(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName),
						Conf.getOrdererProperties(orderName)));
			}
			for (String peerName : sampleOrg.getPeerNames()) {
				logger.debug(peerName);
				String peerLocation = sampleOrg.getPeerLocation(peerName);
				Peer peer = client.newPeer(peerName, peerLocation, Conf.getPeerProperties(peerName));
				Set<String> channels = client.queryChannels(peer);
				if (!channels.contains(channelName)) {
					logger.info("Peer %s does not appear to belong to channel %s", peerName, channelName);
				}
				newChannel.addPeer(peer);
				sampleOrg.addPeer(peer);
			}
			for (String eventHubName : sampleOrg.getEventHubNames()) {
				EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
						Conf.getEventHubProperties(eventHubName));
				newChannel.addEventHub(eventHub);
			}
			newChannel.initialize();
			return newChannel;
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | reconstructChannel ");
			return null;
		}
	}

	/**
	 * enroll and register user at starting takes username as input returns
	 * status as string
	 * 注册管理员
	 */
	public String enrollAndRegister(String uname) {
		try {
			checkConfig();
			if(enrollNum == 0) {
				enrollNum = 1;
			}
			File sampleStoreFile = new File(PATH + "/HyperledgerEnroll.properties");
			final Store sampleStore = new Store(sampleStoreFile);
			for (Org sampleOrg : SampleOrgs) { 
				HFCAClient ca = sampleOrg.getCAClient();
				final String orgName = sampleOrg.getName();
				final String mspid = sampleOrg.getMSPID();
				ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
				HyperUser admin = sampleStore.getMember(adminName, orgName);
				if (!admin.isEnrolled()) { // Preregistered admin only needs to
					// be enrolled with Fabric caClient.
					admin.setEnrollment(ca.enroll(admin.getName(), "adminpw"));
					admin.setMspId(mspid);
				}
				sampleOrg.setAdmin(admin); // The admin of this org --
				if (sampleStore.hasMember(uname, sampleOrg.getName())) {
					String result = loadUserFromPersistence(uname);
					return result;
				}
				HyperUser user = sampleStore.getMember(uname, sampleOrg.getName());
				if (!user.isRegistered()) { // users need to be registered AND
					// enrolled
					RegistrationRequest rr = new RegistrationRequest(user.getName(), "org1.department1");
					user.setEnrollmentSecret(ca.register(rr, admin));
				}
				if (!user.isEnrolled()) {
					user.setEnrollment(ca.enroll(user.getName(), user.getEnrollmentSecret()));
					user.setMspId(mspid);
				}
				sampleOrg.addUser(user); // Remember user belongs to this Org
				final String sampleOrgName = sampleOrg.getName();
				final String sampleOrgDomainName = sampleOrg.getDomainName();
				HyperUser peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName,
						sampleOrg.getMSPID(),
						ConnectionUtil.findFileSk(Paths.get(Conf.getChannelPath(), "crypto-config/peerOrganizations/",
								sampleOrgDomainName, format("/users/Admin@%s/msp/keystore", sampleOrgDomainName))
								.toFile()),
						Paths.get(Conf.getChannelPath(), "crypto-config/peerOrganizations/", sampleOrgDomainName,
								format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", sampleOrgDomainName,
										sampleOrgDomainName))
								.toFile());
				sampleOrg.setPeerAdmin(peerOrgAdmin);
				return "User " + user.getName() + " Enrolled Successfuly";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ChaincodeServiceImpl | enrollAndRegister | " + e.getMessage());
			return "Failed to enroll user";
		}
		return "Something went wrong";
	}

	/**
	 * Construct the channel returns channel that has been constructed
	 * 创建通道
	 */
	public String constructChannel() {
		checkConfig();
		String result = enrollAndRegister(userName);
		if (result == "Failed to enroll user") {
			return result;
		}
		try {
			Org sampleOrg = Conf.getSampleOrg("peerOrg1");
			logger.info("Constructing channel %s", channelName);
			// Only peer Admin org
			client.setUserContext(sampleOrg.getPeerAdmin());
			Collection<Orderer> orderers = new LinkedList<>();
			for (String orderName : sampleOrg.getOrdererNames()) {
				Properties ordererProperties = Conf.getOrdererProperties(orderName);
				orderers.add(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName), ordererProperties));
			}
			// Just pick the first orderer in the list to create the channel.
			Orderer anOrderer = orderers.iterator().next();
			orderers.remove(anOrderer);
			ChannelConfiguration channelConfiguration = new ChannelConfiguration(
					new File(PATH + "/artifacts/channel/" + channelName + ".tx"));
			Channel newChannel = client.newChannel(channelName, anOrderer, channelConfiguration,
					client.getChannelConfigurationSignature(channelConfiguration, sampleOrg.getPeerAdmin()));
			logger.info("Created channel %s", channelName);
			for (String peerName : sampleOrg.getPeerNames()) {
				String peerLocation = sampleOrg.getPeerLocation(peerName);
				Properties peerProperties = Conf.getPeerProperties(peerName);
				if (peerProperties == null) {
					peerProperties = new Properties();
				}
				Peer peer = client.newPeer(peerName, peerLocation, peerProperties);
				newChannel.joinPeer(peer);
				logger.info("Peer %s joined channel %s", peerName, channelName);
				sampleOrg.addPeer(peer);
			}
			for (Orderer orderer : orderers) { // add remaining orderers if any.
				newChannel.addOrderer(orderer);
			}
			for (String eventHubName : sampleOrg.getEventHubNames()) {
				final Properties eventHubProperties = Conf.getEventHubProperties(eventHubName);
				EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
						eventHubProperties);
				newChannel.addEventHub(eventHub);
			}
			newChannel.initialize();
			logger.info("Finished initialization channel %s", channelName);
			return "Channel created successfully";
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | createChannel |" + e.getMessage());
			return "Something went wrong";
		}
	}

	Collection<ProposalResponse> responses;
	Collection<ProposalResponse> successful = new LinkedList<>();
	Collection<ProposalResponse> failed = new LinkedList<>();

	/**
	 * installs the chaincode takes as input chaincode name returns status as string
	 * 安装链码
	 */
	public String installChaincode(String chaincodeName) {
		try {
			checkConfig();
			String result = enrollAndRegister(userName);
			if (result == "Failed to enroll user") {
				return result;
			}
			chaincodeID = getChaincodeId(chaincodeName);
			Org sampleOrg = Conf.getSampleOrg("peerOrg1");
			Channel channel = reconstructChannel();
			final String channelName = channel.getName();
			boolean isFooChain = channelName.equals(channelName);
			logger.info("Running channel %s", channelName);
			Collection<Peer> channelPeers = channel.getPeers();
			Collection<Orderer> orderers = channel.getOrderers();

			client.setUserContext(sampleOrg.getPeerAdmin());
			logger.info("Creating install proposal");
			InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
			installProposalRequest.setChaincodeID(chaincodeID);
			installProposalRequest.setChaincodeSourceLocation(new File(PATH + "/artifacts/"));
			installProposalRequest.setChaincodeVersion(chainCodeVersion);
			logger.info("Sending install proposal");
			int numInstallProposal = 0;

			Set<Peer> peersFromOrg = sampleOrg.getPeers();
			numInstallProposal = numInstallProposal + peersFromOrg.size();
			responses = client.sendInstallProposal(installProposalRequest, peersFromOrg);
			for (ProposalResponse response : responses) {
				if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
					out("Successful install proposal response Txid: %s from peer %s", response.getTransactionID(),
							response.getPeer().getName());
					successful.add(response);
				} else {
					failed.add(response);
				}
			}
			SDKUtils.getProposalConsistencySets(responses);
			logger.info("Received %d install proposal responses. Successful+verified: %d . Failed: %d",
					numInstallProposal, successful.size());
			if (failed.size() > 0) {
				ProposalResponse first = failed.iterator().next();
				fail("Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage());
				return "Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage();
			}
			return "Chaincode installed successfully";
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | installChaincode | " + e.getMessage());
			return "Chaincode installation failed";
		}
	}

	/**
	 * instantiates the chaincode takes input chaincode name, function and args
	 * returns status as string
	 * 实例化链码
	 */
	public String instantiateChaincode(String chaincodeName, String chaincodeFunction, String[] chaincodeArgs) {
		try {
			checkConfig();
			String result = enrollAndRegister(userName);
			if (result == "Failed to enroll user") {
				return result;
			}
			chaincodeID = getChaincodeId(chaincodeName);
			Org sampleOrg = Conf.getSampleOrg("peerOrg1");
			Channel channel = reconstructChannel();
			final String channelName = channel.getName();
			boolean isFooChain = channelName.equals(channelName);
			Collection<Orderer> orderers = channel.getOrderers();
			InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
			instantiateProposalRequest.setProposalWaitTime(Conf.getProposalWaitTime());
			instantiateProposalRequest.setChaincodeID(chaincodeID);
			instantiateProposalRequest.setFcn(chaincodeFunction);
			instantiateProposalRequest.setArgs(chaincodeArgs);
			Map<String, byte[]> tm = new HashMap<>();
			tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
			tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
			instantiateProposalRequest.setTransientMap(tm);
			ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
			chaincodeEndorsementPolicy.fromYamlFile(new File(PATH + "/artifacts/chaincodeendorsementpolicy.yaml"));
			instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
			logger.info(
					"Sending instantiateProposalRequest to all peers with arguments: a and b set to 100 and %s respectively",
					"" + (200));
			successful.clear();
			failed.clear();
			if (isFooChain) { // Send responses both ways with specifying peers
				// and by using those on the channel.
				responses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
			} else {
				responses = channel.sendInstantiationProposal(instantiateProposalRequest);

			}
			for (ProposalResponse response : responses) {
				if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
					successful.add(response);
					logger.info("Succesful instantiate proposal response Txid: %s from peer %s",
							response.getTransactionID(), response.getPeer().getName());
				} else {
					failed.add(response);
				}
			}
			logger.info("Received %d instantiate proposal responses. Successful+verified: %d . Failed: %d",
					responses.size(), successful.size());
			if (failed.size() > 0) {
				ProposalResponse first = failed.iterator().next();

				return "Chaincode instantiation failed , reason " + "Not enough endorsers for instantiate :"
						+ successful.size() + "endorser failed with " + first.getMessage() + ". Was verified:"
						+ first.isVerified();
			}
			/// Send instantiate transaction to orderer
			logger.info("Sending instantiateTransaction to orderer with a and b set to 100 and %s respectively",
					"" + (200));
			logger.info("orderers", orderers);
			channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {
				waitOnFabric(0);
				logger.info("transaction event is valid", transactionEvent.isValid()); // must
				logger.info("Finished instantiate transaction with transaction id %s",
						transactionEvent.getTransactionID());
				return null;
			}).exceptionally(e -> {
				if (e instanceof TransactionEventException) {
					BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
					if (te != null) {
						fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
						logger.info("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage());
					}
				}
				logger.info(" failed with %s exception %s", e.getClass().getName(), e.getMessage());
				return null;
			}).get(Conf.getTransactionWaitTime(), TimeUnit.SECONDS);
			return "Chaincode instantiated Successfully";
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | instantiateChaincode |" + e.getMessage());
			return "Chaincode instantiation failed , reason " + e.getMessage();
		}
	}

	/**
	 * update the chaincode takes input chaincode name, function and args
	 * returns status as string
	 * 更新链码
	 */
	public String upgradeChaincode(String chaincodeName, String chaincodeFunction, String[] chaincodeArgs) {
		String installChaincode = installChaincode(chaincodeName);
		if (!"Chaincode installed successfully".equals(installChaincode)) {
			return installChaincode;
		}
		try {
			checkConfig();
			chaincodeID = getChaincodeId(chaincodeName);
			Org sampleOrg = Conf.getSampleOrg("peerOrg1");
			Channel channel = reconstructChannel();
			final String channelName = channel.getName();
			boolean isFooChain = channelName.equals(channelName);
			Collection<Orderer> orderers = channel.getOrderers();
			UpgradeProposalRequest upgradeProposalRequest = client.newUpgradeProposalRequest();
			upgradeProposalRequest.setProposalWaitTime(Conf.getProposalWaitTime());
			upgradeProposalRequest.setChaincodeID(chaincodeID);
			upgradeProposalRequest.setFcn(chaincodeFunction);
			upgradeProposalRequest.setArgs(chaincodeArgs);
			Map<String, byte[]> tm = new HashMap<>();
			tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
			tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
			upgradeProposalRequest.setTransientMap(tm);
			ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
			chaincodeEndorsementPolicy.fromYamlFile(new File(PATH + "/artifacts/chaincodeendorsementpolicy.yaml"));
			upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
			logger.info(
					"Sending instantiateProposalRequest to all peers with arguments: a and b set to 100 and %s respectively",
					"" + (200));
			successful.clear();
			failed.clear();
			if (isFooChain) { 
				responses = channel.sendUpgradeProposal(upgradeProposalRequest, channel.getPeers());
			} else {
				responses = channel.sendUpgradeProposal(upgradeProposalRequest);
			}
			for (ProposalResponse response : responses) {
				if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
					successful.add(response);
					logger.info("Succesful instantiate proposal response Txid: %s from peer %s",
							response.getTransactionID(), response.getPeer().getName());
				} else {
					failed.add(response);
				}
			}
			logger.info("Received %d instantiate proposal responses. Successful+verified: %d . Failed: %d",
					responses.size(), successful.size());
			if (failed.size() > 0) {
				ProposalResponse first = failed.iterator().next();

				return "Chaincode instantiation failed , reason " + "Not enough endorsers for instantiate :"
						+ successful.size() + "endorser failed with " + first.getMessage() + ". Was verified:"
						+ first.isVerified();
			}
			/// Send instantiate transaction to orderer
			logger.info("Sending instantiateTransaction to orderer with a and b set to 100 and %s respectively",
					"" + (200));
			logger.info("orderers", orderers);
			channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {
				waitOnFabric(0);
				logger.info("transaction event is valid", transactionEvent.isValid()); // must
				logger.info("Finished instantiate transaction with transaction id %s",
						transactionEvent.getTransactionID());

				return null;
			}).exceptionally(e -> {
				if (e instanceof TransactionEventException) {
					BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
					if (te != null) {
						fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
						logger.info("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage());
					}
				}
				logger.info(" failed with %s exception %s", e.getClass().getName(), e.getMessage());
				return null;
			}).get(Conf.getTransactionWaitTime(), TimeUnit.SECONDS);
			return "Chaincode instantiated Successfully";
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | instantiateChaincode |" + e.getMessage());
			return "Chaincode instantiation failed , reason " + e.getMessage();
		}
	}

	/**
	 * invokes the chaincode takes input chaincode name, function and args
	 * returns status as string
	 *调用链码方法
	 */
	public String invokeChaincode(String chaincodename, String chaincodeFunction, String[] chaincodeArgs) {
		try {
			checkConfig();
			if(enrollNum == 0) {
				enrollAndRegister(userName);
			}
			String result = enrollAndRegister(userName);
			if (result == "Failed to enroll user") {
				return result;
			}
			chaincodeID = getChaincodeId(chaincodename);
			Org sampleOrg = Conf.getSampleOrg("peerOrg1");
			Channel channel = reconstructChannel();
			successful.clear();
			failed.clear();
			/// Send transaction proposal to all peers
			logger.debug("chaincodeFunction" + chaincodeFunction);
			TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
			transactionProposalRequest.setChaincodeID(chaincodeID);
			transactionProposalRequest.setFcn(chaincodeFunction);
			transactionProposalRequest.setProposalWaitTime(Conf.getProposalWaitTime());
			transactionProposalRequest.setArgs(chaincodeArgs);
			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
			tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
			tm2.put("result", ":)".getBytes(UTF_8)); /// This should be returned
			transactionProposalRequest.setTransientMap(tm2);
			logger.info("sending transactionProposal to all peers ");
			Collection<ProposalResponse> transactionPropResp = channel
					.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
			for (ProposalResponse response : transactionPropResp) {
				if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
					logger.info("Successful transaction proposal response Txid: %s from peer %s",
							response.getTransactionID(), response.getPeer().getName());
					successful.add(response);
				} else {
					failed.add(response);
				}
			}
			Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils
					.getProposalConsistencySets(transactionPropResp);
			if (proposalConsistencySets.size() != 1) {
				logger.info(format("Expected only one set of consistent proposal responses but got %d",
						proposalConsistencySets.size()));
			}
			logger.info("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d",
					transactionPropResp.size(), successful.size());
			if (failed.size() > 0) {
				ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
				logger.info("Not enough endorsers for invoke(move a,b,100):" + failed.size() + " endorser error: "
						+ firstTransactionProposalResponse.getMessage() + ". Was verified: "
						+ firstTransactionProposalResponse.isVerified());
			}
			logger.info("Successfully received transaction proposal responses.");
			ProposalResponse resp = transactionPropResp.iterator().next();
			byte[] x = resp.getChaincodeActionResponsePayload(); // This is the
			String resultAsString = null;
			if (x != null) {
				resultAsString = new String(x, "UTF-8");
			}
			logger.debug("getChaincodeActionResponseReadWriteSetInfo:::"
					+ resp.getChaincodeActionResponseReadWriteSetInfo());
			ChaincodeID cid = resp.getChaincodeID();
			// Send Transaction Transaction to orderer
			logger.info("Sending chaincode transaction(move a,b,100) to orderer.");
			channel.sendTransaction(successful).thenApply(transactionEvent -> {
				waitOnFabric(0);
				logger.info("transaction event is valid", transactionEvent.isValid()); // must
				logger.info("Finished invoke transaction with transaction id %s", transactionEvent.getTransactionID());
				return "Chaincode invoked successfully " + transactionEvent.getTransactionID();
			}).exceptionally(e -> {
				if (e instanceof TransactionEventException) {
					BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
					if (te != null) {
						fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
						logger.info("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage());
					}
				}
				logger.info("failed with %s exception %s", e.getClass().getName(), e.getMessage());
				return "Error";
			}).get(Conf.getTransactionWaitTime(), TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Caught an exception while invoking chaincode");
			logger.error("ChaincodeServiceImpl | invokeChaincode | " + e.getMessage());
			return "Caught an exception while invoking chaincode";
		}
		return "Transaction invoked successfully";
	}

	/**
	 * queries the chaincode takes input chaincode name, function and args
	 * returns payload from blockchain as string
	 * 调用链码查询方法
	 */
	public String queryChaincode(String name, String chaincodeFunction, String[] chaincodeArgs) {
		try {
			checkConfig();
			if(enrollNum == 0) {
				enrollAndRegister(userName);
			}
			String result = enrollAndRegister(userName);
			if (result == "Failed to enroll user") {
				return result;
			}
			chaincodeID = getChaincodeId(name);
			Channel channel = reconstructChannel();
			logger.debug("Now query chaincode for the value of b.");
			logger.debug(chaincodeFunction);
			QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
			queryByChaincodeRequest.setArgs(chaincodeArgs);
			queryByChaincodeRequest.setFcn(chaincodeFunction);
			queryByChaincodeRequest.setChaincodeID(chaincodeID);
			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
			tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
			queryByChaincodeRequest.setTransientMap(tm2);
			Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest,
					channel.getPeers());
			for (ProposalResponse proposalResponse : queryProposals) {
				if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
					logger.debug("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: "
							+ proposalResponse.getStatus() + ". Messages: " + proposalResponse.getMessage()
							+ ". Was verified : " + proposalResponse.isVerified());
				} else {
					String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
					return payload;
				}
			}
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | queryChaincode | " + e.getMessage());
		}
		return "Caught an exception while quering chaincode";
	}

	/**
	 * gives blockchain info
	 * 获取区块链区块详细信息
	 */
	public void blockchainInfo(Org sampleOrg, Channel channel) {
		try {
			checkConfig();
			String channelName = channel.getName();
			Set<Peer> peerSet = sampleOrg.getPeers();
			BlockchainInfo channelInfo = channel.queryBlockchainInfo();
			logger.info("Channel info for : " + channelName);
			logger.info("Channel height: " + channelInfo.getHeight());
			String chainCurrentHash = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
			String chainPreviousHash = Hex.encodeHexString(channelInfo.getPreviousBlockHash());
			logger.info("Chain current block hash: " + chainCurrentHash);
			logger.info("Chainl previous block hash: " + chainPreviousHash);
			// Query by block number. Should return latest block, i.e. block
			// number 2
			BlockInfo returnedBlock = channel.queryBlockByNumber(channelInfo.getHeight() - 1);
			String previousHash = Hex.encodeHexString(returnedBlock.getPreviousHash());
			logger.info("queryBlockByNumber returned correct block with blockNumber " + returnedBlock.getBlockNumber()
					+ " \n previous_hash " + previousHash);
			// Query by block hash. Using latest block's previous hash so should
			// return block number 1
			byte[] hashQuery = returnedBlock.getPreviousHash();
			returnedBlock = channel.queryBlockByHash(hashQuery);
			logger.info("queryBlockByHash returned block with blockNumber " + returnedBlock.getBlockNumber());
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | blockchainInfo | " + e.getMessage());
		}
	}
	
	/**
	 * gives blockchain info
	 * 获取区块详细信息
	 * @return
	 */
	public String getBlockInfo() {
		try {
			checkConfig();
			if(enrollNum == 0) {
				enrollAndRegister(userName);
			}
			String result = enrollAndRegister(userName);
			if (result == "Failed to enroll user") {
				return result;
			}
			Channel channel = reconstructChannel();
			String channelName = channel.getName();
			BlockchainInfo channelInfo = channel.queryBlockchainInfo();
			logger.info("Channel info for : " + channelName);
			logger.info("Channel height: " + channelInfo.getHeight());
			long height = channelInfo.getHeight() - 1;
			long txSum = 0;
			StringBuffer buffer = new StringBuffer();
			buffer.append("{\"blocks\":[");
			boolean isFirst = true;
			String preHashRecord = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
			while (height >= 0) {
				BlockInfo blockInfo = channel.queryBlockByNumber(height--);
				BlockHeader header = blockInfo.getBlock().getHeader();
				long number = header.getNumber();
				String dataHash = Hex.encodeHexString(header.getDataHash().toByteArray());
				String preHash = Hex.encodeHexString(header.getPreviousHash().toByteArray());
				Iterable<EnvelopeInfo> iterable = blockInfo.getTransactionActionInfos();
				if (!isFirst) {
					buffer.append(",");
				} else {
					isFirst = false;
				}
				buffer.append("{\"id\":\"" + (number + 1) + "\",\"blockNum\":\"" + number + "\",\"preHash\":\"" + preHash + "\",\"dataHash\":\""
						+ dataHash + "\",\"currHash\":\"" + preHashRecord + "\",\"txInfo\":[");
				preHashRecord = preHash;
				long txNum = 0;
				boolean txIsFirst = true;
				Date date = new Date();
				long timestamp = date.getTime();
				for (EnvelopeInfo envelopeInfo : iterable) {
					date = envelopeInfo.getTimestamp();
					timestamp = date.getTime();
					txNum++;
					if (!txIsFirst) {
						buffer.append(",");
					} else {
						txIsFirst = false;
					}
					buffer.append("\"" + envelopeInfo.getTransactionID() + "\"");
				}
				txSum += txNum;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateStr = sdf.format(new Date(timestamp));
				buffer.append("],\"txNum\":\"" + txNum + "\",\"date\":\"" + dateStr + "\",\"timestamp\":\"" + timestamp + "\"}");
			}
			buffer.append("]");
			buffer.append(",\"blockNum\":\"" + (channelInfo.getHeight() - 1) + "\"");
			buffer.append(",\"channelName\":\"" + channelName + "\"");
			buffer.append(",\"txSum\":\"" + txSum + "\"}");
			return buffer.toString();
		} catch (Exception e) {
			logger.error("ChaincodeServiceImpl | blockchainInfo | " + e.getMessage());
		}
		return null;
	}
}
