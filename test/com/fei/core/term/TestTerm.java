package com.fei.core.term;

import org.apache.solr.client.solrj.SolrServerException;

import com.fei.core.exception.QueryException;
import com.fei.core.query.CommonQuery;
import com.fei.core.query.TestQuery;

public class TestTerm {

	public static void main(String[] args) throws QueryException, SolrServerException {
		
		CommonQuery query = new TestQuery();
		query.queryTerm("Ůʿ", 2, "title");
		
	}
}
