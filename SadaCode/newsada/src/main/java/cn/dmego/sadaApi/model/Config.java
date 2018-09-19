package cn.dmego.sadaApi.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.helper.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dmego.sadaApi.service.ChaincodeServiceImpl;
import cn.dmego.sadaApi.test.TestDemo;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);
	private static final String PROPBASE = "config.";
	//private static String PATH = Config.class.getClassLoader().getResource("") .getPath();
	private static String PATH = Config.class.getClassLoader().getResource("") .getPath().substring(1);
	private static final String GOSSIPWAITTIME = PROPBASE + "GossipWaitTime";
	private static final String INVOKEWAITTIME = PROPBASE + "InvokeWaitTime";
	private static final String DEPLOYWAITTIME = PROPBASE + "DeployWaitTime";
	private static final String PROPOSALWAITTIME = PROPBASE + "ProposalWaitTime";

	private static final String ORGS = PROPBASE + "property.";
	private static final Pattern orgPat = Pattern.compile("^" + Pattern.quote(ORGS) + "([^\\.]+)\\.mspid$");

	private static final String BLOCKCHAINTLS = PROPBASE + "blockchain.tls";

	private static Config config;
	public static final Properties sdkProperties = new Properties();
	private final boolean runningTLS;
	private final boolean runningFabricCATLS;
	private final boolean runningFabricTLS;
	private static final HashMap<String, Org> sampleOrgs = new HashMap<>();

	private Config() {
		try {
			/**
			 * All the properties will be obtained from config.properties file
			 * 读取配置文件
			 */
			System.out.println(PATH);
			sdkProperties.load(new FileInputStream(PATH+"/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("Failed to load any configuration");
		} finally {
			// Default values
			//如果配置文件读取不成功，采用默认参数
			defaultProperty(GOSSIPWAITTIME, "5000");
			defaultProperty(INVOKEWAITTIME, "100000");
			defaultProperty(DEPLOYWAITTIME, "120000");
			defaultProperty(PROPOSALWAITTIME, "240000");

			defaultProperty(ORGS + "peerOrg1.mspid", "Org1MSP");
			defaultProperty(ORGS + "peerOrg1.domname", "org1.example.com");
			defaultProperty(ORGS + "peerOrg1.ca_location", "http://192.168.60.129:7054");
			defaultProperty(ORGS + "peerOrg1.peer_locations",
					"peer0.org1.example.com@grpc://192.168.60.129:7051, peer1.org1.example.com@grpc://192.168.60.129:8051");
			defaultProperty(ORGS + "peerOrg1.orderer_locations", "orderer.example.com@grpc://192.168.60.129:7050");
			defaultProperty(ORGS + "peerOrg1.eventhub_locations",
					"peer0.org1.example.com@grpc://192.168.60.129:7053,peer1.org1.example.com@grpc://192.168.60.129:8053");

			defaultProperty(BLOCKCHAINTLS, null);
			runningTLS = null != sdkProperties.getProperty(BLOCKCHAINTLS, null);
			runningFabricCATLS = runningTLS;
			runningFabricTLS = runningTLS;

			for (Map.Entry<Object, Object> x : sdkProperties.entrySet()) {
				final String key = x.getKey() + "";
				final String val = x.getValue() + "";

				if (key.startsWith(ORGS)) {
					Matcher match = orgPat.matcher(key);
					if (match.matches() && match.groupCount() == 1) {
						String orgName = match.group(1).trim();
						sampleOrgs.put(orgName, new Org(orgName, val.trim()));
					}
				}
			}

			for (Map.Entry<String, Org> org : sampleOrgs.entrySet()) {
				final Org sampleOrg = org.getValue();
				final String orgName = org.getKey();
				String peerNames = sdkProperties.getProperty(ORGS + orgName + ".peer_locations");
				String[] ps = peerNames.split("[ \t]*,[ \t]*");
				for (String peer : ps) {
					String[] nl = peer.split("[ \t]*@[ \t]*");
					sampleOrg.addPeerLocation(nl[0], grpcTLSify(nl[1]));
				}
				final String domainName = sdkProperties.getProperty(ORGS + orgName + ".domname");
				sampleOrg.setDomainName(domainName);
				String ordererNames = sdkProperties.getProperty(ORGS + orgName + ".orderer_locations");
				ps = ordererNames.split("[ \t]*,[ \t]*");
				for (String peer : ps) {
					String[] nl = peer.split("[ \t]*@[ \t]*");
					sampleOrg.addOrdererLocation(nl[0], grpcTLSify(nl[1]));
				}
				String eventHubNames = sdkProperties.getProperty(ORGS + orgName + ".eventhub_locations");
				ps = eventHubNames.split("[ \t]*,[ \t]*");
				for (String peer : ps) {
					String[] nl = peer.split("[ \t]*@[ \t]*");
					sampleOrg.addEventHubLocation(nl[0], grpcTLSify(nl[1]));
				}
				sampleOrg.setCALocation(httpTLSify(sdkProperties.getProperty((ORGS + org.getKey() + ".ca_location"))));
				if (true) {
					String cert = PATH + "artifacts/channel/crypto-config/peerOrganizations/DNAME/ca/ca.DNAME-cert.pem"
							.replaceAll("DNAME", domainName);
					File cf = new File(cert);
					if (!cf.exists() || !cf.isFile()) {
						throw new RuntimeException(" missing cert file " + cf.getAbsolutePath());
					}
					Properties properties = new Properties();
					properties.setProperty("pemFile", cf.getAbsolutePath());

					properties.setProperty("allowAllHostNames", "true");

					sampleOrg.setCAProperties(properties);
				}
			}
		}
	}
	private String grpcTLSify(String location) {
		location = location.trim();
		Exception e = Utils.checkGrpcUrl(location);
		if (e != null) {
			throw new RuntimeException(String.format("Bad  parameters for grpc url %s", location), e);
		}
		return runningFabricTLS ? location.replaceFirst("^grpc://", "grpcs://") : location;
	}

	private String httpTLSify(String location) {
		location = location.trim();
		return runningFabricCATLS ? location.replaceFirst("^http://", "https://") : location;
	}
	
	/**
	 * getConfig return back singleton for SDK configuration.
	 *获取配置文件
	 * @return Global configuration
	 */
	public static Config getConfig() {
		if (null == config) {
			config = new Config();
		}
		return config;

	}

	/**
	 * getProperty return back property for the given value.
	 *
	 * @param property
	 * @return String value for the property
	 */
	private String getProperty(String property) {
		String ret = sdkProperties.getProperty(property);
		if (null == ret) {
			logger.warn(String.format("No configuration value found for '%s'", property));
		}
		return ret;
	}

	private static void defaultProperty(String key, String value) {
		String ret = System.getProperty(key);
		if (ret != null) {
			sdkProperties.put(key, ret);
		} else {
			String envKey = key.toUpperCase().replaceAll("\\.", "_");
			ret = System.getenv(envKey);
			if (null != ret) {
				sdkProperties.put(key, ret);
			} else {
				if (null == sdkProperties.getProperty(key) && value != null) {
					sdkProperties.put(key, value);
				}
			}
		}
	}

	public int getTransactionWaitTime() {
		return Integer.parseInt(getProperty(INVOKEWAITTIME));
	}

	public int getDeployWaitTime() {
		return Integer.parseInt(getProperty(DEPLOYWAITTIME));
	}

	public int getGossipWaitTime() {
		return Integer.parseInt(getProperty(GOSSIPWAITTIME));
	}

	public long getProposalWaitTime() {
		return Integer.parseInt(getProperty(PROPOSALWAITTIME));
	}

	public Collection<Org> getSampleOrgs() {
		return Collections.unmodifiableCollection(sampleOrgs.values());
	}

	public Org getSampleOrg(String name) {
		return sampleOrgs.get(name);
	}

	public Properties getPeerProperties(String name) {
		return getEndPointProperties("peer", name);

	}

	public Properties getOrdererProperties(String name) {
		return getEndPointProperties("orderer", name);

	}

	private Properties getEndPointProperties(final String type, final String name) {
		final String domainName = getDomainName(name);
		File cert = Paths.get(getChannelPath(), "crypto-config/ordererOrganizations".replace("orderer", type),
				domainName, type + "s", name, "tls/server.crt").toFile();
		if (!cert.exists()) {
			throw new RuntimeException(String.format("Missing cert file for: %s. Could not find at location: %s", name,
					cert.getAbsolutePath()));
		}

		Properties ret = new Properties();
		ret.setProperty("pemFile", cert.getAbsolutePath());
		ret.setProperty("hostnameOverride", name);
		ret.setProperty("sslProvider", "openSSL");
		ret.setProperty("negotiationType", "TLS");
		return ret;
	}

	public Properties getEventHubProperties(String name) {
		return getEndPointProperties("peer", name); // uses same as named peer

	}

	public String getChannelPath() {
		/**
		 * for loading properties from hyperledger.properties file
		 * 获取通道路径
		 */
		Properties hyperproperties = new Properties();
		try {
			hyperproperties.load(new FileInputStream(PATH+"/hyperledger.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return PATH+"/artifacts/channel";
	}

	private String getDomainName(final String name) {
		int dot = name.indexOf(".");
		if (-1 == dot) {
			return null;
		} else {
			return name.substring(dot + 1);
		}
	}
}
