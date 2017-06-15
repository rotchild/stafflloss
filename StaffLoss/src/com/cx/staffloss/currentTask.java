package com.cx.staffloss;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.ipset.UpdateCheck;
import com.cx.myobject.DealingTaskRecordList;
import com.cx.myobject.MyUndealList;
import com.cx.myobject.MyUpdateObject;
import com.cx.myobject.TaskRecordList;
import com.cx.netset.MHttpParams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.XGPushManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class currentTask extends FragmentActivity {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mDatas;
	private CurrentTaskFragment currentTaskFragment;
	private DealingTaskFragment dealingTaskFragment;
	private UndealTaskFragment undealTaskFragment;
	private FinishedTaskFragment finishedTaskFragment;
	private TextView currentTaskTv,dealingTaskTv,undealTaskTv,finishedTaskTv;
	private ImageView currentImg,dealingImg,undealImg,finishImg;
	private ImageView currentTaskMenuIv;

	
	TextView currentTitle;
	private LinearLayout currentTaskLinear,dealingTaskLinear,undealingTaskLinear,finishTaskLinear;
	
	
	private final String TAG="currentTask";
	public static final int UPDATE_RECORDLIST=1;
	public static final int ORINALTYPE=1;
	public static final int REFRESHTYPE=2;
	public SharedPreferences currentTaskSP;
	static int screenWidth;
	static int screenHeight;
	public static String SPName="StaffLossSP";
	private AlertDialog mAlertDialog;
	private ProgressDialog progressbar;
	private String fileName;
	private final int percentStep=5;
	private final int SHOWUPDATEDIAG=1;
	private final int UPDATEPROGRESS=2;
	private final int UPDATEWRONG=3;
	boolean stopThread=false;
	//ProgressDialog pd;
	
	public Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			int msgId=msg.what;
			switch(msgId){
			case UPDATE_RECORDLIST:
				//currentTaskFragment.taskRecordAdapter.notifyDataSetChanged();
			//	Toast.makeText(currentTask.this, "it has changed", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};
	Handler dealerHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			int msgId=msg.what;
			switch(msgId){
			case SHOWUPDATEDIAG://需要更新
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
			case UPDATEWRONG:
				Toast.makeText(currentTask.this, "下载错误", Toast.LENGTH_SHORT).show();;
				if(progressbar!=null){
					if(progressbar.isShowing()){
						progressbar.dismiss();
						progressbar=null;
					}
				}
				Log.d("ChoosePage", "UPDATEWRONG");
				try{
					File apkFile=new File(Environment.getExternalStorageDirectory(),fileName);
					if(apkFile.exists()){
						apkFile.delete();
					}
				}catch(Exception e){
					e.printStackTrace();
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.current_task_layout);
		WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
		screenWidth=wm.getDefaultDisplay().getWidth();
		screenHeight=wm.getDefaultDisplay().getHeight();
		currentTaskSP=getSharedPreferences(SPName,0);//在checkupdate之前
		checkUpdateByHttp();
		initView();
	}
public void initView(){
	currentTitle=(TextView)findViewById(R.id.current_title);
	
	
	currentTaskMenuIv=(ImageView)findViewById(R.id.currentTask_menuId);
	currentTaskMenuIv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			View popmenuView=currentTask.this.getLayoutInflater().inflate(R.layout.menupopup_layout, null);
			int menuWindowW=screenWidth/3;
			int menuWindowH=menuWindowW*2/3;
			final PopupWindow popupwindow=new PopupWindow(popmenuView,menuWindowW,menuWindowH);
			popupwindow.setAnimationStyle(R.style.popupwindowanim);
			popupwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00F8F8F8")));
			popupwindow.setFocusable(true);
			popupwindow.setOutsideTouchable(true);
			popupwindow.update();
			popupwindow.showAsDropDown(currentTaskMenuIv, -2, -10);
			LinearLayout versionSetLayout=(LinearLayout)popmenuView.findViewById(R.id.ipset_linear_layout);
			LinearLayout logoutLayout=(LinearLayout)popmenuView.findViewById(R.id.logout_linear_layout);
			versionSetLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent toUpdate=new Intent(currentTask.this,UpdateCheck.class);
					startActivity(toUpdate);
					if(popupwindow!=null&&popupwindow.isShowing()){
						popupwindow.dismiss();
					}
				}
				
			});
			logoutLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					SharedPreferences.Editor currentTaskEditor=currentTaskSP.edit();
					currentTaskEditor.putInt("state", 0);
					boolean result=currentTaskEditor.commit();
					XGPushManager.unregisterPush(currentTask.this);
					currentTask.this.finish();
					
					System.exit(0);
				}
				
			});
		}
		
	});
	
	
	currentImg=(ImageView)findViewById(R.id.currentTaskImg);
	dealingImg=(ImageView)findViewById(R.id.dealingtaskimg);
	undealImg=(ImageView)findViewById(R.id.undealtaskimg);
	finishImg=(ImageView)findViewById(R.id.finishtaskimg);
	
	
	currentTaskTv=(TextView)findViewById(R.id.currentTaskTV);
	dealingTaskTv=(TextView)findViewById(R.id.dealingTaskTV);
	undealTaskTv=(TextView)findViewById(R.id.undealTaskTV);
	finishedTaskTv=(TextView)findViewById(R.id.finishedTaskTV);
	
	mViewPager=(ViewPager)findViewById(R.id.id_viewpager);
	mDatas=new ArrayList<Fragment>();
	currentTaskFragment=new CurrentTaskFragment();
	dealingTaskFragment=new DealingTaskFragment();
	undealTaskFragment=new UndealTaskFragment();
	
	finishedTaskFragment=new FinishedTaskFragment();
	mDatas.add(currentTaskFragment);
	mDatas.add(dealingTaskFragment);
	mDatas.add(undealTaskFragment);
	mDatas.add(finishedTaskFragment);
	
	
	mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()){

		@Override
		public Fragment getItem(int arg0) {
			return mDatas.get(arg0);
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}
		
	};
	mViewPager.setAdapter(mAdapter);
	
	currentTaskLinear=(LinearLayout)findViewById(R.id.currentTaskLinear);
	currentTaskLinear.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			mViewPager.setCurrentItem(0);
		}
		
	});
	
	dealingTaskLinear=(LinearLayout)findViewById(R.id.dealingTaskLinear);
	dealingTaskLinear.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			mViewPager.setCurrentItem(1);
		}
		
	});
	
	undealingTaskLinear=(LinearLayout)findViewById(R.id.undealingTaskLinear);
	undealingTaskLinear.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			mViewPager.setCurrentItem(2);
		}
		
	});
	
	finishTaskLinear=(LinearLayout)findViewById(R.id.finishTaskLinear);
	finishTaskLinear.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			mViewPager.setCurrentItem(3);
		}
		
	});
	
	mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onPageSelected(int position) {
			// TODO 自动生成的方法存根
			resetTextView();
			Log.d(TAG, "position:"+position);
		switch(position){
		
		case 0:
			Log.d(TAG, "case0");
			currentTitle.setText("当前任务");
		/*	if(MyUndealList.mList!=null&&MyUndealList.mList.size()>0){
				MyUndealList.mList.clear();
			}
			if(TaskRecordList.mTaskList!=null&&TaskRecordList.mTaskList.size()>0){
				TaskRecordList.mTaskList.clear();
			}*/
			
			//currentTaskFragment.getCurrentTaskByHttp();
			currentTaskTv.setTextColor(Color.parseColor("#FF6666"));
			currentImg.setImageResource(R.drawable.currenttasko);
			break;
		case 1:
			Log.d(TAG, "case1");
			currentTitle.setText("定损中");
			if(DealingTaskRecordList.mDealingTaskList!=null&&DealingTaskRecordList.mDealingTaskList.size()>0){
				DealingTaskRecordList.mDealingTaskList.clear();	
			}
			
			dealingTaskFragment.getDealingTaskbyHttp(ORINALTYPE);
			dealingTaskTv.setTextColor(Color.parseColor("#FF6666"));
			dealingImg.setImageResource(R.drawable.dealingtasko);
			break;
		case 2:
		//	if(MyUndealList.mList)
			Log.d(TAG, "case2");
			currentTitle.setText("未处理");
			undealTaskTv.setTextColor(Color.parseColor("#FF6666"));
			undealImg.setImageResource(R.drawable.undealingtasko);
			break;
		case 3:
			Log.d(TAG, "case3");
			currentTitle.setText("已完成");
			finishedTaskFragment.getfinishTasklistByHttp(ORINALTYPE);
			finishedTaskTv.setTextColor(Color.parseColor("#FF6666"));
			finishImg.setImageResource(R.drawable.finishtasko);
			break;
		default:
			break;
		}
		}
		
	});
	
	//currentTaskFragment.getCurrentTaskByHttp();//进入时调用一次，无法使用：currentFragmentSP未初始化
}




