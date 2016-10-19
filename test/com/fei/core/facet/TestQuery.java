package com.fei.core.facet;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import com.fei.base.util.QueryResult;
import com.fei.core.exception.QueryException;
import com.fei.core.index.business.Terms;
import com.fei.core.query.CommonQuery;

public class TestQuery  extends CommonQuery {

	public TestQuery() throws QueryException {
		super();
	}

	@Override
	public void setQueryField(SolrQuery query) {
	}
	
	public static void main(String[] args) throws QueryException, SolrServerException {
		CommonQuery query = new TestQuery();
		query.getServer();
		List<Terms> reuslt =query.queryFacet("*:*", new String[]{"attrs_lingxing","attrs_pinpai"});
		for (int i = 0; i < reuslt.size(); i++) {	
			Terms sd = (Terms) reuslt.get(i);
			System.out.println(sd.getKey());
			System.out.println(sd.getValue());
		}
	}

}
