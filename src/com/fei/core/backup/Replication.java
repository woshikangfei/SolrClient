package com.fei.core.backup;

import com.fei.core.exception.SolrBackupException;


/**
 * solr索引备份
 * @author Administrator
 *
 */
public interface Replication {

	/**
	 * replication
	 * 需要执行的URL
	 * @param urls
	 */
	void replication(String[] url)throws SolrBackupException;
}
