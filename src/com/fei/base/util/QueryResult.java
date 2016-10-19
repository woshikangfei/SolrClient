package com.fei.base.util;



import java.util.List;
import java.util.Map;

public class QueryResult<T> {
	private List<T> resultlist;
	private long totalrecord;
	
	private List clustering;
	
	private Map<String, Map<String, List<String>>> highlighting;
	
	//下一个开始位置
	private int nextStart;
	
	
	public int getNextStart() {
		return nextStart;
	}

	public void setNextStart(int nextStart) {
		this.nextStart = nextStart;
	}

	/**
	 * 返回聚类后结果
	 * @return
	 */
	public List getClustering() {
		return clustering;
	}
	
	public void setClustering(List clustering) {
		this.clustering = clustering;
	}
	
	/**
	 * 返回高亮部分结果
	 * @return
	 */
	public Map<String, Map<String, List<String>>> getHighlighting() {
		return highlighting;
	}
	public void setHighlighting(Map<String, Map<String, List<String>>> highlighting) {
		this.highlighting = highlighting;
	}
	
	/**
	 * 返回查询结果
	 * @return
	 */
	public List<T> getResultlist() {
		return resultlist;
	}
	public void setResultlist(List<T> resultlist) {
		this.resultlist = resultlist;
	}
	public long getTotalrecord() {
		return totalrecord;
	}
	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}
}
