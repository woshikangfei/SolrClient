package com.fei.core.index.business;

import org.apache.solr.client.solrj.SolrQuery;

import com.fei.core.exception.QueryException;
import com.fei.core.query.CommonQuery;

/**
 * 一个ref表查询的实现，
 * @author Administrator
 *
 */
public class RefQuery extends CommonQuery {

	public RefQuery() throws QueryException {
		super();
	}
	
	

	@Override
	public void setQueryField(SolrQuery query) {
		query.addField("KR_INFOTYPE");
		query.addField("KR_ISLOCAL");
		query.addField("KR_KEYWORDID");
		query.addField("KR_STATE");
		query.addField("KR_UID");
		query.addField("KV_ABSTRACT");
		query.addField("KV_CTIME");
		query.addField("KV_DK_TIME");
		query.addField("KV_ORIEN_LEVEL");
		query.addField("KV_ORIENTATION");
		query.addField("KV_REPLY");
		query.addField("KV_SITE");
		query.addField("KV_SOURCE");
		query.addField("KV_SOURCETYPE");
		query.addField("KV_STATE");
		query.addField("kvTitle");
		query.addField("KV_URL");
		query.addField("KV_UUID");
		query.addField("KV_VISITCOUNT");
		query.addField("KR_UUID");
		query.addField("KR_ISMYATTENTION");
		query.addField("KR_ISYJ");
		query.addField("KR_ISWTFK");
		query.addField("kvUuid");
	}



	

}
