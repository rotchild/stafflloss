package com.cx.staffloss;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.ipset.IPSetActivity;
import com.cx.myobject.MyPushReceiver;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.utils.MD5Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.XGBasicPushNotificationBuilder;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotificationBuilder;
import com.tencent.android.tpush.service.XGPushService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
EditText userNameEt,passwordEt;
Button loginButton,ipsetButton;
CheckBox isRemember;
SharedPreferences loginSp;
public static String TAG="loginActivity";
public static String SPName="StaffLossSP";
SharedPreferences loginSP;
public XGPushNotificationBuilder xgNotificationBuilder;
public static final int notificationBuilderId=1;
ProgressDialog pd;
String tokenConfig="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		loginSP=getSharedPreferences(SPName,0);
		int loginState=loginSP.getInt("state", 0);
		if(loginState==1){
			Intent tocurrent=new Intent(LoginActivity.this,currentTask.class);
			startActivity(tocurrent);
			LoginActivity.this.finish();
		}else{
			
			setContentView(R.layout.activity_main);
			initView();
		}
		
	}
public void initView(){
	xgNotificationBuilder=new XGBasicPushNotificationBuilder();
	//Uri soundUri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.test_ring);
	//xgNotificationBuilder.setSound(soundUri);
	loginSp=getSharedPreferences(SPName,0);
	userNameEt=(EditText)findViewById(R.id.username_et);
	passwordEt=(EditText)findViewById(R.id.password_et);
	isRemember=(CheckBox)findViewById(R.id.remember_password);
	
	String userNamePre=loginSp.getString("userNameStr", "");
	userNameEt.setText(userNamePre);
	int checkState=loginSp.getInt("checkstate", 0);
	if(checkState==0){
		isRemember.setChecked(false);
	}else{
		isRemember.setChecked(true);
		String userPasswordPre=loginSp.getString("passwordStr", "");
		passwordEt.setText(userPasswordPre);
	}
	
	loginButton=(Button)findViewById(R.id.login_btn);
	ipsetButton=(Button)findViewById(R.id.setip_btn);
	
	
	isRemember.setOnCheckedChangeListener(new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO 自动生成的方法存根
			
		}
		
	});
	
	loginButton.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			
			String userNameStr=userNameEt.getText().toString();
			String passwordStr=passwordEt.getText().toString();
			
			if(null!=userNameStr&&userNameStr.length()>0&&null!=passwordStr&&passwordStr.length()>0){
				SharedPreferences.Editor spEditor=loginSp.edit();
				spEditor.putString("userNameStr", userNameStr);
				
				if(isRemember.isChecked()){
					spEditor.putString("passwordStr", passwordStr);
					spEditor.putInt("checkstate", 1);
				}else{
					spEditor.putString("passwordStr", "");
					spEditor.putInt("checkstate", 0);
				}
				spEditor.commit();
				tokenConfig=XGPushConfig.getToken(getApplicationContext());
				int length=tokenConfig.length();
				Log.d(TAG, "token"+tokenConfig+"tokenLen"+tokenConfig.length());
				if(tokenConfig==null||tokenConfig.length()<=0){
					//提示dialog
					showAlertDialog(userNameStr,passwordStr);
				}else{
					LoginByAsyncHttp(userNameStr,passwordStr);		
				}
				
				
			}else{
				Toast.makeText(getApplicationContext(), "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
			}
			
			
		}
		
	});
	
	ipsetButton.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent toIPset=new Intent(LoginActivity.this,IPSetActivity.class);
			startActivity(toIPset);
			//LoginActivity.this.finish();
		}
		
	});
}

public void showAlertDialog(final String usernameStr,final String passwordStr){
	final MyAlertDialog mAlertDialog=new MyAlertDialog(LoginActivity.this,R.style.Dialog);
	mAlertDialog.setMyAlertDialog();
	mAlertDialog.setOnPositiveListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			LoginByAsyncHttp(usernameStr,passwordStr);
		}
		
	});
	mAlertDialog.setOnNegativeListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			if(mAlertDialog==null||mAlertDialog.isShowing()){
				mAlertDialog.dismiss();
			}
		}
		
	});
	mAlertDialog.show();
}


private void LoginByAsyncHttp(String userNameStr,String password){
	pd=ProgressDialog.show(LoginActivity.this, "请等待", "加载中");
	//hide softkeyboard
	InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if(imm.isActive()){
         imm.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
	String passwordMD5Str=MD5Util.getMd532Value(password);
	AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
	asyncHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	asyncHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	String dUrl=MHttpParams.IP;
	String mUrl=loginSP.getString("IP", dUrl);
	String dPort=MHttpParams.DEFAULT_PORT;
	String mPort=loginSP.getString("Port", dPort);
	String loginUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.LoginBUrl;
	

	
	RequestParams params=new RequestParams();
	params.put("username", userNameStr);
	params.put("password", passwordMD5Str);
	params.put("token", tokenConfig);

	asyncHttpClient.post(loginUrl,params, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, JSONObject response) {
			// TODO 自动生成的方法存根
			super.onSuccess(statusCode, response);
			boolean success=false;
			try{
				success=response.getBoolean("success");
				if(success){
					if(pd!=null){
						pd.dismiss();
						pd=null;
					}
					JSONObject data=response.getJSONObject("data");
					int id=data.getInt("id");
					String dingsunerName=data.getString("real_name");
					String telephoneStr=data.getString("telephone");
					
					MyhttpStorage.id=id;
					MyhttpStorage.dingsunerName=dingsunerName;
					MyhttpStorage.dingsunerTel=telephoneStr;
				
					
					SharedPreferences.Editor loginEditor=loginSp.edit();
					loginEditor.putInt("id", id);//不可使用MyhttpStorage;
					loginEditor.putString("dingsunerName", dingsunerName);
					loginEditor.putString("dingsunerTel", telephoneStr);
					
					loginEditor.putInt("state", 1);
					loginEditor.commit();
					
					MyPushReceiver myPushReceiver=new MyPushReceiver();
					IntentFilter myFilter=new IntentFilter();
					myFilter.addAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
					myFilter.addAction("com.tencent.android.tpush.action.FEEDBACK");
					registerReceiver(myPushReceiver,myFilter);//unregister????
					
					Context context = getApplicationContext();
					XGPushManager.registerPush(context);
					XGPushManager.setPushNotificationBuilder(context, notificationBuilderId, xgNotificationBuilder); 
					Intent service = new Intent(context, XGPushService.class);
					context.startService(service);

					
					Intent loginIntent=new Intent(LoginActivity.this,currentTask.class);
					startActivity(loginIntent);
					LoginActivity.this.finish();
				}else{
					if(pd!=null){
						pd.dismiss();
						pd=null;
					}
					Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
				}
			}catch(JSONException e){
				e.printStackTrace();
			}finally{
			

			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO 自动生成的方法存根
			super.onFailure(e, errorResponse);
		}
		
		@Override
		public void onFailure(Throwable e, String errorResponse) {
			// TODO 自动生成的方法存根
			super.onFailure(e, errorResponse);
			if(pd!=null){
				pd.dismiss();
				pd=null;
			}
			Toast.makeText(getApplicationContext(), "连接错误", Toast.LENGTH_SHORT).show();
		}
		
	});
}
}
