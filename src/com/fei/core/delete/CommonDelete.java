package com.fei.core.delete;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;

import com.fei.base.util.SolrContains;
import com.fei.core.exception.DeleteException;

public class CommonDelete implements Delete {
	
	public static final Logger log =Logger.getLogger(CommonDelete.class);
	
	private SolrServer server ;
	
	public CommonDelete(){
		server = new HttpSolrServer(getUrl());
	}
	
	public CommonDelete(SolrServer server){
		if(server ==null)
			log.error("SolrServer is null,NullPointException ....");
		this.server =server;
	}
	

	@Override
	public void deleteIndexByIds(List<String> listIds) throws DeleteException {
		
		if(listIds ==null|| listIds.size()==0){
			log.error("删除的集合不能为空！");
			return;
		}
		
		try {
			log.info("delete index by listid");
			server.deleteById(listIds);
			UpdateResponse response=server.commit();
			log.info("delete index sends times:"+response.getQTime());
			log.info("delete status:"+response.getStatus());
		} catch (SolrServerException e) {
			throw new DeleteException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteIndexById(String id) throws DeleteException {
		
		if(id==null){
			log.error("id can not be null");
			return;
		}
		
		try {
			log.info("delete index by id "+id);
			server.deleteById(id);
			UpdateResponse response=server.commit();
			log.info("delete status:"+response.getStatus());
		} catch (SolrServerException e) {
			throw new DeleteException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteIndexByQuery(String deleteQuery) throws DeleteException {
		if(deleteQuery==null){
			log.error("deleteQuery can not be null");
			return;
		}
		
		try {
			server.deleteByQuery(deleteQuery);
			UpdateResponse response = server.commit();
			log.info("delete status:"+response.getStatus());
		} catch (SolrServerException e) {
			throw new DeleteException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		return true;
	}

}
