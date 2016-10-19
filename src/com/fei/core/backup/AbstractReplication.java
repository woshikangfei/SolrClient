package com.fei.core.backup;


import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.fei.core.exception.SolrBackupException;

/**
 * 备份的实现
 * @author Administrator
 *
 */
public abstract class AbstractReplication implements Replication{
	
	private HttpSolrServer server;
	
	public static final Logger log = Logger.getLogger(AbstractReplication.class);

	@Override
	public void replication(String[] shared) throws SolrBackupException {
		long now = System.currentTimeMillis();
		log.info("begin replication.......");
		if(shared == null || shared.length==0){
			throw new SolrBackupException("shared is null");
		}
		
		for (int i = 0; i < shared.length; i++) {
			server = new HttpSolrServer(shared[i]);
			server.setSoTimeout(10000);
			
			doReplication(server);
		}
		long end = System.currentTimeMillis();
		log.info("end replication.spend times:"+(end-now));
			
	}

	/**
	 * Replication方法,支持的操作有：
	 * <br>fetchindex
	 * <br>disablepoll
	 * <br>enablepoll
	 * <br>details
	 * <br>indexversion
	 * <br>disablereplication
	 * <br>enablereplication
	 * <br>backup
	 * @param server
	 */
	public abstract void doReplication(HttpSolrServer server);
}
