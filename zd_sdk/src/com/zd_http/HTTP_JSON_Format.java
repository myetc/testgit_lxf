package com.zd_http;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * JSON解析的抽象类
 * @author Administrator
 *
 */
public  abstract class HTTP_JSON_Format {
//  <!--
//┏━━━━━━━━━━┓
//┃▉▉▉▉▉▉ 99.9% ┃
//┗━━━━━━━━━━┛
//  -->
//搜索JSON值
 public static String sendImageType = "";
 public static String sendImageType_qiniu = "";
 //用户头像不进行七牛处理
 private static String image_key_avatar = "avatar_new#$%^&*()_+";

	public static String jsonGetString(JSONObject json,String key){
		return jsonGetString(json, key,null, -1);
	}
	public static String jsonGetString(JSONObject json,String key,String defa){
		return jsonGetString(json, key,defa, -1);
	}
	public static String jsonGetString(JSONObject json,String key,int maxDim){
		return jsonGetString(json, key,null, maxDim);
	}
	public static String jsonGetString(JSONObject json,String key,String defa,int maxDim){
		try {
			String value = json.getString(key);
			
//			
			if(defa==null) defa = "";
			value = (value == null || "null".equals(value) || ""
					.equals(value)) ? defa : value;
			if(image_key_avatar.equals(key)){
				return value;
			}else{
				return jsonGetString(value, maxDim);
			}
			
		} catch (Exception e) {
			//Log.e("JSON解析值不存在", key+"不存在");
		}
		return defa;
	}
	
	public static String jsonGetString(String value,int maxDim){
		return jsonGetString(value, "0", maxDim,maxDim);
	}
	public static String jsonGetString(String value,String mode,int w,int h){
			if(
					w>0
					&&sendImageType.equals(sendImageType_qiniu)
					&&value!=null
					&&value.length()>0
					&&value.startsWith("http")
					&&!value.contains("taobao")
					&&!value.contains("om=avatar_attach")
					){
				value = getNewImagePath(value, mode,w,h);
//				for (int i = 0; i < ImageConfig.sendImageType_qiniu_s.length; i++) {
//					if(value.contains(ImageConfig.sendImageType_qiniu_s[i])){
//						//是七牛 进行地址更换
//						value = getNewImagePaht(value,maxDim);
//						break;
//					};
//				}
			}
			
			return value;
	}
	
	public static String getNewImagePath(String path,int maxDim){
		return getNewImagePath(path, "0", maxDim,maxDim);
	}
	public static String getNewImagePath(String path,String mode,int w,int h){
//		System.out.println("path:"+path);
		if(path==null||path.length()<1)return path;
		int po = path.indexOf("?");
		if(po<0)return path;
		path = path.substring(0, po);
//		http://developer.qiniu.com/docs/v6/api/reference/fop/image/imageview2.html
//		imageView2/<mode>
//        /w/<Width>
//        /h/<Height>
//        /q/<Quality>
//        /format/<Format>
		if(w!=Integer.MAX_VALUE){
			path = path+"?imageView2/"+mode+"/w/"+w+"/h/"+h+"/q/70";
		}
//		System.out.println("path:"+path);
		return path;
	}
	
	/**
	 * 格式化图片
	 * @param path
	 * @param maxDim
	 * @return
	 */
	public static String getNewImagePath(String path,int maxDim,int blur){
//		System.out.println("path:"+path);
		if(path==null||path.length()<1)return path;
		int po = path.indexOf("?");
		if(po<0)return path;
		path = path.substring(0, po);
//		http://developer.qiniu.com/docs/v6/api/reference/fop/image/imageview2.html
//		imageView2/<mode>
//        /w/<Width>
//        /h/<Height>
//        /q/<Quality>
//        /format/<Format>
		if(blur<1)blur = 1;
		if(blur>50)blur = 50;
		if(maxDim!=Integer.MAX_VALUE){
			if(maxDim>10000)maxDim = 9999;
			path = path+"?imageMogr2/thumbnail/"+maxDim+"x"+maxDim+"/blur/"+blur+"x10";
		}else{
			//取原图
			path = path+"?imageMogr2/blur/"+blur+"x10";
			
		}
//		System.out.println("path:"+path);
		return path;
	}
	public static int jsonGetInt(JSONObject json, String key){
		return jsonGetInt(json, key, 0);
	}
	public static int jsonGetInt(JSONObject json, String key,int defa) {
		try {
			return json.getInt(key);
		} catch (Exception e) {
			//Log.e("JSON解析值不存在", key + "不存在");
		}
		return defa;
	}

	public static double jsonGetDouble(JSONObject json, String key) {
		return jsonGetDouble(json, key, 0.0d);
	}
	public static double jsonGetDouble(JSONObject json, String key,double defa) {
		try {
			return json.getDouble(key);
		} catch (Exception e) {
			//Log.e("JSON解析值不存在", key + "不存在");
		}
		return defa;
	}
	public static JSONArray jsonGetArray(JSONObject json, String key) {
		try {
			return json.getJSONArray(key);
		} catch (Exception e) {
			//Log.e("JSON解析值不存在", key + "不存在");
		}
		return null;
	}

	public static JSONObject jsonGetObject(JSONObject json, String key) {
		try {
			return json.getJSONObject(key);
		} catch (Exception e) {
			//Log.e("JSON解析值不存在", key + "不存在");
		}
		return null;
	}

	public static JSONObject jsonGetObject(JSONArray ja, int i) {
		try {
			return ja.getJSONObject(i);
		} catch (Exception e) {
			//Log.e("JSON解析值不存在", i + "不存在");
		}
		return null;
	}

}
