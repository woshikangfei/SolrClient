package com.fei.core.udpate;

import java.util.HashMap;
import java.util.Map;

import com.fei.core.update.AtmoUpdate;

public class testUpdate {

	public static void main(String[] args) {
		
		AtmoUpdate au = new AtmoUpdate();
		
		Map<String,String> maps = new HashMap<String,String>();
		maps.put("price", "1");
		au.update("10086", maps);
	}
}
