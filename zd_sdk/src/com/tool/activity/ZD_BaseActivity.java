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
 * ��������ҳ����.
 * 1:ʵ�ֶ���Ч��
 * 2:ʵ��ҳ��ͳһ��
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
		
		//���̼
		addManager();
		
		initData();
		initView_base();
		//���Ĭ��ҳ��
//		
	
	
		//�����¼,������֤�Ƿ�ı��ֻ����һ��Ȧ��,
		//���ֻ����һ��Ȧ��,�����иı�,SO,����ѡ��ֻ��Ҫһ��Ȧ��
		
		return layout_main;

	}
	
	
	public void setShowType(int showType){
		this.showType=showType;
	}
	
	/**
	 * ������ʾ��λ XY�������
	 */
	private void initData() {
		
		// ֪ͨ/״̬���߶�
		StatusBarHeight = Tool_Screen.getInstance().getStatusBarHeight(context);
		// ��Ļ���
		screen_width = Tool_Screen.getInstance().getScreenWidth(context);
		// ��Ļ�߶� - ֪ͨ/״̬���߶�
		screen_height = Tool_Screen.getInstance().getScreenHeight(context) - StatusBarHeight;
		Intent intent = activity.getIntent();
		//��ʾЧ��
		showType = intent.getIntExtra("showType", showType);
//		showPath = intent.getBooleanExtra("showPath", showPath);
		// �������Ŀ��
		view_width = intent.getFloatExtra("view_width", view_width);
		// �������ĸ߶�
		view_height = intent.getFloatExtra("view_height", view_height);
		// ���������Ļ�� Xλ��
		view_x = intent.getFloatExtra("view_x", view_x);
		// ���������Ļ�� Yλ��- ֪ͨ/״̬���߶�
		view_y = intent.getFloatExtra("view_y", view_y) - StatusBarHeight;
		// �����������Ļ�����X����
		pivotXValue = (view_x + view_width / 2) / screen_width;
		// �����������Ļ�����Y����
		pivotYValue = (view_y + view_height / 2) / screen_height;
		// ��֤������
		if (screen_width < 1 || screen_height < 1 || view_width < 1
				|| view_height < 1 || view_x < 0 || view_y < 0
				|| view_x > screen_width || view_y > screen_height) {
			// ���û�еõ���Ļ��С ����û�еõ�����Ĵ�С���������������λ�ã����߿ӵ����������Ļ��λ�ó�������Ļ
			// �������������ȫ���Ծ���Ļ���ĵ������ʾ
			pivotXValue = 0.5f;
			pivotYValue = 0.5f;
		}

		 //System.out.println("==================");
	}
	
	/**
	 * ��Ϣ����¼�����
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
	 * ����������ҳ��
	 * @param view
	 */
	private void addMessage_netListen(ViewGroup viewGroup){
//		View view1 = LayoutInflater.from(context).inflate(R.layout.tool_show_toast_message, null);// ��������൱��ʱ��
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
//	                //�ж��ֻ�ϵͳ�İ汾  ��API����10 ����3.0�����ϰ汾 
//				 if(android.os.Build.VERSION.SDK_INT > 10 ){
//					    //3.0���ϴ����ý��棬Ҳ����ֱ����ACTION_WIRELESS_SETTINGS�򿪵�wifi����
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
 * ��������������ͼ��ͳһ��ӵ� layout_main
 */


	/**
	 * �������ָ��layout ���� view 
	 * @param view
	 */
	public void setSelfContent(View view) {
	
		//������������Ƿ���ScrollView ����ǣ������滻��LinearLayout 
				//ע��һ��������ò��������ΪScrollView ����XML�������ΪScrollView ֻ��Ϊ��XML�ĸ߶ȿ��Զ����ء���
				//Ҳ�ɿ�������Զ�������������㲼�֣����ʵ����Ҫ�������ScrollView���Ǿ���ScrollView����һ��ScrollView
				//Ҳ������ScrollView�� tagֵ Ϊ clear
				if(view!=null){
					if(view instanceof ScrollView){
						Object tag_sv = view.getTag();
						if(tag_sv!=null&&tag_sv.toString().equals("clear")){

							//�����滻�����
							int count = ((ScrollView) view).getChildCount();
							if(count>0){
								//ScrollViewֻ�������һ���Ӳ��֣�����
								View child = ((ScrollView) view).getChildAt(0);
								//���Ӳ��ָ��µĶ���
								LinearLayout view_ll = new LinearLayout(context);
								//orientation �Ķ�Ӧ�᲼��
								view_ll.setOrientation(LinearLayout.VERTICAL);
								LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
								view_ll.setLayoutParams(params);
								view_ll.setBackgroundColor(Color.WHITE);
								//������ϼ����֣������֣��е�ָ��
								 ViewGroup p = (ViewGroup) child.getParent(); 
						         if (p != null) { 
						             p.removeAllViewsInLayout(); 
						         } 
								view_ll.addView(child);
								view = view_ll;
							}
						
						}else{
							//������滻����㲼�ֹ���
						}
					}
				}
		
				
		layout_main.removeAllViews();
		
		
		layout_main.addView(view);
		
		
		//���ҳ���� ��.���һ����ť
		if(false){
//			showLoad_layout = LayoutInflater.from(context).inflate(R.layout.defaultload_base, null);// ��������൱��ʱ��
			Button showLoad_layout = new Button(context);
			showLoad_layout.setText("test");
			showLoad_layout.setVisibility(View.GONE);
			layout_main.addView(showLoad_layout);
		}
		
		
		
		//��֤�Ƿ��������
//		if(Tool_NetworkUtils.isNetworkConnected(context)){
//			addMessage_netListen(layout_main);
//		}
		//���ָ����ҳ��
		if(views!=null){
			for (int i = 0; i < views.length; i++) {
				if(views[i]!=null)
				layout_main.addView(views[i]);
			}
		}
		
		
	
	}
	
	public void start(){
		// ��Ч��
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
	//����ϲ�ҳ��
	View[] views ;
	public void addView(View... views){
		this.views = views;
		
	}

//	
//	public void addView(View view){
//		layout_main.addView(view);
//	}
	
	



	

	/**
	 * ����Ĭ�ϵ�һ��ҳ���
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
		animation.setDuration(showtime);// ���ö�������ʱ��
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
		animation.setDuration(showtime);// ���ö�������ʱ��
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
	 * ����������ϣ����ô˺���
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
		
		anim.setDuration(showtime);// ���ö�������ʱ��
		
		
		
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
	 * @param view�򿪵��¼����
	 * @param intent
	 * @param activity ���null���봫��context 
	 * @param requestCode ����activity��ʱ�򣬻���÷��ش򿪣�startActivityForResult��,code�� �����ԭ���õ�code
	 * @param context
	 * @param showType ����Ч��BaseActivity.SHOWTYOE_ZOOM �Ŵ� SHOWTYOE_FANDEOUT ���뵭�� NONE û��
	 * @param showPath �Ƿ���ʾpath������Ĭ�ϲ���ʾ
	 */
	@SuppressLint("NewApi")
	public static void startActivity(final View view,Intent intent,Activity activity,int requestCode,Context context,int showType) {
		float view_width = 0;
		float view_height = 0;
		float view_x = 0;
		float view_y = 0;
		if(view!=null){
		
		// �õ�ͼƬԴ�Ĳ��ִ�С
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
		// ����ҳ��������
				AppManager.getAppManager().addActivity(activity);
	}
	public void delManager(){
		// ��ҳ���������������ҳ��
				AppManager.getAppManager().finishActivity(activity);
	}

	
	
	/**
	 * �����һ����Ķ�������
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
				//ͷ��  ��
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue,
//				fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, 0, 1,0,   
				1, -1,1, 0);
			}else{
				//ͷ�� �ر�
				
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, 0, 1,0,   
						1, 0,1, -1);
			}
		
		}else{
			if(open){
				//�� ��
				translateAnimation = new TranslateAnimation(1, 0, 1, 0,    1,1, 1, 0);
			}else{
				//�Źر�
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
				//ͷ��  ��
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue,
//				fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, -1, 1,0,   1, 0,1, 0);
//				translateAnimation = new TranslateAnimation(1, 0, 1,0, 1, -1,1, 0);
			}else{
				//ͷ�� �ر�
				
//				new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue)
				translateAnimation = new TranslateAnimation(1, 0, 1,-1,   1, 0,1, 0);
//				translateAnimation = new TranslateAnimation(1, 0, 1,0,  1, 0,1, -1);
			}
		
		}else{
			if(open){
				//�� ��
				translateAnimation = new TranslateAnimation(1, 0, 1, 0,    1,1, 1, 0);
			}else{
				//�Źر�
				translateAnimation = new TranslateAnimation(1, 0, 1,0,     1, 0,1, 1);
			}
			
		}
	
		
		translateAnimation.setDuration(showtime);
		layout.startAnimation(translateAnimation);
		return translateAnimation;
	}
	
	
	
	
	
}