@Override
protected void onNewIntent(Intent intent) {
	// TODO 自动生成的方法存根
	super.onNewIntent(intent);
	if(mViewPager.getCurrentItem()==0){
		currentTaskFragment.limit=currentTaskFragment.limit+5;//刷新
		currentTaskFragment.getCurrentTaskByHttp(REFRESHTYPE);
	}else{
		mViewPager.setCurrentItem(0);
	}
	
	//currentTaskFragment.getCurrentTaskByHttp(REFRESHTYPE);
	//dealingTaskFragment.getDealingTaskbyHttp(REFRESHTYPE);
	
}
public void checkUpdateByHttp(){
	AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
	asyncHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	asyncHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	//String mUrl="192.168.1.63";
	//String mPort="3003";
    String mUrl=currentTaskSP.getString("IP", MHttpParams.IP);
    String mPort=currentTaskSP.getString("Port", MHttpParams.DEFAULT_PORT);
	//String getCurrentUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.CurrentTaskUrl;
	String checkUpdateUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.checkUpdateUrl;
	Log.d(TAG, "checkUpdateUrl"+checkUpdateUrl);
	RequestParams params=new RequestParams();
	params.put("SystemOS", MHttpParams.SystemOS);
	params.put("ProjectName",MHttpParams.ProjectName);
	asyncHttpClient.post(checkUpdateUrl, params, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(JSONObject response) {
			// TODO 自动生成的方法存根
			super.onSuccess(response);
			boolean success=false;
			try{
				success=response.getBoolean("success");
				if(success){
					Log.d(TAG, "success is true");
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
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO 自动生成的方法存根
			super.onFailure(e, errorResponse);
			Toast.makeText(currentTask.this, "自动更新检查失败", Toast.LENGTH_SHORT).show();
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
	mAlertDialog=new AlertDialog.Builder(currentTask.this).create();
	mAlertDialog.setCancelable(false);
	LayoutInflater myInflater=LayoutInflater.from(currentTask.this);
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

public void downLoadAPK(final String url){
	if(mAlertDialog!=null){
		if(mAlertDialog.isShowing()){
			mAlertDialog.dismiss();
		}
		mAlertDialog=null;
	}
	if(!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))){
		Toast.makeText(currentTask.this, "SD卡不可用", Toast.LENGTH_SHORT).show();
	}else{//原生http请求...
		progressbar=new ProgressDialog(currentTask.this);
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

public void inStallAPK(){
	Intent intent=new Intent(Intent.ACTION_VIEW);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	File apkFile=new File(Environment.getExternalStorageDirectory(),fileName);
	if(apkFile.length()>0){
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
	}else{
		Toast.makeText(currentTask.this, "apk文件为0B", Toast.LENGTH_SHORT).show();
	}
	
	startActivity(intent);
	android.os.Process.killProcess(android.os.Process.myPid());
}

public void resetTextView(){

	currentTaskTv.setTextColor(Color.parseColor("#949494"));
	currentImg.setImageResource(R.drawable.currentaskg);
	
	dealingTaskTv.setTextColor(Color.parseColor("#949494"));
	dealingImg.setImageResource(R.drawable.dealingtaskg);
	
	undealTaskTv.setTextColor(Color.parseColor("#949494"));
	undealImg.setImageResource(R.drawable.undealtaskg);
	
	finishedTaskTv.setTextColor(Color.parseColor("#949494"));
	finishImg.setImageResource(R.drawable.finishtaskg);
}
}
