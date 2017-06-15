package com.cx.myobject;

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

public class MyNoteDialog extends Dialog {
public int screenWidth,screenHeight;
private TextView titleTv;
private EditText noteEt;
private Button dialogSaveBtn,dialogCancelBtn;
	public MyNoteDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO 自动生成的构造函数存根
		WindowManager wm=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		screenWidth=wm.getDefaultDisplay().getWidth();
		screenHeight=wm.getDefaultDisplay().getHeight();
	}
	
	public void setDialog(){
		View view=LayoutInflater.from(getContext()).inflate(R.layout.notedialog_layout, null);
		titleTv=(TextView)view.findViewById(R.id.notedialog_title);
		noteEt=(EditText)view.findViewById(R.id.dialog_not_et);
		dialogSaveBtn=(Button)view.findViewById(R.id.dialog_save_btn);
		dialogCancelBtn=(Button)view.findViewById(R.id.dialog_cancel_btn);
		int dialogWidth=(int)(screenWidth*3/4);
		int dialogHeight=(int)(dialogWidth*2/3);
		//noteEt.getLayoutParams().height=dialogHeight*4/7;
		super.addContentView(view, new LayoutParams(dialogWidth,dialogHeight));
	}
	
	public void setTitle(String titleStr){
		titleTv.setText(titleStr);
	}
	public View getEditText(){
		return noteEt;
	}
	public void setSaveBtn(View.OnClickListener listener){
		dialogSaveBtn.setOnClickListener(listener);
	}
	
	public void setCancelBtn(View.OnClickListener listener){
		dialogCancelBtn.setOnClickListener(listener);
	}

}
