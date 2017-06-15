package com.cx.applytask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.MyTaskObject;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.staffloss.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TuidingApplyTask extends Activity {
	MyTaskObject selectTaskObject;
	Button ok_btn,cancel_btn;
	public static String SPName="StaffLossSP";
	SharedPreferences TuidingTaskSP;
	TextView tuiding_reportNo_tv,tuiding_carNo_tv,tuiding_carType_tv,tuiding_isTheCar_tv,tuiding_theCarNo_tv,tuiding_reportDeport_tv,
	tuiding_deportConnector_tv,tuiding_deportConnectorphone_tv,tuiding_reporterName_tv,tuiding_theReporterPhone_tv,tuiding_theAboutMoney_tv;
	ProgressDialog tudingApplyPd;
	String caseidStr="";
	ProgressDialog queryPd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuidingapply_layout);
		selectTaskObject=(MyTaskObject) this.getIntent().getSerializableExtra("tuiselectTaskObject");
		initView();
	}
	public void initView(){
		TuidingTaskSP=getSharedPreferences(SPName,0);
		
		ok_btn=(Button)findViewById(R.id.tuiding_yes_btn);
		cancel_btn=(Button)findViewById(R.id.tuiding_no_btn);
		
		ok_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				queryStateByHttp();
				//applyTuidingByHttp();
			}
			
		});
		
		cancel_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				TuidingApplyTask.this.finish();
			}
			
		});
		
		
		tuiding_reportNo_tv=(TextView)findViewById(R.id.tuiding_reportNo_tv);	
		tuiding_carNo_tv=(TextView)findViewById(R.id.tuiding_carNo_tv);
		tuiding_carType_tv=(TextView)findViewById(R.id.tuiding_carType_tv);
		tuiding_isTheCar_tv=(TextView)findViewById(R.id.tuiding_isTheCar_tv);
		tuiding_theCarNo_tv=(TextView)findViewById(R.id.tuiding_theCarNo_tv);
		tuiding_reportDeport_tv=(TextView)findViewById(R.id.tuiding_reportDeport_tv);
		tuiding_deportConnector_tv=(TextView)findViewById(R.id.tuiding_deportConnector_tv);
		tuiding_deportConnectorphone_tv=(TextView)findViewById(R.id.tuiding_deportConnectorphone_tv);
		tuiding_reporterName_tv=(TextView)findViewById(R.id.tuiding_reporterName_tv);
		tuiding_theReporterPhone_tv=(TextView)findViewById(R.id.tuiding_theReporterPhone_tv);
		tuiding_theAboutMoney_tv=(TextView)findViewById(R.id.tuiding_theAboutMoney_tv);
		
		String reportNostr=selectTaskObject.getCase_No();
		String carNostr=selectTaskObject.getCar_No();
		String carTypestr=selectTaskObject.getBrand_name();
		String isTheCarstr=selectTaskObject.getIs_target();
		String theCarNostr=selectTaskObject.getTarget_No();
		String reportDeportstr=selectTaskObject.getParters_name();
		String deportConnectorstr=selectTaskObject.getParter_manager();
		String deportConnectorphonestr=selectTaskObject.getParter_mobile();
		String reporterNamestr=selectTaskObject.getDelivery_name();
		String theReporterPhonestr=selectTaskObject.getDelivery_mobile();
		String theAboutMoneystr=selectTaskObject.getLoss_price();
		caseidStr=selectTaskObject.getCase_id();
		
		
		tuiding_reportNo_tv.setText(reportNostr);
		tuiding_carNo_tv.setText(carNostr);
		tuiding_carType_tv.setText(carTypestr);
		tuiding_isTheCar_tv.setText(isTheCarstr);
		tuiding_theCarNo_tv.setText(theCarNostr);
		tuiding_reportDeport_tv.setText(reportDeportstr);
		tuiding_deportConnector_tv.setText(deportConnectorstr);
		tuiding_deportConnectorphone_tv.setText(deportConnectorphonestr);
		tuiding_reporterName_tv.setText(reporterNamestr);
		tuiding_theReporterPhone_tv.setText(theReporterPhonestr);
		tuiding_theAboutMoney_tv.setText(theAboutMoneystr);
		
	}
	
	public void applyTuidingByHttp(){
		tudingApplyPd=ProgressDialog.show(TuidingApplyTask.this, "请稍候", "提交中...");
		AsyncHttpClient applyTuidingTaskHttpClient=new AsyncHttpClient();
		applyTuidingTaskHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		applyTuidingTaskHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=TuidingTaskSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=TuidingTaskSP.getString("Port", dPort);
		String applyTuidingUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.applyTuidingUrl;
		RequestParams requestparams=new RequestParams();
		//int user_id=MyhttpStorage.id;
		int user_id=TuidingTaskSP.getInt("id", -1);
		//int case_id=MyhttpStorage.case_id;
		requestparams.put("id", String.valueOf(user_id));
		requestparams.put("case_id", caseidStr);
		applyTuidingTaskHttpClient.post(applyTuidingUrl, requestparams, new JsonHttpResponseHandler(){

			
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO 自动生成的方法存根
				super.onSuccess(statusCode, response);
				if(tudingApplyPd!=null&&tudingApplyPd.isShowing()){
					tudingApplyPd.dismiss();
					tudingApplyPd=null;
				}
				boolean success=false;
				try{
					success=response.getBoolean("success");
					if(success){
						Toast.makeText(TuidingApplyTask.this, "申请成功", Toast.LENGTH_SHORT).show();
						TuidingApplyTask.this.finish();
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
				super.onFailure(e, errorResponse);
				if(tudingApplyPd!=null&&tudingApplyPd.isShowing()){
					tudingApplyPd.dismiss();
					tudingApplyPd=null;
				}
			}
			
		});
	}
	
	public void queryStateByHttp(){
		queryPd=ProgressDialog.show(TuidingApplyTask.this, "加载中", "请稍后");
		AsyncHttpClient queryClient=new AsyncHttpClient();
		queryClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		queryClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=TuidingTaskSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=TuidingTaskSP.getString("Port", dPort);
		String queryUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.queryUrl;
		RequestParams params=new RequestParams();
		//int case_id=caseidStr;
		params.put("case_id", caseidStr);
		queryClient.put(queryUrl, params,  new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO 自动生成的方法存根
				super.onSuccess(statusCode, response);
				if(queryPd!=null&&queryPd.isShowing()){
					queryPd.dismiss();
				}
				boolean success=false;
				try{
					success=response.getBoolean("success");
					if(success){
						JSONArray data=response.getJSONArray("data");
						JSONObject mData=(JSONObject) data.get(0);
						//int repair_state=mData.getInt("repair_state");
						//int audit_state=mData.getInt("audit_state");
						int loss_state=mData.getInt("loss_state");
						if(loss_state==0){
							Toast.makeText(TuidingApplyTask.this, "推定任务尚未完成", Toast.LENGTH_SHORT).show();
						}else{
							applyTuidingByHttp();
						}
					}else{
						Toast.makeText(TuidingApplyTask.this, "连接异常", Toast.LENGTH_SHORT).show();
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, JSONArray errorResponse) {
				// TODO 自动生成的方法存根
				super.onFailure(e, errorResponse);
			}
			
			@Override
			public void onFailure(Throwable e, String errorResponse) {
				// TODO 自动生成的方法存根
				super.onFailure(e, errorResponse);
				Log.d("dealingTask", "query on Failure");
			}
			
		});
	}
	
}
