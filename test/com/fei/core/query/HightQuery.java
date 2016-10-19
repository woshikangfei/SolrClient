package com.fei.core.query;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import com.fei.base.util.QueryResult;
import com.fei.core.exception.QueryException;

public class HightQuery {

	public static void main(String[] args) throws QueryException, SolrServerException {
		TestQuery query = new TestQuery();
		query.hightLightQuery("name:³´¹ø");
	}
}
