package com.tool.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 圆形进度条组件
 * 
 * @author lixifeng
 *
 */
public class MyCircleProgress extends View {
	private Context context;
	private Paint paint;
	private boolean opt;

	/**
	 * 
	 */
	private int mR = 51;// 圆半径，决定圆大小
	private int startAo = 135;
	private int bgColor = Color.parseColor("#c1c7c4");// 圆或弧的背景颜色
	private int fgColor = Color.parseColor("#35b374");// 圆或弧的前景颜色，即绘制时的颜色
	private int lineColor = Color.parseColor("#bfbfbf");// 圆或弧的线色
	private int pointColor = Color.parseColor("#c1c7c4");// 圆或弧的线色
	private int drawStyle = 0; // 绘制类型 FILL画圆形进度条，STROKE绘制弧形进度条
	private int strokeWidth = 11;// STROKE绘制弧形的弧线的宽度
	private int max = 50;// 最大值，设置进度的最大值
	private int fontsize = 25;// 最大值，设置进度的最大值
	public int progress = max / 2;// 进度实际值,当前进度

	private boolean showFont = true;
	public void setShowFont(boolean showFont) {
		this.showFont = showFont;
	}
	public MyCircleProgress(Context context) {
		super(context);
		init(context);
	}

	public MyCircleProgress(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public MyCircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);

	}

	private void init(Context context) {
		this.context = context;
		this.paint = new Paint();
		this.paint.setAntiAlias(true); // 消除锯齿
		paint.setAntiAlias(true);// 抗锯齿,这个是有效的
		this.paint.setStyle(Style.STROKE); // 绘制空心圆或 空心矩形
		refresh(this);

	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int center = getWidth() / 2; // 圆心位置

		Paint linepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linepaint.setFilterBitmap(true);// 防抖动
		linepaint.setAntiAlias(true);// 抗锯齿,这个是有效的
		linepaint.setColor(lineColor);
		linepaint.setStyle(Style.STROKE); // 绘制空心圆或 空心矩形
		linepaint.setStrokeWidth(1);
		canvas.drawCircle(center, center, mR - strokeWidth / 2, linepaint);
		canvas.drawCircle(center, center, mR + strokeWidth / 2, linepaint);

		this.paint.setColor(bgColor);
		this.paint.setStrokeWidth(strokeWidth);
		canvas.drawCircle(center, center, mR, this.paint);
		// 绘制圆环
		this.paint.setColor(fgColor);
		if (drawStyle == 0) {
			this.paint.setStyle(Style.STROKE);
			opt = false;
		} else {
			this.paint.setStyle(Style.FILL);
			opt = true;
		}
		int top = (center - mR);
		int bottom = (center + mR);
		RectF oval = new RectF(top, top, bottom, bottom);

		canvas.drawArc(oval, startAo, 360 * progress / max, opt, paint);

		double d_pro = (double) progress / ((double) max / 100d);
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");



		if(showFont){
			String mCurrentDrawText = df.format(d_pro) + "%";
			Paint fontpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			fontpaint.setFilterBitmap(true);// 防抖动
			fontpaint.setAntiAlias(true);// 抗锯齿,这个是有效的

			fontpaint.setTextSize(fontsize);
			fontpaint.setColor(fgColor);
			FontMetricsInt fontMetrics = fontpaint.getFontMetricsInt();
			int baseline = (int) ((oval.bottom + oval.top - fontMetrics.bottom - fontMetrics.top) / 2);
			// 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
			fontpaint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(mCurrentDrawText, oval.centerX(), baseline, fontpaint);
		}
	
		//

		// 圆点坐标：(x0,y0)
		// 半径：r
		// 角度：a0
		// 则圆上任一点为：（x1,y1）
		int ao = 360 * progress / max + startAo;
		addPoingForCircle(canvas, ao, mR, center, center, false);
		addPoingForCircle(canvas, startAo, mR, center, center, false);

		// 绘制内圆点
		if (ao_value >= 0) {
			ao_value += 1;
			int ao2 = 360 * ao_value / 100 + startAo;
			if (ao_value >= 100) {
				ao_value = 0;
			}
			addPoingForCircle(canvas, ao2, mR - strokeWidth * 2, center,
					center,true);

		}

	}

	int ao_value = 0;

	private void addPoingForCircle(Canvas canvas, int ao, int r, int x0,
			int y0, boolean auto) {
		int x1 = (int) (x0 + r * Math.cos(ao * 3.1415926 / 180));
		int y1 = (int) (y0 + r * Math.sin(ao * 3.1415926 / 180));

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setFilterBitmap(true);// 防抖动
		paint.setAntiAlias(true);// 抗锯齿,这个是有效的
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(pointColor);
		canvas.drawCircle(x1, y1, strokeWidth / 2, paint);
		if (auto) {
			if (progress >= max) {
				// 不再显示圆点
				ao_value = -1;
			} else {
			}
			// 刷新圆点
			invalidate();
		}
	}

	@SuppressLint("NewApi")
	public void refresh(final View anim_layout) {
		if (anim_layout == null)
			return;
		ViewTreeObserver vto = anim_layout.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {

				double wid = anim_layout.getWidth();
				double hei = anim_layout.getHeight();
				if (wid != 0 && hei != 0) {
					mR = (int) Math.min(wid, hei) / 2 - strokeWidth;
				}

				return true;

			}

		});
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			progress = 0;
		} else if (progress > max) {
			progress = max;
		} else {
			this.progress = progress;
		}
		// start();
		// invalidate();
		// handler.sendEmptyMessageDelayed(0,10);
	}

	
	
	public int getMax() {
		return max;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public void setFgColor(int fgColor) {
		this.fgColor = fgColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public void setDrawStyle(int drawStyle) {
		this.drawStyle = drawStyle;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}
	
	public void setPointColor(int pointColor) {
		this.pointColor = pointColor;
	}

}
