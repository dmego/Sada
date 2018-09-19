package cn.dmego.utils;

import javax.servlet.http.HttpServletRequest;

import cn.dmego.pojo.PageBean;

public class InfoPage {
	private HttpServletRequest req;//HTTP请求
	private int pageSize = SysConstant.PAGE_SIZE; //每页记录数
	private int currentPageNo = 1; //当前页
	private int beginResult = 0; //开始记录数
	private int totalResult = 0; //总记录数
	private int totalPage = 0; //总页数
	private PageBean page = null;
	
	/**
	 * 初始化操作
	 * @param req
	 */
	public InfoPage(HttpServletRequest req){
		if(req.getParameter("pageNo") != null && !req.getParameter("pageNo").equals("")){
			this.currentPageNo = new Integer(req.getParameter("pageNo")).intValue();
		}else{
			this.currentPageNo = 1;
		}
		this.pageSize = SysConstant.PAGE_SIZE;
		if(this.pageSize <=0){
			this.pageSize = SysConstant.PAGE_SIZE;
		}
		this.req = req;
	}
	
	public InfoPage(){
    	this.currentPageNo=1;
    	this.pageSize = SysConstant.PAGE_SIZE;
    	
    }
    public InfoPage(int currentPageNo){
    	this.currentPageNo=currentPageNo;
    	this.pageSize = SysConstant.PAGE_SIZE;
    }
    /**
     * 计算总页数
     */
    private void countPages(){
    	if(totalResult == 0){
    		this.totalPage = 1;
    	}else{
    		this.totalPage = (totalResult / pageSize); //分页页数
    		if((totalResult / pageSize) != 0){
    			this.totalPage += 1;//总页数加1
//    			this.pageSize = this.pageSize + 1; //如果不能整除，则总页数加1
    		}
    	}
    }
    //是否为第一页
    public boolean isFirstPage(){
    	if(this.currentPageNo<=1){
    		
    		return true;
    	}else
    	{
    		return false;	
    	}	
    }
    //是否为最后一页
    public boolean isLastPage(){
    	if(this.currentPageNo>=this.totalPage){
    		return true;
    	}else
    	{
    		return false;	
    	}	
    }

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	
	/**
	 * 获取开始记录数
	 * @return int 开始记录数
	 */
	public int getBeginResult() {
		if(totalPage != 1){
			if(currentPageNo>=totalPage){
				currentPageNo = totalPage;
				beginResult = (currentPageNo - 1) * pageSize;
				pageSize = totalResult - beginResult;
			}else{
				beginResult = (currentPageNo - 1) * pageSize;
			}
			if(totalPage == 1){
				currentPageNo = totalPage;
				beginResult = 0;
				pageSize = totalResult;
			}
		}
		setRequestValue();
		return beginResult;
	}

	public void setBeginResult(int beginResult) {
		this.beginResult = beginResult;
	}

	public int getTotalResult() {
		return totalResult;
	}
	
	//设置该分页信息总有多少条记录
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
		countPages();
	}

	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
    
	/**
	 * 设置序号
	 */
	private void setRequestValue(){
		page = new PageBean();
		page.setFirstPage(isFirstPage());
		page.setLastPage(isLastPage());
		page.setPageNo(currentPageNo);
		page.setPageSize(pageSize);
		page.setSumPage(totalPage);
		page.setTotalResult(totalResult);
	}

	public PageBean getPage() {
		return page;
	}

	public void setPage(PageBean page) {
		this.page = page;
	}
    
	
	
}
