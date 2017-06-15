package com.cx.myobject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MyPushReceiver extends XGPushBaseReceiver {

	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult clickResult) {
		// TODO 自动生成的方法存根
		Intent clickIntent=new Intent();
		clickIntent.setComponent(new ComponentName("com.cx.staffloss","com.cx.staffloss.currentTask"));
		context.getApplicationContext().startActivity(clickIntent);
	}

	@Override
	public void onNotifactionShowedResult(Context arg0, XGPushShowedResult arg1) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onRegisterResult(Context arg0, int arg1,
			XGPushRegisterResult arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onSetTagResult(Context arg0, int arg1, String arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onTextMessage(Context arg0, XGPushTextMessage arg1) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO 自动生成的方法存根
		
	}

}
