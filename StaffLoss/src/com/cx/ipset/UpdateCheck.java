package com.cx.ipset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.MyUpdateObject;
import com.cx.netset.MHttpParams;
import com.cx.staffloss.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCheck extends Activity {
Button checkUpdateBtn;
ProgressDialog pd;
public static String TAG="updateCheck";
private final int percentStep=5;
private final int SHOWUPDATEDIAG=1;
private final int UPDATEPROGRESS=2;
private final int UPDATEWRONG=3;
private ProgressDialog progressbar;
private AlertDialog mAlertDialog;
private String fileName;
boolean stopThread=false;
TextView versionCode;
//TextView setBackTv;
ImageView setBackIv;
SharedPreferences updateCheckSP;
public static final String SPName="StaffLossSP";
Handler dealerHandler=new Handler(){

	@Override
	public void handleMessage(Message msg) {
		// TODO 自动生成的方法存根
		super.handleMessage(msg);
		int msgId=msg.what;
		switch(msgId){
		case SHOWUPDATEDIAG://显示下载dialog
			showUpdateDialog();	
			break;
		case UPDATEPROGRESS:
			Bundle mBundle=msg.getData();
			int percentCount=mBundle.getInt("percentCount");
			if(percentCount>0){
				if(progressbar!=null){
					progressbar.setProgress(percentCount);
				}
			}
			
			if(percentCount==100){
				if(progressbar!=null){
					if(progressbar.isShowing()){
						progressbar.dismiss();
						progressbar=null;
					}
					inStallAPK();
				}
				
			}
			break;
		default:
			break;
		}
	}
	
};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_layout);
		updateCheckSP=getSharedPreferences(SPName,0);
		initView();
	}
public void initView(){
	setBackIv=(ImageView)findViewById(R.id.setback_iv);
	setBackIv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			UpdateCheck.this.finish();
		}
		
	});
/*	setBackTv=(TextView)findViewById(R.id.setback_tv);
	setBackTv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			UpdateCheck.this.finish();
		}
		
	});*/
	
	versionCode=(TextView)findViewById(R.id.version_id);
	String localVersionStr=getLocalVersion();
	versionCode.setText(localVersionStr+"版");
	checkUpdateBtn=(Button)findViewById(R.id.checkupdate_btn);
	checkUpdateBtn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			checkUpdate();
		//	pd.show(UpdateCheck.this, "查询中", "请稍后...");
		}
		
	});
}

public void checkUpdate(){
	
	pd=ProgressDialog.show(UpdateCheck.this, "查询中", "请稍候");
	AsyncHttpClient updateClient=new AsyncHttpClient();
	updateClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	updateClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	//String mUrl="192.168.1.63";
	//String mPort="3003";
	String mUrl=updateCheckSP.getString("IP", MHttpParams.IP);
	String mPort=updateCheckSP.getString("Port", MHttpParams.DEFAULT_PORT);
	String checkUpdateUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.checkUpdateUrl;
	RequestParams params=new RequestParams();
	params.put("SystemOS", MHttpParams.SystemOS);
	params.put("ProjectName",MHttpParams.ProjectName);
	updateClient.post(checkUpdateUrl, params,new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, JSONObject response) {
			// TODO 自动生成的方法存根
			super.onSuccess(statusCode, response);
			if(pd!=null&&pd.isShowing()){
				pd.dismiss();
			}
			boolean success=false;
			try{
				success=response.getBoolean("success");
				if(success){
					JSONObject value=response.getJSONObject("value");
					if(value!=null){
						Log.d(TAG, "value is not null");
						JSONObject latestVersion=value.getJSONObject("latest_version");
						if(latestVersion!=null){
							Log.d(TAG, "lastVersion is not null");
							String versionCode=latestVersion.getString("version_code");
							String updateHintMsg=latestVersion.getString("update_hint_msg");
							String downLoadUrl=latestVersion.getString("update_url");
							String releaseDate=latestVersion.getString("release_date");
							MyUpdateObject.versionCode=versionCode;
							MyUpdateObject.hintMessage=updateHintMsg;
							MyUpdateObject.downLoadUrl=downLoadUrl;
							MyUpdateObject.releaseDate=releaseDate;
							Log.d(TAG, "servierversionCode"+MyUpdateObject.versionCode);
							if(isNeedUpdate(MyUpdateObject.versionCode)){
								Log.d(TAG, "needUpdate");
							 Message message=new Message();
							 message.what=SHOWUPDATEDIAG;
							 dealerHandler.sendMessage(message);
							}
							
						}
					}else{
						Log.d(TAG, "value is  null");
					}
				}else{
					Toast.makeText(UpdateCheck.this, "当前版本已是最新版本", Toast.LENGTH_SHORT).show();
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO 自动生成的方法存根
			super.onFailure(e, errorResponse);
			if(pd!=null&&pd.isShowing()){
				pd.dismiss();
			}
			Toast.makeText(UpdateCheck.this, "当前版本已是最新版本", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onFailure(Throwable e, String errorResponse) {
			// TODO 自动生成的方法存根
			super.onFailure(e, errorResponse);
			if(pd!=null&&pd.isShowing()){
				pd.dismiss();
			}
			Toast.makeText(UpdateCheck.this, "当前版本已是最新版本", Toast.LENGTH_SHORT).show();
		}
		
	});
}

public boolean isNeedUpdate(String serverVersionCode){
	String localVersion=getLocalVersion();
	Log.d(TAG, "localVersion"+localVersion);
	if(localVersion.equals("-1")){
		Log.d(TAG, "本地版本namenofound");
		return false;
	}else if(localVersion.compareTo(MyUpdateObject.versionCode)==0){
		Log.d(TAG, "本地版本不需要更新");
		Toast.makeText(UpdateCheck.this, "当前版本已是最新版本", Toast.LENGTH_SHORT).show();
		return false;
	}else if(localVersion.compareTo(MyUpdateObject.versionCode)<0){
		return true;
	}else{
		Log.d(TAG, "本地版大于新版本");
		return false;
	}
		
	}

public String getLocalVersion(){
	String localVersionStr="";
	PackageManager mPackagemanager=getPackageManager();
	try{
		PackageInfo packageInfo=mPackagemanager.getPackageInfo(getPackageName(), 0);
		localVersionStr=packageInfo.versionName;
	}catch(NameNotFoundException e){
		e.printStackTrace();
		localVersionStr= "-1";
	}
	return localVersionStr;
}

public void showUpdateDialog(){
	mAlertDialog=new AlertDialog.Builder(UpdateCheck.this).create();
	mAlertDialog.setCancelable(false);
	LayoutInflater myInflater=LayoutInflater.from(UpdateCheck.this);
	View mView=myInflater.inflate(R.layout.alertdialog_layout, null);
	Button sureToUpdate=(Button)mView.findViewById(R.id.suretoUpdate_id);
	TextView versionMsg=(TextView)mView.findViewById(R.id.versionmsg_id);
	if(MyUpdateObject.versionCode!=null){
		versionMsg.setText("请更新到版本"+MyUpdateObject.versionCode);
	}
	sureToUpdate.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			if(MyUpdateObject.downLoadUrl!=null){
				downLoadAPK(MyUpdateObject.downLoadUrl);
			}else{
				Log.d(TAG, "downLoadUrl is null");
			}
		}
		
	});
	mAlertDialog.show();
	mAlertDialog.getWindow().setContentView(mView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
}
public void inStallAPK(){
	Intent intent=new Intent(Intent.ACTION_VIEW);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	File apkFile=new File(Environment.getExternalStorageDirectory(),fileName);
	if(apkFile.length()>0){
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
	}else{
		Toast.makeText(UpdateCheck.this, "apk文件为0B", Toast.LENGTH_SHORT).show();
	}
	
	startActivity(intent);
	android.os.Process.killProcess(android.os.Process.myPid());
}

