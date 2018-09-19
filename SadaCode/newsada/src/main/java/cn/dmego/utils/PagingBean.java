package cn.dmego.utils;

public class PagingBean {
	private int totalPage;
	private int totalCount;
	private int currentPage;
	private int pageSize;
	private int prePage;
	private int postPage;
	private int isFirst;
	private int isLast;
	private String jsonStr;
	
	public PagingBean(int totalCount,int currentPage, int pageSize) {
		this.totalCount = totalCount;
		this.totalPage = (totalCount - 1) / pageSize + 1;
		prePage = currentPage - 1;
		postPage = currentPage + 1;
		if(currentPage >= (this.totalPage - 1)){
			currentPage = this.totalPage - 1;
			postPage = currentPage;
			isLast = 1;
		}
		if(currentPage <= 0){
			currentPage = 0;
			prePage = 0;
			isFirst = 1;
		}
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getPostPage() {
		return postPage;
	}

	public void setPostPage(int postPage) {
		this.postPage = postPage;
	}

	public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}

	public int getIsLast() {
		return isLast;
	}

	public void setIsLast(int isLast) {
		this.isLast = isLast;
	}

	public String getJsonStr() {
		jsonStr = "\"prePage\":\"" + prePage + "\",\"postPage\":\"" + postPage + "\",\"currPage\":\"" + currentPage + "\",\"totalPage\":\"" + totalPage + "\",\"isLast\":\"" + isLast + "\",\"isFirst\":\"" + isFirst + "\"";
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	@Override
	public String toString() {
		return "PagingBean [totalPage=" + totalPage + ", totalCount=" + totalCount + ", currentPage=" + currentPage
				+ ", pageSize=" + pageSize + ", prePage=" + prePage + ", postPage=" + postPage + ", isFirst=" + isFirst
				+ ", isLast=" + isLast + ", jsonStr=" + jsonStr + "]";
	}
	
	
	
}
