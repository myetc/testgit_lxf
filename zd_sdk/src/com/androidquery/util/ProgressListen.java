package com.androidquery.util;

import android.view.View;

import com.androidquery.AQuery;

public interface ProgressListen {
	public void setVisibility(int VISIBLE);
	public void setProgress(int progress);
	public void setMax(int max);
	public int getMax();
	
}
