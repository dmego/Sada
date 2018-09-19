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
import cn.dmego.domain.ChainMsg;
import cn.dmego.domain.Tag;
import cn.dmego.domain.User;
import cn.dmego.exception.MyException;
import cn.dmego.pojo.Result;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IAssetService;
import cn.dmego.service.IChainService;
import cn.dmego.service.IDictService;
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
	// 注入ChainServiceImpl
	@Resource(name = IChainService.SERVICE_NAME)
	private IChainService chainService;

	private String code;// 资产类型code
	private String page;// 页数

	public String db2chain() throws Exception {
		List<Asset> list = assetService.getAll();
		for (Asset asset : list) {
			chainService.save(asset);
		}
		return null;
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

			// 从区块链中获取资产信息
			ChainMsg chainMsg = new ChainMsg();
			chainMsg.setId(asset.getId());
			String chainAssetResp = chainService.query(chainMsg);

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

	public String getFileKeyInfoAndCompare() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		try {
			// 从区块链中获取资产信息
			ChainMsg chainMsg = new ChainMsg();
			String id = asset.getId();
			if (id == null || id.isEmpty()) {
				throw new MyException("资产id参数为空！");
			}
			chainMsg.setId(id);
			String chainAssetResp = chainService.query(chainMsg);

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
			ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
			String path = sc.getRealPath("/");
			String fileAbstractPath = path + asset.getFilePath() + "/" + asset.getFileName();
			File file = new File(fileAbstractPath);
			if (!file.exists()) {
				throw new MyException("鉴权上传的文件不存在！");
			}
			InputStream in = new FileInputStream(file);
			String md5Hex = DigestUtils.md5Hex(in);
			logger.debug("fileAbstractPath:" + fileAbstractPath);
			logger.debug("md5Hex:" + md5Hex);
			String chainAssetMd5 = chainAssetJSON.getString("assetMd5");
			if (chainAssetMd5 == null || chainAssetMd5.isEmpty()) {
				throw new MyException("区块链中资产关键信息为空");
			}
			if (md5Hex.equals(chainAssetMd5)) {
				resp.put("md5CompareResult", "yes");
			} else {
				resp.put("md5CompareResult", "no");
			}
			resp.put("nMd5", md5Hex);
			

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
				//从区块链中删除该资产
				ChainMsg chainMsg = new ChainMsg();
				chainMsg.setArgsStr("delete-" + id);
				String chainResp = chainService.invoke(chainMsg);
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

	//飞去
	public String assetValidate() throws Exception {
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("id", asset.getId());
		return "assetValidate";
	}

	public String save() throws Exception {
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
			ChainMsg chainMsg = new ChainMsg();
			chainMsg.setArgsStr("save-" + asset.getId() + "-" + asset.getName() + "-" + asset.getKeyInfo() + "-"
					+ asset.getUser().getName() + "-" + asset.getAssetMd5() + "-" + code + "-" + asset.getFileName()
					+ "-" + asset.getFilePath());
			String resp = chainService.invoke(chainMsg);
			if (resp.contains("successfully") || "chain netword is close".equals(resp)) {
				result.setMessage("保存资产信息成功！");
			} else {
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
