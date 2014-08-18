package com.mycorp.tmlib;

import org.oasisopen.xliff.om.v1.IContent;

public class Entry {

	private String searchKey;
	private IContent source;
	private IContent target;
	
	static public String makeSearchKey (IContent source) {
		return source.getPlainText().toLowerCase();
	}
	
	public Entry (IContent src,
		IContent trg)
	{
		this.source = src;
		this.target = trg;
		searchKey = makeSearchKey(this.source);
	}
	
	public IContent getSource () {
		return source;
	}
	
	public IContent getTarget () {
		return target;
	}
	
	public String getSearchKey () {
		return searchKey;
	}
}
