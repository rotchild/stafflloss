package com.cx.staffloss;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.cx.mcamera.MCameraView;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.utils.MUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TakePhotoActivity extends Activity {
ImageButton takePhotoIB,cancelIB,okIB;
MCameraView mCameraView;
ImageView tempImageView;
static int scrW;
static int scrH;
private double latitude=0.0;//纬度
private double longitude=0.0;//经度
private String latitudeStr="";
private String longitudeStr="";
//private String locationStr="";
LocationListener locationListener;
LocationManager locationManager;

public static String SPName="StaffLossSP";
SharedPreferences TakePhotoSP;
ProgressDialog takePhotopd;
String case_idStr="-1";

Camera theCamera;
private final static String TAG="takePhoto";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takephoto_layout);
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		scrW=metrics.widthPixels;
		scrH=metrics.heightPixels;
		Bundle bundle=getIntent().getExtras();
		case_idStr=bundle.getString("case_id");
		
	    initView();

		
	}
public void initView(){
	TakePhotoSP=getSharedPreferences(SPName,0);
	
	mCameraView=(MCameraView)findViewById(R.id.mCamera);
	tempImageView=(ImageView)findViewById(R.id.tempimg_iv);
	takePhotoIB=(ImageButton)findViewById(R.id.takePhoto_ib);
	cancelIB=(ImageButton)findViewById(R.id.cancel_ib);
	okIB=(ImageButton)findViewById(R.id.ok_ib);
	
	
	takePhotoIB.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			try{
				takePhoto(pictureCallback);
				takePhotoIB.setVisibility(View.GONE);
				takePhotoIB.setClickable(false);
				cancelIB.setVisibility(View.VISIBLE);
				okIB.setVisibility(View.VISIBLE);
				cancelIB.setClickable(true);
				okIB.setClickable(true);	
			}catch(Exception e){
				
			}

		}
		
	});
	
	cancelIB.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			destroyTheTempPicture();
			if(theCamera!=null){
				theCamera.startPreview();
				takePhotoIB.setVisibility(View.VISIBLE);
				takePhotoIB.setClickable(true);
				cancelIB.setVisibility(View.GONE);
				okIB.setVisibility(View.GONE);
				cancelIB.setClickable(false);
				okIB.setClickable(false);
			}
		}
		
	});
	
	
	
	okIB.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			tempImageView.setDrawingCacheEnabled(true);
			Bitmap bitmap=Bitmap.createBitmap(tempImageView.getDrawingCache());
			tempImageView.setDrawingCacheEnabled(false);
			String picPath=createFilePath();
			if(picPath.length()>0){
				saveBitmapFile(bitmap,picPath);
				//File imgFile=new File(picPath);
				
				uploadPicture(picPath);
				//Toast.makeText(TakePhotoActivity.this, "you click", Toast.LENGTH_SHORT).show();
			}
		}
		
	});
	
}

public void destroyTheTempPicture(){
	BitmapDrawable drawable=(BitmapDrawable)tempImageView.getDrawable();
	Bitmap bmp=drawable.getBitmap();
	if(null!=bmp&&!bmp.isRecycled()){
		bmp.recycle();
		bmp=null;
		tempImageView.setVisibility(View.GONE);
	}else{
		Toast.makeText(TakePhotoActivity.this, "not clear bmp",Toast.LENGTH_SHORT ).show();
	}
}

public void takePhoto(PictureCallback pictureCallback){
	theCamera=mCameraView.takePhoto(pictureCallback);
}
public final PictureCallback pictureCallback=new PictureCallback(){

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO 自动生成的方法存根
		Bitmap bm=BitmapFactory.decodeByteArray(data, 0, data.length);
		
		Bitmap bmresize=rotatingImageView(90,bm);
		Bitmap mbmresize=bitmapresize(bmresize);
		boolean canLocation=getGPSInfor();
		if(canLocation){
			latitudeStr="纬度："+String.valueOf(latitude);
			longitudeStr="经度："+String.valueOf(longitude);
			//locationStr="经度："+longitudeStr+"纬度："+latitudeStr;
		}else{
			//locationStr="无法获取当前经纬度";
			longitudeStr="无法获取当前经纬度";
			Toast.makeText(TakePhotoActivity.this, "请在设置中开启定位权限", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri=Uri.fromParts("package", getPackageName(), null);
			intent.setData(uri);
			startActivity(intent);
		}
		setTextOnBitmap(mbmresize,longitudeStr,latitudeStr);
		Log.d(TAG, "with:"+bmresize.getWidth()+"/height:"+bmresize.getHeight());
		Log.d(TAG, "mwith:"+mbmresize.getWidth()+"/mheight:"+mbmresize.getHeight());
		Log.d(TAG, "Swith:"+scrW+"/Sheight:"+scrH);
		tempImageView.setImageBitmap(mbmresize);
		tempImageView.setVisibility(View.VISIBLE);
	}
	
};
public static Bitmap rotatingImageView(int angle,Bitmap bitmap){
	Matrix matrix=new Matrix();
	matrix.postRotate(angle);
	Bitmap resizeBitmap=Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(),matrix,true);

	return resizeBitmap;
}

