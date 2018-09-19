package cn.dmego.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemLoginFilter implements Filter{

	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		//获取访问的servlet的路径
		String path = request.getServletPath();
		//在跳转到index.jsp页面之前，先获取Cookie，传值的方式给index.jsp
		this.initIndexPage(request,path);
		chain.doFilter(request, response);
    }
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**在跳转到index.jsp页面之前，先获取Cookie，传值的方式给index.jsp*/
	private void initIndexPage(HttpServletRequest request, String path) {
		if(path!=null && path.equals("/login.jsp")){
			//获取Cookie中存放的用户名和密码
			//用户名
			String username = "";
			//密码
			String password = "";
			//复选框
			String checked = "";
			//从Cookie中获取记住我中存放的登录名和密码
			Cookie[] cookies = request.getCookies();
			if(cookies!=null && cookies.length>0){
				for(Cookie cookie:cookies){
					if("username".equals(cookie.getName())){
						//用户名
						username = cookie.getValue();
						//如果是中文，需要解码
						try {
							username = URLDecoder.decode(username, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						//复选框选中
						checked = "checked";
					}
					if("password".equals(cookie.getName())){
						//密码
						password = cookie.getValue();
					}
  
				}
			}
			//存放到request中
			request.setAttribute("username", username);
			request.setAttribute("password", password);
			request.setAttribute("checked", checked);
		}
	}
	

}
