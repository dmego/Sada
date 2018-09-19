package cn.dmego.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.dmego.domain.Dict;
import cn.dmego.domain.User;
import cn.dmego.pojo.Result;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IDictService;
import cn.dmego.service.IUserService;
import cn.dmego.utils.DateUtil;
import cn.dmego.utils.VauleUtil;

/**
* @Name: DictionaryAction
* @Description: 数据字典Action类（继承基础Action）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-24（创建日期）
*/
@SuppressWarnings("serial")
@Controller("dictAction")
@Scope(value="prototype")
public class DictAction extends BaseAction<Dict>{
	Dict dict= this.getModel();
	
	/**注入数据字典Service*/
	@Resource(name=IDictService.SERVICE_NAME)
	IDictService dictService;
	
	/**  
	* @Name: home
	* @Description: 数据字典的首页显示
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-24（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/dictionaryIndex.jsp
	*/
	public String home(){
		return "home";
	}

	/**  
	* @Name: save
	* @Description: 保存新增的元数据
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-24（创建日期）
	* @Parameters: 无
	* @Return: 
	*/
	public String save(){
		if(StringUtils.isBlank(dict.getId())){
			dict.setCreatetime(DateUtil.nowDateToStamp());
		}
		dict.setUpdatetime(DateUtil.nowDateToStamp());
		try {
			dictService.saveOrupdate(dict);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Result result = new Result(true);
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	/**  
	* @Name: del
	* @Description: 删除元数据
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-24（创建日期）
	* @Parameters: 无
	* @Return: 
	*/
	@SuppressWarnings("unchecked")
	public String del(){
		String id = request.getParameter("id");
		Dict dict = (Dict) dictService.get(Dict.class, id);
		dictService.delete(dict);
		Result result = new Result(true);
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	
	/**  
	* @Name: codeCheck
	* @Description: 检查编码唯一性
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-24（创建日期）
	* @Parameters: 无
	* @Return: 
	*/
	public String codeCheck(){
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		boolean flag = dictService.checkCodeByIdAndCode(id,code);
		if(flag == false){//如果存在该编码，返回 false,
			map.put("valid", false);
		}else{
			map.put("valid", true);
		}
		VauleUtil.putValueStack(map);
		return "jsonMsg";
	}
	
	
	public String getDictsByCode(){
		String code = request.getParameter("code");
		Dict dict= dictService.getDictByCode(code);
		VauleUtil.putValueStack(dict);
		return "jsonMsg";
	}
	
	
	/**  
	* @Name: treeData
	* @Description: 构造bootstrap-treeview格式数据并返回
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-29（创建日期）
	* @Parameters: 无
	* @Return: 
	*/
	public String treeData(){	
		List<TreeNode> list = dictService.findAllDictList();
		//将数据字典数组压入值栈,通过struts2的json工具将数组转json数组
		VauleUtil.putValueStack(list);
		return "jsonMsg";
	}
	
	@SuppressWarnings("unchecked")
	public String getById(){	
		String id = request.getParameter("id");
		Dict dict = (Dict) dictService.get(Dict.class, id);
		//父节点不为空,说明是子节点
		if(StringUtils.isNotBlank(dict.getParentId())){
			dict.setParentName(((Dict) dictService.get(Dict.class,dict.getParentId())).getName());
		}else{
			//如果该节点是父节点,则显示该节点的子节点为数据字典
			dict.setParentName("数据字典");
		}
		//将数据字典数组压入值栈,通过struts2的json工具将数组转json数组
		VauleUtil.putValueStack(dict);
		return "jsonMsg";
	}
	
	
	@SuppressWarnings("unchecked")
	public String show(){
		String id = request.getParameter("id");
		Dict dict = (Dict) dictService.get(Dict.class, id);
		Map<String, String> map = new HashMap<String, String>();
		map.put("data", dict.getName());
		VauleUtil.putValueStack(map);
		return "jsonMsg";
	}
	
	/**
	 * 获取资产类型
	 * @return
	 * @throws Exception
	 */
	public String getAssetTypeList() throws Exception {
		List<Dict> list = dictService.getAssetTypeList();
		Result result = new Result();
		if(list != null){
			result.setSuccess(true);
			result.setData(list);
		}
		else{
			result.setSuccess(false);
			result.setMessage("获取资产类型失败！");
		}
		ActionContext.getContext().getValueStack().push(result);
		return "jsonMsg";
	}
 	
}
