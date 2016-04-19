package com.tool.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * 镂空效果实现
 * 
 * 使用方法 在xml上,添加一个全屏的此组件 <com.example.showlayout.HollowImageView
 * android:id="@+id/hollowImageView" android:layout_width="match_parent"
 * android:layout_height="match_parent" android:src="@drawable/fan" />
 * 
 * 
 * 
 * 引用此组件后,调用hollowImageView.refresh(anim_layout); anim_layout 指,要镂空的组件区域
 * 
 * 
 * @author Alan
 */
@SuppressLint("DrawAllocation")
public class HollowImageView extends ImageView {
	private Context mContext;

	private double view_width = 100;
	private double view_height = 100;
	private double view_x = 0;
	private double view_y = 0;

	private double radius = 100;// 圆的半径
	private double x = 100;// X所在的位置
	private double y = 100;// Y所在的位置

	private Paint paint;

	// 设置背色 透明度,由8位的色值决定
	private int colorBackground = Color.parseColor("#7dffffff");
	// 设置级色 透明度,由8位的色值决定
	private int colorLine = Color.parseColor("#7dffffff");

	// 设置圆的区域,放大与缩小,支持负数内缩
	private double padding = 0;
	private double zoom = 0;// 另一个名字,与padding相同
	// 是否完全包含组件,默认为包含(注:正方形组件,当然是包含了)
	private boolean isContain = true;

	public HollowImageView(Context context) {
		super(context);
		mContext = context;
		setCustomAttributes(null);
	}

	public HollowImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setCustomAttributes(attrs);
	}

	public HollowImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setCustomAttributes(attrs);
	}

	private void setCustomAttributes(AttributeSet attrs) {
		// 创建一个画笔
		paint = new Paint();
		paint.setFilterBitmap(true);// 防抖动
		paint.setAntiAlias(true);// 抗锯齿,这个是有效的
		paint.setColor(colorLine);// 指定画笔的颜色,也可以理解为.圆框色,
		paint.setStyle(Style.STROKE);// 这是个空心的
		paint.setStrokeWidth(2);// 这个边框的粗细是2PX
		// 指定这个空心圆的位置与剪裁的位置一样
	}

	@SuppressLint("NewApi")
	public void refresh(final View anim_layout) {
		if (anim_layout == null)
			return;
		// 得到图片源的布局大小
		ViewTreeObserver vto = anim_layout.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {

				double wid = anim_layout.getWidth();
				double hei = anim_layout.getHeight();
				if (wid != 0 && hei != 0) {
					view_width = anim_layout.getWidth();
					view_height = anim_layout.getHeight();
					view_x = anim_layout.getX();
					view_y = anim_layout.getY();

					// 取指定VIEW的宽或者高的较大值

					double w2 = view_width / 2;
					double h2 = view_height / 2;
					// 如果包含组件,计算斜边.如果不包含,计算直角边
					radius = isContain ? Math.sqrt(Math.pow(w2, 2)
							+ Math.pow(h2, 2)) : Math.max(view_height,
							view_width) / 2;
					// 组件的放大与缩小
					radius += padding;
					x = view_x + w2;
					y = view_y + h2;
					// 圆的位置,与组件所在的位置,偏差为半径值
					// x = view_x+radius;
					// y = view_y+radius;

//					System.out.println("view_width:" + view_width);
//					System.out.println("view_height:" + view_height);
//					System.out.println("Screen_X" + view_x);
//					System.out.println("Screen_Y" + view_y);

					// HollowImageView

					// frameLayout.addContentView(view, params);;

				}

				return true;

			}

		});
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// 清一下画布
		canvas.restore();
		canvas.save();

		// 对画布抗锯齿.好像没什么用
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
				Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));

		// 指定一个画圆的路径
		Path path = new Path();

		// 进行这个圆的位置计算.与指定的VIEW的位置,大小相同
		// 注:原点,是圆心

		path.addCircle((float) x, (float) y, (float) radius, Direction.CW);
		path.close();// 圆画完了.结束它

		// 把这个圆,在这个画布上给裁剪掉.(注:画布的大小,与当前VIEW的大小是一样的)
		canvas.clipPath(path, Region.Op.DIFFERENCE);

		// 给整体画布画上一个颜色 注:此时,所绘制的区域为裁剪之后的画布
		canvas.drawColor(colorBackground);

		// 保存下操作
		canvas.restore();
		canvas.save();

		// 由于抗锯齿的可能无效性.这里再画一个空心的圆,把圆给覆盖起来.

		canvas.drawCircle((float) x, (float) y, (float) radius, paint);

		// 保存下操作
		canvas.restore();
		canvas.save();
	}

	public double getPadding() {
		return padding;
	}

	public void setPadding(double padding) {
		this.padding = padding;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public boolean isContain() {
		return isContain;
	}

	public void setContain(boolean isContain) {
		this.isContain = isContain;
	}

	public void setColorBackground(int colorBackground) {
		this.colorBackground = colorBackground;
	}

	public void setColorLine(int colorLine) {
		this.colorLine = colorLine;
		if (paint != null)
			paint.setColor(colorLine);
	}

}