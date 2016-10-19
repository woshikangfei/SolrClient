package com.fei.core.mlt;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;

import com.fei.base.util.QueryResult;
import com.fei.core.exception.QueryException;
import com.fei.core.query.CommonQuery;

public class TestQuery  extends CommonQuery {

	public TestQuery() throws QueryException {
		super();
	}

	@Override
	public void setQueryField(SolrQuery query) {
		query.addField("id");
		query.addField("title");
		query.addField("name");
	}
	
	public static void main(String[] args) throws QueryException {
		CommonQuery query = new TestQuery();
		//query.queryMoreLikethis("title:ÄÐ¿ã", 1);
	}

}
