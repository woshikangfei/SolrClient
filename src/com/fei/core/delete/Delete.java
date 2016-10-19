package com.fei.core.delete;


import java.util.List;

import com.fei.core.exception.DeleteException;
import com.fei.core.index.SolrGenerater;

/**
 * 删除索引
 * 2014年5月13日10:11:56
 * @author kangfei
 *
 */
public interface Delete extends SolrGenerater{
	
	 /**
	  * 根据id集合进行删除索引
	  * @param listIds
	  */
	 public void deleteIndexByIds(List<String> listIds)throws DeleteException;
	 
	 /**
	  * 根据id进行删除索引
	  * @param id
	  */
	 public void deleteIndexById(String id)throws DeleteException;
	
	 /**
	  * 根据查询条件删除，比如:id:*，将删除所有索引
	  * @param deleteQuery
	  */
	 public void deleteIndexByQuery(String deleteQuery)throws DeleteException;
}
