package com.cx.staffloss;



import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class MyAlertDialog extends Dialog {
	static int screenWidth;
	static int screenHeight;
	private Button positiveButton,negativeButton;
	
	public MyAlertDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO 自动生成的构造函数存根
		WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		screenWidth=wm.getDefaultDisplay().getWidth();
		screenHeight=wm.getDefaultDisplay().getHeight();
	}
	
	public void setMyAlertDialog(){
		View mView=LayoutInflater.from(getContext()).inflate(R.layout.malert_layout, null);
		positiveButton=(Button)mView.findViewById(R.id.alert_positive_btn);
		negativeButton=(Button)mView.findViewById(R.id.alert_negative_btn);
		int dialogWidth=(int)(screenWidth*3/4);
		int dialogHeight=(int)(dialogWidth*2/3);
		positiveButton.getLayoutParams().width=(int)(dialogWidth*1/3);
		negativeButton.getLayoutParams().width=(int)(dialogWidth*1/3);
		super.addContentView(mView, new LayoutParams(dialogWidth,dialogHeight));
		
	}
	
	public void setOnPositiveListener(View.OnClickListener listener){
		positiveButton.setOnClickListener(listener);
	}
	
	public void setOnNegativeListener(View.OnClickListener listener){
		negativeButton.setOnClickListener(listener);
	}

}
