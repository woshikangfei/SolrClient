package com.fei.core.update;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fei.base.util.SolrContains;
import com.fei.core.exception.SolrException;

public class AtmoUpdate implements Update {
	
public static final Logger log =Logger.getLogger(AtmoUpdate.class);
	
	private SolrServer server ;
	
	public AtmoUpdate(){
		server = new HttpSolrServer(getUrl());
	}
	
	public AtmoUpdate(SolrServer server){
		if(server == null){
			log.error("server can not be null");
			try {
				throw new SolrException("SolrServer can not be null");
			} catch (SolrException e) {
				e.printStackTrace();
			}
		}
		
		this.server = server;
	}
	
	/**
	 * 单字段更新,
	 * @param id
	 * @param value
	 * @param field
	 */
	public void update(String id,String value,String field){
		SolrInputDocument doc = new SolrInputDocument();
		Map<String, String> opr = new HashMap<String, String>();
		opr.put("set", value);
		doc.addField(field,opr);
		doc.addField("id",id);
		try {
			server.add(doc);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据ID更新
	 * @param id 需要更新的ID
	 * @param maps 更新的字段，key=字段名,value=更新后的值
	 */
	public void update(String id ,Map<String,String> maps){
		SolrInputDocument doc = new SolrInputDocument();
		
		
		Set<String> keySet = maps.keySet();
		for(Object obj:keySet){
			Map<String, String> opr = new HashMap<String, String>();
			opr.put("set", maps.get(obj));
			doc.addField(String.valueOf(obj),opr);
		}
		
		doc.addField("id",id);
		try {
			server.add(doc);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据ID数组批量更新
	 * @param id 需要批量更新的ID数组
	 * @param maps 更新的字段，key=字段名,value=更新后的值
	 */
	public void update(String[] id ,Map<String,String> maps){
		
		 Collection docs = new ArrayList();
		 
		for (int i = 0; i < id.length; i++) {
			SolrInputDocument doc = new SolrInputDocument();
			
			Set<String> keySet = maps.keySet();
			for(Object obj:keySet){
				Map<String, String> opr = new HashMap<String, String>();
				opr.put("set", maps.get(obj));
				doc.addField(String.valueOf(obj),opr);
			}
			
			doc.addField("id",id[i]);
			
			docs.add(doc);
		}
		
		try {
			server.add(docs);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void update(Map map, String query) {
		if(map==null){
			log.error("map can not be null");
			return;
		}
		
		JSONArray content = new JSONArray();
		JSONObject json = new JSONObject();
		//Set<Object> keySet = map.keySet();
		JSONObject set = new JSONObject();
		JSONObject inc = new JSONObject();
		JSONObject add = new JSONObject();
		
		try{
			/*for(Object obj:keySet){
				json.put(String.valueOf(obj), map.get(obj));
			
			}*/
			set.put("set", "111111"); //
			//inc.put("KV_SOURCE", "111111111"); 
			//add.put("KV_TITLE", "111111111"); //
			json.put("ID", "f9f14e8a5006468580eb6a7d0e3e5e04").put("KV_SITE", set);//.put("KV_SOURCE", inc).put("KV_TITLE", add);
			
			
			content.put(json);
			
			System.out.println(content);
			System.out.println(json);
			sendHttpMessage(this.getUrl() + "update", content.toString());
			
		}catch(Exception e){
			log.error("updatge error"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void UpdateIndex() {
		JSONArray content = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject set = new JSONObject();
		JSONObject inc = new JSONObject();
		JSONObject add = new JSONObject();
		
		try {
			set.put("set", "Neal Stephenson"); // {"set":"Neal Stephenson"}
			inc.put("inc", "3"); //{"inc":3}
			add.put("add", "Cyberpunk"); //{"add":"Cyberpunk"}			
			json.put("id", "1").put("author_s", set).put("type_i", inc).put("cat_ss", add);
			content.put(json);
			System.out.println(content);
			System.out.println(json);
		} catch (final JSONException e) {
		}
		sendHttpMessage(this.getUrl() + "update", content.toString());
	}
	
	private String sendHttpMessage(String url, String contents){
		return sendHttpMessage(url,"POST",contents);
	}
	
	private static String sendHttpMessage(String url, String method, String contents) {
		try {
			
			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl
					.openConnection();
			conn.setConnectTimeout(20000);
	
			conn.setRequestMethod(method);// "POST" ,"GET"
			
			conn.addRequestProperty("Accept", "*/*");
			conn.addRequestProperty("Accept-Language", "zh-cn");
			conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.addRequestProperty("Cache-Control", "no-cache");
			conn.addRequestProperty("Accept-Charset", "UTF-8");
			conn.addRequestProperty("Content-type", "application/json");			
			
			if (method.equalsIgnoreCase("GET")) {
				conn.connect();
			}
	
			else if (method.equalsIgnoreCase("POST")) {
	
				conn.setDoOutput(true);
				conn.connect();
				conn.getOutputStream().write(contents.getBytes());
			} else {
				throw new RuntimeException("your method is not implement");
			}
	
			InputStream ins = conn.getInputStream();
	
			// 处理GZIP压缩的
			if (null != conn.getHeaderField("Content-Encoding")
					&& conn.getHeaderField("Content-Encoding").equals("gzip")) {
				byte[] b = null;
				GZIPInputStream gzip = new GZIPInputStream(ins);
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((num = gzip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
				gzip.close();
				ins.close();
				return new String(b, "UTF-8").trim();
			}
	
			String charset = "UTF-8";
			InputStreamReader inr = new InputStreamReader(ins, charset);
			BufferedReader br = new BufferedReader(inr);
	
			String line = "";
			StringBuffer sb = new StringBuffer();
			do {
				sb.append(line);
				line = br.readLine();
			} while (line != null);			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();			
			return "";
		}
	
	}
	
	@Override
	public String getUrl() {
		return SolrContains.Instance().solr_url;
	}
	@Override
	public int getSoTimeOut() {
		return SolrContains.Instance().soTimeout;
	}
	@Override
	public int getConnectionTimeOut() {
		return SolrContains.Instance().connectionTimeout;
	}
	@Override
	public boolean optimize() {
		return true;
	}

}
