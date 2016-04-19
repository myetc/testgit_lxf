package com.zd_http.request_json;

import org.json.JSONObject;

import com.androidquery.callback.AjaxStatus;
import com.zd_http.HTTP_DATA;
/**
 * 数据实现抽像接口
 * @author lixifeng
 *
 */
public interface IRequest_JSON {
	/**
	 * 调用者实现体,用来解析JSON数据到一个LIST容器中去
	 * @param <T>
	 * @param <T>
	 */
	public   HTTP_DATA getJson(String url, JSONObject json, AjaxStatus status);
}
