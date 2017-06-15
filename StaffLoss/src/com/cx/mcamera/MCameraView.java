package com.cx.mcamera;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MCameraView extends SurfaceView {
	private Camera mCamera;
	private static final String TAG="MCameraView";
	
	public MCameraView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		getHolder().addCallback(callback);
		try{
			openCamera();	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public MCameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		getHolder().addCallback(callback);
		try{
			openCamera();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private SurfaceHolder.Callback callback=new SurfaceHolder.Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO 自动生成的方法存根
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO 自动生成的方法存根
			try {
				if(mCamera==null){
					openCamera();
				}
				setCameraParameters();
				
				mCamera.setPreviewDisplay(getHolder());
				mCamera.setDisplayOrientation(90);
				mCamera.startPreview();
			} catch (Exception e) {
				Toast.makeText(getContext(), "打开相机失败,请开启拍照权限", Toast.LENGTH_SHORT).show();
				//Log.e(TAG,e.getMessage());
			}
			//mCamera.startPreview();	
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO 自动生成的方法存根
			
		}
	};
	
	public void setCameraParameters(){
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		List<Camera.Size> sizeList=parameters.getSupportedPreviewSizes();
		if(sizeList.size()>0){
			Size cameraSize=sizeList.get(0);
			parameters.setPreviewSize(cameraSize.width,cameraSize.height);
		}
		
		List<Camera.Size>  picturesizeList=parameters.getSupportedPictureSizes();
		if(picturesizeList.size()>0){
			Size cameraPictureSize=picturesizeList.get(0);
			for(Size size:picturesizeList){
				if(size.width*size.height<100*10000){
					cameraPictureSize=size;
					break;
				}
			}
			parameters.setPictureSize(cameraPictureSize.width, cameraPictureSize.height);
			Log.d("takePhoto", "parameters"+"width:"+cameraPictureSize.width+"/height"+cameraPictureSize.height);
		}
	}
	
	private boolean openCamera(){
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		try {
			mCamera=Camera.open();
		} catch (Exception e) {
			mCamera =null;
			return false;
		}
	return true;
	}
	public Camera takePhoto(PictureCallback pictureCallback){
		mCamera.takePicture(null, null, pictureCallback);
		return mCamera;
	}
}
