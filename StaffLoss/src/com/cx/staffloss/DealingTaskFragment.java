package com.cx.staffloss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.DealingTaskRecordList;
import com.cx.myobject.MyTaskObject;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.utils.MUtil;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnScrollChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class DealingTaskFragment extends Fragment {
	//List<MyTaskObject> dealingtaskRecorderList;
	PullToRefreshListView dealingTasklv;
	TaskRecordAdapter dealingtaskRecordAdapter;
	public static final String TAG="dealingTask";
	int start=0;
	int limit=5;
	public static String SPName="StaffLossSP";
	SharedPreferences dealingTaskFragmentSP;
	ProgressDialog dealingTaskPd;
	private static int REFRESHTYPE=2;//刷新调用,lv移动到底
	private static int ORINALTYPE=1;//请求调用,lv不移动
//	int scrollX=0,scrollY=0;
	//Map<String,String> stateMap;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		
		dealingTaskFragmentSP=getActivity().getSharedPreferences(SPName,0);
		//dealingtaskRecorderList=new ArrayList<MyTaskObject>();
		//TaskRecord taskRecord1=new TaskRecord("201702121111","17/2/12 12:11","鄂A SQ880","VIP","外修中","拆检点","1");
		//TaskRecord taskRecord2=new TaskRecord("201702121112","17/2/13 12:12","鄂B SQ880","","推定中","拆检点","2");
		/*dealingtaskRecorderList.add(taskRecord1);
		dealingtaskRecorderList.add(taskRecord2);*/
		
		/*final MyTaskObject taskObject=new MyTaskObject("12222", "鄂A23333", "奥迪",
				"是", "鄂A23333", "是", "1",
				"拆检点1", "cx", "151111934567",
				"cxsx","1511111", "45000","1",
				"1234567", "2", "3",
				"4");*/
		//dealingtaskRecorderList.add(taskObject);
		if(DealingTaskRecordList.mDealingTaskList==null){
			DealingTaskRecordList.mDealingTaskList=new ArrayList<MyTaskObject>();
		}
		dealingTasklv=(PullToRefreshListView)getView().findViewById(R.id.lv_dealing);
		dealingTasklv.setMode(Mode.BOTH);
		dealingtaskRecordAdapter=new TaskRecordAdapter(this.getActivity(),R.layout.task_item2_layout,DealingTaskRecordList.mDealingTaskList);
		dealingTasklv.setAdapter(dealingtaskRecordAdapter);
		
/*		dealingTasklv.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO 自动生成的方法存根
				if(scrollState==OnScrollListener.SCROLL_STATE_IDLE){
					scrollX=dealingTasklv.getScrollX();
					scrollY=dealingTasklv.getScrollY();
				}
			}
			
			

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO 自动生成的方法存根
				
			}
			
		});*/

		
		dealingTasklv.setOnRefreshListener(new OnRefreshListener<ListView>(){

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO 自动生成的方法存根
				limit=limit+5;//刷新功能，同时每次多读5条
				DealingTaskRecordList.mDealingTaskList.clear();
				dealingtaskRecordAdapter.notifyDataSetChanged();
				
				getDealingTaskbyHttp(REFRESHTYPE);
				//Log.d(TAG, "bottom item"+dealingTasklv.getRefreshableView().getBottom());
				//dealingTasklv.scrollTo(scrollX, scrollY);
				//dealingTasklv.setScrollY(dealingTasklv.getBottom());//
				
				
				//dealingtaskRecordAdapter.notifyDataSetChanged();

			}
			
		});
		
		dealingTasklv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
			MyTaskObject mTaskObject=DealingTaskRecordList.mDealingTaskList.get(position-1);//??/position start 1
			String  case_idSelect=mTaskObject.getCase_id();
			Log.d(TAG, "postion"+position);
			Bundle bundle=new Bundle();
			if(mTaskObject!=null){
				bundle.putSerializable("selectTask", mTaskObject);	
			}
			//bundle.putString("btnSelect", "2");//btnSelect false
			SharedPreferences mapSP=getActivity().getSharedPreferences(SPName, 0);
			String mapObjStr=mapSP.getString("mMap", "");
			
			if(mapObjStr.equals("")){
				Map<String,String> stateMap=new HashMap<String,String>();
				saveMap(getActivity(),stateMap);
			}
			mapObjStr=mapSP.getString("mMap", "");
			Map<String,String> refreshMap=getMap(getActivity(),mapObjStr);
			if(refreshMap.containsKey(case_idSelect)){
				
			}else{
				refreshMap.put(case_idSelect, "0");
			}
			saveMap(getActivity(),refreshMap);
			Intent toDetailIntent=new Intent(getActivity(),DealingTaskDetail.class);
			toDetailIntent.putExtras(bundle);
			int requestCode=3;
			startActivityForResult(toDetailIntent,requestCode);
			}
			
		});
	}
	
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		switch(resultCode){
		case 3://返回时刷新
			DealingTaskRecordList.mDealingTaskList.clear();
			dealingtaskRecordAdapter.notifyDataSetChanged();
			getDealingTaskbyHttp(ORINALTYPE);
			break;
		default:
			break;
		}
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
		//getDealingTaskbyHttp();
		Log.d(TAG, "onCreate");
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		Log.d(TAG, "onCreateView");
		return inflater.inflate(R.layout.dealing_fragment_layout, container, false);
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		Log.d(TAG, "onDestroy");
		DealingTaskRecordList.mDealingTaskList.clear();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		Log.d(TAG, "onPause");
		super.onPause();
	}
	
	
