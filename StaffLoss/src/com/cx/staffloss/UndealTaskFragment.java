package com.cx.staffloss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.MyTaskObject;
import com.cx.myobject.MyUndealList;
import com.cx.myobject.TaskRecordList;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.utils.MRegex;
import com.cx.utils.MyReportNoInputDialog;
import com.cx.utils.MySeCustomeDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UndealTaskFragment extends Fragment {
//List<MyTaskObject> undealTaskList;
PullToRefreshListView undealTasklv;
TaskRecordAdapter taskRecordAdapter;
SharedPreferences undealTaskSP;
public static final String SPName="StaffLossSP";
String case_idStr="";
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		undealTaskSP=getActivity().getSharedPreferences(SPName, 0);
	//	undealTaskList=new ArrayList<MyTaskObject>();
	/*	for(MyTaskObject myTaskObject:MyUndealList.mList){
			undealTaskList.add(myTaskObject);
		}*/
/*		MyTaskObject taskObject=new MyTaskObject("12222", "鄂A23333", "奥迪",
				"是", "鄂A23333", "是", "1",
				"拆检点1", "cx", "151111934567",
				"cxsx","1511111", "45000","1",
				"1234567", "2", "3",
				"4");*/
		//undealTaskList.add(taskObject);
		if(MyUndealList.mList==null){
			MyUndealList.mList=new ArrayList<MyTaskObject>();
		}
		undealTasklv=(PullToRefreshListView) getView().findViewById(R.id.lv_undeal);
		undealTasklv.setMode(Mode.BOTH);
		taskRecordAdapter=new TaskRecordAdapter(this.getActivity(),R.layout.task_item_layout,MyUndealList.mList);
		undealTasklv.setAdapter(taskRecordAdapter);
		undealTasklv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				MyTaskObject selectObject=MyUndealList.mList.get(position-1);
				case_idStr=selectObject.getCase_id();
				String objectCase=selectObject.getCase_No();
				String objectCarNo=selectObject.getCar_No();
				if(objectCase!=null&&objectCase.length()>0){
					showCaseNoDialog(position-1,objectCase,objectCarNo);
				}else{
					showCaseNoDialog(position-1,"",objectCarNo);
				}
				Log.d("tag", "postion"+position);
			
				
			}
			
		});
	}
	
	public void showCaseNoDialog(final int position,String caseNoStr,final String carNo){
		final MyReportNoInputDialog myDialog=new MyReportNoInputDialog(this.getContext(),R.style.Dialog);
		myDialog.setReportCustomDialog();;
		final EditText editText=(EditText) myDialog.getEditText();
		if(caseNoStr!=null&&caseNoStr.length()>0){
			editText.setText(caseNoStr);
		}
		myDialog.setOnPositiveListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String caseNoStr=editText.getText().toString();
				if(MRegex.isRightCaseNo(caseNoStr)){
					updateCaseNo( myDialog,position,caseNoStr,carNo);
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
		return inflater.inflate(R.layout.undeal_fragment_layout, container, false);
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
	}

	public void updateCaseNo(final MyReportNoInputDialog dialog,final int position,String caseNoStr,String carNoStr){
		AsyncHttpClient updateCaseNoHttpClient=new AsyncHttpClient();
		updateCaseNoHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		updateCaseNoHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=undealTaskSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=undealTaskSP.getString("Port", dPort);
		String updateCaseNoUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.UpdateCaseNoUrl;
		RequestParams requestparams=new RequestParams();
		//String case_idstr=String.valueOf(MyhttpStorage.case_id );
		requestparams.put("case_NO", caseNoStr);
		requestparams.put("car_NO", carNoStr);
		requestparams.put("case_id",case_idStr);
		updateCaseNoHttpClient.post(updateCaseNoUrl, requestparams, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO 自动生成的方法存根
				super.onSuccess(statusCode, response);
				boolean success=false;
				try{
					success=response.getBoolean("success");
					if(success){
						dialog.dismiss();
						Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
						MyUndealList.mList.remove(position);
						taskRecordAdapter.notifyDataSetChanged();
					}else{
						if(response.has("err")){
							JSONObject err=response.getJSONObject("err");
							if(err.has("message")){
								String message=err.getString("message");
								Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(getContext(), "提交失败", Toast.LENGTH_SHORT).show();
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
			}
			
			@Override
			public void onFailure(Throwable e, String errorResponse) {
				// TODO 自动生成的方法存根
				super.onFailure(e, errorResponse);
			}
			
		});
	}
	
	
}
