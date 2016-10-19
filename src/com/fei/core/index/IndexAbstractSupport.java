package com.fei.core.index;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.fei.base.util.SolrContains;
import com.fei.core.exception.InvalidParameterException;

/**
 *  索引生成抽象类
 * @author Administrator
 *
 */
public abstract class IndexAbstractSupport extends IndexAbstract{

	public static final Logger log =Logger.getLogger(IndexAbstractSupport.class);
	
	/**
	 * 需要处理的数据
	 */
	public  List data;
	
	public List mysqlDate;
	
	public IndexAbstractSupport(HttpSolrServer server)throws SolrServerException,IOException{
		super(server);
	}
	
	public IndexAbstractSupport()throws SolrServerException,IOException{
		
	}

	protected IndexAbstractSupport(List data) throws IOException, SolrServerException {
		super();
		if(data == null || data.isEmpty())
			try {
				throw new InvalidParameterException("List不能为空");
			} catch (InvalidParameterException e) {
				e.printStackTrace();
			}
		
		this.data = data;
	}

	@Override
	public String getUrl() {
		return SolrContains.Instance().solr_url;
	}

	@Override
	public int getSoTimeOut() {
		return SolrContains.Instance().soTimeout;
	}

	@Override
	public int getConnectionTimeOut() {
		return SolrContains.Instance().connectionTimeout;
	}

	@Override
	public boolean optimize() {
		return false;
	}

}
