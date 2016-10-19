package com.fei.core.exception;


public class DeleteException extends SolrException {

	public DeleteException(String message) {
		super(message+"£¬É¾³ýË÷ÒýÊ§°Ü¡£");
	}
}
