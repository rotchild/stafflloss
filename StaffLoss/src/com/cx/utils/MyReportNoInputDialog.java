package com.cx.utils;


import com.cx.staffloss.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyReportNoInputDialog extends Dialog{
	static int screenWidth;
	static int screenHeight;
	private EditText editText;
	private Button positiveButton,negativeButton;
	private TextView title,itemname;	
	
	public MyReportNoInputDialog(Context context, int theme) {
		super(context,theme);
		// TODO 自动生成的构造函数存根
		WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		screenWidth=wm.getDefaultDisplay().getWidth();
		screenHeight=wm.getDefaultDisplay().getHeight();
	}
 
	public void setReportCustomDialog(){
		
		View mView=LayoutInflater.from(getContext()).inflate(R.layout.reportnodialog_layout, null);
		title=(TextView)mView.findViewById(R.id.rp_dialog_title);
		//itemname=(TextView)mView.findViewById(R.id.item_name_tv);
		editText=(EditText)mView.findViewById(R.id.rp_inputCaseNo_et);
		positiveButton=(Button)mView.findViewById(R.id.rp_positive_btn);
		negativeButton=(Button)mView.findViewById(R.id.rp_negative_btn);
		int dialogWidth=(int)(screenWidth*3/4);
		int dialogHeight=(int)(dialogWidth*3/5);
		editText.getLayoutParams().width=(int)(dialogWidth*4/5);
		positiveButton.getLayoutParams().width=(int)(dialogWidth*1/4);
		negativeButton.getLayoutParams().width=(int)(dialogWidth*1/4);
		super.addContentView(mView, new LayoutParams(dialogWidth,dialogHeight));
	}
	
	public void setTitle(String titleStr){
		title.setText(titleStr);
	}
	
/*	public void setItemName(String itemnameStr){
		itemname.setText(itemnameStr);
	}*/
	
	public View getEditText(){
		return editText;
	}
	public void setOnPositiveListener(View.OnClickListener listener){  
	    	positiveButton.setOnClickListener(listener);  
	}  
	 public void setOnNegativeListener(View.OnClickListener listener){  
	    	negativeButton.setOnClickListener(listener);  
	    }
}
