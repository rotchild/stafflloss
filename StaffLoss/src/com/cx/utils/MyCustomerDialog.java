package com.cx.utils;

import com.cx.staffloss.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyCustomerDialog extends Dialog {
	static int screenWidth;
	static int screenHeight;
	
	
	public MyCustomerDialog(Context context, int themeResId) {
		super(context, themeResId);
		WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		screenWidth=wm.getDefaultDisplay().getWidth();
		screenHeight=wm.getDefaultDisplay().getHeight();
		
		// TODO 自动生成的构造函数存根
	}
	
public static class Builder{
	private Context context;
	private String title;
	private OnClickListener positiveOnClickListener;
	private OnClickListener negativeOnClickListener;
	public EditText inputCaseNo;
	public View layout;
	public Builder(Context context){
		this.context=context;
		
	}
	public EditText getEditText(){
		return inputCaseNo;
	}
	
	public Builder setTitle(String title){
		this.title=title;
		return this;
	}
	
	
	public Builder setPositiveButton(OnClickListener listener){
		this.positiveOnClickListener=listener;
		return this;
	}
	public Builder setNagtiveButton(OnClickListener listener){
		this.negativeOnClickListener=listener;
		return this;
	}
	
	
	public MyCustomerDialog create(){
		
		final MyCustomerDialog dialog=new MyCustomerDialog(context,R.style.Dialog);
		
		
		//inputCaseNo=(EditText)layout.findViewById(R.id.inputCaseNo_et);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout=inflater.inflate(R.layout.mdialog_layout, null);
		
		TextView titleTv=(TextView)layout.findViewById(R.id.dialog_title);
		titleTv.setText(title);
		if(positiveOnClickListener!=null){
			Button mPositiveBtn=(Button)layout.findViewById(R.id.positive_btn);
			mPositiveBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					positiveOnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
				}
			});
		}
		if(positiveOnClickListener!=null){
			Button mNegativeBtn=(Button)layout.findViewById(R.id.negative_btn);
			mNegativeBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					negativeOnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
				}
			});
		}
		int dialogWidth=(int)(screenWidth*3/4);
		int dialogHeight=(int)(dialogWidth*2/3);
		inputCaseNo.getLayoutParams().width=(int)(dialogWidth*2/3);//edittext.setWidth 无效
		dialog.addContentView(layout, new LayoutParams(dialogWidth,dialogHeight));
		dialog.setCancelable(false);
		return dialog;
	}
}
}
