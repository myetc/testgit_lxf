package com.zd_http;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.R.fraction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.tool.Tool_NetworkUtils;
import com.zd_http.request_json.IRequest_JSON;
import com.zd_http.request_json.IRequest_JSON_Rule;
/**
 * 信息返回说明
 *  msg.waht = -1;json为NULL 缺少解析块 等解析错误
 *  msg.waht = -2;连接错误,网络连接,请求连接
 *  
 * @author lixifeng
 * @param <K>
 *
 */
public class HTTP_Send extends AbsHTTP_Send<HTTP_Send> {



//取消单例
	public static final HTTP_Send getInstance() {
//		return LazyHolder.INSTANCE;
		return new HTTP_Send();
	}
	
}
