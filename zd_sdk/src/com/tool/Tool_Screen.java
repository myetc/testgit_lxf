package com.tool;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tool_Screen {

	private int keyHeight = 0;

	private Tool_Screen() {

	}

	private static class LazyHolder {
		private static final Tool_Screen INSTANCE = new Tool_Screen();
	}

	public static final Tool_Screen getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * 监听得到键盘的高度
	 * 
	 * @author lixifeng
	 *
	 */
	public interface KeyHeightListen {
		public void getHeight(int height);
	}

	// 计算屏幕的高度
	// 虚拟物理键盘高度
	// 状态栏高度

	public void computeSize(final Activity activity,
			final FrameLayout layout_main, final KeyHeightListen keyHeightListen) {
		computeSize(activity, layout_main, false, keyHeightListen);
	}

	// 是否去计算底部的高度
	/**
	 * 计算键盘的高度 原理:在FrameLayout之上,添加一层透明的布局,进行交替计算出键盘的高度.计算完毕,进行删除此透明层
	 * 
	 * @param activity
	 *            一个页面级
	 * @param layout_main
	 *            一个FrameLayout布局,注:此布局的规则为:全屏幕,存在状态栏的页面layout.
	 * @param RefreshKeyHeight
	 *            是否重新获取键盘高度
	 * @param keyHeightListen
	 *            监听得到键盘高度
	 */
	public void computeSize(final Activity activity,
			final FrameLayout layout_main, final boolean RefreshKeyHeight,
			final KeyHeightListen keyHeightListen) {
		if (!RefreshKeyHeight && keyHeight > 0) {
			keyHeightListen.getHeight(keyHeight);
			return;
		}

		final int value_top = 100;
		final int value_bottom = 200;
		final FrameLayout frameLayout = new FrameLayout(activity);
		frameLayout.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		frameLayout.setBackgroundColor(Color.parseColor("#00ff00ff"));

		// 向上计算的布局 组件
		RelativeLayout relativeLayoutTop = new RelativeLayout(activity);
		relativeLayoutTop.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		relativeLayoutTop.setGravity(Gravity.TOP);
		// 向下计算的布局 键盘
		RelativeLayout relativeLayoutBottom = new RelativeLayout(activity);
		relativeLayoutBottom.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		relativeLayoutBottom.setGravity(Gravity.BOTTOM);
		// 键盘
		final TextView t = new TextView(activity);
		t.setBackgroundColor(Color.parseColor("#0000ffff"));
		MarginLayoutParams mp = new MarginLayoutParams(
				LayoutParams.MATCH_PARENT, 10);

		// 事件
		final TextView t2 = new TextView(activity);
		t2.setBackgroundColor(Color.parseColor("#0000ff00"));
		MarginLayoutParams mp2 = new MarginLayoutParams(
				LayoutParams.MATCH_PARENT, 10);

		mp.setMargins(0, 0, 0, value_bottom);
		mp2.setMargins(0, (int) value_top, 0, 0);
		t.setLayoutParams(new RelativeLayout.LayoutParams(mp));
		t2.setLayoutParams(new RelativeLayout.LayoutParams(mp2));

		relativeLayoutBottom.addView(t);

		relativeLayoutTop.addView(t2);

		frameLayout.addView(relativeLayoutTop);

		frameLayout.addView(relativeLayoutBottom);

		layout_main.addView(frameLayout);
		final int s = getStatusBarHeight(activity);
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int widthS = metric.widthPixels; // 屏幕宽度（像素）
		final int heightS = metric.heightPixels; // 屏幕高度（像素）

		t.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						t.getViewTreeObserver().removeGlobalOnLayoutListener(
								this);
						t2.getViewTreeObserver().addOnGlobalLayoutListener(
								new OnGlobalLayoutListener() {
									@Override
									public void onGlobalLayout() {
										t2.getViewTreeObserver()
												.removeGlobalOnLayoutListener(
														this);

										// ("mHeaderViewHeight:"+mHeaderViewHeight);

										int[] location_key = new int[2];
										t.getLocationOnScreen(location_key);

										int[] location_view = new int[2];
										t2.getLocationOnScreen(location_view);

										int y_key = location_key[1];
										int y_view = location_view[1];

										// System.out.println("==key 结果 键盘所在Y:"+y_key);
										// System.out.println("==key 结果 组件所在Y:"+y_view);

										int diffY = y_key - y_view;
										// System.out.println("==key 结果 Y值相差值:"+diffY);
										// if(diffY>0){
										// System.out.println("==key 结果 键盘之上");
										// }else{
										// System.out.println("==key 结果 键盘之下");
										// }

										// 10 调试手机得到的误差值,原因..还不确定
										// 调试手机,小米,三星,魅族,一加,虚拟机
										int value = heightS - s - value_bottom
												- diffY - value_top - 10;
										// 此时的值,为键盘的高度
										keyHeight = value;
										keyHeightListen.getHeight(value);
										layout_main.removeView(frameLayout);
										// 本次程序不再计算此高度

									}
								});
					}
				});

	}

	/**
	 * 得到通知栏高度
	 * 
	 * @param context
	 * @return
	 */
	public int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;

	}

	/**
	 * 得到屏幕的高度,包含状态栏
	 * @param context
	 * @return
	 */
	public int getScreenHeight(Context context) {

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}
	/**
	 * 得到屏幕的宽度
	 * @param context
	 * @return
	 */
	public int getScreenWidth(Context context) {

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
}
