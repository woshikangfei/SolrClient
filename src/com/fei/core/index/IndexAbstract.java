package com.fei.core.index;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;


/**
 * 
 * @author Administrator
 *
 */
public abstract class IndexAbstract implements SolrGenerater{
	
	
	public static final Logger log =Logger.getLogger(IndexAbstract.class);
	
	public HttpSolrServer server;
	protected long start = System.currentTimeMillis();
	public int _totalTika = 0;
	public int _totalSql = 0;
	public int errorsize = 0;


	protected Collection docs = new CopyOnWriteArrayList();
	
	protected IndexAbstract(HttpSolrServer server) throws IOException, SolrServerException {
		log.info(".........begin index........");
		//
		
		if(server==null)
			throw new SolrServerException("solr cannot be null,NullPointException");
		
		this.server = server;

		int soTimeOut = getSoTimeOut();
		if(soTimeOut==0)
			soTimeOut =100000;
		
		this.server.setSoTimeout(getSoTimeOut()); // socket read timeout
		
		int connecitonTimeOut = getConnectionTimeOut();
		if(connecitonTimeOut == 0 )
			connecitonTimeOut = 100000 ;
		
		this.server.setConnectionTimeout(getConnectionTimeOut());
		this.server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
		// binary parser is used by default for responses
		this.server.setParser(new XMLResponseParser());
	}


	protected IndexAbstract() throws IOException, SolrServerException {
		log.info(".........begin index........");
		// Create a multi-threaded communications channel to the Solr server.
		// Could be CommonsHttpSolrServer as well.
		//
		server = new HttpSolrServer(getUrl());

		int soTimeOut = getSoTimeOut();
		if(soTimeOut==0)
			soTimeOut =100000;
		
		server.setSoTimeout(getSoTimeOut()); // socket read timeout
		
		int connecitonTimeOut = getConnectionTimeOut();
		if(connecitonTimeOut == 0 )
			connecitonTimeOut = 100000 ;
		
		server.setConnectionTimeout(getConnectionTimeOut());
		server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
		// binary parser is used by default for responses
		server.setParser(new XMLResponseParser());

		// One of the ways Tika can be used to attempt to parse arbitrary files.
		//_autoParser = new AutoDetectParser();
	}
	
	boolean flags=true;

	// Just a convenient place to wrap things up.
	public synchronized void endIndexing() throws IOException, SolrServerException {
		start = System.currentTimeMillis();
		
		
		if (docs.size() > 0) { // Are there any documents left over?
			server.add(docs); // Commit within 5 minutes
		}
		
		
		
		//if(optimize())
			//server.optimize();
		
		server.commit(); // Only needs to be done at the end,
		// commitWithin should do the rest.
		// Could even be omitted
		// assuming commitWithin was specified.
		long endTime = System.currentTimeMillis();
		log.info("commit indexs: "+_totalSql+",sepnd times："+((endTime - start)));
		docs.clear();
		log.info(".........end index........");
		
		//发送邮件，提交索引数
		/*Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if(calendar.get(Calendar.HOUR_OF_DAY)==23 && flags){
			IndexAlarm.sendIndexCommit(_totalSql,"--24小时整体统计---");
			
			flags=false;
			_totalSql=0;
		}
		
		if(calendar.get(Calendar.HOUR_OF_DAY)!=23 ){
			flags=true;
		}*/
	}

	// I hate writing System.out.println() everyplace,
	// besides this gives a central place to convert to true logging
	// in a production system.
	protected static void log(String msg) {
		log.info(msg);
	}


	/**
	 * 需要处理生成的索引，数据来源是data
	 * @throws SQLException
	 */
	public abstract void process() throws SQLException;

}