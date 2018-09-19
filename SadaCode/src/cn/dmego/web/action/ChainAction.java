package cn.dmego.web.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.dmego.domain.ChainMsg;
import cn.dmego.pojo.Result;
import cn.dmego.service.IAssetService;
import cn.dmego.service.IChainService;

/**
 * @Name: ChainAction
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月14日
 * 
 */
@Controller("chainAction")
@Scope(value = "prototype")
public class ChainAction extends BaseAction<ChainMsg> {
	ChainMsg chainMsg = this.getModel();

	// 注入AssetServiceImpl
	@Resource(name = IChainService.SERVICE_NAME)
	private IChainService chainService;

	public String enroll() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.enroll(chainMsg);
		result.setMessage(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String construct() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.construct(chainMsg);
		result.setMessage(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String reconstruct() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.reconstruct(chainMsg);
		result.setMessage(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String install() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.install(chainMsg);
		result.setMessage(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String instantiate() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.instantiate(chainMsg);
		result.setMessage(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String upgrade() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.upgrade(chainMsg);
		result.setMessage(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String invoke() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.invoke(chainMsg);
		result.setMessage(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	public String query() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.query(chainMsg);
		result.setData(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	public String queryBlockInfo() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.queryBlockInfo(chainMsg);
		result.setData(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	public String reloadConfig() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		String resp = chainService.reloadConfig(chainMsg);
		result.setData(resp);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

}
