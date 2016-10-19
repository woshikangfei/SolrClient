package com.fei.core.delete;

import com.fei.core.exception.DeleteException;

public class TestDelete {

	public static void main(String[] args) throws DeleteException {
		
		CommonDelete delete = new CommonDelete();
		delete.deleteIndexByQuery("addday:20150213");
	}
}
