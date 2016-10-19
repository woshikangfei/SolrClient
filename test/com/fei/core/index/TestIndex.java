package com.fei.core.index;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

public class TestIndex extends IndexAbstractSupport {

	public TestIndex() throws SolrServerException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process() throws SQLException {
		for (int i = 0; i < this.data.size(); i++) {
			
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", "kangfeitest"+i);
			doc.addField("name", "nameakngfei"+i);
			
			docs.add(doc);
			++_totalSql;
		}
	}

	public static void main(String[] args) throws SolrServerException, IOException, SQLException {
		TestIndex index = new TestIndex();
		index.data = new ArrayList();
		index.data.add("1");
		index.data.add("2");
		index.data.add("3");
		index.process();
		index.endIndexing();
	}

}