public void getDealingTaskbyHttp(final int type){
	dealingTaskPd=ProgressDialog.show(getActivity(), "加载中", "请稍后...");
	AsyncHttpClient getDealingTaskHttpClient=new AsyncHttpClient();
	getDealingTaskHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	getDealingTaskHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	String dUrl=MHttpParams.IP;
	String mUrl=dealingTaskFragmentSP.getString("IP", dUrl);
	String dPort=MHttpParams.DEFAULT_PORT;
	String mPort=dealingTaskFragmentSP.getString("Port", dPort);
	String getDealingUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.DealingTaskUrl;
	RequestParams requestparams=new RequestParams();
	//int id=MyhttpStorage.id;
	int id=dealingTaskFragmentSP.getInt("id", -1);
	requestparams.put("start", String.valueOf(start));
	requestparams.put("limit", String.valueOf(limit));
	requestparams.put("id", String.valueOf(id));
	getDealingTaskHttpClient.post(getDealingUrl, requestparams, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, JSONObject response) {
			// TODO 自动生成的方法存根
			super.onSuccess(statusCode, response);
			if(dealingTaskPd!=null&&dealingTaskPd.isShowing()){
				dealingTaskPd.dismiss();
				dealingTaskPd=null;
			}
			boolean success=false;
			try{
				success=response.getBoolean("success");
				if(success){
					//DealingTaskRecordList.mDealingTaskList.clear();
					//Toast.makeText(getActivity(), "get it", Toast.LENGTH_SHORT).show();
					JSONArray data=response.getJSONArray("data");
					if(data.length()>0){
						for(int i=0;i<data.length();i++){
							JSONObject mdata=data.getJSONObject(i);
							int case_id=mdata.getInt("case_id");//案件id
							String case_idStr=String.valueOf(case_id);
							MyhttpStorage.case_id=case_id;//最好不用,会覆盖
							String case_no=mdata.getString("case_NO");
							String car_no=mdata.getString("car_NO");
							String brand_name=mdata.getString("brand_name");
							int isTargetInt=mdata.getInt("is_tartget");
							String isTarget="否";
							if(isTargetInt==1){
								isTarget="是";
							}
							String target_NO=mdata.getString("target_NO");
							int isvipInt=mdata.getInt("isvip");
							String isvip="";
							if(isvipInt==1){
								isvip="VIP";
							}
							String parters_name=mdata.getString("parters_name");
							Object case_remarkObj=mdata.get("case_remark");
							if(case_remarkObj instanceof String){//test for null
								
							}else{
								Log.d(TAG, "case_remark is null");
							}
							
							String parter_manager=mdata.getString("parter_manager");
							String parter_mobile=mdata.getString("parter_mobile");
							String delivery_name=mdata.getString("delivery_name");
							String delivery_mobile=mdata.getString("delivery_mobile");
							int loss_priceInt=mdata.getInt("loss_price");
							String loss_price=String.valueOf(loss_priceInt);
							String yard_timeStamp=mdata.getString("yard_time");//时间戳
							String yard_time=MUtil.getDetailTime(yard_timeStamp);
							int repair_stateInt=mdata.getInt("repair_state");
							int audit_stateInt=mdata.getInt("audit_state");//jihe
							int loss_stateInt=mdata.getInt("loss_state");
							//int 
							String repair_state=String.valueOf(repair_stateInt);
							String audit_state=String.valueOf(audit_stateInt);
							String loss_state=String.valueOf(loss_stateInt);
							int case_code=mdata.getInt("case_state");
							String case_state=MUtil.stateChange(case_code);
							//String case_state=String.valueOf(case_stateInt);
							/*final MyTaskObject taskObject=new MyTaskObject("12222", "鄂A23333", "奥迪",
									"是", "鄂A23333", "是", "1",
									"拆检点1", "cx", "151111934567",
									"cxsx","1511111", "45000","1234567","1",
									 "2", "3",
									"4");*/
							MyTaskObject taskObject=new MyTaskObject(case_idStr,case_no,car_no,brand_name,isTarget,target_NO
									,isvip,"",parters_name,parter_manager,parter_mobile,delivery_name,delivery_mobile,
									loss_price,yard_time,case_state,repair_state,audit_state,loss_state);
							DealingTaskRecordList.mDealingTaskList.add(taskObject);
						}
						
						dealingtaskRecordAdapter.notifyDataSetChanged();
						
						dealingTasklv.onRefreshComplete();
						if(type==REFRESHTYPE){//下拉刷新滚到底部
						dealingTasklv.getRefreshableView().setSelection(dealingTasklv.getRefreshableView().getBottom());	
						}else{//请求回顶部
						dealingTasklv.getRefreshableView().setSelection(dealingTasklv.getRefreshableView().getTop());	
						}
						
					}else{
						dealingTasklv.onRefreshComplete();
						Toast.makeText(getContext(), "暂无新的任务", Toast.LENGTH_SHORT).show();
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
			dealingTasklv.onRefreshComplete();
			if(dealingTaskPd!=null&&dealingTaskPd.isShowing()){
				dealingTaskPd.dismiss();
				dealingTaskPd=null;
			}
			if(DealingTaskFragment.this!=null&&DealingTaskFragment.this.isAdded()){
				Toast.makeText(getActivity(), "连接错误", Toast.LENGTH_SHORT).show();
			}
			
		}
		
		
		
	});
}

public void saveMap(Context context,Map<String,String>mMap){
	JSONObject mapObj=new JSONObject();
	Iterator<Entry<String,String>> mapIterator=mMap.entrySet().iterator();
	while(mapIterator.hasNext()){
		Entry<String,String> entry=mapIterator.next();
		try{
			mapObj.put(entry.getKey(), entry.getValue());
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	SharedPreferences mapSP=context.getSharedPreferences(SPName, 0);
	SharedPreferences.Editor mapEditor=mapSP.edit();
	mapEditor.putString("mMap", mapObj.toString());
	mapEditor.commit();
}

public Map<String,String> getMap(Context context,String mapObjStr){
	Map<String,String> resultMap=new HashMap<String,String>();
	//SharedPreferences mapSP=context.getSharedPreferences(SPName, 0);
	//String mapObjStr=mapSP.getString("mMap", "");
	try {
		JSONObject mapObj=new JSONObject(mapObjStr);
		JSONArray names=mapObj.names();
		if(names!=null){
			for(int i=0;i<names.length();i++){
				String name=names.getString(i);
				String value=mapObj.getString(name);
				resultMap.put(name, value);
			}
		}
	} catch (JSONException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
	return resultMap;
}


}
