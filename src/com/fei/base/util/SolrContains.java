package com.fei.base.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * solr常量
 * @author kangf
 * 2014年5月12日10:31:14
 *
 */
public class SolrContains {

	public  String solr_url = "";
	public  int soTimeout = 0;
	public  int connectionTimeout = 0 ;
	
	private  final String filename = "solr_properties.properties";
	
	private static SolrContains instance;
	
	private SolrContains(){
		InputStream in = SolrContains.class.getClassLoader().getResourceAsStream(filename);
		 Properties p = new Properties();
		 try {
			p.load(in);
			
			solr_url = p.getProperty("solr_url");
			soTimeout = Integer.parseInt(p.getProperty("solr_soTimeout"));
			connectionTimeout = Integer.parseInt(p.getProperty("solr_connectionTimeout"));
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static SolrContains Instance(){
		if(instance == null){
			instance = new SolrContains();
			
		}
		
		return instance;
	}

}
