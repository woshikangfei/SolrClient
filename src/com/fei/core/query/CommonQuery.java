package com.fei.core.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;

import com.fei.base.util.QueryResult;
import com.fei.base.util.SolrContains;
import com.fei.core.backup.AbstractReplication;
import com.fei.core.exception.QueryException;
import com.fei.core.index.business.Terms;

/**
 * 查询类,需要集成该类实现自己的业务 2014年5月12日15:34:50
 * 
 * @author kangfei
 * 
 */
public abstract class CommonQuery extends AbstractReplication implements Query  {

	public static final Logger log = Logger.getLogger(CommonQuery.class);

	/**
	 * 查询条件
	 */
	private SolrQuery solrQuery;

	/**
	 * 服务器
	 */
	private SolrServer server;

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

	public CommonQuery() throws QueryException {
		solrQuery = new SolrQuery();
		server = new HttpSolrServer(getUrl());
	}
	
	public  List<Terms> queryFacet(String query, String... facetfield)
			throws SolrServerException {
		
		List<Terms> terms =new ArrayList<Terms>();
		solrQuery = new SolrQuery();
		System.out.println(solrQuery.toString());
		//solrQuery.set("q", query);
		solrQuery.setFacet(true).setQuery(query).addFacetField(facetfield);
		/*.setFacetMinCount(1)
        .setFacetLimit(5)//段
        //.setFacetPrefix("electronics", "cat")
        .setFacetPrefix("cor")//查询manu、name中关键字前缀是cor的
        .addFacetField("manu")
        .addFacetField("name");//分片字段*/	
		
		try {
	        QueryResponse response = server.query(solrQuery);
	        
	        //输出查询结果集
	        SolrDocumentList list = response.getResults();
	        log.info("Query result nums: " + list.getNumFound());
	        
	        for (int i = 0; i < list.size(); i++) {
	        	log.info(list.get(i));
	        }
	        
	        log.info("All facet filed result: ");
	        //输出分片信息
	        List<FacetField> facets = response.getFacetFields();
	        for (FacetField facet : facets) {
	        	log.info("-------"+facet);
	            List<Count> facetCounts = facet.getValues();
	            for (FacetField.Count count : facetCounts) {
	                //关键字 - 出现次数
	            	log.info(count.getName() + ": " + count.getCount());
	            	Terms t = new Terms();
					t.setKey(count.getName());
					t.setValue( count.getCount());
					terms.add(t);
	            }
	        }
	        
	        /*log.info("Search facet [name] filed result: ");
	        //输出分片信息
	        FacetField facetField = response.getFacetField(facetfield);
	        List<Count> facetFields = facetField.getValues();
	        for (Count count : facetFields) {
	            //关键字 - 出现次数
	        	log.info(count.getName() + ": " + count.getCount());
	        }*/
	    } catch (SolrServerException e) {
	        e.printStackTrace();
	    } 
		
		return terms;
	}
	
	public  List<Terms> queryFacet(String query, String facetfield)
			throws SolrServerException {
		
		List<Terms> terms =new ArrayList<Terms>();
		solrQuery = new SolrQuery();
		//solrQuery.set("q", query);
		solrQuery.setFacet(true).setQuery(query).addFacetField(facetfield);
		/*.setFacetMinCount(1)
        .setFacetLimit(5)//段
        //.setFacetPrefix("electronics", "cat")
        .setFacetPrefix("cor")//查询manu、name中关键字前缀是cor的
        .addFacetField("manu")
        .addFacetField("name");//分片字段*/	
		
		try {
	        QueryResponse response = server.query(solrQuery);
	        
	        //输出查询结果集
	        SolrDocumentList list = response.getResults();
	        log.info("Query result nums: " + list.getNumFound());
	        
	        for (int i = 0; i < list.size(); i++) {
	        	log.info(list.get(i));
	        }
	        
	        log.info("All facet filed result: ");
	        //输出分片信息
	        List<FacetField> facets = response.getFacetFields();
	        for (FacetField facet : facets) {
	        	log.info("-------"+facet);
	            List<Count> facetCounts = facet.getValues();
	            for (FacetField.Count count : facetCounts) {
	                //关键字 - 出现次数
	            	//log.info(count.getName() + ": " + count.getCount());
	            	Terms t = new Terms();
					t.setKey(count.getName());
					t.setValue( count.getCount());
					terms.add(t);
	            }
	        }
	        
	        /*log.info("Search facet [name] filed result: ");
	        //输出分片信息
	        FacetField facetField = response.getFacetField(facetfield);
	        List<Count> facetFields = facetField.getValues();
	        for (Count count : facetFields) {
	            //关键字 - 出现次数
	        	log.info(count.getName() + ": " + count.getCount());
	        }*/
	    } catch (SolrServerException e) {
	        e.printStackTrace();
	    } 
		
		return terms;
	}
	
