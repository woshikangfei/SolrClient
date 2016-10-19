package com.fei.core.update;

import java.util.Map;

import com.fei.core.index.SolrGenerater;

/**
 * 更新索引
 * 2014年5月13日11:00:22
 * @author kangfei
 *
 */
public interface Update extends SolrGenerater{

	/**
	 * 更新文档
	 * @param map 需要更新的字段
	 * @param query 查询条件，如果更新一条记录，使用ID:XXX，如果批量更新，请传入查询条件。
	 */
	public void update(Map map,String query);
}
