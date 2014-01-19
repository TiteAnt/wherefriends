package com.tite.system.comfc.nosql.commontypes;
/**
 * NoSQL数据库查询参数
 * @author jiaoyk 2012-04-06
 * @version 6.2.0
 * */
public class QueryOptions {
	//每页显示结果数,为0时表示不分页
	private int PageSize = 10;
	//当前查询页,当PageSize设置为0时,该参数无效
	private int PageIndex = 1;
	//结果排序字段,为空时,没有排序
	private String SortField = "";
	//排序方式,排序字段为空时,该设置不生效
	private boolean isASC = true;
	//返回结果字段集合,为空时,返回所有字段
	private String[] ReturnFields = null;
	
	public QueryOptions() {
		super();
	}
	/**
	 * @param pageSize 每页显示结果数,为0时表示不分页
	 * @param pageIndex 当前查询页,当PageSize设置为0时,该参数无效
	 * @param sortField 结果排序字段,为空时,没有排序
	 * @param returnFields 返回结果字段集合,为空时,返回所有字段
	 * */
	public QueryOptions(int pageSize, int pageIndex, String sortField,
			boolean isASC, String[] returnFields) {
		super();
		PageSize = pageSize;
		PageIndex = pageIndex;
		SortField = sortField;
		this.isASC = isASC;
		ReturnFields = returnFields;
	}
	/**
	 * 设置每页显示结果数,为0时表示不分页
	 * */
	public int getPageSize() {
		return PageSize;
	}
	/**
	 * 设置每页显示结果数,为0时表示不分页
	 * */
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	/**
	 * 获取当前查询页,当PageSize设置为0时,该参数无效
	 * */
	public int getPageIndex() {
		return PageIndex;
	}
	/**
	 * 设置当前查询页,当PageSize设置为0时,该参数无效
	 * */
	public void setPageIndex(int pageIndex) {
		PageIndex = pageIndex;
	}
	/**
	 * 获取结果排序字段,为空时,没有排序
	 * */
	public String getSortField() {
		return SortField;
	}
	/**
	 * 设置结果排序字段,为空时,没有排序
	 * */
	public void setSortField(String sortField) {
		SortField = sortField;
	}
	/**
	 * 获取返回结果字段集合,为空时,返回所有字段
	 * */
	public String[] getReturnFields() {
		return ReturnFields;
	}
	/**
	 * 设置返回结果字段集合,为空时,返回所有字段
	 * */
	public void setReturnFields(String[] returnFields) {
		ReturnFields = returnFields;
	}
	/**
	 * 获取排序方式,排序字段为空时,该设置不生效
	 * */
	public boolean isASC() {
		return isASC;
	}
	/**
	 * 设置排序方式,排序字段为空时,该设置不生效
	 * */
	public void setASC(boolean isASC) {
		this.isASC = isASC;
	}
}
