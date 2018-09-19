package cn.dmego.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dmego.domain.Asset;
import cn.dmego.domain.ChainMsg;
import cn.dmego.service.IAssetService;
import cn.dmego.service.IChainService;
import cn.dmego.utils.ChainMutualUtil;

/**
 * @Name: ChainServiceImpl
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月14日
 * 
 */
@Service(IChainService.SERVICE_NAME)
@Transactional(readOnly = true)
public class ChainServiceImpl implements IChainService {

	@Override
	public String enroll(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.enroll(chainMsg);
		return resp;
	}

	@Override
	public String construct(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.construct(chainMsg);
		return resp;
	}

	@Override
	public String reconstruct(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.reconstruct(chainMsg);
		return resp;
	}

	@Override
	public String install(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.install(chainMsg);
		return resp;
	}

	@Override
	public String instantiate(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.instantiate(chainMsg);
		return resp;
	}

	@Override
	public String invoke(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.invoke(chainMsg);
		return resp;
	}

	@Override
	public String query(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.query(chainMsg);
		return resp;
	}

	@Override
	public String upgrade(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.upgrade(chainMsg);
		return resp;
	}

	@Override
	public String reloadConfig(ChainMsg chainMsg) {
		String resp = ChainMutualUtil.reloadConfif();
		return resp;
	}

	  
	@Override
	public String save(Asset asset) {
		ChainMsg chainMsg = new ChainMsg();
//		String[] args = new String[9];
//		args[0] = "save";
//		args[1] = asset.getId();
//		args[2] = asset.getName();
//		args[3] = asset.getKeyInfo();
//		args[4] = asset.getf;
//		args[5] = "save";
//		args[6] = "save";
//		args[7] = "save";
//		args[8] = "save";
		return null;
	}

	  
	@Override
	public String queryBlockInfo(ChainMsg chainMsg) {
		String queryBlockInfo = ChainMutualUtil.queryBlockInfo(chainMsg);
		return queryBlockInfo;
	}

}
