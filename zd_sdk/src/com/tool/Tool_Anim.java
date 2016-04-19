package com.tool;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * 一些动画的常用代码.封装只是为了把重复代码进行精简,而不是为了解决动画复杂问题.
 * 这个类,本身不复杂
 * @author lixifeng
 *
 */
public class Tool_Anim {
	/**
	 * 旋转动画,匀速
	 * 支持顺,逆时针
	 * 
	 * 
	 * @param view  
	 * @param duration 动画时长
	 * @param dw true 顺 false逆
	 */
	public static void rotateAnimation(ImageView view, long duration,boolean dw) {
		if(view==null)return;
		RotateAnimation rotate = new RotateAnimation(0f, dw?359f:-359f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// Animation animation = AnimationUtils.loadAnimation(this,
		// R.anim.load_loding1);
		rotate.setFillAfter(true);
		rotate.setDuration(duration);
		rotate.setRepeatCount(-1);
		rotate.setInterpolator(new LinearInterpolator());
		// rotate.setRepeatMode(Animation.RESTART);
		view.clearAnimation();
		view.startAnimation(rotate);
	}
	
//	ImageView imageview = (ImageView) page_nonetwork.findViewById(R.id.imageview);
//	if (imageview != null) {
//		//加载 帧动画
//		AnimationDrawable anim = (AnimationDrawable) getResources()
//				.getDrawable(R.anim.anim_nonetwork);
//		imageview.setImageDrawable(anim);
//		anim.stop();
//		anim.start();
//	}
//	
}
