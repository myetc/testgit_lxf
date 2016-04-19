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
public class AbsHTTP_Send<K> {

	HTTP_Config HTTP_Config;
	boolean refresh = true;
	boolean get = false;
	View view;
	 Map cookie ;
	String tag = getClass().getName();
	List<String> list_cliear = new ArrayList<String>();
	
	public AbsHTTP_Send() {
	}

//	public static final AbsHTTP_Send getInstance() {
////		return LazyHolder.INSTANCE;
//		return new AbsHTTP_Send();
//	}
	
//	// 线程安全单例
//	private static class LazyHolder {
//		private static final AbsHTTP_Send INSTANCE = new AbsHTTP_Send();
////		return new HTTP_Send();
//	}
	private K self(){
		return (K) this;
	}
	//取消单例
//	public static final K getInstance() {
////		return LazyHolder.INSTANCE;
//		return new HTTP_Send();
//	}
//	
	
	public  void getData(final Context context, String url,
			Map<String, Object> params, final List listOld, final Handler handler,final IRequest_JSON Request) {
		// 传入的应该一个指定的JSON
		// 传出的应该是一个数据源

		// 回调解析模块
		// 如果让解析模块的回调的重用性
		if(url==null){
			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message("param url:"+url ), 0).show();
			return;
		}
		if(handler==null){
			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message("param handler:"+handler ), 0).show();
			return;
		}
		final Message msg  = handler.obtainMessage();
		
		if(context==null){
			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message("param context:"+context ), 0).show();
			return;
		}
		if(Request==null){
			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message("param Request:"+Request ), 0).show();
			return;
		}
		if(listOld==null){
			//数据源不能为NULL.返回给开发者
			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message("param listOld:"+listOld ), 0).show();
			return;
		}
		
		//调用接口,进行请求网格
	
		AjaxCallback<JSONObject> callback = new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				HTTP_Log.getInstance().setTag(tag).sdk_message("服务器返回了……");
				HTTP_Log.getInstance().setTag(tag).sdk_message("请求url:" + url);
				HTTP_Log.getInstance().setTag(tag).sdk_message("请求返回code:" + status.getCode());
				HTTP_Log.getInstance().setTag(tag).sdk_message("请求返回json:" + json);
				// 根据服务器返回情况来向控制中心返回
				if (status.getCode() > 0) {
					
					if(json==null){
						msg.what = -1;
						msg.obj = HTTP_Log.shownojsonmessage+"	json:"+json;
					}else{
						//请求成功
						HTTP_DATA HTTP_DATA = Request.getJson(url, json, status);
						if(HTTP_DATA==null){

							//新数据源不能为NULL.返回给开发者
							msg.what = -1;
							msg.obj = HTTP_Log.shownodatamessageforjson_http+"	HTTP_DATA:"+HTTP_DATA;
						
						} else {
							try {
								//反解析值进行返回
								HTTP_MESSAGE.ToMessage(msg, HTTP_DATA.getT());
							

								if (HTTP_DATA.getList() == null) {
									// 新数据源不能为NULL.返回给开发者
									msg.what = -1;
									msg.obj = HTTP_Log.shownodatamessageforjson
											+ "	listNew:" + HTTP_DATA.getList();
								} else if (HTTP_DATA.getList().size() < 1) {
									// 新数据源不能为NULL.返回给开发者
									msg.what = 2;
									msg.obj = HTTP_Log.shownodatamessageforjson
											+ "	listNew size:"
											+ HTTP_DATA.getList().size();

								} else {
									// 进行数据匹配
									if (Request instanceof IRequest_JSON_Rule) {
										// 进进子类的验证
										int count = ((IRequest_JSON_Rule) Request)
												.RuleData(listOld,
														HTTP_DATA.getList());
										if (count > 0) {

											msg.what = 1;
											msg.obj = HTTP_Log.showdatamessageforjson_yes;

										} else {
											// 验证后无数据
											msg.what = 2;
											msg.obj = HTTP_Log.showdatamessageforjson_no;
										}
									} else {
										// 如果无规则,直接进行数据添加到最后
										listOld.addAll(HTTP_DATA.getList());
										msg.what = 1;
										msg.obj = HTTP_Log.showdatamessageforjson;
									}
								}

							} catch (ClassCastException e) {
								msg.what = -1;
								msg.obj = HTTP_Log.ClassCastException
										+ "	Please Serializable "
										+ HTTP_DATA.getT().getClass().getName()
										+ ";";
								e.printStackTrace();
							} catch (Exception e) {
								msg.what = -1;
								msg.obj = HTTP_Log.ClassCastException
										+ "	other error "
										+ HTTP_DATA.getT().getClass().getName()
										+ ";";
								;
								e.printStackTrace();
							}
						}
					}
					
				} else {
					status.invalidate();// 不保存缓存
					//请求失败,请求错误参数
					if(status.getCode()==AjaxStatus.NETWORK_ERROR){
						msg.what = -2;
						msg.obj = HTTP_Log.showerrormessage;
					}else if(status.getCode()==AjaxStatus.TRANSFORM_ERROR){
						msg.what = -1;
						msg.obj = HTTP_Log.showerrormessageforserver;
					}else{
						msg.what = -1;
						msg.obj = HTTP_Log.showerrormessageforOther;
					}
					
				
				}
				sendMessage(context,handler, msg);
				// 测试例：总是返回数据（JSON解析模拟数据）
				// 解析JSON，得到数据

			}

		};
		callback.setReuseHttpClient(true);
		//控制是否显示加载中
		if(view!=null)
		callback.progress(view);
		// 请求url，注意：缓存（是以文件形式，而非sqlite）是以本url来缓存的，
		// 如果是post请求，每次请求的分页url都相同，参数填写在params中，请模拟一个get的参数来
		// 让程序区分缓存
		// isRefresh true代表刷新，不取缓存 false代表有缓存直接取缓存，无缓存取网络
		// isFileCache 同 isRefresh
		
		
		// 对URL进行处理，用来拼结get，也同时用来更精确的区别本地缓存文件
		
		if (params != null) {
			String value = "";
			boolean is_head = true;
			for (Entry<String, Object> me : params.entrySet()) {
				//如果存在？ 号，不进行 第一个问号 
				boolean  isE = url.contains("?");
				String wenhao = isE?"&":"?";
				String type = is_head?wenhao:"&";
				is_head = false;
				
			
				
				String v_d = type + me.getKey() + "="
						+ ((me.getValue()==null)?"":me.getValue().toString());
				value += v_d;
			
				
					HTTP_Log.getInstance().setTag(tag).sdk_message("执行参数 post or get || key：  " + me.getKey()
							+ "   vlaue ： " + me.getValue());
			}
			//是否调用get请求，如果调用GET 同时也会POST 默认POST请求
			if(get){
				url = url + value;
			}
		
			
			
			//去除不进行处理的变量
			String value_demp = value;
			for (String object : list_cliear) {
				value_demp = value_demp.replace(object, "");
			}
//			System.err.println("value_demp:"+value_demp);
			
			boolean  isE = url.contains("?");
			String wenhao = isE?"&":"?";
			url = url+wenhao+"otherparam="+AQUtility.getMD5Hex(value_demp);
		}
		
		try {
			exeUrl(HTTP_Config,context, params, callback, url, refresh);
		} catch (SecurityException e) {
			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message("no permission"), 0).show();
			e.printStackTrace();
		} catch (Exception e) {
			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message("other error"), 0).show();
			e.printStackTrace();
		}
		return ;

	}
	/**
	 * 请求配置 
	 * @param context
	 * @param params
	 * @param callback
	 * @param url
	 * @param refresh 是否刷新
	 * 
	 * 请求指定刷新情况下
	 * 1:如果是连接网络,一定请求刷新
	 * 2:如果无网络 请求刷新
	 * 请求不刷新尾部下
	 * 1:如果连接WIFI,不刷新更改为刷新
	 * 2:如果有网络,没连接WIFI,拿本地缓存,如果存在本地缓存,不刷新,否则刷新
	 * 3:如果无网络 请求不刷新
	 * 
	 */
	private  void exeUrl(HTTP_Config HTTP_Config,Context context, Map<String, Object> params,
			AjaxCallback<JSONObject> callback, String url,boolean refresh) {
		
		
		// isRefresh true代表刷新，不取缓存 false代表有缓存直接取缓存，无缓存取网络
		// isFileCache 同 isRefresh
//		refresh = true;
		boolean isRefresh;
		boolean isFileCache = true;
		boolean isMemCache = true;
		AQuery aq = new AQuery(context);
		// cacheTime 代表缓存时间 1000*60*60*24 代表一天
		long cacheTime =  HTTP_Log.cachedTime;// 半个月
		boolean isNet= verifyNET(context);
		if (isNet) {// 有网络，进行取网络数据，并刷新本地

			if(refresh){
				isRefresh = true;
			}else{
				if(isWifiConnected(context)){
					isRefresh = true;
				}else{
					File file = aq.getCachedFile(url);
					if(file!=null){
						isRefresh = false;
					}else{
						isRefresh = true;
					}
				}
			}
		} else {// 无网络，取本地缓存
			isRefresh = false;// 不刷新，代表去搜索本地缓存
			
			
		}
		
		//必须去执行的
		if (HTTP_Config != null) {
			if (HTTP_Config.must) {
				if (HTTP_Config.must_refresh) {
					isRefresh = true;

				} else {
					isRefresh = false;

				}
				isFileCache = HTTP_Config.isFileCache;
				if (HTTP_Config.content != null) {
					callback.saveContent(HTTP_Config.content.toString());
				}
				callback.saveSD(HTTP_Config.saveSD);

				isMemCache = HTTP_Config.isMemCache;
			}
		}
		
		
		callback.encoding("utf8");
		// 请求并发数量，即同时向服务器请求线程数量
		callback.setNetworkLimit(1);
		// 请求刷新 既不取内存也不取文件缓存 true代表刷新，默认为false
		callback.memCache(isMemCache);
		callback.refresh(isRefresh);
		callback.fileCache(isFileCache);
		callback.expire(cacheTime);
		callback.url(url);
		// 回调数据设置，一般都是json，这里就写死了，当然，也可以更改，更改的同时请更改回调类型
		callback.type(JSONObject.class);
		callback.params(params);
		// cookie 的添加
		
		callback.cookies(cookie);
		
		aq.ajax(callback);
		
	}
	private  void sendMessage(Context context,Handler handler,Message msg){
		
		HTTP_Log.getInstance().setTag(tag).sdk_message("提示语:"+msg.obj );
		HTTP_Log.getInstance().setTag(tag).sdk_message("提示码  :"+msg.what);
		if(msg.what==-1&&msg.obj!=null){
			//不再提示错误
//			Toast.makeText(context, HTTP_Log.getInstance().setTag(tag).sdk_message(msg.obj.toString()), 0).show();
		}
		if(handler==null){
			HTTP_Log.getInstance().setTag(tag).sdk_message( "提示handler :"+handler);
		}else{
			handler.sendMessage(msg);
			
		}
	}
	
	/**
	 * 验证网络是否正常
	 */
	private static boolean verifyNET(Context context) {
		return Tool_NetworkUtils.isNetworkConnected(context.getApplicationContext());
	}
	/**
	 * 验证是否是WIFI网络
	 */
	private static boolean isWifiConnected(Context context) {
		return Tool_NetworkUtils.isWifiConnected(context.getApplicationContext());
	}

	public HTTP_Config getHTTP_Config() {
		return HTTP_Config;
	}

	public boolean isRefresh() {
		return refresh;
	}


	
public Map getCookie() {
	return cookie;
}




	public boolean isGet() {
		return get;
	}

	public View getView() {
		return view;
	}

	public K setHTTP_Config(HTTP_Config hTTP_Config) {
		HTTP_Config = hTTP_Config;
		return self();
	}

	public K setRefresh(boolean refresh) {
		this.refresh = refresh;
		return self();
	}

		public K setCookie(Map cookie) {
		this.cookie = cookie;	return self();
	}
		
		public K putClearKey(String key){
			list_cliear.add(key);
			return self();
		}
	
	


	public K setGet(boolean get) {
		this.get = get;
		return self();
	}

	public K setView(View view) {
		this.view = view;
		return self();
	}

	

}//