public static Bitmap bitmapresize(Bitmap bitmap){
	int rawW=bitmap.getWidth();
	int rawH=bitmap.getHeight();
	float scaleW=((float)scrW)/rawW;
	float scaleH=((float)scrH)/rawH;
	Log.d(TAG, "Rwith:"+rawW+"/Rheight:"+rawH);
	Matrix matrix=new Matrix();
	matrix.postScale(scaleW, scaleH);
	Log.d(TAG, "scaleW:"+scaleW+"/scaleH:"+scaleH);
	Bitmap mresizeBitmap=Bitmap.createBitmap(bitmap, 0, 0, rawW, rawH, matrix, true);
	return mresizeBitmap;
}

public void setTextOnBitmap(Bitmap bitmap,String longtitude,String latitude){
	Canvas canvasTmp=new Canvas(bitmap);
	canvasTmp.drawColor(Color.TRANSPARENT);
	Paint p=new Paint();
	Typeface font=Typeface.create("宋体", Typeface.BOLD);
	p.setColor(Color.RED);
	p.setTypeface(font);
	p.setTextSize(40);
	canvasTmp.drawBitmap(bitmap, 0, 0, p);
	canvasTmp.drawText(longtitude,20,100,p);
	canvasTmp.drawText(latitude,20,200,p);
	canvasTmp.save(Canvas.ALL_SAVE_FLAG);
	
	canvasTmp.restore();
}

public boolean getGPSInfor(){
	boolean result=false;
	locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	List<String> providers=locationManager.getProviders(true);
	 locationListener=new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			// TODO 自动生成的方法存根
			if(location!=null){
				  Log.e("Map", "Location changed : Lat: "    
	                        + location.getLatitude() + " Lng: "    
	                        + location.getLongitude());   
			}
		}

		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO 自动生成的方法存根
			Toast.makeText(TakePhotoActivity.this, "GPS已开启", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO 自动生成的方法存根
			Toast.makeText(TakePhotoActivity.this, "GPS已关闭", Toast.LENGTH_SHORT).show();
		}
		
	};
for(String provider:providers){
	locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
	Location location=locationManager.getLastKnownLocation(provider);
	if(location!=null){
		latitude=location.getLatitude();
		longitude=location.getLongitude();
		result=true;
		break;
	}
}

	return result;
}

public void uploadPicture(String path){
	takePhotopd=ProgressDialog.show(TakePhotoActivity.this, "上传中", "请稍候...");
	AsyncHttpClient uploadClient=new AsyncHttpClient();
	uploadClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	uploadClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	String dUrl=MHttpParams.IP;
	String mUrl=TakePhotoSP.getString("IP", dUrl);
	String dPort=MHttpParams.DEFAULT_PORT;
	String mPort=TakePhotoSP.getString("Port", dPort);
	String uploadUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.uploadImg;
	//String uploadUrl="http://192.168.1.18:3091/upload";//for test
	RequestParams params=new RequestParams();
	int id=TakePhotoSP.getInt("id", -1);
	int case_id=MyhttpStorage.case_id;

	params.put("case_id", String.valueOf(case_id));
	Log.d(TAG, "case_id"+case_id);
	params.put("user_id", String.valueOf(id));
	File imgFile=new File(path);
	
	if(imgFile.exists()){
		try {
			params.put("files", imgFile);
			uploadClient.post(uploadUrl, params, new JsonHttpResponseHandler(){

				@Override
				public void onSuccess(int statusCode, JSONObject response) {
					// TODO 自动生成的方法存根
					super.onSuccess(statusCode, response);
					if(takePhotopd!=null&&takePhotopd.isShowing()){
						takePhotopd.dismiss();
						takePhotopd=null;
					}
					Log.d(TAG, "onSuccess");
					boolean success=false;
					try{
						success=response.getBoolean("success");
						if(success){
							//TakePhotoSP
							//case_idStr
							String mapObjStr=TakePhotoSP.getString("mMap", "");
							Map<String,String> stateMap=MUtil.getMap(TakePhotoActivity.this, mapObjStr);
							if(stateMap.containsKey(case_idStr)){
								stateMap.put(case_idStr, "1");
							}
							MUtil.saveMap(TakePhotoActivity.this,stateMap);
							
							SharedPreferences.Editor takePhotoEditor=TakePhotoSP.edit();
							takePhotoEditor.putInt("photoState", 1);
							takePhotoEditor.commit();
							
							Toast.makeText(TakePhotoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
							TakePhotoActivity.this.finish();
						}else{
							destroyTheTempPicture();
							if(theCamera!=null){
								theCamera.startPreview();
								takePhotoIB.setVisibility(View.VISIBLE);
								takePhotoIB.setClickable(true);
								cancelIB.setVisibility(View.GONE);
								okIB.setVisibility(View.GONE);
								cancelIB.setClickable(false);
								okIB.setClickable(false);
							}
							Toast.makeText(TakePhotoActivity.this, "上传失败，请重新拍", Toast.LENGTH_SHORT).show();
						}
						
					}catch(JSONException e){
						e.printStackTrace();
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
					Log.d(TAG, "onFailure");
					super.onFailure(e, errorResponse);
					if(takePhotopd!=null&&takePhotopd.isShowing()){
						takePhotopd.dismiss();
						takePhotopd=null;
					}
				}
				
			});
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
}

public String createFilePath(){
	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		String path=Environment.getExternalStorageDirectory()+"/01.jpg";
		return path;
	}else{
		Toast.makeText(TakePhotoActivity.this, "sd卡未挂载", Toast.LENGTH_SHORT).show();
		return "";
	}
}

public void saveBitmapFile(Bitmap bitmap,String path){
	File file=new File(path);
	try{
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.flush();
		bos.close();
	}catch(IOException e){
		e.printStackTrace();
	}
}

}
