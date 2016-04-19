package com.tool.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * 显示超出设定的图(进行缩放显示)
 * 支持拖放的操作(配置)
 * @author lixifeng
 * @param <T>
 *
 */
@SuppressLint("NewApi")
public class ImageViewShow<T> extends ImageView {
	T t;
	public ImageViewShow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ImageViewShow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ImageViewShow(Context context) {
		super(context);

	}

	private static int MAXMIMUM_BITMAP_SIZE = 0;
	Bitmap bitmap = null;
	private boolean success = false;

	@Override
	protected void onDraw(Canvas canvas) {
		ImageViewShow.MAXMIMUM_BITMAP_SIZE = canvas.getMaximumBitmapHeight();
		// System.out.println("MyImage.MAXMIMUM_BITMAP_SIZE onDraw:"+MyImage.MAXMIMUM_BITMAP_SIZE);
		super.onDraw(canvas);
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			System.out.println("MyImage.MAXMIMUM_BITMAP_SIZE :"
					+ ImageViewShow.MAXMIMUM_BITMAP_SIZE);
			setImageBitmap();
//			bitmap = null;
		};
	};

	private void setImageBitmap() {
		if(bitmap==null)return;
		int h = bitmap.getHeight();
		int w = bitmap.getWidth();
		if (ImageViewShow.MAXMIMUM_BITMAP_SIZE >= h
				&& ImageViewShow.MAXMIMUM_BITMAP_SIZE >= w) {
			// 正确显示图片
			super.setImageBitmap(bitmap);
		} else {
			// 图片太大了.缩小显示
			super.setImageBitmap(resampleImage(bitmap,
					(int) (ImageViewShow.MAXMIMUM_BITMAP_SIZE)));
		}
		success = true;
		if(BitmapListen!=null){
			t = (T) BitmapListen.setBitmap(this);
		}
	}

	@Override
	public void setImageBitmap(final Bitmap bm) {

		// 如果无图片
		if (bm == null)
			return;
		this.bitmap = bm;
		if (ImageViewShow.MAXMIMUM_BITMAP_SIZE > 0) {
			handler.sendEmptyMessage(1);
		} else {
			invalidate();
			// 等待得到MyImage.MAXMIMUM_BITMAP_SIZE
			final Timer Timer = new Timer();
			Timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (ImageViewShow.MAXMIMUM_BITMAP_SIZE > 0) {
						this.cancel();
						Timer.cancel();
						// 再来
						handler.sendEmptyMessage(1);
					}
				}
			}, 100, 100);
			return;
		}

	}

	/**
	 * 缩放到指定大小
	 * @param bmpt
	 * @param maxDim
	 * @return
	 */
	public static Bitmap resampleImage(Bitmap bmpt, int maxDim) {

		System.out.println("maxDim:" + maxDim);
		Matrix m = new Matrix();

		if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
			BitmapFactory.Options optsScale = getResampling(bmpt.getWidth(),
					bmpt.getHeight(), maxDim);
			m.postScale((float) optsScale.outWidth / (float) bmpt.getWidth(),
					(float) optsScale.outHeight / (float) bmpt.getHeight());
		} else {
			// 如果图片的大小正常,不进行处理.
			return bmpt;
		}
		bmpt = Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(),
				bmpt.getHeight(), m, true);
		// System.out.println("图片保存 bmpt.getWidth():"+bmpt.getWidth());
		// System.out.println("图片保存 bmpt.getHeight():"+bmpt.getHeight());

		return bmpt;
	}

	private static BitmapFactory.Options getResampling(int cx, int cy, int max) {
		float scaleVal = 1.0f;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		if (cx > cy) {
			scaleVal = (float) max / (float) cx;
		} else if (cy > cx) {
			scaleVal = (float) max / (float) cy;
		} else {
			scaleVal = (float) max / (float) cx;
		}
		bfo.outWidth = (int) (cx * scaleVal + 0.5f);
		bfo.outHeight = (int) (cy * scaleVal + 0.5f);
		return bfo;
	}



	public boolean isSuccess() {
		return success;
	}

	BitmapListen BitmapListen;
	public void setBitmapListen(BitmapListen bitmapListen) {
		BitmapListen = bitmapListen;
	}
	public interface BitmapListen<T>{
		public T  setBitmap(ImageView iv);
		public void reset(T t);
		
	}
	public  void reset() {
		if(BitmapListen!=null){
			BitmapListen.reset(t);
		}
	}
	

}
