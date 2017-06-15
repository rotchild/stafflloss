package com.cx.myobject;

import com.cx.staffloss.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class MyNoteShowDialog extends Dialog {
public int screenWidth,screenHeight;
private TextView titleTv,noteTv;
private Button closeBtn;
	public MyNoteShowDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO 自动生成的构造函数存根
		WindowManager wm=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		screenWidth=wm.getDefaultDisplay().getWidth();
		screenHeight=wm.getDefaultDisplay().getHeight();
	}
	public void setMyNoteShowDialog(){
	View view=LayoutInflater.from(getContext()).inflate(R.layout.noteshowdialog_layout, null);
	titleTv=(TextView)view.findViewById(R.id.noteshowdialog_title);
	noteTv=(TextView)view.findViewById(R.id.dialog_note_tv);
	closeBtn=(Button)view.findViewById(R.id.dialogshow_close_btn);
	int dialogWidth=(int)(screenWidth*3/4);
	int dialogHeight=(int)(dialogWidth*2/3);
	super.addContentView(view, new LayoutParams(dialogWidth,dialogHeight));
	}
	
	public void setTitle(String titleStr){
		titleTv.setText(titleStr);
	}
	public View getNoteText(){
		return noteTv;
	}
	public void setCloseBtn(View.OnClickListener listener){
		closeBtn.setOnClickListener(listener);
	}

}
