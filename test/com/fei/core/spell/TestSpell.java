package com.fei.core.spell;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;

public class TestSpell {

	public String spellCheck(String word){ 
		HttpSolrServer server  =new HttpSolrServer ("http://localhost:8080/solr/");
        SolrQuery query = new SolrQuery();   
        query.setRequestHandler("/mlt");
        query.setQuery("name:100")              
        .setParam("fl", "id,title")          
        .setParam("mlt", "true")              
        .setParam("mlt.fl", "title") ;          
        //.setParam("mlt.mindf", "1")           
       // .setParam("mlt.mintf", "1");
      /* query.set("defType","edismax");//加权     
        query.set("qf","name^20.0"); 
          
        query.set("spellcheck", "true");   
        query.set("spellcheck.q", word); 
        query.set("qt", "/spell");   
        query.set("spellcheck.build", "true");//遇到新的检查词，会自动添加到索引里面           query.set("spellcheck.count", 5); 
*/             System.out.println(query.toString());
        try {   
         QueryResponse rsp = server.query(query);   
         SpellCheckResponse re=rsp.getSpellCheckResponse();   
         if (re != null) {   
          if(!re.isCorrectlySpelled()){ 
              String t = re.getFirstSuggestion(word);//获取第一个推荐词        System.out.println("推荐词：" + t); 
     return t; 
          }                   
         }  
        } catch (SolrServerException e) {   
            e.printStackTrace();   
        }   
        return null; 
    }
	
	public static void main(String[] args) {
		TestSpell ts = new TestSpell();
		ts.spellCheck("p");
	}
}
