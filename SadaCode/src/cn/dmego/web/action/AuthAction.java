package cn.dmego.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.dmego.domain.Popedom;
import cn.dmego.domain.Role;
import cn.dmego.domain.User;
import cn.dmego.pojo.JstreeNode;
import cn.dmego.pojo.Result;
import cn.dmego.pojo.TreeNode;
import cn.dmego.service.IPopedomService;
import cn.dmego.service.IRoleService;
import cn.dmego.service.IUserService;
import cn.dmego.utils.DateUtil;
import cn.dmego.utils.VauleUtil;

/**  
* @Name: PopedomAction
* @Description: 权限Action类（继承基础Action）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-25（创建日期）
*/
@SuppressWarnings("serial")
@Controller("authAction")
@Scope(value="prototype")
public class AuthAction extends BaseAction<Popedom>{

	Popedom auth = this.getModel();
	
	/**注入权限Service*/
	@Resource(name=IPopedomService.SERVICE_NAME)
	IPopedomService authService;
	
	/**注入用户Service*/
	@Resource(name=IUserService.SERVICE_NAME)
	IUserService userService;
	
	
	/**  
	* @Name: authTree
	* @Description: 功能管理的首页显示
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-13（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/userIndex.jsp
	*/
	public String home(){
		
		return "home";
	}
	
	public String save(){
		if(StringUtils.isBlank(auth.getId())){
			auth.setCreatetime(DateUtil.nowDateToStamp());
		}
		auth.setUpdatetime(DateUtil.nowDateToStamp());
		authService.saveOrupdate(auth);
		Result result = new Result(true);
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	//删除功能权限
	@SuppressWarnings("unchecked")
	public String del(){
		String id = request.getParameter("id");
		Popedom auth = (Popedom) authService.get(Popedom.class, id);		
		authService.delete(auth);
		Result result = new Result(true);
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	
	public String codeCheck(){
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		boolean flag = authService.checkCodeByIdAndCode(id,code);
		if(flag == false){//如果存在该编码，返回 false,
			map.put("valid", false);
		}else{
			map.put("valid", true);
		}
		VauleUtil.putValueStack(map);
		return "jsonMsg";
	}
	
	public String getAuthsByCode(){
		String code = request.getParameter("code");
		Popedom auth= authService.getAuthsByCode(code);
		VauleUtil.putValueStack(auth);
		return "jsonMsg";
	}
	
	@SuppressWarnings("unchecked")
	public String getById(){	
		String id = request.getParameter("id");
		Popedom auth = (Popedom) authService.get(Popedom.class, id);
		//父节点不为空,说明是子节点
		if(StringUtils.isNotBlank(auth.getParentId())){
			auth.setParentName(((Popedom) authService.get(Popedom.class,auth.getParentId())).getName());
		}else{
			//如果该节点是父节点,则显示该节点的子节点为数据字典
			auth.setParentName("菜单/功能");
		}
		//将数据字典数组压入值栈,通过struts2的json工具将数组转json数组
		VauleUtil.putValueStack(auth);
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
		List<TreeNode> list = authService.findAllAuthList();
		//将数据字典数组压入值栈,通过struts2的json工具将数组转json数组
		VauleUtil.putValueStack(list);
		return "jsonMsg";
	}
	
	public String iconSelect(){
		String iconName = request.getParameter("iconName");
		request.setAttribute("iconName",iconName);
		return "iconSelect";
	}
	
	//判断当前选中的功能菜单下是否被某个角色绑定
	@SuppressWarnings("unchecked")
	public String hasRole(){
		//获取选中的功能权限id
		String id = request.getParameter("id");
		Popedom selectPo = (Popedom) authService.get(Popedom.class, id);
		Set<Role> roles = selectPo.getRoles();
		Result result = new Result();
		if(roles != null && roles.size() > 0 ){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		VauleUtil.putValueStack(result);
		return "jsonMsg";
	}
	

}