public void downLoadAPK(final String url){
	if(mAlertDialog!=null){
		if(mAlertDialog.isShowing()){
			mAlertDialog.dismiss();
		}
		mAlertDialog=null;
	}
	if(!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))){
		Toast.makeText(UpdateCheck.this, "SD卡不可用", Toast.LENGTH_SHORT).show();
	}else{//原生http请求...
		progressbar=new ProgressDialog(UpdateCheck.this);
		progressbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressbar.setTitle("正在下载...");
		progressbar.setMessage("请稍候...");
		progressbar.setProgress(0);
		progressbar.show();
		progressbar.setCancelable(false);
		
		new Thread(){

			@Override
			public void run() {
				if(!stopThread){
					// TODO 自动生成的方法存根
					int updateTotalSize=0;
					int totalSize=0;
					int percentCount=0;
					InputStream is=null;
					FileOutputStream fileOutputStream=null;
					HttpClient httpClient=new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, MHttpParams.DEFAULT_TIME_OUT);
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, MHttpParams.DEFAULT_TIME_OUT);
					HttpGet get=new HttpGet(url);
					HttpResponse response;
					try{
						response=httpClient.execute(get);
						HttpEntity entity=response.getEntity();
						updateTotalSize=(int)entity.getContentLength();
						 is=entity.getContent();
						
						if(is!=null){
							fileName=MHttpParams.APKName+MyUpdateObject.versionCode+".apk";
							File apkFile=new File(Environment.getExternalStorageDirectory(),fileName);
							/*if(!apkFile.exists()){ 
								apkFile.createNewFile();//cause to apkFile download 0B???
							}*/
							fileOutputStream=new FileOutputStream(apkFile);
							byte[] buf=new byte[32];
							int readSize=-1;
							int progress=0;//????
							while((readSize=is.read(buf))!=-1){
								fileOutputStream.write(buf,0,readSize);
								Log.d("ChoosePage", "apkFileLength"+apkFile.length());
								totalSize+=readSize;
								if(percentCount==0||((totalSize*100)/updateTotalSize-percentStep)>=percentCount){
									percentCount+=percentStep;
									Message message=Message.obtain();//减少开销???
									message.what=UPDATEPROGRESS;
									Bundle mBundle=new Bundle();
									mBundle.putInt("percentCount", percentCount);
									message.setData(mBundle);
									dealerHandler.sendMessage(message);
								}
							}
							
						}else{
							Message message=Message.obtain();
							message.what=UPDATEWRONG;
							dealerHandler.sendMessage(message);
						}
					}catch(ClientProtocolException e){
						Message message=Message.obtain();
						message.what=UPDATEWRONG;
						dealerHandler.sendMessage(message);
						e.printStackTrace();
					}catch(IOException e){
						Message message=Message.obtain();
						message.what=UPDATEWRONG;
						dealerHandler.sendMessage(message);
						e.printStackTrace();
					}finally{
					try{
						if(is!=null){
							is.close();
						}
						if(fileOutputStream!=null){
							fileOutputStream.close();
						}
						
					}catch(IOException e){
						e.printStackTrace();
					}
					}
				}
			}
			
		}.start();
		
	}	
}

}
