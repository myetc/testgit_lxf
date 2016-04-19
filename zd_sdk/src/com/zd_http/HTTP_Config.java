package com.zd_http;

public class HTTP_Config {
	public boolean must = false;
	public boolean must_refresh;
	public boolean isFileCache;
	public StringBuffer content;
	public boolean saveSD;
	public boolean isMemCache;
	
	public boolean isMust() {
		return must;
	}
	public HTTP_Config setMust(boolean must) {
		this.must = must;
		return this;
	}
	public boolean isMust_refresh() {
		return must_refresh;
	}
	public HTTP_Config setMust_refresh(boolean must_refresh) {
		this.must_refresh = must_refresh;
		return this;
	}
	public boolean isFileCache() {
		return isFileCache;
	}
	public void setFileCache(boolean isFileCache) {
		this.isFileCache = isFileCache;
	}
	public StringBuffer getContent() {
		return content;
	}
	public void setContent(StringBuffer content) {
		this.content = content;
	}
	public boolean isSaveSD() {
		return saveSD;
	}
	public void setSaveSD(boolean saveSD) {
		this.saveSD = saveSD;
	}
	public boolean isMemCache() {
		return isMemCache;
	}
	public void setMemCache(boolean isMemCache) {
		this.isMemCache = isMemCache;
	}
	
	
	
	
}
