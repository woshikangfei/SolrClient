package com.fei.core.query;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;

import com.fei.base.util.QueryResult;
import com.fei.core.exception.QueryException;

public class TestQuery  extends CommonQuery {

	public TestQuery() throws QueryException {
		super();
	}

	@Override
	public void setQueryField(SolrQuery query) {
	}
	
	public static void main(String[] args) throws QueryException {
		CommonQuery query = new TestQuery();
		try {
			QueryResult reuslt =query.queryDataCommon("title:Å®¿ã", 1, 20, "addtime", true);
			List list = reuslt.getResultlist();
			for (int i = 0; i < list.size(); i++) {	
				SolrDocument sd = (SolrDocument) list.get(i);
				System.out.println(sd.getFieldValue("title"));
				System.out.println(sd.getFieldValue("area"));
			}
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
