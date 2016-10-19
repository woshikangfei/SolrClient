package com.fei.core.query;

import com.fei.base.util.QueryResult;
import com.fei.core.exception.QueryException;
import com.fei.core.index.SolrGenerater;

/**
 * 查询
 * @author kangf
 *
 */
public interface Query extends SolrGenerater{

	/**
	 * 查询数据，分页查询
	 * @param query 查询条件
	 * @param pageNo 当前页
	 * @param pagesize 每页显示条数
	 * @return 
	 * @throws QueryException
	 */
	public QueryResult queryData(String query,int pageNo,int pagesize) throws QueryException;
	
	/**
	 * 查询，默认查询，不分页
	 * @param query
	 * @return
	 * @throws QueryException
	 */
	public QueryResult queryData(String query) throws QueryException;
}
