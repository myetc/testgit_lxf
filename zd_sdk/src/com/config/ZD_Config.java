package com.config;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 存放整个APP的配置文件 注:插件也借用此配置
 * 
 * @author lixifeng
 *
 */
public class ZD_Config {

	String tag = getClass().getName();

	private ZD_Config() {
	}

	// 线程安全单例
	private static class LazyHolder {
		private static final ZD_Config INSTANCE = new ZD_Config();
	}

	public static final ZD_Config getInstance() {
		return LazyHolder.INSTANCE;
	}

	public String FILE_PATH = "food";

	/**
	 * 得到文件存储目录 数据缓存
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public String getTempData(Context context) {
		return getTempBase(context) + "/data/";
	}
	/**
	 * 得到文件存储目录 插件缓存
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public String getTempPlug(Context context) {
		return getTempBase(context) + "/plug/";
	}
	/**
	 * 得到文件存储目录 APK下载缓存
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public String getTempApk(Context context) {
		return getTempBase(context) + "/apk/";
	}

	/**
	 * 得到文件存储目录
	 * 
	 * @param context
	 * @return
	 */
	public String getTempBase(Context context) {
		String path = null;

		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			String dir = Environment.getExternalStorageDirectory() + "/"
					+ FILE_PATH;
			File dirFile = new File(dir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			path = dirFile.getAbsolutePath();
		}
		return path;
	}
}
