package com.fei.core.update;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.fei.base.util.SolrContains;
import com.fei.core.exception.SolrException;

public class CommonUpdate implements Update {
	
	public static final Logger log =Logger.getLogger(CommonUpdate.class);
	
	private SolrServer server ;
	
	public CommonUpdate(){
		server = new HttpSolrServer(getUrl());
	}
	
	public CommonUpdate(SolrServer server){
		if(server == null){
			log.error("server can not be null");
			try {
				throw new SolrException("SolrServer can not be null");
			} catch (SolrException e) {
				e.printStackTrace();
			}
		}
		
		this.server = server;
	}
	
	/**
	 * 索引重建
	 * @param map
	 * @param query
	 */
	public void updateAll(Map map,String query){
		
		if(map==null){
			log.error("map can not be null");
			return;
		}
		
		
		int start=0;
		int rows =100;
		boolean flag=true;
		//int 
		int i=0;
		
		do{
			long starttime = System.currentTimeMillis();
			
			i++;
			log.info("批量更新第"+i+"次");
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery(query);
			solrQuery.setStart(start);
			solrQuery.setRows(rows);
			
			 try {
				 log.info("this start in "+start);
				 Collection docs = new ArrayList();
				 
				 QueryResponse response = server.query(solrQuery);
				 SolrDocumentList list = response.getResults();
				//List ids= new ArrayList(list.size());
				 if(list!=null&&list.size()>0){
					 
					 for (int j = 0; j < list.size(); j++) {
						 
						 SolrInputDocument output = new SolrInputDocument();
						 
						 SolrDocument document =list.get(j);
						 Collection items=document.getFieldNames();
						 for (Iterator iterator = items.iterator(); iterator.hasNext();) {
							String object = (String) iterator.next();
							output.addField(object, document.getFieldValue(String.valueOf(object)));
						 }
						//ids.add(document.get("ID").toString());
						output.addField("copy_title", output.getFieldValue("KV_TITLE").toString());
						output.addField("copy_source", output.getFieldValue("KV_SOURCE").toString());
						output.addField("copy_site", output.getFieldValue("KV_SITE").toString());
						
						String ctime = output.getFieldValue("KV_CTIME").toString();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
						Date date = new Date(Long.parseLong(ctime));
						output.addField("copy_cyear", sdf.format(date));
						sdf = new SimpleDateFormat("yyyyMM");
						output.addField("copy_cmonth", sdf.format(date));
						sdf = new SimpleDateFormat("yyyyMMdd");
						output.addField("copy_cday", sdf.format(date));
						sdf = new SimpleDateFormat("yyyyMMddHH");
						output.addField("copy_chour", sdf.format(date)); 
						 
						Set<Object> keySet = map.keySet();
						for(Object obj:keySet){
						  // System.out.println("key:"+obj+",Value:"+map.get(obj));
						    if(output.containsKey(obj)){
						    	log.info("已经存在，不需要存建。"+output.get(obj).getName());
						    }else{
						    	output.addField(String.valueOf(obj), map.get(obj));
						    }
						}
						
						docs.add(output);
					}
					
					// server.deleteById(ids);
					// server.commit();
					server.add(docs);
					server.commit();
					
				 }else{
					 log.info("ID不存在");
					 flag=false;
				 }
				
				
				log.info("update documents");
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			 start = start+rows;
			 
			 long end = System.currentTimeMillis();
			 System.out.println("sendtime:"+(end-starttime)/1000+"秒");
		}while(flag);
		
	}
	
	public void update(Map map,String query){
		
		if(map==null){
			log.error("map can not be null");
			return;
		}
		
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(query);
		
		
		 try {
			
			 Collection docs = new ArrayList();
			 QueryResponse response = server.query(solrQuery);
			 SolrDocumentList list = response.getResults();
			 if(list!=null&&list.size()>0){
				 
				 
				 for (int i = 0; i < list.size(); i++) {
					 SolrInputDocument output = new SolrInputDocument();
					 SolrDocument document =list.get(i);
					 Collection items=document.getFieldNames();
					 for (Iterator iterator = items.iterator(); iterator.hasNext();) {
						String object = (String) iterator.next();
						output.addField(object, document.getFieldValue(String.valueOf(object)));
						
						
					}
					 
					output.addField("copy_title", output.getFieldValue("KV_TITLE").toString());
					output.addField("copy_source", output.getFieldValue("KV_SOURCE").toString());
					output.addField("copy_site", output.getFieldValue("KV_SITE").toString());
					
					String ctime = output.getFieldValue("KV_CTIME").toString();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
					Date date = new Date(Long.parseLong(ctime));
					output.addField("copy_cyear", sdf.format(date));
					sdf = new SimpleDateFormat("yyyyMM");
					output.addField("copy_cmonth", sdf.format(date));
					sdf = new SimpleDateFormat("yyyyMMdd");
					output.addField("copy_cday", sdf.format(date));
					sdf = new SimpleDateFormat("yyyyMMddHH");
					output.addField("copy_chour", sdf.format(date));
					 
					Set<Object> keySet = map.keySet();
					for(Object obj:keySet){
					    if(output.containsKey(obj)){
					    	output.setField(String.valueOf(obj), map.get(obj));
					    }
					}
					docs.add(output);
				}
				 
				
				 
				server.add(docs);
				server.commit();
			 }else{
				 log.info("ID不存在");
			 }
			
			
			log.info("update documents");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
