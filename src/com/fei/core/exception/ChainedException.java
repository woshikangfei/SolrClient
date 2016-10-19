package com.fei.core.exception;


import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * 异常处理类
 * 2014年5月12日10:54:04
 * @author kangfei
 *
 */
public class ChainedException extends Exception {
	protected Throwable throwable;

	public ChainedException() {
	}

	public ChainedException(String message) {
		super(message);
	}

	public ChainedException(Throwable throwable) {
		this.throwable = throwable;
	}

	public ChainedException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}

	public String[] getMessageChain() {
		Vector list = getMessageList();
		String[] chain = new String[list.size()];
		list.copyInto(chain);
		return chain;
	}

	public Vector getMessageList() {
		Vector list = new Vector();
		list.addElement(getMessage());
		if (throwable != null) {
			if (throwable instanceof ChainedException) {
				ChainedException chain = (ChainedException) throwable;
				Vector sublist = chain.getMessageList();
				for (int i = 0; i < sublist.size(); i++)
					list.addElement(sublist.elementAt(i));
			} else {
				String message = throwable.getMessage();
				if (message != null && !message.equals("")) {
					list.addElement(message);
				}
			}
		}
		return list;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void printStackTrace() {
		printStackTrace(System.err);
	}

	public void printStackTrace(PrintStream out) {
		synchronized (out) {
			if (throwable != null) {
				out.println(getClass().getName() + ": " + getMessage() + ";");
				throwable.printStackTrace(out);
			} else {
				super.printStackTrace(out);
			}
		}
	}

	public void printStackTrace(PrintWriter out) {
		synchronized (out) {
			if (throwable != null) {
				out.println(getClass().getName() + ": " + getMessage() + ";");
				throwable.printStackTrace(out);
			} else {
				super.printStackTrace(out);
			}
		}
	}
}
