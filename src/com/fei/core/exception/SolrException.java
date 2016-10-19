package com.fei.core.exception;


import org.apache.log4j.Logger;


/**
 * 2014年5月12日10:33:47
 * @author kangf
 * chained exceptions实现类。
 */
public class SolrException extends ChainedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6236680757456762381L;
	public static final Logger log =Logger.getLogger(SolrException.class);
	
	public SolrException() {
	}

	public SolrException(String message) {
		super(message);
		log.error(message);
	}

	public SolrException(Throwable throwable) {
		super(throwable);
		log.error(throwable.getMessage());
	}

	public SolrException(String message, Throwable throwable) {
		super(message, throwable);
		log.error(message);
	}

}
