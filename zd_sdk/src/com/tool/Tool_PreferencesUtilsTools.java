package com.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用于保存缓存信息
 * @author lixifeng
 *
 */
public class Tool_PreferencesUtilsTools {
	public static final String SP_SETTING = "setting_myetc"; 

	/**
	 * 
	 * @param context
	 */
	public static void save(Context context, String value, boolean isfirst) {
		SharedPreferences sp = context.getSharedPreferences(SP_SETTING, 0);
		sp.edit().putBoolean(value, isfirst).commit();
	}

	public static void save(Context context, String name, String value) {
		SharedPreferences sp = context.getSharedPreferences(SP_SETTING, 0);
		sp.edit().putString(name, value).commit();
	}

	public static void save(Context context, String name, int value) {
		SharedPreferences sp = context.getSharedPreferences(SP_SETTING, 0);
		sp.edit().putInt(name, value).commit();
	}


	public static boolean get(Context context, String value) {
		SharedPreferences sp = context.getSharedPreferences(SP_SETTING, 0);
		return sp.getBoolean(value, false);
		// return false;
	}

	public static String getString(Context context, String name) {
		SharedPreferences sp = context.getSharedPreferences(SP_SETTING, 0);
		return sp.getString(name, "");
		// return false;
	}
	public static int getInt(Context context, String name) {
		SharedPreferences sp = context.getSharedPreferences(SP_SETTING, 0);
		return sp.getInt(name, 0);
		// return false;
	}
	public static void save(Context context, String name, List<String> value) {
		if (value == null)
			return;
		String v = "";
		int size = value.size();
		for (int i = 0; i < size; i++) {
			v += value.get(i) + ((i >= size) ? "" : ",");
		}
		save(context, name, v);
	}
	
	public static List<String> getList(Context context, String name) {
		ArrayList<String> list = new ArrayList<String>();
		String value = getString(context, name);
		System.out.println("value:::" + value);
		if (value.length() > 0) {
			list.addAll(Arrays.asList(value.split(",")));
		}
		System.out.println("value:::" + list.size());
		return list;
	}
}
