package com.cx.staffloss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.MyTaskObject;
import com.cx.myobject.MyUndealList;
import com.cx.myobject.TaskRecordList;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyReceiveParams;
import com.cx.netset.MyhttpStorage;
import com.cx.utils.MRegex;
import com.cx.utils.MUtil;
import com.cx.utils.MyCustomerDialog;
import com.cx.utils.MyCustomerDialog.Builder;
import com.cx.utils.MyReportNoInputDialog;
import com.cx.utils.MySeCustomeDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CurrentTaskFragment extends Fragment {
//List<MyTaskObject> taskRecorderList;
PullToRefreshListView currentTasklv;
TaskRecordAdapter taskRecordAdapter;
Timer timer;
TimerTask timerTask;
int start=0;
int limit=5;
public static final String TAG="currentTaskFragment";
public static final int UPDATE_RECORDLIST=1;
public static final int UPDATE_RECORDLIST2=2;

private static int REFRESHTYPE=2;//刷新调用,lv移动到底
private static int ORINALTYPE=1;//请求调用,lv不移动

public static final long PERIODTTIME=5*1000;
private int count=0;
private int wcount=0;

public static String SPName="StaffLossSP";
SharedPreferences currentFragmentSP;

ProgressDialog pd,currentTaskpd;

boolean isVisible=false;//是否可见
boolean hasPost=false;//frag3到frag1,先调用了setVisible,后调用onCreate,调用了两次
Handler mHandler;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "currentTask onActivityCreate");
		currentFragmentSP=getActivity().getSharedPreferences(SPName,0);
		//FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		
		//getActivity()
		Log.d("tag","onActivityCreated");
	/*	MyTaskObject mTaskObject=new MyTaskObject("201702121111","鄂A SQ880","奥迪","是","鄂A SQ880"
				,"是","1","拆检点test","cjdren","123456789","sxren",
				"12354566","1000","1490691230575","1","1","1","1");*/
		
		if(TaskRecordList.mTaskList==null){
			TaskRecordList.mTaskList=new ArrayList<MyTaskObject>();
		//	TaskRecordList.mTaskList.add(mTaskObject);
		}
		

		
		currentTasklv=(PullToRefreshListView)getView().findViewById(R.id.lv_current);
		currentTasklv.setMode(Mode.BOTH);
		taskRecordAdapter=new TaskRecordAdapter(this.getActivity(),R.layout.task_item_layout,TaskRecordList.mTaskList);

		currentTasklv.setAdapter(taskRecordAdapter);
		
		if(isVisible){
			if(hasPost){
				
			}else{
				Log.d(TAG, "getCurrentTaskByHttpV");
				TaskRecordList.mTaskList.clear();
				MyUndealList.mList.clear();
				getCurrentTaskByHttp(ORINALTYPE);
				hasPost=false;	
			}

		}
		
		
		currentTasklv.setOnRefreshListener(new OnRefreshListener<ListView>(){

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO 自动生成的方法存根
				limit=limit+5;
				TaskRecordList.mTaskList.clear();
				taskRecordAdapter.notifyDataSetChanged();
				getCurrentTaskByHttp(REFRESHTYPE);
				
			}
			
		});
		
		currentTasklv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				Log.d("tag", "position"+position);
				MyTaskObject selectObject=TaskRecordList.mTaskList.get(position-1);
				String objectCase=selectObject.getCase_No().trim();//去空
				if(objectCase!=null&&objectCase.length()>0){
					showCaseNoDialog(position-1,objectCase);
				}else{
					showCaseNoDialog(position-1,"");
				}
				
				
			}
			
		});
	}

	
	

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO 自动生成的方法存根
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			isVisible=true;
			Log.d(TAG, "currentTask is visible");
			if(currentFragmentSP!=null){
				Log.d(TAG, "getCurrentTaskByHttpS");
				TaskRecordList.mTaskList.clear();
				MyUndealList.mList.clear();
				getCurrentTaskByHttp(ORINALTYPE);
				hasPost=true;
			}
		//	getCurrentTaskByHttp();
		}else{
			isVisible=false;
			Log.d(TAG, "currentTask is unvisible");
		}
	}




	@Override
	public void onAttach(Context context) {
		// TODO 自动生成的方法存根
		super.onAttach(context);
	}




	public void showCaseNoDialog(final int position,String caseNoStr){
		final MyReportNoInputDialog myDialog=new MyReportNoInputDialog(this.getContext(),R.style.Dialog);
		myDialog.setReportCustomDialog();
		//myDialog.setItemName("");//去掉报案号item
		final EditText editText=(EditText) myDialog.getEditText();
		if(caseNoStr!=null&&caseNoStr.length()>0){
			editText.setText(caseNoStr);
		}else{
			editText.setText("");
		}
		myDialog.setOnPositiveListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String caseNoStr=editText.getText().toString();
				//是否为正确报案号
				if(MRegex.isRightCaseNo(caseNoStr)){
					updateCaseNo( myDialog,position,caseNoStr);
				}else{
					Toast.makeText(getActivity(), "报案号格式错误", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
		myDialog.setOnNegativeListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(myDialog!=null&&myDialog.isShowing()){
					myDialog.dismiss();
				}
			}
			
		});
		myDialog.show();
	}

	
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		if(activity instanceof currentTask){
			currentTask mCurrentTask=(currentTask)activity;
			mHandler=mCurrentTask.mHandler;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}
	
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.current_fragment_layout, container, false);
	}

	@Override
	public void onDestroy() {
		Log.d("tag","onDestroy");
		// TODO 自动生成的方法存根
		if(TaskRecordList.mTaskList!=null){
			TaskRecordList.mTaskList=null;
		}
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		if(timerTask!=null){
			timerTask.cancel();
			timerTask=null;
		}
		
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		Log.d("tag","onPause");
		super.onPause();
	}

	public void getCurrentTaskByHttp(final int type){
		currentTaskpd=ProgressDialog.show(getActivity(), "加载中", "请稍后...");
		AsyncHttpClient getCurrentTaskHttpClient=new AsyncHttpClient();
		getCurrentTaskHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		getCurrentTaskHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=currentFragmentSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=currentFragmentSP.getString("Port", dPort);
		String getCurrentUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.CurrentTaskUrl;
		RequestParams requestparams=new RequestParams();
		//int id=MyhttpStorage.id;
		int id=currentFragmentSP.getInt("id", -1);
		requestparams.put("start", String.valueOf(start));
		requestparams.put("limit", String.valueOf(limit));
		requestparams.put("id", String.valueOf(id));
		getCurrentTaskHttpClient.post(getCurrentUrl, requestparams, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO 自动生成的方法存根
				super.onSuccess(statusCode, response);
				if(currentTaskpd!=null&&currentTaskpd.isShowing()){
					currentTaskpd.dismiss();
				}
				boolean success=false;
				try{
					Log.d(TAG, "getsuccess"+response.getBoolean("success"));
					success=response.getBoolean("success");
					if(success){
						JSONObject data=response.getJSONObject("data");
						if(data.length()>0){
							//MyUndealList.mList.clear();
							//TaskRecordList.mTaskList.clear();
							long server_timeLong=data.getLong("server_time");
							JSONArray case_datas=data.getJSONArray("case_datas");
							/* MyTaskObject(String case_No, String car_No, String brand_name,
										String is_target, String target_No, String isvip, String parter_id,
										String parters_name, String parter_manager, String parter_mobile,
										String delivery_name, String delivery_mobile, String loss_price,
										String yard_time,String case_state, String repair_state, String audit_state,
										String loss_state)*/
							MyUndealList.clear();//得到数据后，先清空undealList
							ArrayList<MyTaskObject> tempList=new ArrayList<MyTaskObject>();
							for(int i=0;i<case_datas.length();i++){
								
								JSONObject mData=(JSONObject) case_datas.get(i);
								String case_id=mData.getString(MyReceiveParams.id);
								String brand_name=mData.getString(MyReceiveParams.brand_name);
								int id=mData.getInt(MyReceiveParams.id);
								MyhttpStorage.case_id=id;
								String idStr=String.valueOf(id);
								
								String case_No=mData.getString(MyReceiveParams.case_no);
								if(case_No==null||case_No.length()<=0){
									case_No=" ";
								}
								
								String car_No=mData.getString(MyReceiveParams.car_no);
								MyhttpStorage.car_no=car_No;
								int is_target=mData.getInt(MyReceiveParams.is_target);
								String is_targetStr=String.valueOf(is_target);
								
								String target_No=mData.getString(MyReceiveParams.target_No);
								//int is_Vip=0;
								int is_Vip=mData.getInt(MyReceiveParams.is_Vip);
								String is_VipStr="";
								if(is_Vip==1){
									is_VipStr="VIP";
								}

								
								
								int parter_id=mData.getInt(MyReceiveParams.parter_id);
								String parter_idStr=String.valueOf(parter_id);
								
								String parter_name=mData.getString(MyReceiveParams.parter_name);
								String parter_manager=mData.getString(MyReceiveParams.parter_manager);
								String parter_mobile=mData.getString(MyReceiveParams.parter_mobile);
								String delivery_name=mData.getString(MyReceiveParams.delivery_name);
								String delivery_mobile=mData.getString(MyReceiveParams.delivery_mobile);
								double loss_price=mData.getDouble(MyReceiveParams.loss_price);
								String loss_priceStr=String.valueOf(loss_price);
								
								Long yard_time=mData.getLong(MyReceiveParams.yard_time);
								String yard_timeStrTimeStamp=String.valueOf(yard_time);
								String yard_timeStr=MUtil.getDetailTime(yard_timeStrTimeStamp);
								//int case_state=0;
								//int repair_state=0;
								//int audit_state=0;
								//int loss_state=0;
								int case_state=mData.getInt(MyReceiveParams.case_state);
								/*if(case_stateIn==null){
									//case_stateIn=0;
								}*/
								//case_state=(int)case_stateIn;
								String case_stateStr="待定损";
								if(case_state==2){
									case_stateStr="定损中";
								}else if(case_state==3){
									case_stateStr="已完成";
								}
								
								
								int repair_state=mData.getInt(MyReceiveParams.repair_state);
								/*if(repair_stateIn==null){
									case_stateIn=0;
								}*/
								//repair_state=(int)repair_stateIn;
								String repair_stateStr=String.valueOf(repair_state);
								
								int audit_state=mData.getInt(MyReceiveParams.audit_state);
								/*if(audit_stateIn==null){
									//audit_stateIn=0;
								}*/
								//audit_state=(int)audit_stateIn;
								String audit_stateStr=String.valueOf(audit_state);
								
								int loss_state=mData.getInt(MyReceiveParams.loss_state);
								/*if(loss_stateIn==null){
									//loss_stateIn=0;
								}*/
								//loss_state=(int)loss_stateIn;
								String loss_stateStr=String.valueOf(loss_state);
								MyTaskObject mTaskObject=new MyTaskObject(case_id,case_No,car_No,brand_name,is_targetStr,target_No
										,is_VipStr,parter_idStr,parter_name,parter_manager,parter_mobile,delivery_name,
										delivery_mobile,loss_priceStr,yard_timeStr,case_stateStr,repair_stateStr,audit_stateStr,loss_stateStr);
								Long yardTimeLong=Long.parseLong(yard_timeStrTimeStamp);
								
								if(server_timeLong-yardTimeLong.longValue()<24*60*60*1000){
									tempList.add(mTaskObject);
								}else{
									MyUndealList.add(mTaskObject);
								}
								
							}
							TaskRecordList.mTaskList.clear();//清空currenttask
							for(int i=0;i<tempList.size();i++){

								TaskRecordList.mTaskList.add(tempList.get(i));	
							}
							taskRecordAdapter.notifyDataSetChanged();
							currentTasklv.onRefreshComplete();
							if(type==REFRESHTYPE){//下拉刷新滚到底部
								currentTasklv.getRefreshableView().setSelection(currentTasklv.getRefreshableView().getBottom());	
								}else{//请求回顶部
									currentTasklv.getRefreshableView().setSelection(currentTasklv.getRefreshableView().getTop());	
								}
						}else{
							currentTasklv.onRefreshComplete();
							Toast.makeText(getContext(), "暂无新的任务", Toast.LENGTH_SHORT).show();
						}
					}else{
						currentTasklv.onRefreshComplete();
						Toast.makeText(getContext(), "获取任务失败", Toast.LENGTH_SHORT).show();
					}
				}catch(JSONException e){
					e.printStackTrace();
					Log.d(TAG, e.getMessage());
				}
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				// TODO 自动生成的方法存根
				super.onFailure(e, errorResponse);
				if(currentTaskpd!=null&&currentTaskpd.isShowing()){
					currentTaskpd.dismiss();
				}
			}
			
			@Override
			public void onFailure(Throwable e, String errorResponse) {
				// TODO 自动生成的方法存根
				super.onFailure(e, errorResponse);
				currentTasklv.onRefreshComplete();
				if(currentTaskpd!=null&&currentTaskpd.isShowing()){
					currentTaskpd.dismiss();
				}
				Toast.makeText(getActivity(), "连接错误", Toast.LENGTH_SHORT).show();
			}
			
		});
		
	}
	
	public void updateCaseNo(final MyReportNoInputDialog dialog,final int position,String caseNoStr){
		pd=ProgressDialog.show(getActivity(), "加载中", "请稍后...");
		AsyncHttpClient updateCaseNoHttpClient=new AsyncHttpClient();
		updateCaseNoHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		updateCaseNoHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=currentFragmentSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=currentFragmentSP.getString("Port", dPort);
		String updateCaseNoUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.UpdateCaseNoUrl;
		RequestParams requestparams=new RequestParams();
		String case_idstr=String.valueOf(MyhttpStorage.case_id );
		requestparams.put("case_NO", caseNoStr);
		requestparams.put("case_id",case_idstr);
		requestparams.put("car_NO", MyhttpStorage.car_no);//车牌号
		updateCaseNoHttpClient.post(updateCaseNoUrl, requestparams, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO 自动生成的方法存根
				super.onSuccess(statusCode, response);
				if(pd!=null&&pd.isShowing()){
					pd.dismiss();
					pd=null;
				}
				boolean success=false;
				try{
					success=response.getBoolean("success");
					if(success){
						dialog.dismiss();
						Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
						if(TaskRecordList.mTaskList.size()>position){
							TaskRecordList.mTaskList.remove(position);//just for test
						}
						
						taskRecordAdapter.notifyDataSetChanged();
						
					}else{
						String errorMessage=response.getString("err");
						Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
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
					pd=null;
				}
			}
			
			@Override
			public void onFailure(Throwable e, String errorResponse) {
				// TODO 自动生成的方法存根
				super.onFailure(e, errorResponse);
				if(pd!=null&&pd.isShowing()){
					pd.dismiss();
					pd=null;
				}
				Toast.makeText(getActivity(), "连接错误", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
}
