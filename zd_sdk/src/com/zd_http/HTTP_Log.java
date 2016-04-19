package com.zd_http;


import android.util.Log;

public class HTTP_Log {
	

	public static long cachedTime = 1000 * 60 * 60 * 24 * 15;
	public static boolean ZDDebugMode_server = false;
	
	/**
	 * 刷新规则：
	 * A：有网络情况下
	 * ZDisRefresh = true状态下
	 * 1：WIFI下刷新（取最新数据）
	 * 2：非WIFI刷新（取最新数据）
	 *   ZDisRefresh = false状态下
	 * 1：WIFI下刷新（取最新数据）
	 * 2：非WIFI不刷新（验证是滞有缓存，有就取，没有就刷）
	 * A：无网络情况下
	 * 取缓存
	 */
	public static boolean ZDisRefresh = true;
	
	public static String showdatamessageforjson_yes = "数据对比完毕_有新数据";
	public static String showdatamessageforjson_no = "数据对比完毕_重复数据";
	public static String showdatamessageforjson = "数据对比完毕";
	public static String shownodatamessageforjson = "未解析出数据";
	public static String shownodatamessageforjson_http = "缺少解析块";
	public static String shownojsonmessage = "未返回数据/接口可能错误";
	public static String shownodatamessage = "数据加载完毕";
	public static String showerrormessage = "网络连接错误";
	public static String showerrormessageforserver = "服务器返回异常";
	public static String showerrormessageforOther = "连接其它异常";
	public static String showerrorstate = "调用状态错误";
	public static String shownoanalyticalmessage = "未找到匹配JSON解析模块";
	public static String showlocationstarterror = "启动Activity错误，请检查";
	
	public static String ClassCastException = "指定对应解析模块必须为序列化对像";
	
	
	
	
	 String tag  = getClass().getName();
	// 线程安全单例
	private static class LazyHolder {
		
		private static final HTTP_Log INSTANCE = new HTTP_Log();
	}

	private HTTP_Log() {
		
	}

	public  final HTTP_Log setTag(String tag) {
		this.tag = tag;
		return getInstance();
	}
	public static final HTTP_Log getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	public  final String sdk_message(String message)
	{
		if(ZDDebugMode_server){
			Log.w(tag,message);
			
		}
		
		return message;
		
	}
	

}
