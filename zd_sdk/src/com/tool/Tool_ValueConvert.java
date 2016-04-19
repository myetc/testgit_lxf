package com.tool;

import android.content.Context;

public class Tool_ValueConvert {
	public static int dip2px(Context context, double d) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (d * scale + 0.5f);
	}
}
