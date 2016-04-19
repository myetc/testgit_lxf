package com.zd_http;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.os.Message;

public class HTTP_DATA<T> {

	private List list;
	private  T t;
	
	
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	

}
