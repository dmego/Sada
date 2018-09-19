package cn.dmego.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.dmego.utils.TUtil;

/**  
* @Name: BaseAction
* @Description: 基础Action类
* @Author: 曾凯（作者）
* @Version: V1.00 （版本号）
* @Create Date: 2018-04-13（创建日期）
*/
public class BaseAction<T> extends ActionSupport implements ModelDriven<T>,ServletRequestAware,ServletResponseAware {

	T entity;
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	//T型实例化
	public BaseAction(){
		/**T型转换*/
		Class entityClass = TUtil.getActualType(this.getClass());
		try {
			entity = (T) entityClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public T getModel() {
		return entity;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}
	
}
