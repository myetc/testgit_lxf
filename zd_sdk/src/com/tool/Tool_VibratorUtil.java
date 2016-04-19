package com.tool;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * 手机震动工具类
 * @author Administrator
 *
 */
public class Tool_VibratorUtil {
	
	/**
	 * final Activity activity  ：调用该方法的Activity实例
	 * long milliseconds ：震动的时长，单位是毫秒
	 * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
	 * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
	 */
	
	 public static void Vibrate(final Context activity, long milliseconds) { 
		 try {
			
		
	        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE); 
	        vib.vibrate(milliseconds); 
		 } catch (Exception e) {
				// TODO: handle exception
			}
	 } 
	 public static void Vibrate(final Context activity, long[] pattern,boolean isRepeat) { 
		 try {
			  Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE); 
		        
		        vib.vibrate(pattern, isRepeat ? 1 : -1); 
		} catch (Exception e) {
			// TODO: handle exception
		}
	      
	        
	        
	 } 
}
