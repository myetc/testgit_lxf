package com.tool.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.tool.Tool_Screen;
//import android.widget.Toast;

/**
 * 基础引用页面类.
 * 1:实现动画效果
 * 2:实现页面统一性
 * 3:
 * @author lixifeng
 *
 */
@SuppressLint("NewApi")
public abstract class ZD_BaseActivity {
	
	public String TAG =getClass().getName();
	public Context context ;
	public Activity activity ;
	private final int layoutid = Integer.MAX_VALUE;
	private FrameLayout layout_main;
	public long showtime = 400;
	private float view_width = 0.0f;
	private float view_height = 0.0f;
	private float view_x = 0.5f;
	private float view_y = 0.5f;
	public float screen_width = 360f;
	public float screen_height = 480f;
	public float StatusBarHeight = 0;
	private float pivotXValue, pivotYValue;
	private  int showType=0;
	public final static int SHOWTYOE_ZOOM =1, SHOWTYOE_FANDEOUT=2, NONE=0;
	private boolean showMessage_net = true;
	
	public ZD_BaseActivity(Context context, Activity activity) {
		super();
		this.context = context;
		this.activity = activity;
	}

	public long showMessageLayoutDuration = 3000;
	public View onCreate(Bundle savedInstanceState) {
		
		//添加碳
		addManager();
		
		initData();
		initView_base();
		//添加默认页面
//		
	
	
		//如果登录,进行验证是否改变过只加入一个圈子,
		//如果只加入一个圈子,不进行改变,SO,进行选择只能要一个圈子
		
		return layout_main;

	}
	
	
	public void setShowType(int showType){
		this.showType=showType;
	}
	
	/**
	 * 计算显示方位 XY相对坐标
	 */
	private void initData() {
		
		// 通知/状态栏高度
		StatusBarHeight = Tool_Screen.getInstance().getStatusBarHeight(context);
		// 屏幕宽度
		screen_width = Tool_Screen.getInstance().getScreenWidth(context);
		// 屏幕高度 - 通知/状态栏高度
		screen_height = Tool_Screen.getInstance().getScreenHeight(context) - StatusBarHeight;
		Intent intent = activity.getIntent();
		//显示效果
		showType = intent.getIntExtra("showType", showType);
//		showPath = intent.getBooleanExtra("showPath", showPath);
		// 点击组件的宽度
		view_width = intent.getFloatExtra("view_width", view_width);
		// 点击组件的高度
		view_height = intent.getFloatExtra("view_height", view_height);
		// 组件所在屏幕的 X位置
		view_x = intent.getFloatExtra("view_x", view_x);
		// 组件所在屏幕的 Y位置- 通知/状态栏高度
		view_y = intent.getFloatExtra("view_y", view_y) - StatusBarHeight;
		// 计算相对于屏幕的相对X比例
		pivotXValue = (view_x + view_width / 2) / screen_width;
		// 计算相对于屏幕的相对Y比例
		pivotYValue = (view_y + view_height / 2) / screen_height;
		// 验证合理性
		if (screen_width < 1 || screen_height < 1 || view_width < 1
				|| view_height < 1 || view_x < 0 || view_y < 0
				|| view_x > screen_width || view_y > screen_height) {
			// 如果没有得到屏幕大小 或者没有得到组件的大小，或者组件的所在位置，或者坑爹的组件在屏幕的位置超出了屏幕
			// 以上情况，，，全部以尽屏幕中心点进行显示
			pivotXValue = 0.5f;
			pivotYValue = 0.5f;
		}

		 //System.out.println("==================");
	}
	
	/**
	 * 信息点击事件处理
	 * @param view
	 */
	private void addMessageListen(View view){
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
	}
	/**
	 * 打开网络设置页面
	 * @param view
	 */
	private void addMessage_netListen(ViewGroup viewGroup){
//		View view1 = LayoutInflater.from(context).inflate(R.layout.tool_show_toast_message, null);// 这个过程相当耗时间
//		final View tool_show_toast_message_net = view1.findViewById(R.id.layout);
//		tool_show_toast_message_net.setVisibility(View.INVISIBLE);
//		viewGroup.addView(view1);
//		
//		tool_show_toast_message_net.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//	            hidn(tool_show_toast_message_net, true, false);
//	            
//				 Intent intent=null;
//	                //判断手机系统的版本  即API大于10 就是3.0或以上版本 
//				 if(android.os.Build.VERSION.SDK_INT > 10 ){
//					    //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
//					    intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//					} else {
//						  intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//					}
////	                context.startActivity(intent);
//	                BaseActivity.startActivity(tool_show_toast_message_net, intent, context, SHOWTYOE_FANDEOUT);
//	    
//			}
//		});
	}
	
	
/**
 * 拦截子类设置视图，统一添加到 layout_main
 */


