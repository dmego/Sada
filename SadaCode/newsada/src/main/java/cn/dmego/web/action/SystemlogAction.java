package cn.dmego.web.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.dmego.domain.Dict;
import cn.dmego.domain.User;
import cn.dmego.service.IDictService;
import cn.dmego.service.IUserService;

/**
* @Name: SystemlogAction
* @Description: 系统日志Action类（继承基础Action）
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
@SuppressWarnings("serial")
@Controller("systemlogAction")
@Scope(value="prototype")
public class SystemlogAction extends BaseAction<Dict>{
	Dict dictionary = this.getModel();
	
	/**注入数据字典Service*/
	@Resource(name=IDictService.SERVICE_NAME)
	IDictService dictionaryService;
	
	/**  
	* @Name: systemLog
	* @Description: 数据字典的首页显示
	* @Author: 曾凯（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-04-24（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/dictionary.jsp
	*/
	public String systemLog(){
		
		return "systemLog";
	}	
}
