package cn.dmego.web.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;

import cn.dmego.domain.Asset;
import cn.dmego.domain.Tag;
import cn.dmego.domain.User;
import cn.dmego.exception.MyException;
import cn.dmego.pojo.Result;
import cn.dmego.pojo.TreeNode;
import cn.dmego.sadaApi.service.IChaincodeService;
import cn.dmego.service.IAssetService;
import cn.dmego.utils.ChainMutualUtil;
import cn.dmego.utils.VauleUtil;

/**
 * @ClassName: AssetAction
 * @Description: 资产Action类
 * @author liuxining
 * @date 2018年4月16日
 * 
 */
@SuppressWarnings("serial")
@Controller("assetAction")
@Scope(value = "prototype")
public class AssetAction extends BaseAction<Asset> {
	private Asset asset = this.getModel();

	private static Logger logger = Logger.getLogger(AssetAction.class);

	// 注入AssetServiceImpl
	@Resource(name = IAssetService.SERVICE_NAME)
	private IAssetService assetService;
	
	/**注入链码Service*/
	@Resource(name=IChaincodeService.SERVICE_NAME)
	private IChaincodeService chaincodeService;

	private String code;// 资产类型code
	private String page;// 页数
	
	public String queryBlockInfo() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chaincodeService.getBlockInfo();
		result.setData(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String compareAssetFileMd5() throws Exception {
		Result result = new Result();
		result.setSuccess(true);		
		
		try {
			// 获取该文件的md5
			ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
			String path = sc.getRealPath("/");
			String fileAbstractPath = path + asset.getFilePath() + "/" + asset.getFileName();
			File file = new File(fileAbstractPath);
			if (!file.exists()) {
				throw new MyException("鉴权上传的文件不存在！");
			}
			String md5Hex = DigestUtils.md5Hex(fileAbstractPath);
			logger.debug("fileAbstractPath:" + fileAbstractPath);
			logger.debug("md5Hex:" + md5Hex);

			String chainAssetResp = chaincodeService.queryChaincode(ChainMutualUtil.chainCodeName, "invoke", new String[] {"query",asset.getId()});
			if (chainAssetResp == null || chainAssetResp.isEmpty()) {
				throw new MyException("从区块链中获取资产信息失败");
			}

			JSONObject chainAssetJSON = JSON.parseObject(chainAssetResp);
			String chainAssetMd5 = chainAssetJSON.getString("assetMd5");

			if (chainAssetMd5 == null || chainAssetMd5.isEmpty()) {
				throw new MyException("区块链中资产关键信息为空");
			}

			HashMap<String, String> compareResp = new HashMap<String, String>();
			if (md5Hex.equals(chainAssetMd5)) {
				compareResp.put("compareResult", "yes");
			} else {
				compareResp.put("compareResult", "no");
			}
			compareResp.put("nMd5", md5Hex);
			compareResp.put("chainAssetMd5", chainAssetMd5);

			result.setData(JSON.toJSONString(compareResp));

		} catch (MyException e) {
			result.setMessage(e.getMessage());
		} catch (Exception e1) {
			throw e1;
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";
	}

	public String toCompareMd5() throws Exception {
//		Map request = (Map) ActionContext.getContext().get("request");
//		request.put("id", asset.getId());
//		request.put("fileName", asset.getFileName());
//		request.put("filePath", asset.getFilePath());
		return "toCompareMd5";
	}
	
	public String validateMd5() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		try {
			
			ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
			String path = sc.getRealPath("/");
			String fileAbstractPath = path + asset.getFilePath() + "/" + asset.getFileName();
			File file = new File(fileAbstractPath);
			if (!file.exists()) {
				throw new MyException("文件不存在！");
			}
			InputStream in = new FileInputStream(file);
			String md5Hex = DigestUtils.md5Hex(in);
			
			String chainAssetMd5 = asset.getAssetMd5();
			if (chainAssetMd5 == null || chainAssetMd5.isEmpty()) {
				throw new MyException("区块链中资产关键信息为空");
			}
			if (md5Hex.equals(chainAssetMd5)) {
				result.setData("success");
			} else {
				result.setData("error");
			}
		}
		catch (Exception e) {
			result.setMessage(e.getMessage());
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";
	}

	public String getFileKeyInfoAndCompare() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		try {
			String id = asset.getId();
			if (id == null || id.isEmpty()) {
				throw new MyException("资产id参数为空！");
			}
			String chainAssetResp = chaincodeService.queryChaincode(ChainMutualUtil.chainCodeName, "invoke", new String[] {"query",id});
//			String chainAssetResp = "{\"assetMd5\":\"efa65101b8fdf029cbdfd6de7b898ddf\",\"assetType\":\"IDCARD\",\"fileName\":\"247ed275463c4585914a52c3415585bd_小凯.png\",\"filePath\":\"upload/xiaokai/图片\",\"id\":\"4028bc8164e43d3a0164e43f52740000\",\"keyInfo\":\"{\\\"住址\\\":\\\"河北省石家庄市北二环东17号8栋1单元516室\\\",\\\"公民身份号码\\\":\\\"130103199703093819\\\",\\\"出生\\\":\\\"1997年03月09日\\\",\\\"姓名\\\":\\\"小凯\\\",\\\"性别\\\":\\\"男\\\",\\\"民族\\\":\\\"汉\\\",}\",\"name\":\"身份证\",\"owner\":\"xiaokai\"}";
			if (chainAssetResp == null || chainAssetResp.isEmpty()) {
				throw new MyException("从区块链中获取资产信息失败");
			}

			JSONObject chainAssetJSON = JSON.parseObject(chainAssetResp);

			String assetTypeCode = chainAssetJSON.getString("assetType");

			String basePath = ServletActionContext.getServletContext().getRealPath(File.separator);
			Map<String, String> nKeyInfo = assetService.getKeyInfo(
					basePath + File.separator + asset.getFilePath() + File.separator + asset.getFileName(),
					assetTypeCode);

			String chainKeyInfo = chainAssetJSON.getString("keyInfo");
			HashMap<String, String> resp = new HashMap<String, String>();
			HashMap<String, String> compareResult = assetService.compareKeyInfo(JSON.toJSONString(nKeyInfo),
					chainKeyInfo, assetTypeCode);
			resp.put("compareResult", JSON.toJSONString(compareResult));
			resp.put("chainAsset", chainAssetResp);
			resp.put("nKeyInfo", JSON.toJSONString(nKeyInfo));

			// 比对md5
//			
//			resp.put("nMd5", md5Hex);
			

			result.setData(JSON.toJSONString(resp));

		} catch (MyException e) {
			result.setMessage(e.getMessage());
		} catch (Exception e1) {
			throw e1;
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";
	}

	public String deleteAsset() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		try {

			String basePath = ServletActionContext.getServletContext().getRealPath(File.separator);
			File file = new File(
					basePath + File.separator + asset.getFilePath() + File.separator + asset.getFileName());
			file.deleteOnExit();

			String id = asset.getId();
			if (id == null || id.isEmpty()) {
				throw new MyException("要删除的资产id为空");
			}
			String deleteAsset = assetService.deleteAsset(id);
			if (deleteAsset != null && deleteAsset.equals("success")) {
				String chainResp = chaincodeService.invokeChaincode(ChainMutualUtil.chainCodeName, "invoke", new String[] {"delete",id});
				result.setMessage("删除成功");
			} else {
				result.setSuccess(false);
				result.setMessage(deleteAsset);
			}

		} catch (MyException e) {
			result.setMessage(e.getMessage());
		} catch (Exception e1) {
			throw e1;
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";
	}

	public String getUserTag() throws Exception {
		User loginUser = (User) ActionContext.getContext().getSession().get("user");
		String userId = loginUser.getId();
		List<Tag> list = assetService.getUserTag(userId);
		Result result = new Result();
		if (list != null) {
			result.setSuccess(true);
			result.setData(list);
		} else {
			result.setSuccess(false);
			result.setMessage("获取用户标签列表失败");
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";

	}

	public String getFileKeyInfo() throws Exception {
		String basePath = ServletActionContext.getServletContext().getRealPath(File.separator);
		Map<String, String> keyInfo = assetService.getKeyInfo(
				basePath + File.separator + asset.getFilePath() + File.separator + asset.getFileName(), code);
		Result result = new Result();
		if (keyInfo != null) {
			result.setSuccess(true);
			result.setData(keyInfo);
		} else {
			result.setSuccess(false);
			result.setMessage("获取资产关键信息失败");
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";

	}

	public String getAssetById() throws Exception {
		Asset assetById = assetService.getAssetById(asset.getId());
		Result result = new Result();
		if (assetById != null) {
			result.setSuccess(true);
			result.setData(assetById);
		} else {
			result.setSuccess(false);
			result.setMessage("获取资产详细信息失败");
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";

	}

	public String getAssetByParam() throws Exception {
		String userId = null;

		User loginUser = (User) ActionContext.getContext().getSession().get("user");
		userId = loginUser.getId();
		Map<String, Object> resultMap = assetService.getAssetByParam(asset.getCommonType(), asset.getTag(),
				asset.getName(), userId, Integer.parseInt(page));
		Result result = new Result();
		if (resultMap != null) {
			result.setSuccess(true);
			result.setData(resultMap);
		} else {
			result.setSuccess(false);
			result.setMessage("获取资产列表失败");
		}
		ActionContext.getContext().getValueStack().push(result);

		return "jsonMsg";

	}

	public String manage() throws Exception {
		return "manage";
	}

	public String add() throws Exception {
		return "add";
	}

	// -----------------------曾凯添加方法start----------------
	public String core() throws Exception {
		return "core";
	}

	public String auth() throws Exception {
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("id", asset.getId());
		return "auth";
	}

	public String compar() throws Exception {
		return "compar";
	}

	// 通过组织用户id加载该用户下的所有资产树
	public String listTreeByUser() throws Exception {
		String id = request.getParameter("id");
		List<TreeNode> list = assetService.findTreeByUserId(id);
		VauleUtil.putValueStack(list);
		return "jsonMsg";
	}

	// ----------------------曾凯添加方法end------------------
	public String deleteAssetFile() throws Exception {
		ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
		String path = sc.getRealPath("/");
		String fileAbstractPath = path + asset.getFilePath() + "/" + asset.getFileName();
		File file = new File(fileAbstractPath);
		file.deleteOnExit();
		return "jsonMsg";
	}

	public String save() throws Exception {
		System.out.println(chaincodeService);
		Result result = new Result();
		ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
		String path = sc.getRealPath("/");
		String fileAbstractPath = path + asset.getFilePath() + "/" + asset.getFileName();
		File file = new File(fileAbstractPath);
		if (!file.exists()) {
			result.setMessage("资产文件不存在！");
			ActionContext.getContext().getValueStack().push(result);
			return "jsonMsg";
		}
		InputStream in = new FileInputStream(file);
		String md5Hex = DigestUtils.md5Hex(in);
		logger.debug("fileAbstractPath:" + fileAbstractPath);
		logger.debug("md5Hex:" + md5Hex);
		asset.setAssetMd5(md5Hex);

		User loginUser = (User) ActionContext.getContext().getSession().get("user");
		asset.setUser(loginUser);
		String saveResult = assetService.save(asset);
		if (saveResult.equals("success")) {
			result.setSuccess(true);
			String argsStr = "save-" + asset.getId() + "-" + asset.getName() + "-" + asset.getKeyInfo() + "-"
					+ asset.getUser().getName() + "-" + asset.getAssetMd5() + "-" + code + "-" + asset.getFileName()
					+ "-" + asset.getFilePath();
			String[] args = argsStr.split("-");
			String resp = chaincodeService.invokeChaincode(ChainMutualUtil.chainCodeName, "invoke", args);
			if (resp.contains("Transaction invoked successfully")) {
				result.setMessage("保存资产信息成功！");
			} else {
				System.out.println("保存资产信息失败");
				result.setMessage("保存资产信息失败！");

			}
			logger.info(resp);
		} else {
			result.setSuccess(false);
			result.setMessage("保存资产信息失败！");
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";
	}

	public InputStream getInputStream() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		String realPath = sc.getRealPath(File.separator);

		File downloadFile = new File(realPath + asset.getFilePath() + File.separator + asset.getFileName());
		System.out.println("fileExist:" + downloadFile.exists());
		System.out.println("downloadFile.getName:" + downloadFile.getName());
		System.out.println("downloadFile.getAbsolutePath():" + downloadFile.getAbsolutePath());
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(downloadFile));
		return inputStream;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