	public void backup(){
		System.out.println("===进入备份====");
		solrQuery.setRequestHandler("/replication");
		//solrQuery.set("qt", "/replication");
		solrQuery.set("command", "backup");
		System.out.println(solrQuery.toString());
		QueryResponse response = null;
		try {
			response = server.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doReplication(HttpSolrServer myserver) {
		System.out.println("-----------");
		solrQuery  =new SolrQuery();
		solrQuery.setRequestHandler("/replication");
		//solrQuery.set("qt", "/replication");
		solrQuery.set("command", "backup");
		QueryResponse response = null;
		try {
			response = myserver.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * term查询
	 * @param query 查询条件
	 * @param val 需要trem的字段
	 * @throws SolrServerException
	 */
	public List<Terms> queryTerm(String prfix,int count, String... val)
			throws SolrServerException {
		
		List<Terms> terms =new ArrayList<Terms>();

		//solrQuery.set("q", query);
		solrQuery.set("qt", "/terms");
		solrQuery.set("terms.prefix",prfix);
		solrQuery.set("terms", "true");
		solrQuery.set("terms.fl", val);
		solrQuery.set("terms.limit", count);
		// solrQuery.set("terms.sort", "count");
		//solrQuery.set("terms.mincount", "1");  
		//solrQuery.set("terms.maxcount", "100");  
		log.info(solrQuery.toString());
		
		// 查询并获取相应的结果！
		QueryResponse response = null;
		response = server.query(solrQuery);

		if (response != null) {
			log.info("查询耗时（ms）：" + response.getQTime());
			TermsResponse termsResponse = response.getTermsResponse();
			if (termsResponse != null) {
				Map<String, List<TermsResponse.Term>> termsMap = termsResponse
						.getTermMap();

				Set<String> fieldSet = termsMap.keySet();
				for (String field : fieldSet) {
					List<TermsResponse.Term> termList = termsResponse
							.getTerms(field);
					for (TermsResponse.Term term : termList) {
						Terms t = new Terms();
						String keys =term.getTerm();
						//keys = keys.replace(prfix, "<font color='#FF5B91'>"+prfix+"</font>");
						//System.out.println(keys);
						t.setKey(keys);
						t.setValue(term.getFrequency());
						terms.add(t);
					}
				}

			}
		}
		
		return terms;
	}

	/**
	 * term查询
	 * @param query 查询条件
	 * @param val 需要trem的字段
	 * @throws SolrServerException
	 */
	public List<Terms> queryTerm(String query, String... val)
			throws SolrServerException {
		
		List<Terms> terms =new ArrayList<Terms>();

		//solrQuery.set("q", query);
		solrQuery.set("qt", "/terms");
		solrQuery.set("terms.prefix",query);
		solrQuery.set("terms", "true");
		solrQuery.set("terms.fl", val);
		solrQuery.set("terms.limit", 1000);
		// solrQuery.set("terms.sort", "count");
		//solrQuery.set("terms.mincount", "1");  
		//solrQuery.set("terms.maxcount", "100");  
		System.out.println(solrQuery.toString());
		
		// 查询并获取相应的结果！
		QueryResponse response = null;
		response = server.query(solrQuery);

		if (response != null) {
			log.info("查询耗时（ms）：" + response.getQTime());
			TermsResponse termsResponse = response.getTermsResponse();
			if (termsResponse != null) {
				Map<String, List<TermsResponse.Term>> termsMap = termsResponse
						.getTermMap();

				Set<String> fieldSet = termsMap.keySet();
				for (String field : fieldSet) {
					List<TermsResponse.Term> termList = termsResponse
							.getTerms(field);
					for (TermsResponse.Term term : termList) {
						Terms t = new Terms();
						t.setKey(term.getTerm());
						t.setValue(term.getFrequency());
						terms.add(t);
					}
				}

			}
		}
		
		return terms;
	}

	/**
	 * 返回查询结果
	 * 
	 * @param query
	 * @return
	 * @throws QueryException
	 */
	public QueryResult queryData(String query, int pageNo, int pagesize)
			throws QueryException {
		if (solrQuery == null)
			throw new QueryException("query查询条件不能为空！");

		QueryResult result = new QueryResult();

		try {

			// 设置默认查询content
			solrQuery.setQuery(query);
			setQueryField(solrQuery);
			//solrQuery.setSort("copy_site", ORDER.asc);
			log.info("begin query,start page " + pageNo + " ,pagesize "
					+ pagesize);
			solrQuery.setParam(GroupParams.GROUP, true);
			//solrQuery.setParam(GroupParams.GROUP_SORT, "commentcount desc");
			solrQuery.setParam(GroupParams.GROUP_FIELD, "grupname");
			solrQuery.setParam(GroupParams.GROUP_LIMIT, "10");
			solrQuery.setParam(GroupParams.GROUP_TOTAL_COUNT,true);

			// solrQuery.setParam(GroupParams.GROUP_OFFSET,
			// String.valueOf(size));// 当前结果起始位置
			solrQuery.setStart(pageNo);
			solrQuery.setRows(pagesize);

			//CloudSolrServer server = new CloudSolrServer("192.168.2.88:9983");
			//CloudSolrServer server = new CloudSolrServer("192.168.2.88:2181");
			//server.setDefaultCollection("collection1");
			QueryResponse response = server.query(solrQuery);

			log.info("query send times:" + response.getQTime() / 1000 + " s");

			log.info("query condition：" + solrQuery.toString());

			GroupResponse groups = response.getGroupResponse();
			System.out.println(groups.getValues().get(0).getNGroups());
			List<GroupCommand> list = groups.getValues();
			for (GroupCommand gc : list) {
				List<Group> gs = gc.getValues();
				System.out.println("总记录数:" + gs.size());
				result.setTotalrecord(gs.size());
			}
			
			

			solrQuery.setStart(pageNo);
			// 显示10条记录
			solrQuery.setRows(pagesize);

			response = server.query(solrQuery);

			log.info("query send times:" + response.getQTime() / 1000 + " s");

			log.info("query condition：" + solrQuery.toString());

			groups = response.getGroupResponse();
			list = groups.getValues();

			for (GroupCommand gc : list) {
				List<Group> gs = gc.getValues();
				System.out.println("当前组查询数量" + gs.size());
				
				int w = 0;
				if (gs != null) {
					for (Group g : gs) {
						w++;
						SolrDocumentList sds = g.getResult();
						System.out.println("-----------第" + w
								+ "租记录--------------"+sds.getNumFound() +",名字:"+g.getGroupValue());
						if (sds != null) {
							for (int m = 0; m < sds.size(); m++) {
								SolrDocument d = sds.get(m);
								//System.out.println("-----文档开始-----");
								System.out.println("name:" + d.get("name"));
								System.out.println("source:" + d.get("source"));
								System.out.println("url:" + d.get("id"));
								/*
								 * System.out .println("title:" +
								 * d.get("KV_TITLE"));
								 * System.out.println("time:" +
								 * d.get("KV_CTIME"));
								 * System.out.println("KV_ABSTRACT:" +
								 * d.get("KV_ABSTRACT"));
								 */
								//System.out.println("-----文档结束-----");
							}

						}
					}
				}
			}

			GroupCommand group = groups.getValues().get(0);
			System.out.println("获取数据总量：" + group.getMatches());
			List<Group> list1 = group.getValues();
			// Group list = group.getValues().get(0);
			// System.out.println("获取数据总量："+list1.size());
			for (Group l : list1) {
				SolrDocumentList doc = l.getResult();
				// System.out.println("重复数据条数："+doc.size());
				for (SolrDocument d : doc) {
					/*
					 * System.out.println("ID:"+d.get("ID"));
					 * System.out.println("title:"+d.get("KV_TITLE"));
					 * System.out.println("time:"+d.get("KV_CTIME"));
					 * System.out.println("KV_ABSTRACT:"+d.get("KV_ABSTRACT"));
					 */
				}
			}

			Map<String, Map<String, List<String>>> map = response
					.getHighlighting();

			// log.info("total docs ："+docs.getNumFound());

			// result.setResultlist(docs);
			// result.setTotalrecord(docs.getNumFound());
			result.setHighlighting(map);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public Map<String, Integer> queryByGroup(String qStr, String groupField,
			String sortField, boolean asc, Integer pageSize, Integer pageNum) {
		Map<String, Integer> rmap = new LinkedHashMap<String, Integer>();
		try {
			// 方法就是返回一个CommonsHttpSolrServer
			SolrQuery query = new SolrQuery();
			if (qStr != null && qStr.length() > 0)
				query.setQuery(qStr);
			else
				query.setQuery("*:*");// 如果没有查询语句，必须这么写，否则会报异常
			query.setIncludeScore(false);// 是否按每组数量高低排序
			query.setFacet(true);// 是否分组查询
			query.setRows(0);// 设置返回结果条数，如果你时分组查询，你就设置为0
			query.addFacetField(groupField);// 增加分组字段
			query.setFacetSort(true);// 分组是否排序
			query.setFacetLimit(pageSize);// 限制每次返回结果数
			// query.setSortField(sortField, asc ? SolrQuery.ORDER.asc
			// : SolrQuery.ORDER.desc);// 分组排序字段
			query.set(FacetParams.FACET_OFFSET, (pageNum - 1) * pageSize);// 当前结果起始位置
			QueryResponse rsp = server.query(query);

			List<Count> countList = rsp.getFacetField(groupField).getValues();
			List<Count> returnList = new ArrayList<Count>();
			if (pageNum * pageSize < countList.size())
				returnList = countList.subList((pageNum - 1) * pageSize,
						pageNum * pageSize);
			else
				returnList = countList.subList((pageNum - 1) * pageSize,
						countList.size() - 1);

			for (Count count : returnList) {
				if (count.getCount() > 0) {
					rmap.put(count.getName(), (int) count.getCount());
					System.out.println(count.getName());
					System.out.println(count.getCount());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rmap;
	}

	public QueryResult queryData2(String query, int pageNo, int pagesize)
			throws QueryException {
		if (solrQuery == null)
			throw new QueryException("query查询条件不能为空！");

		QueryResult result = new QueryResult();

		try {

			// 设置默认查询content
			solrQuery.setQuery(query);
			setQueryField(solrQuery);
			log.info("begin query,start page " + pageNo + " ,pagesize "
					+ pagesize);

			// solrQuery.setParam(GroupParams.GROUP, true);
			// solrQuery.setParam(GroupParams.GROUP_FIELD, "KV_TITLE");
			// solrQuery.setParam(GroupParams.GROUP_LIMIT, "100");

			solrQuery.setStart(pageNo);
			// 显示10条记录
			solrQuery.setRows(pagesize);

			QueryResponse response = server.query(solrQuery);

			log.info("query send times:" + response.getQTime() / 1000 + " s");

			log.info("query condition：" + solrQuery.toString());
			SolrDocumentList docs = response.getResults();
			// GroupResponse groups = response.getGroupResponse();
			// GroupCommand group = groups.getValues().get(0);
			// List<Group> list1 = group.getValues();
			// System.out.println("获取数据量："+list1.size());
			// for(Group l:list1){
			// SolrDocumentList doc= l.getResult();
			// System.out.println("重复数据条数："+doc.size());
			// for(SolrDocument d:doc){
			// System.out.println("title:"+d.get("KV_TITLE"));
			// System.out.println("time:"+d.get("KV_CTIME"));
			// // System.out.println(d.get("KV_ABSTRACT"));
			// }
			// }
			//
			Map<String, Map<String, List<String>>> map = response
					.getHighlighting();

			// log.info("total docs ："+docs.getNumFound());

			result.setResultlist(docs);
			result.setTotalrecord(docs.getNumFound());
			result.setHighlighting(map);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 添加需要返回的字段
	 * 
	 * @param query
	 */
	public abstract void setQueryField(SolrQuery query);

	/**
	 * 返回查询结果
	 * 
	 * @param query
	 * @return
	 * @throws QueryException
	 */
	public QueryResult queryData(String query) throws QueryException {

		if (solrQuery == null)
			throw new QueryException("query查询条件不能为空！");

		QueryResult result = new QueryResult();

		try {
			// 设置默认查询content
			solrQuery.setQuery(query);
			setQueryField(solrQuery);
			log.info("begin query,not pagination ");
			
			QueryResponse response = server.query(solrQuery);

			log.info("query condition：" + solrQuery.toString());

			SolrDocumentList docs = response.getResults();
			log.info("total docs ：" + docs.getNumFound());
			log.info("query times：" + response.getQTime() / 1000);

			result.setResultlist(docs);
			result.setTotalrecord(docs.getNumFound());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	/**
	 * 通用查询
	 * @param query
	 * @param pageNo
	 * @param pagesize
	 * @return
	 * @throws QueryException
	 */
	public QueryResult queryDataCommon(String query,int pageNo,int pagesize) throws QueryException {
		return queryDataCommon(query,pageNo,pagesize,null,false);
	}
	
	/**通用查询**/
	public QueryResult queryDataCommon(String query,int pageNo,int pagesize,String sortfield,boolean asc) throws QueryException {
		return  queryDataCommon(query,pageNo,pagesize,sortfield,asc,null);
	}
	
	/**通用查询**/
	public QueryResult queryDataCommon(String query,int pageNo,int pagesize,String sortfield,boolean asc,String... fq) throws QueryException {

		if (solrQuery == null)
			throw new QueryException("query查询条件不能为空！");

		QueryResult result = new QueryResult();

		try {
			// 设置默认查询content
			solrQuery.setQuery(query);
			setQueryField(solrQuery);
			log.info("begin query,not pagination ");
			solrQuery.setStart(pageNo);//s
			solrQuery.setRows(pagesize);//n
			if(sortfield!=null&&sortfield.length()>0){
				solrQuery.setSort(sortfield, asc ? SolrQuery.ORDER.asc
						 : SolrQuery.ORDER.desc);
			}
			if(fq!=null&&fq.length>0){
				solrQuery.setFilterQueries(fq);
			}
			
			//加入高亮设置
			//solrQuery.setParam("hl", true);
			// query.setHighlightSimplePost("&lt;/font&gt;");
			//solrQuery.setHighlightSimplePost("</font>");
			// query.setHighlightSimplePre("&lt;font color=&quot;red&gt;");
			//solrQuery.setHighlightSimplePre("<font color='FF5B91'>");
			//solrQuery.setHighlightFragsize(100);
			//solrQuery.addHighlightField("title");
			//solrQuery.setHighlightSnippets(5);

			//solrQuery.setParam("hl.", "title");
			
			QueryResponse response = server.query(solrQuery);
			log.info("query condition：" + solrQuery.toString());
			SolrDocumentList docs = response.getResults();
			log.info("total docs ：" + docs.getNumFound());
			log.info("query times：" + response.getQTime() / 1000);
			
			
			/*Map<String, Map<String, List<String>>> map = response
					.getHighlighting();*/
			
			/*for (SolrDocument doc : docs) {
				String id =doc.getFieldValue("id").toString();
				if (map.containsKey(id)) {
					if(map.get(id).containsKey("title")){
						doc.setField("title", map.get(id).get("title").get(0));
						System.out.println(map.get(id).get("title").get(0));
					}
					
				}
			}*/
			result.setResultlist(docs);
			result.setTotalrecord(docs.getNumFound());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public void hightLightQuery(String query) throws SolrServerException{
		solrQuery.setQuery(query);
		setQueryField(solrQuery);
		solrQuery.setParam("hl", true);
		// query.setHighlightSimplePost("&lt;/font&gt;");
		solrQuery.setHighlightSimplePost("</font>");
		// query.setHighlightSimplePre("&lt;font color=&quot;red&gt;");
		solrQuery.setHighlightSimplePre("<font color='FF5B91'>");
		solrQuery.setHighlightFragsize(100);
		solrQuery.addHighlightField("name");
		solrQuery.setHighlightSnippets(5);

		solrQuery.setParam("hl.", "name");
		
		QueryResponse response = server.query(solrQuery);

		log.info("query condition：" + solrQuery.toString());

		SolrDocumentList docs = response.getResults();
		log.info("total docs ：" + docs.getNumFound());
		log.info("query times：" + response.getQTime() / 1000);
		
		
		Map<String, Map<String, List<String>>> map = response
				.getHighlighting();
		
		for (SolrDocument doc : docs) {
			String id =doc.getFieldValue("id").toString();
			if (map.containsKey(id)) {
				doc.setField("name", map.get(id).get("name").get(0));
				System.out.println(map.get(id).get("name").get(0));
			}
		}
	}

	/**
	 * 使用聚类算法返回查询结果
	 * 
	 * @param query
	 * @return
	 * @throws QueryException
	 */
	public QueryResult queryDataWithClustering(String query)
			throws QueryException {

		if (solrQuery == null)
			throw new QueryException("query查询条件不能为空！");

		QueryResult result = new QueryResult();

		try {
			query(query, result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 使用聚类算法返回查询结果
	 * 
	 * @param query
	 * @return
	 * @throws QueryException
	 */
	public QueryResult queryDataWithClustering(String query, int pageNo,
			int pagesize) throws QueryException {

		if (solrQuery == null)
			throw new QueryException("query查询条件不能为空！");

		QueryResult result = new QueryResult();

		try {

			// CloudSolrServer server = new
			// CloudSolrServer("192.168.2.88:9983");
			// server.setDefaultCollection("collection1");

			// solrQuery.setStart(pageNo);
			// 显示10条记录
			// solrQuery.setRows(pagesize);

			query(query, result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @param query
	 *            查询条件
	 * @param pageStart
	 *            记录开始位置
	 * @param pagesize
	 *            记录长度
	 * @return
	 * @throws QueryException
	 */
	public QueryResult queryDataWithClusteringProcess(String query,
			int pageStart, int pagesize) throws QueryException {
		QueryResult result = new QueryResult();
		int totals = 0; // 标记当前查询出多少条
		int differ = 0;
		int countsize = pagesize;

		do {
			totals = 0;
			countsize += differ;
			System.out.println("-----查询记录数---" + countsize);
			QueryResult<?> reuslt = queryDataWithClustering(query, pageStart,
					countsize);
			result.setTotalrecord(reuslt.getTotalrecord());
			result.setResultlist(reuslt.getResultlist());
			result.setClustering(reuslt.getClustering());

			System.out.println("进来---------");
			List cluster = reuslt.getClustering();

			if (cluster != null && cluster.size() > 0) {

				ArrayList list = (ArrayList) cluster.get(0);
				for (int i = 0; i < list.size(); i++) {
					SimpleOrderedMap map = (SimpleOrderedMap) list.get(i);
					String[] mydoc = map.get("docs").toString().split(",");
					List templist = (List) map.get("labels");

					String title = templist.get(0).toString();
					if (title.equals("Other Topics")) {
						totals += mydoc.length;
					} else {
						totals += 1;
					}
				}

			}

			if (totals < pagesize) {
				differ = pagesize - totals;
				System.out.println(differ + "differ");
			}

			System.out.println(totals);
		} while (totals < pagesize);

		// 设置下一条记录的开始位置
		result.setNextStart((countsize + pageStart));

		return result;
	}

	private void query(String query, QueryResult result)
			throws SolrServerException {
		// 设置默认查询content
		solrQuery.setQuery(query);
		setQueryField(solrQuery);
		solrQuery.setRequestHandler("/clustering");

		QueryResponse response = server.query(solrQuery, METHOD.POST);

		SolrDocumentList docs = response.getResults();
		NamedList<Object> obj = response.getResponse();
		List list = obj.getAll("clusters");
		if (list.size() > 0) {
			result.setClustering(list);
		}

		result.setResultlist(docs);
		result.setTotalrecord(docs.getNumFound());
	}
	
	public List queryMoreLikethis(String querys,int count,String sortfield,boolean isasc,String... filter){
		solrQuery.setQuery(querys);
		setQueryField(solrQuery);
		solrQuery.setRequestHandler("/mlt");
		List articles = new ArrayList();
		try {
			solrQuery//.setParam("fl", "id,title,score")
					.setParam("mlt", "true").setParam("mlt.fl", "name")
					.setParam("mlt.mindf", "1").setParam("mlt.mintf", "1").setFilterQueries(filter);
			if(sortfield!=null&&sortfield.length()>0){
				solrQuery.setSort(sortfield, isasc ? SolrQuery.ORDER.asc
						 : SolrQuery.ORDER.desc);
			}
			//query.addFilterQuery("status:" + Article.STATUS_PUBLISHED);
			solrQuery.setRows(count);// mlt.count无效，需要此方法控制返回条数
			log.info("查询条件："+solrQuery.toString());
			QueryResponse response = server.query(solrQuery);
			if (response == null)
				return articles;
			SolrDocumentList docs = response.getResults();
			if(docs==null)
				return null;
			/*Iterator<SolrDocument> iter = docs.iterator();
			while (iter.hasNext()) { // 相关结果中不包含自己，循环中无需排除处理

				SolrDocument doc = iter.next();
				String idStr = doc.getFieldValue("id").toString();
				Article article = new Article();
				article.setId(Integer.parseInt(idStr));
				article.setTitle(doc.getFieldValue("title").toString());
				System.out.println(doc.getFieldValue("title").toString());
				System.out.println(doc.getFieldValue("id"));
				//articles.add(doc);
			}*/
			
			return docs;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("从solr获取相关新闻时遇到错误", e);
		}
		return null;
	}
	
	
	public  List<String> suggest(String word,int count) {

		List<String> wordList = new ArrayList<String>();
		SolrQuery query = new SolrQuery();
		query.set("q", "title:" + word);// 查询的词
		query.set("qt", "/suggest");// 请求到suggest中
		query.set("spellcheck.count", count);// 返回数量
		try {
			QueryResponse rsp = server.query(query);
			// System.out.println("直接命中:"+rsp.getResults().size());
			SpellCheckResponse re = rsp.getSpellCheckResponse();// 获取拼写检查的结果集
			if (re != null) {
				for (Suggestion s : re.getSuggestions()) {
					List<String> list = s.getAlternatives();// 获取所有 的检索词
					for (String spellWord : list) {
						System.out.println(spellWord);
						wordList.add(spellWord);
					}

					return wordList;// 建议词汇
				}

			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		return null;

	}

	public SolrQuery getSolrQuery() {
		return solrQuery;
	}

	public void setSolrQuery(SolrQuery solrQuery) {
		this.solrQuery = solrQuery;
	}
	

	public SolrServer getServer() {
		return server;
	}
}
