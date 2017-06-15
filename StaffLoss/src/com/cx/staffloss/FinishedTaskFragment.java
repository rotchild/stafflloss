package com.cx.staffloss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.MyFinishObject;
import com.cx.myobject.MyTaskObject;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.utils.MUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FinishedTaskFragment extends Fragment {
	List<MyFinishObject> finishtaskRecorderList;
	PullToRefreshListView finishTasklv;
	FinishTaskRecordAdapter finishtaskRecordAdapter;
	public static final String TAG="finishTask";
	public static String SPName="StaffLossSP";
	SharedPreferences FinishTaskFragmentSP;
	ProgressDialog getFinishTaskPd;
	int start=0;
	int limit=20;//已完成limit上限提高
	
	private static int REFRESHTYPE=2;//刷新调用,lv移动到底
	private static int ORINALTYPE=1;//请求调用,lv不移动
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		finishtaskRecorderList=new ArrayList<MyFinishObject>();
		FinishTaskFragmentSP=getActivity().getSharedPreferences(SPName, 0);
/*		final MyFinishObject taskObject=new MyFinishObject("12222", "鄂A23333", "奥迪",
				"是", "鄂A23333", "是", "1",
				"拆检点1", "cx", "151111934567",
				"cxsx","1511111", "45000","1234567","45000");*/
		//finishtaskRecorderList.add(taskObject);
		
		finishTasklv=(PullToRefreshListView)getView().findViewById(R.id.lv_finished);
		finishTasklv.setMode(Mode.BOTH);
		finishtaskRecordAdapter=new FinishTaskRecordAdapter(this.getActivity(),R.layout.task_item2_layout,finishtaskRecorderList);
		
		finishTasklv.setOnRefreshListener(new OnRefreshListener<ListView>(){

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO 自动生成的方法存根
				limit=limit+5;
				getfinishTasklistByHttp(REFRESHTYPE);//与之前的写法不同，clear写在了里面，同时list只是全局变量
			
			}
			
		});
		
		finishTasklv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				MyFinishObject selectedFTaskObject=finishtaskRecorderList.get(position-1);
				Log.d(TAG, "postion"+position);
				Bundle bundle=new Bundle();
				if(selectedFTaskObject!=null){
					bundle.putSerializable("selectFtask", selectedFTaskObject);
				}else{
					Log.d(TAG, "postion"+position);
				}
				//Toast.makeText(getContext(), "you click", Toast.LENGTH_SHORT).show();
				Intent toFinishDetail=new Intent(getActivity(),FinishDetailActivity.class);
				toFinishDetail.putExtras(bundle);
				startActivity(toFinishDetail);
				
			}
			
		});
		finishTasklv.setAdapter(finishtaskRecordAdapter);
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
		return inflater.inflate(R.layout.finished_fragment_layout, container, false);
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
	
	public void getfinishTasklistByHttp(final int type){
		getFinishTaskPd=ProgressDialog.show(getActivity(), "加载中", "请稍后...");
		
		AsyncHttpClient getfinishTaskHttpClient=new AsyncHttpClient();
		getfinishTaskHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
		getfinishTaskHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
		String dUrl=MHttpParams.IP;
		String mUrl=FinishTaskFragmentSP.getString("IP", dUrl);
		String dPort=MHttpParams.DEFAULT_PORT;
		String mPort=FinishTaskFragmentSP.getString("Port", dPort);
		String getfinishTaskUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.FinishTaskUrl;
		RequestParams requestparams=new RequestParams();
		//int id=MyhttpStorage.id;
		int id=FinishTaskFragmentSP.getInt("id", -1);
		requestparams.put("start", String.valueOf(start));
		requestparams.put("limit", String.valueOf(limit));
		requestparams.put("id", String.valueOf(id));
		getfinishTaskHttpClient.post(getfinishTaskUrl, requestparams, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONObject response) {
				// TODO 自动生成的方法存根
				super.onSuccess(response);
				
				if(getFinishTaskPd!=null&&getFinishTaskPd.isShowing()){
					getFinishTaskPd.dismiss();
					getFinishTaskPd=null;
				}
				boolean success=false;
				try{
					success=response.getBoolean("success");
					if(success){
						finishtaskRecorderList.clear();
						JSONArray data=response.getJSONArray("data");
						if(data.length()>0){
							for(int i=0;i<data.length();i++){
								JSONObject mdata=data.getJSONObject(i);
								String case_NO=mdata.getString("case_NO");
								String car_NO=mdata.getString("car_NO");
								String brand_name=mdata.getString("brand_name");
								int is_target=mdata.getInt("is_tartget");
								String isTargetStr="是";
								if(is_target==1){
									isTargetStr="是";
								}else{
									isTargetStr="否";
								}
								String target_NO=mdata.getString("target_NO");
								int isVipInt=mdata.getInt("isvip");
								String isVip=String.valueOf(isVipInt);
								String parters_name=mdata.getString("parters_name");
								String parter_manager=mdata.getString("parter_manager");
								String parter_mobile=mdata.getString("parter_mobile");
								String delivery_name=mdata.getString("delivery_name");
								String delivery_mobile=mdata.getString("delivery_mobile");
								double loss_priceD=mdata.getDouble("loss_price");
							
								
								String loss_price=String.valueOf(loss_priceD);
								loss_price=mdata.getString("loss_price");//testok
								String yard_time=mdata.getString("yard_time");
								//String yard_time=MUtil.getDetailTime(yard_timeStamp);
								int case_stateInt=mdata.getInt("case_state");
								String dingsun_price="";
								if(mdata.has("final_price")){
									double final_priceD=mdata.getDouble("final_price");
									dingsun_price=String.valueOf(final_priceD);
									dingsun_price=mdata.getString("final_price");
								}
/*								double dinsun_priceD=mdata.getDouble("loss_price");//dingusnprice
								dingsun_price=String.valueOf(dinsun_priceD);*/
								String case_state=String.valueOf(case_stateInt);
								
								int repair_stateInt=mdata.getInt("repair_state");
								int audit_stateInt=mdata.getInt("audit_state");
								int loss_stateInt=mdata.getInt("loss_state");
								
								/*MyTaskObject(String case_No, String car_No, String brand_name,
										String is_target, String target_No, String isvip, String parter_id,
										String parters_name, String parter_manager, String parter_mobile,
										String delivery_name, String delivery_mobile, String loss_price,String yard_time,
										String case_state, String repair_state, String audit_state,
										String loss_state) */
								String parterid="1";//无此返回字段
								String repair_state=String.valueOf(repair_stateInt);
								String audit_state=String.valueOf(audit_stateInt);
								String loss_state=String.valueOf(loss_stateInt);
								
								String repairTimeStamp="";
								String repair_factory="";
								String repair_parts="";
								String parts_price="";
								String repair_price="";
								String repair_remark="";//外修
								
								String loss_add_timeStamp="";
								String loss_remark="";//推定
								
								String audit_starttimeStamp="";
								String audit_remark="";//稽核
								
								
								if(repair_state.equals("1")){
									repairTimeStamp=mdata.getString("repair_time");
									repair_factory=mdata.getString("repair_factoryname");
									repair_parts=mdata.getString("repair_parts");
									parts_price=mdata.getString("parts_price");
									repair_price=mdata.getString("repair_price");
									repair_remark=mdata.getString("repair_remark");
								}
								
								if(loss_state.equals("1")){
									if(mdata.has("losstasks_add_time")){
										loss_add_timeStamp=mdata.getString("losstasks_add_time");
									}
									if(mdata.has("loss_remark")){
										loss_remark=mdata.getString("loss_remark");
									}
								}
								
								if(audit_state.equals("1")){
									if(mdata.has("audit_starttime")){
										audit_starttimeStamp=mdata.getString("audit_starttime");
									}
									
									if(mdata.has("audit_remark")){//or use cut_remark????
										audit_remark=mdata.getString("audit_remark");
									}
								}
								
								
								MyFinishObject mFinishObject=new MyFinishObject(case_NO,car_NO,brand_name,isTargetStr,target_NO,
										isVip,parterid,parters_name,parter_manager,parter_mobile,delivery_name,delivery_mobile,
										loss_price,yard_time,dingsun_price,repair_state,audit_state,loss_state,repairTimeStamp,repair_factory,
										repair_parts,parts_price,repair_price,repair_remark,loss_add_timeStamp,loss_remark,
										audit_starttimeStamp,audit_remark);//yard_time 为timestamp
								finishtaskRecorderList.add(mFinishObject);
							}
							finishtaskRecordAdapter.notifyDataSetChanged();
							finishTasklv.onRefreshComplete();
							if(type==REFRESHTYPE){//下拉刷新滚到底部
								finishTasklv.getRefreshableView().setSelection(finishTasklv.getRefreshableView().getBottom());	
								}else{//请求回顶部
								finishTasklv.getRefreshableView().setSelection(finishTasklv.getRefreshableView().getTop());	
								}
						}else{
							finishTasklv.onRefreshComplete();
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
				finishTasklv.onRefreshComplete();
				if(getFinishTaskPd!=null&&getFinishTaskPd.isShowing()){
					getFinishTaskPd.dismiss();
					getFinishTaskPd=null;
				}
			}
			
			@Override
			public void onFailure(Throwable e, String errorResponse) {
				// TODO 自动生成的方法存根
				super.onFailure(e, errorResponse);
				finishTasklv.onRefreshComplete();
				if(getFinishTaskPd!=null&&getFinishTaskPd.isShowing()){
					getFinishTaskPd.dismiss();
					getFinishTaskPd=null;
				}
			}
			
		});
	}

}
