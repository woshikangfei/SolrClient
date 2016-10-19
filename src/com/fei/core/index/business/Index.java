package com.fei.core.index.business;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.fei.base.util.UUIDGenerator;
import com.fei.core.index.IndexAbstractSupport;

/**
 * 2014年5月12日11:00:39 生成索引.
 * 
 * @author kangf
 * 
 */
public class Index extends IndexAbstractSupport {
	
	public static final Logger log =Logger.getLogger(Index.class);

	public Index(List list) throws IOException, SolrServerException {
		super(list);
	}

	@Override
	public void process() throws SQLException {
		log.info("current data size ："+this.data.size());
		long start = System.currentTimeMillis();
		if (this.data == null)
			return;
		for(int i=0;i<data.size();i++){
			HashMap map = (HashMap) data.get(i);
			
			SolrInputDocument doc = new SolrInputDocument();
			
			if(map.get("KR_INFOTYPE")!=null)
				doc.addField("KR_INFOTYPE", map.get("KR_INFOTYPE").toString().trim());
			if(map.get("KR_ISLOCAL")!=null)
				doc.addField("KR_ISLOCAL", map.get("KR_ISLOCAL").toString().trim());
			if(map.get("KR_KEYWORDID")!=null)
				doc.addField("KR_KEYWORDID", map.get("KR_KEYWORDID").toString().trim());
			if(map.get("KR_STATE")!=null)
				doc.addField("KR_STATE", map.get("KR_STATE").toString().trim());
			if(map.get("KR_UID")!=null)
				doc.addField("KR_UID", map.get("KR_UID").toString().trim());
			if(map.get("KV_ABSTRACT")!=null)
				doc.addField("KV_ABSTRACT", map.get("KV_ABSTRACT").toString().trim());
			if(map.get("KV_CTIME")!=null)
				doc.addField("KV_CTIME", map.get("KV_CTIME").toString().trim());
			if(map.get("KV_DK_TIME")!=null)
				doc.addField("KV_DK_TIME", map.get("KV_DK_TIME").toString().trim());
			if(map.get("KV_ORIEN_LEVEL")!=null)
				doc.addField("KV_ORIEN_LEVEL", map.get("KV_ORIEN_LEVEL").toString().trim());
			if(map.get("KV_ORIENTATION")!=null)
				doc.addField("KV_ORIENTATION",map.get("KV_ORIENTATION").toString().trim());
			if(map.get("KV_REPLY")!=null)
				doc.addField("KV_REPLY",map.get("KV_REPLY").toString().trim());
			if(map.get("KV_SITE")!=null)
				doc.addField("KV_SITE", map.get("KV_SITE").toString().trim());
			if(map.get("KV_SOURCE")!=null)
				doc.addField("KV_SOURCE", map.get("KV_SOURCE").toString().trim());
			if(map.get("KV_SOURCETYPE")!=null)
				doc.addField("KV_SOURCETYPE", map.get("KV_SOURCETYPE").toString());
			if(map.get("KV_STATE")!=null)
				doc.addField("KV_STATE", map.get("KV_STATE").toString().trim());
			if(map.get("KV_TITLE")!=null)
				doc.addField("KV_TITLE", map.get("KV_TITLE").toString().trim());
			if(map.get("KV_URL")!=null)
				doc.addField("KV_URL",map.get("KV_URL").toString().trim());
			if(map.get("KV_UUID")!=null)
				//doc.addField("KV_UUID",map.get("KV_UUID").toString().trim());
			if(map.get("KV_VISITCOUNT")!=null)
				doc.addField("KV_VISITCOUNT", map.get("KV_VISITCOUNT").toString().trim());
			if(map.get("KR_UUID")!=null){
				//doc.addField("ID", map.get("KR_UUID").toString().trim());
				doc.addField("ID", UUIDGenerator.getUUID());
				//doc.addField("KR_UUID", map.get("KR_UUID").toString().trim());
			}
			
			
			doc.addField("KR_ISMYATTENTION", "0");
			doc.addField("KR_ISYJ",0);
			doc.addField("KR_ISWTFK", 0);
			
			docs.add(doc);
			++_totalSql;
		}
		long end = System.currentTimeMillis();
		log.info(" document add finish,send times:"+((end-start)/1000)+"秒");
		
	}

}