	/**
	 * 添加子类指定layout 或者 view 
	 * @param view
	 */
	public void setSelfContent(View view) {
	
		//进行拦截外层是否是ScrollView 如果是，进行替换成LinearLayout 
				//注：一般情况下用不到最外层为ScrollView ，在XML中最外层为ScrollView 只是为了XML的高度可自动加载。。
				//也可开发完成自动换成其它的外层布局，如果实在需要在外层用ScrollView，那就在ScrollView中套一个ScrollView
				//也可设置ScrollView的 tag值 为 clear
				if(view!=null){
					if(view instanceof ScrollView){
						Object tag_sv = view.getTag();
						if(tag_sv!=null&&tag_sv.toString().equals("clear")){

							//进行替换最外层
							int count = ((ScrollView) view).getChildCount();
							if(count>0){
								//ScrollView只能最多有一个子布局，所以
								View child = ((ScrollView) view).getChildAt(0);
								//把子布局给新的而已
								LinearLayout view_ll = new LinearLayout(context);
								//orientation 的对应坚布局
								view_ll.setOrientation(LinearLayout.VERTICAL);
								LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
								view_ll.setLayoutParams(params);
								view_ll.setBackgroundColor(Color.WHITE);
								//清除在上级布局（父布局）中的指向
								 ViewGroup p = (ViewGroup) child.getParent(); 
						         if (p != null) { 
						             p.removeAllViewsInLayout(); 
						         } 
								view_ll.addView(child);
								view = view_ll;
							}
						
						}else{
							//清楚了替换最外层布局功能
						}
					}
				}
		
				
		layout_main.removeAllViews();
		
		
		layout_main.addView(view);
		
		
		//添加页面层次 例.添加一个按钮
		if(false){
//			showLoad_layout = LayoutInflater.from(context).inflate(R.layout.defaultload_base, null);// 这个过程相当耗时间
			Button showLoad_layout = new Button(context);
			showLoad_layout.setText("test");
			showLoad_layout.setVisibility(View.GONE);
			layout_main.addView(showLoad_layout);
		}
		
		
		
		//验证是否存在网络
//		if(Tool_NetworkUtils.isNetworkConnected(context)){
//			addMessage_netListen(layout_main);
//		}
		//添加指定的页面
		if(views!=null){
			for (int i = 0; i < views.length; i++) {
				if(views[i]!=null)
				layout_main.addView(views[i]);
			}
		}
		
		
	
	}
	
	public void start(){
		// 打开效果
				switch (showType) {
				case SHOWTYOE_ZOOM:
					show_zoom(layout_main);
					break;
				case SHOWTYOE_FANDEOUT:
					show_fadeout(layout_main);
					break;
				default:
					init();
					break;
				}
	}
	//添加上层页面
	View[] views ;
	public void addView(View... views){
		this.views = views;
		
	}

//	
//	public void addView(View view){
//		layout_main.addView(view);
//	}
	
	



	

	/**
	 * 设置默认的一个页面层
	 */
	private void initView_base() {
		layout_main = new FrameLayout(context);
		LinearLayout.LayoutParams params_main = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout_main.setLayoutParams(params_main);
		layout_main.setBackgroundColor(Color.parseColor("#00000000"));
		layout_main.removeAllViews();
		layout_main.setId(layoutid);
		Button button = new Button(context);
		button.setText("This is baseActivity page!");
		button.setId(layoutid - 1);
		layout_main.addView(button);
	}

	private void show_fadeout(FrameLayout layout) {
		final AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		show_anim(layout, animation);
	}

