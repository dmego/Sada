package cn.dmego.web.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.dmego.pojo.Query;
import cn.dmego.service.IQueryService;
import cn.dmego.utils.VauleUtil;
@SuppressWarnings("serial")
@Controller("queryAction")
@Scope(value="prototype")
public class QueryAction extends BaseAction<Query>{

	Query query= this.getModel();
	
	
	@Resource(name=IQueryService.SERVICE_NAME)
	IQueryService queryService;
	
	/**  
	* @Name: home
	* @Description: 查询
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-24（创建日期）
	* @Parameters: 无
	* @Return: 
	*/
	public String home(){
		
		return "home";
	}
	
	public String loadData(){
		String reqObj = request.getParameter("reqObj");
		System.out.println("reqObj="+reqObj);
		try {
			Map<String, Object> map = queryService.loadData(reqObj);
			VauleUtil.putValueStack(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "loadData";
	}
	
}
