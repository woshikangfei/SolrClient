package com.fei.core.mlt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class testSpell {

	private static HttpSolrServer solrServer;
	static {
		solrServer = new HttpSolrServer("http://203.195.220.128:8080/solr/");
		solrServer.setConnectionTimeout(5000);

	}

	public static String spellCheck(String word) {
		SolrQuery query = new SolrQuery();
		// query.set("defType", "edismax");// 加权
		// query.set("qf", "name^20.0");
		query.set("suggest", "true");
		query.set("suggest.q", word);
		query.set("qt", "/suggest");
		//query.set("suggest.dictionary", "file");
		//query.set("suggest.build", "true");// 遇到新的检查词，会自动添加到索引里面
		query.set("suggest.count", 5);
		System.out.println(query.toString());
		try {
			QueryResponse rsp = solrServer.query(query);
			System.out.println(rsp.getStatus());
			SpellCheckResponse re = rsp.getSpellCheckResponse();
			if (re != null) {
				if (!re.isCorrectlySpelled()) {
					String t = re.getFirstSuggestion(word);// 获取第一个推荐词
					System.out.println("推荐词：" + t);
					return t;
				}
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List getRelated(String id, int count) {
		SolrQuery query = new SolrQuery();
		query.setRequestHandler("/mlt");
		List articles = new ArrayList();
		try {
			query.setQuery("id:" + id)
					// .setParam("fl", "id,title,score")
					.setParam("mlt", "true").setParam("mlt.fl", "name")
					.setParam("mlt.mindf", "1").setParam("mlt.mintf", "1");
			// query.addFilterQuery("status:" + Article.STATUS_PUBLISHED);
			query.setRows(count);// mlt.count无效，需要此方法控制返回条数

			QueryResponse response = solrServer.query(query);
			if (response == null)
				return articles;
			SolrDocumentList docs = response.getResults();
			Iterator<SolrDocument> iter = docs.iterator();
			while (iter.hasNext()) { // 相关结果中不包含自己，循环中无需排除处理

				SolrDocument doc = iter.next();
				/*
				 * String idStr = doc.getFieldValue("id").toString(); Article
				 * article = new Article();
				 * article.setId(Integer.parseInt(idStr));
				 * article.setTitle(doc.getFieldValue("title").toString());
				 */
				System.out.println(doc.getFieldValue("title").toString());
				System.out.println(doc.getFieldValue("id"));
				articles.add(docs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("从solr获取相关新闻时遇到错误", e);
		}
		return articles;
	}

	// TODO:可用
	public static List<String> spellcheck(String word) {

		List<String> wordList = new ArrayList<String>();

		SolrQuery query = new SolrQuery();

		// query.set("q","name:ac");

		// query.set("qt", "/spell");

		// 默认是主索引
		query.set("q", "title:" + word + "");
		// query.set("q", "spell:" + word + "");

		query.set("qt", "/select");

		query.set("spellcheck.build", "true");// 遇到新的检查词，会自动添加到索引里面

		// query.set("spellcheck.dictionary",
		// "file");//使用副索引，checkSpellFile里面的进行使用

		query.set("spellcheck", "true");

		query.set("spellcheck.count", 1);
		query.setRows(1);
		// params.set("spellcheck.build", "true");

		try {

			QueryResponse rsp = solrServer.query(query);

			System.out.println("直接命中:" + rsp.getResults().size());

			SolrDocumentList ss = rsp.getResults();

			for (SolrDocument doc : ss) {

				System.out.println(doc.get("title"));

			}

			// …上面取结果的代码

			SpellCheckResponse re = rsp.getSpellCheckResponse();

			if (re != null) {

				for (Suggestion s : re.getSuggestions()) {

					List<String> list = s.getAlternatives();

					for (String spellWord : list) {

						System.out.println(spellWord);

						// wordList.add(spellWord);

					}

				}

				// for(Collation s: spellCheckResponse.getCollatedResults()){

				// System.out.println(s.toString());

				// }

			}

			return wordList;

		} catch (SolrServerException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return null;

	}

	public static List<String> suggest(String word) throws Exception {

		List<String> wordList = new ArrayList<String>();
		SolrQuery query = new SolrQuery();
		query.set("q", "title:" + word);// 查询的词
		query.set("qt", "/suggest");// 请求到suggest中
		query.set("spellcheck.count", "10");// 返回数量
		QueryResponse rsp = solrServer.query(query);
		// System.out.println("直接命中:"+rsp.getResults().size());
		// …上面取结果的代码
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

			// List<Collation> list=re.getCollatedResults();//
			String t = re.getFirstSuggestion(word);// 获取第一个推荐词
			System.out.println("推荐词：" + t);
			// for(Collation c:list){
			//
			// System.out.println("推荐词:"+c.getCollationQueryString());
			// }

		}

		return null;

	}

//	/http://kfcman.iteye.com/blog/1978709
	public static void main(String[] args) throws Exception {
		// getRelated("18664473319", 10);
	// spellCheck("title:裤子");
		//spellcheck("男裤广泛的鬼地方 ");
		suggest("bf");
	}
	
	
}
