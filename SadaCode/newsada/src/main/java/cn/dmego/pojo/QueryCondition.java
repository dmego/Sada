package cn.dmego.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//页面请求查询的参数对象
public class QueryCondition {
	 //分页信息
    private PageInfo pageInfo;
    private String sortInfo;
    private String filter;
    private List<Map<String, Object>> conditions; //查询条件Map
    private Query query;
    private String queryId;
    private QueryConfig queryConfig;
    private String pageName;

    // ----------------------表格---------------------------
    // 导出Excel
    private String sheetName;

    // 导出Excel 标题
    private String sheetTitle;

    // 导出Excel 副标题
    private String sheetSubTitle;

    // 导出Excel后，sheet调用的方法
    private String sheetMethod;

    // --------------------------------------------------

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getQueryId() {

        return queryId;
    }


    public void setQueryId(String queryId) {

        this.queryId = queryId;
    }

    public Query getQuery() {

        return query;
    }

    public void setQuery(Query query) {

        this.query = query;
    }

    public PageInfo getPageInfo() {

        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {

        this.pageInfo = pageInfo;
    }

    public String getSortInfo() {

        return sortInfo;
    }

    public void setSortInfo(String sortInfo) {

        this.sortInfo = sortInfo;
    }

    public String getFilter() {

        return filter;
    }

    public void setFilter(String filter) {

        this.filter = filter;
    }

    public List<Map<String, Object>> getConditions() {

        return conditions;
    }

    public void setConditions(List<Map<String, Object>> conditions) {

        this.conditions = conditions;
    }

    public String getSheetName() {

        return sheetName;
    }

    public void setSheetName(String sheetName) {

        this.sheetName = sheetName;
    }

    public String getSheetTitle() {

        return sheetTitle;
    }

    public void setSheetTitle(String sheetTitle) {

        this.sheetTitle = sheetTitle;
    }

    public String getSheetSubTitle() {

        return sheetSubTitle;
    }

    public void setSheetSubTitle(String sheetSubTitle) {

        this.sheetSubTitle = sheetSubTitle;
    }

    public String getSheetMethod() {

        return sheetMethod;
    }

    public void setSheetMethod(String sheetMethod) {

        this.sheetMethod = sheetMethod;
    }

    public QueryConfig getQueryConfig() {

        return queryConfig;
    }

    public void setQueryConfig(QueryConfig queryConfig) {

        this.queryConfig = queryConfig;
    }
 
    public Map getConditionMap(){
        Map<String,String> conditionValues=new HashMap<>();
        for (Map<String, Object> condition : conditions) {
            String key=condition.get("key").toString();
            String value= condition.get("value")!=null?condition.get("value").toString():null;
             if(conditionValues.containsKey(key)){
                 conditionValues.put(key,conditionValues.get(key)+","+value);
             }else{
                 conditionValues.put(key,value);
             }
        }
        return conditionValues;
    }
}
