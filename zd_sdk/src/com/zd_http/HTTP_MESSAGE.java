package com.zd_http;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.os.Message;

public  class HTTP_MESSAGE {
	private static String HTTP_DATA_KEY = "HTTP_DATA_KEY";
	
	public  static   <T> T FormatMessage(Message msg){
		if(msg==null)return null;
		Bundle bundle = msg.getData();
		if(bundle==null)return null;
		Serializable Serializable= bundle.getSerializable(HTTP_DATA_KEY);
		if(Serializable==null)return null;
		T tb = (T)Serializable ;
		
		
		
		return tb;
	}

	public static <T> void ToMessage(Message msg, T t) {
		if(msg==null)return ;
		if(t==null)return ;
		Bundle Bundle = new Bundle();
		Bundle.putSerializable(HTTP_MESSAGE.HTTP_DATA_KEY, (Serializable) t);
		msg.setData(Bundle);
	}
	

}