	private void exit_fadeout(FrameLayout layout) {
		final AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(showtime);// 设置动画持续时间
		layout.startAnimation(animation);
		layout.setVisibility(View.GONE);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				activity.finish();
			}
		});
	}
	private void show_zoom(FrameLayout layout) {
		ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);
		
		final AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
		AnimationSet anim = new AnimationSet(true);
		anim.addAnimation(animation);
		anim.addAnimation(animation2);
		
		show_anim(layout,  anim);
	}
	private void show_anim(FrameLayout layout, Animation animation) {
		animation.setDuration(showtime);// 设置动画持续时间
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				init();
			}
		});
		layout.startAnimation(animation);
	}
	/**
	 * 动画加载完毕，调用此函数
	 */
	public abstract void init() ;
	
	
	private void exit_zoom(FrameLayout layout) {
		ScaleAnimation animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);
		
		final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.0f);
		AnimationSet anim = new AnimationSet(true);
		anim.addAnimation(animation);
		anim.addAnimation(animation2);
		
		anim.setDuration(showtime);// 设置动画持续时间
		
		
		
		layout.startAnimation(anim);
		layout.setVisibility(View.GONE);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				activity.finish();
			}
		});
	}
	
	boolean isback = true;
//	private IntentFilter intentFilter;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return false;
	}

	public void back() {
		if (isback){
			switch (showType) {
			case SHOWTYOE_ZOOM:
				exit_zoom(layout_main);
				break;
			case SHOWTYOE_FANDEOUT:
				exit_fadeout(layout_main);
				break;
			default:
				activity.finish();
				break;
			}
		
			
		}
		isback = false;
	}
	public void back(View view){
		back();
	}

	@SuppressLint("NewApi")
	public static void startActivity(final View view,Intent intent,Context context,int showType){
		startActivity(view, intent, null, 0, context, showType);
	}
	@SuppressLint("NewApi")
	public static void startActivity(final View view,Intent intent,Activity activity,int requestCode,int showType){
		startActivity(view, intent, activity, requestCode, null, showType);
	}
	static long lastClickTime;
	/**
	 * 
	 * @param view打开的事件组件
	 * @param intent
	 * @param activity 如果null必须传递context 
	 * @param requestCode 当用activity的时候，会调用返回打开，startActivityForResult（,code） 这里回原调用的code
	 * @param context
	 * @param showType 开的效果BaseActivity.SHOWTYOE_ZOOM 放大 SHOWTYOE_FANDEOUT 淡入淡出 NONE 没有
	 * @param showPath 是否显示path导航。默认不显示
	 */
	@SuppressLint("NewApi")
	public static void startActivity(final View view,Intent intent,Activity activity,int requestCode,Context context,int showType) {
		float view_width = 0;
		float view_height = 0;
		float view_x = 0;
		float view_y = 0;
		if(view!=null){
		
		// 得到图片源的布局大小
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			private boolean hasMeasured;

			@Override
			public boolean onPreDraw() {
				if (hasMeasured == false) {
					int wid = view.getWidth();
					int hei = view.getHeight();
					if (wid != 0 && hei != 0) {
						int widthScreen = wid;
						int heightScreen = hei;
						//System.out.println("widthScreen" + widthScreen);
						//System.out.println("heightScreen" + heightScreen);
						//System.out.println("Screen_X" + view.getX());
						//System.out.println("Screen_Y" + view.getY());
						hasMeasured = true;
					}
				}
				return true;

			}

		});
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		view_width = view.getWidth();
		view_height = view.getHeight();
		view_x = location[0];
		view_y = location[1];
		intent.putExtra("view_width", view_width);
		intent.putExtra("view_height", view_height);
		intent.putExtra("view_x", view_x);
		intent.putExtra("view_y", view_y);
		}
	
		intent.putExtra("showType", showType);
		
		try {
			
		     	long time = System.currentTimeMillis();
		        long timeD = time - lastClickTime;
		        lastClickTime = time;
		        if(timeD <= 300){
		        	return ;
		        }
		        
		        
			if (activity != null) {
				activity.startActivityForResult(intent, requestCode);
			} else {
				context.startActivity(intent);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			String errorMessage = "no find activity";
			Log.e("ZD_BaseActivity", errorMessage);
//			Toast.makeText(context, errorMessage, 0).show();
		
		}
	
	}
	
	public void addManager(){
		// 加入页面管理机制
				AppManager.getAppManager().addActivity(activity);
	}
	public void delManager(){
		// 重页面管理机制中清除本页面
				AppManager.getAppManager().finishActivity(activity);
	}

	
	
	/**
	 * 对左右滑动的动画操作
	 * @param layout
	 * @param visibility
	 * @param HOrV
	 * @return
	 */
	public TranslateAnimation hidn(View layout,int visibility,boolean head,boolean open,boolean HOrV){
		TranslateAnimation t = HOrV?exeAnimLeftOrRight(layout, head, open): exeAnim(layout, false,false);
		layout.setVisibility(visibility);
		return t;
	}
	public TranslateAnimation hidn(View layout,int visibility){
		TranslateAnimation t = exeAnim(layout, false,false);
		layout.setVisibility(visibility);
		return t;
	}
	public TranslateAnimation hidn(View layout,int visibility,boolean head,boolean open){
		TranslateAnimation t = exeAnim(layout, head,open);
		layout.setVisibility(visibility);
		return t;
	}
	public TranslateAnimation hidn(View layout,boolean head,boolean open){
		TranslateAnimation t = exeAnim(layout, head,open);
		layout.setVisibility(View.INVISIBLE);
		return t;
	}
	public TranslateAnimation hidn(View layout) {
		return hidn(layout, View.INVISIBLE);
	}

	public TranslateAnimation show(View layout,boolean head,boolean open,boolean HOrV) {

		TranslateAnimation t= HOrV?exeAnimLeftOrRight(layout, head,open):exeAnim(layout, head,open);
		layout.setVisibility(View.VISIBLE);
		return t;
	
	}
	public TranslateAnimation show(View layout,boolean head,boolean open) {

		TranslateAnimation t= exeAnim(layout, head,open);
		layout.setVisibility(View.VISIBLE);
		return t;
	
	}
	public void show(View layout) {
		show(layout, false,true);
	}
	private TranslateAnimation exeAnim(View layout, boolean head,boolean open) {
		TranslateAnimation translateAnimation;
		if(head){
			if(open){
				//头部  打开
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue,
//				fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, 0, 1,0,   
				1, -1,1, 0);
			}else{
				//头部 关闭
				
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, 0, 1,0,   
						1, 0,1, -1);
			}
		
		}else{
			if(open){
				//脚 打开
				translateAnimation = new TranslateAnimation(1, 0, 1, 0,    1,1, 1, 0);
			}else{
				//脚关闭
				translateAnimation = new TranslateAnimation(1, 0, 1,0,     1, 0,1, 1);
			}
			
		}
	
		
		translateAnimation.setDuration(showtime);
		layout.startAnimation(translateAnimation);
		return translateAnimation;
	}
	public AlphaAnimation hindAlphaAnimation(final View view,final boolean open,int showtime){
		if(view==null)return null;
		view.clearAnimation();
		AlphaAnimation alp = new AlphaAnimation(open?0.0f:1.0f, open?1.0f:0.0f);
		alp.setDuration(showtime);
		view.startAnimation(alp);
		if(open){
			view.setVisibility(View.VISIBLE);
		}
		alp.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.setVisibility(open?View.VISIBLE:View.INVISIBLE);
			}
		});
		return alp;
	} 
	 
	public TranslateAnimation exeAnimLeftOrRight(View layout, boolean head,boolean open) {
		TranslateAnimation translateAnimation;
		//System.out.println("head:"+head);
		//System.out.println("open:"+open);
		if(head){
			if(open){
				//头部  打开
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue,
//				fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, -1, 1,0,   1, 0,1, 0);
//				translateAnimation = new TranslateAnimation(1, 0, 1,0, 1, -1,1, 0);
			}else{
				//头部 关闭
				
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, 0, 1,-1,   1, 0,1, 0);
//				translateAnimation = new TranslateAnimation(1, 0, 1,0,  1, 0,1, -1);
			}
		
		}else{
			if(open){
				//脚 打开
				translateAnimation = new TranslateAnimation(1, 0, 1, 0,    1,1, 1, 0);
			}else{
				//脚关闭
				translateAnimation = new TranslateAnimation(1, 0, 1,0,     1, 0,1, 1);
			}
			
		}
	
		
		translateAnimation.setDuration(showtime);
		layout.startAnimation(translateAnimation);
		return translateAnimation;
	}
	
	
	
	
	
}
