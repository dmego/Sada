package cn.dmego.pojo;

/**
 * @Name: PageBean
 * @Description: 分页Bean
 * @Author: 曾凯
 * @Version: V1.00
 * @Create Date: 2018年4月24日
 * 
 */
public class PageBean {
	private int pageNo; 
	private boolean firstPage;
	private boolean lastPage;
	private int sumPage;//总页数
	private int pageSize;
	private int totalResult;//总数（总人数、总个数）
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public boolean isFirstPage() {
		return firstPage;
	}
	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	public int getSumPage() {
		return sumPage;
	}
	public void setSumPage(int sumPage) {
		this.sumPage = sumPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	
	
}
