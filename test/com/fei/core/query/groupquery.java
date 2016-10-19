package com.fei.core.query;

import com.fei.core.exception.QueryException;

public class groupquery {

	public static void main(String[] args) throws QueryException {
		TestQuery query = new TestQuery();
		query.queryData("addday:20150129 AND name:Å®Ê¿ÏãË®",1,10	);
	}
}
