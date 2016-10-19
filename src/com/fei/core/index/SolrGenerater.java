package com.fei.core.index;

/**
 * 2014年5月12日10:28:10
 * @author kangfei
 *
 */
public interface SolrGenerater {

	
	/**
	 * 获取solr服务器
	 * @param url
	 */
	public String getUrl();
	
	/**
	 * 获取连接超时信息 
	 * @return
	 */
	public int getSoTimeOut();
	
	/**
	 * 获取连接超时信息
	 * @return
	 */
	public int getConnectionTimeOut();
	
	/**
	 * 是否优化索引
	 * @return
	 */
	public boolean optimize();
}
