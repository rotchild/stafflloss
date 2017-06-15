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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JiHeApplyTask extends Activity {
	Button jihe_yes_btn,jihe_no_btn;
	
	TextView jihe_reportNo_tv,jihe_carNo_tv,jihe_carType_tv,jihe_isTheCar_tv,jihe_theCarNo_tv,jihe_reportDeport_tv,
	jihe_deportConnector_tv,jihe_deportConnectorphone_tv,jihe_lossStaff_tv,jihe_lossStaffNumber_tv,jihe_theAboutMoney_tv;
	
	EditText jihe_beizhu_et;
	MyTaskObject selectTaskObject;
	public static final String TAG="jihe";
	public static String SPName="StaffLossSP";
	SharedPreferences JiHeTaskSP;
	ProgressDialog jiheapplyPd;
	String selectCase_id="";
	ProgressDialog queryPd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jiheapply_layout);
		selectTaskObject=(MyTaskObject) this.getIntent().getSerializableExtra("jichooseTaskObject");
		if(selectTaskObject!=null){
			initView();
		}else{
			Log.d(TAG, "selectTaskObject is null");
		}
		
	}
	public void initView(){
		
		JiHeTaskSP=getSharedPreferences(SPName,0);
		jihe_yes_btn=(Button)findViewById(R.id.jihe_yes_btn);
		jihe_no_btn=(Button)findViewById(R.id.jihe_no_btn);
		
		jihe_yes_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//applyJiheByHttp();
				queryStateByHttp();
			}
			
		});
		jihe_no_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				JiHeApplyTask.this.finish();
			}
			
		});
		
		
		jihe_reportNo_tv=(TextView)findViewById(R.id.jihe_reportNo_tv);
		jihe_carNo_tv=(TextView)findViewById(R.id.jihe_carNo_tv);
		jihe_carType_tv=(TextView)findViewById(R.id.jihe_carType_tv);
		jihe_isTheCar_tv=(TextView)findViewById(R.id.jihe_isTheCar_tv);
		jihe_theCarNo_tv=(TextView)findViewById(R.id.jihe_theCarNo_tv);
		jihe_reportDeport_tv=(TextView)findViewById(R.id.jihe_reportDeport_tv);
		jihe_deportConnector_tv=(TextView)findViewById(R.id.jihe_deportConnector_tv);
		jihe_deportConnectorphone_tv=(TextView)findViewById(R.id.jihe_deportConnectorphone_tv);
		jihe_lossStaff_tv=(TextView)findViewById(R.id.jihe_lossStaff_tv);
		jihe_lossStaffNumber_tv=(TextView)findViewById(R.id.jihe_lossStaffNumber_tv);
		jihe_theAboutMoney_tv=(TextView)findViewById(R.id.jihe_theAboutMoney_tv);
		jihe_reportNo_tv=(TextView)findViewById(R.id.jihe_reportNo_tv);
		
		jihe_beizhu_et=(EditText)findViewById(R.id.jihe_beizhu_et);
		
		String reportNostr=selectTaskObject.getCase_No();
		String carNostr=selectTaskObject.getCar_No();
		String carTypestr=selectTaskObject.getBrand_name();
		String isTheCarstr=selectTaskObject.getIs_target();
		String theCarNostr=selectTaskObject.getTarget_No();
		String reportDeportstr=selectTaskObject.getParters_name();
		String deportConnectorstr=selectTaskObject.getParter_manager();
		String deportConnectorphonestr=selectTaskObject.getParter_mobile();
		
		 selectCase_id=selectTaskObject.getCase_id();
		//String dingsunerName=MyhttpStorage.dingsunerName;
		String dingsunerName=JiHeTaskSP.getString("dingsunerName", "");
	//	String dingsunerTel=MyhttpStorage.dingsunerTel;
		String dingsunerTel=JiHeTaskSP.getString("dingsunerTel", "");
		String theAboutMoneystr=selectTaskObject.getLoss_price();
		
		jihe_reportNo_tv.setText(reportNostr);
		jihe_carNo_tv.setText(carNostr);
		jihe_carType_tv.setText(carTypestr);
		jihe_isTheCar_tv.setText(isTheCarstr);
		jihe_theCarNo_tv.setText(theCarNostr);
		jihe_reportDeport_tv.setText(reportDeportstr);
		jihe_deportConnector_tv.setText(deportConnectorstr);
		jihe_deportConnectorphone_tv.setText(deportConnectorphonestr);
		jihe_lossStaff_tv.setText(dingsunerName);
		jihe_lossStaffNumber_tv.setText(dingsunerTel);
		jihe_theAboutMoney_tv.setText(theAboutMoneystr);
		
	}
	
	public void applyJiheByHttp(){
		jiheapplyPd=ProgressDialog.show(JiHeApplyTask.this, "请稍候", "提交中...");
		AsyncHttpClient applyJiheTaskHttpClient=new AsyncHttpClient();
		applyJiheTaskHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		applyJiheTaskHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=JiHeTaskSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=JiHeTaskSP .getString("Port", dPort);
		String applyJiheUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.applyJiheUrl;
		RequestParams requestparams=new RequestParams();
		//int id=MyhttpStorage.id;
		int id=JiHeTaskSP.getInt("id", -1);
		String user_id=MyhttpStorage.jiherenId;
		//int case_id=MyhttpStorage.case_id;
		
		String audit_remark=jihe_beizhu_et.getText().toString();//稽核备注
		requestparams.put("id", String.valueOf(id));
		requestparams.put("user_id",user_id);
		requestparams.put("case_id",selectCase_id);
		requestparams.put("audit_remark", audit_remark);
		applyJiheTaskHttpClient.post(applyJiheUrl, requestparams, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO 自动生成的方法存根
				if(jiheapplyPd!=null&&jiheapplyPd.isShowing()){
					jiheapplyPd.dismiss();
					jiheapplyPd=null;
				}
				boolean success=false;
				try{
					success=response.getBoolean("success");
					if(success){
						Toast.makeText(JiHeApplyTask.this, "申请成功", Toast.LENGTH_SHORT).show();
						if(ChooseJiHeActivity.chooseJiheActiviy!=null){
							ChooseJiHeActivity.chooseJiheActiviy.finish();
						}
						JiHeApplyTask.this.finish();
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
				super.onSuccess(statusCode, response);
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
				if(jiheapplyPd!=null&&jiheapplyPd.isShowing()){
					jiheapplyPd.dismiss();
					jiheapplyPd=null;
				}
			}
			
		});
	}
	
	public void queryStateByHttp(){
		queryPd=ProgressDialog.show(JiHeApplyTask.this, "加载中", "请稍后");
		AsyncHttpClient queryClient=new AsyncHttpClient();
		queryClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		queryClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=JiHeTaskSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=JiHeTaskSP.getString("Port", dPort);
		String queryUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.queryUrl;
		RequestParams params=new RequestParams();
		//int case_id=caseidStr;
		params.put("case_id", selectCase_id);
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
						int audit_state=mData.getInt("audit_state");
						//int loss_state=mData.getInt("loss_state");*/
						if(audit_state==0){
							Toast.makeText(JiHeApplyTask.this, "稽核任务尚未完成", Toast.LENGTH_SHORT).show();
						}else{
							applyJiheByHttp();
						}
					}else{
						Toast.makeText(JiHeApplyTask.this, "连接异常", Toast.LENGTH_SHORT).show();
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
