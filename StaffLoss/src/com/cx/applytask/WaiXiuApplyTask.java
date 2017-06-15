package com.cx.applytask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.MyNoteDialog;
import com.cx.myobject.MyTaskObject;
import com.cx.myobject.MydepartListAdapter;
import com.cx.myobject.MydepartmentObject;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.staffloss.DealingTaskDetail;
import com.cx.staffloss.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class WaiXiuApplyTask extends Activity{
TextView waixiudateTv;
TextView waixiudepartTv;
//TextView waixiuDiallTv;
TextView waixiu_reportNo_tv,waixiu_carNo_tv,waixiu_carType_tv,waixiu_isTheCar_tv,waixiu_theCarNo_tv,waixiu_reportDeport_tv,
waixiu_lossStaff_tv;
ImageView dingsunTelIv;
EditText waixiu_peijian_et,waixiu_peijianPrice_et,waixiu_xiufuamount_et;
//EditText waixiu_note_et;外修备注框
String  dingsunerTel;
String repair_remark="";//外修申请备注;

static int screenWidth;
static int screenHeight;
private int m_year,m_month,m_day;

Button waixiu_yes_button,waixiu_no_button;
Button note_button;//外修备注btn
MyNoteDialog mNoteDialog;
String noteEtStr="";//外修备注内容，添加完后可以查看，完成或取消后消失
PopupWindow popupwindow;
private Calendar mcalendar;
private int selected_id;
MydepartListAdapter mdepartListAdapter;
private final static String TAG="waixiuApply";
List<MydepartmentObject> mdepartList;
MyTaskObject selectTaskObject;

String case_idStr="";
public static String SPName="StaffLossSP";
SharedPreferences WaixiuTaskSP;
ProgressDialog queryPd,factoryListPd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waixiuapply_layout);
		WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
		screenWidth=wm.getDefaultDisplay().getWidth();
		screenHeight=wm.getDefaultDisplay().getHeight();
		
		selectTaskObject=(MyTaskObject) this.getIntent().getSerializableExtra("selectTaskObject");
		initView();
	}
public void initView(){
	WaixiuTaskSP=getSharedPreferences(SPName,0);
	
	
	
	waixiu_peijian_et=(EditText)findViewById(R.id.waixiu_peijian_et);
	waixiu_peijianPrice_et=(EditText)findViewById(R.id.waixiu_peijianPrice_et);
	waixiu_xiufuamount_et=(EditText)findViewById(R.id.waixiu_xiufuamount_et);
	//waixiu_note_et=(EditText)findViewById(R.id.waixiu_note_et);
	note_button=(Button)findViewById(R.id.waixiu_note_btn);
	note_button.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			showNoteDialog();
		}
		
	});
/*	int et_height=waixiu_peijian_et.getLayoutParams().height;
	if(et_height>2){
		note_button.getLayoutParams().height=et_height-2;
		note_button.getLayoutParams().width=2*(note_button.getLayoutParams().height);
	}*/

	
	waixiu_yes_button=(Button)findViewById(R.id.waixiu_yes_btn);
	waixiu_no_button=(Button)findViewById(R.id.waixiu_no_btn);
	
	waixiu_yes_button.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			String repair_parts=waixiu_peijian_et.getText().toString();
			String repair_price=waixiu_peijianPrice_et.getText().toString();
			String parts_price=waixiu_xiufuamount_et.getText().toString();
			//String repair_remark=waixiu_note_et.getText().toString();
			String waixiu_choose=waixiudepartTv.getText().toString();
			if(null!=repair_parts&&repair_parts.length()>0&&null!=repair_price&&repair_price.length()>0&&null!=parts_price&&parts_price.length()>0&&(!waixiu_choose.equals("请选择"))){
				//queryStateByHttp();
				
				applyWaixiuByHttp();
			}else if(parts_price.startsWith(".")||repair_price.startsWith(".")){
				Toast.makeText(WaiXiuApplyTask.this, "金额格式错误", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(WaiXiuApplyTask.this, "外修单位未选择或信息尚未填完", Toast.LENGTH_SHORT).show();
			}
	
		}
		
	});
    waixiu_no_button.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			WaiXiuApplyTask.this.finish();
		}
    	
    });
	
	
	waixiu_reportNo_tv=(TextView)findViewById(R.id.waixiu_reportNo_tv);
	waixiu_carNo_tv=(TextView)findViewById(R.id.waixiu_carNo_tv);
	waixiu_carType_tv=(TextView)findViewById(R.id.waixiu_carType_tv);
	waixiu_isTheCar_tv=(TextView)findViewById(R.id.waixiu_isTheCar_tv);
	waixiu_theCarNo_tv=(TextView)findViewById(R.id.waixiu_theCarNo_tv);
	waixiu_reportDeport_tv=(TextView)findViewById(R.id.waixiu_reportDeport_tv);
	waixiu_lossStaff_tv=(TextView)findViewById(R.id.waixiu_lossStaff_tv);
	//waixiu_lossStaffNumber_tv=(TextView)findViewById(R.id.waixiu_lossStaffNumber_tv);
	
	
	String waixiu_reportNostr=selectTaskObject.getCase_No();
	String waixiu_carNostr=selectTaskObject.getCar_No();
	String waixiu_carTypestr=selectTaskObject.getBrand_name();
	String waixiu_isTheCarstr=selectTaskObject.getIs_target();
	String waixiu_theCarNostr=selectTaskObject.getTarget_No();
	String waixiu_reportDeportstr=selectTaskObject.getParters_name();
	//String dingsunerName=MyhttpStorage.dingsunerName;
	String dingsunerName=WaixiuTaskSP.getString("dingsunerName", "");
	//String dingsunerTel=MyhttpStorage.dingsunerTel;
    dingsunerTel=WaixiuTaskSP.getString("dingsunerTel", "");
    case_idStr=selectTaskObject.getCase_id();
    
	waixiu_reportNo_tv.setText(waixiu_reportNostr);
	waixiu_carNo_tv.setText(waixiu_carNostr);
	waixiu_carType_tv.setText(waixiu_carTypestr);
	waixiu_isTheCar_tv.setText(waixiu_isTheCarstr);
	waixiu_theCarNo_tv.setText(waixiu_theCarNostr);
	waixiu_reportDeport_tv.setText(waixiu_reportDeportstr);
	waixiu_lossStaff_tv.setText(dingsunerName);
	
	
	mdepartList=new ArrayList<MydepartmentObject>();
	
	
	mcalendar=Calendar.getInstance();
	m_year=mcalendar.get(Calendar.YEAR);
	m_month=mcalendar.get(Calendar.MONTH);
	m_day=mcalendar.get(Calendar.DAY_OF_MONTH);
	waixiudateTv=(TextView)findViewById(R.id.waixiu_date_tv);
	waixiudepartTv=(TextView)findViewById(R.id.waixiu_theDepartment_tv);
	
	/*waixiuDiallTv=(TextView)findViewById(R.id.waixiu_lossStaffNumber_tv);
	waixiuDiallTv.setText(dingsunerTel);*/
	
	waixiudateTv.setText(m_year+"-"+(m_month+1)+"-"+m_day);
	
	dingsunTelIv=(ImageView)findViewById(R.id.callReporterDepartTaskDetail_iv);
    dingsunTelIv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			/*String phoneNumber;
			phoneNumber=waixiuDiallTv.getText().toString();*/
			Intent dialintent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+dingsunerTel));
			startActivity(dialintent);
		}
		
	});
	
/*	waixiudateTv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			
			showDateDialog();
		}
		
	});*/
	
	waixiudepartTv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
		/*	mdepartList.clear();
			getAllFactorybyHttp();*/
			showPopupWindow();
		}
		
		
	});
	
	/*waixiuDiallTv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			
			Intent dialintent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+dingsunerTel));
			startActivity(dialintent);
		}
		
	});*/
	mdepartList.clear();
	getAllFactorybyHttp();
}

public void showNoteDialog(){
	final EditText noteEt;
	mNoteDialog=new MyNoteDialog(WaiXiuApplyTask.this,R.style.Dialog);
	mNoteDialog.setDialog();
	String dialogTitle="备注";
	mNoteDialog.setTitle(dialogTitle);
	noteEt=(EditText)mNoteDialog.getEditText();
	if(noteEtStr.length()>0){
		noteEt.setText(noteEtStr);
	}
	mNoteDialog.setSaveBtn(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			noteEtStr=noteEt.getText().toString().trim();
			repair_remark=noteEtStr;
			InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                 imm.hideSoftInputFromWindow(mNoteDialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
			if(mNoteDialog!=null&&mNoteDialog.isShowing()){
				mNoteDialog.dismiss();
				mNoteDialog=null;
			}
		}
		
	});
	mNoteDialog.setCancelBtn(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                 imm.hideSoftInputFromWindow(mNoteDialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
			if(mNoteDialog!=null&&mNoteDialog.isShowing()){
				mNoteDialog.dismiss();
				mNoteDialog=null;
			}
		}
		
	});
	mNoteDialog.show();
}

public void getAllFactorybyHttp(){
	factoryListPd=ProgressDialog.show(WaiXiuApplyTask.this, "加载中", "请稍后...");
	AsyncHttpClient getAllFactoryHttpClient=new AsyncHttpClient();
	getAllFactoryHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	getAllFactoryHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	String dUrl=MHttpParams.IP;
	String mUrl=WaixiuTaskSP.getString("IP", dUrl);
	String dPort=MHttpParams.DEFAULT_PORT;
	String mPort=WaixiuTaskSP.getString("Port", dPort);
	String getAllFactoryUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.getAllFactoryNoUrl;
	getAllFactoryHttpClient.post(getAllFactoryUrl, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, JSONObject response) {
			// TODO 自动生成的方法存根
			super.onSuccess(statusCode, response);
			if(factoryListPd!=null&&factoryListPd.isShowing()){
				factoryListPd.dismiss();
				factoryListPd=null;
			}
			boolean success=false;
			try{
				success=response.getBoolean("success");
				if(success){
					JSONArray data=response.getJSONArray("data");
					if(data.length()>0){
						mdepartList.clear();
						for(int i=0;i<data.length();i++){
							JSONObject mData=(JSONObject) data.get(i);
							String departName=mData.getString("repair_factoryname");
							int departId=mData.getInt("id");
							MydepartmentObject mdepartment=new MydepartmentObject(departName,departId);
							mdepartList.add(mdepartment);
						}
						
					}
					Log.e(TAG, "success is true");
					//showPopupWindow();
				}else{
					Log.e(TAG, "success is false");
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO 自动生成的方法存根
			super.onFailure(e, errorResponse);
			if(factoryListPd!=null&&factoryListPd.isShowing()){
				factoryListPd.dismiss();
				factoryListPd=null;
			}
		}
		
		@Override
		public void onFailure(Throwable e, String errorResponse) {
			// TODO 自动生成的方法存根
			super.onFailure(e, errorResponse);
			if(factoryListPd!=null&&factoryListPd.isShowing()){
				factoryListPd.dismiss();
				factoryListPd=null;
			}
		}
		
	});
}


public void showPopupWindow(){
	int waixiuwidth=waixiudepartTv.getMeasuredWidth();
	int waixiuheight=waixiudepartTv.getMeasuredHeight();
	int[] location=new int[2];
	waixiudepartTv.getLocationOnScreen(location);
	
	View popupView=WaiXiuApplyTask.this.getLayoutInflater().inflate(R.layout.popupwindow_layout, null);
	ListView departList=(ListView)popupView.findViewById(R.id.depart_list);
	
	//mdepartList.add("wai1");
	//mdepartList.add("wai2");
	mdepartListAdapter=new MydepartListAdapter(WaiXiuApplyTask.this,R.layout.itemlist_layout,mdepartList);
	departList.setAdapter(mdepartListAdapter);
	departList.setOnItemClickListener(new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO 自动生成的方法存根
			Log.d(TAG, "position"+position);//start is 0
			MydepartmentObject waixiudepart=mdepartList.get(position);
			if(popupwindow!=null&&popupwindow.isShowing()){
				popupwindow.dismiss();
			}
			waixiudepartTv.setText(waixiudepart.getRepair_factoryname());
			selected_id=waixiudepart.getRepair_id();
		}
		
	});
	//popupwindow=new PopupWindow(popupView,screenWidth/3,screenWidth/3);
	popupwindow=new PopupWindow(popupView,screenWidth,screenHeight/3);
	popupwindow.setAnimationStyle(R.style.popupwindowanim2);
	popupwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
	//popupwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00F8F8F8")));
	 backgroundAlpha(0.5f);
	popupwindow.setFocusable(true);
	popupwindow.setOutsideTouchable(true);
	popupwindow.update();
	/*popupwindow.showAtLocation(waixiudepartTv, Gravity.NO_GRAVITY, location[0]+waixiuwidth/2,location[1]+waixiuheight );
	popupwindow.showAsDropDown(waixiudepartTv,-10, -10);*/
	popupwindow.showAtLocation(waixiudepartTv, Gravity.BOTTOM, 0, 0);
	popupwindow.setOnDismissListener(new OnDismissListener(){

		@Override
		public void onDismiss() {
			// TODO 自动生成的方法存根
			backgroundAlpha(1f);
		}
		
	});
}


private OnDateSetListener ds=new OnDateSetListener(){

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO 自动生成的方法存根
		m_year=year;
		m_month=monthOfYear;
		m_day=dayOfMonth;
		waixiudateTv.setText(m_year+"-"+(m_month+1)+"-"+m_day);
	}
	
};

public void backgroundAlpha(float bgAlpha)  
{  
    WindowManager.LayoutParams lp =getWindow().getAttributes();
    lp.alpha=bgAlpha;
    getWindow().setAttributes(lp); 

}  

public void showDateDialog(){
	DatePickerDialog timerdialog=new DatePickerDialog(WaiXiuApplyTask.this, ds, m_year, m_month, m_day);
	timerdialog.show();
}

public void applyWaixiuByHttp(){
	AsyncHttpClient applywaixiuTaskHttpClient=new AsyncHttpClient();
	applywaixiuTaskHttpClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	applywaixiuTaskHttpClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	String dUrl=MHttpParams.IP;
	String mUrl=WaixiuTaskSP.getString("IP", dUrl);
	String dPort=MHttpParams.DEFAULT_PORT;
	String mPort=WaixiuTaskSP.getString("Port", dPort);
	String applywaixiuUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.applyWaixiuUrl;
	RequestParams requestparams=new RequestParams();
	//int id=MyhttpStorage.id;
	int id=WaixiuTaskSP.getInt("id", -1);
	//int case_id=MyhttpStorage.case_id;
	
	String date=waixiudateTv.getText().toString();
	String timeStamp=toTimeStamp(date);
	String repair_parts=waixiu_peijian_et.getText().toString();
	String parts_price=waixiu_peijianPrice_et.getText().toString();
	String xiufu_price=waixiu_xiufuamount_et.getText().toString();
	//String repair_remark=waixiu_note_et.getText().toString();
	//repair_remark="";//???代替
	requestparams.put("id", String.valueOf(id));
	requestparams.put("case_id",case_idStr);
	requestparams.put("factory_id", String.valueOf(selected_id));//selected_id is the waixiuchoosed
	requestparams.put("repair_parts", repair_parts);
	requestparams.put("repair_price", xiufu_price);
	requestparams.put("parts_price", parts_price);
	requestparams.put("repair_remark", repair_remark);
	requestparams.put("repair_time", timeStamp);
	
	int user_id=WaixiuTaskSP.getInt("id", 0);
	requestparams.put("user_id", String.valueOf(user_id));
	applywaixiuTaskHttpClient.post(applywaixiuUrl, requestparams, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, JSONObject response) {
			// TODO 自动生成的方法存根
			super.onSuccess(statusCode, response);
			boolean success=false;
			try{
				success=response.getBoolean("success");
				if(success){
					Toast.makeText(WaiXiuApplyTask.this, "申请成功", Toast.LENGTH_SHORT).show();
					WaiXiuApplyTask.this.finish();
				}else{
					Toast.makeText(WaiXiuApplyTask.this, "已申请过外修", Toast.LENGTH_SHORT).show();
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
	/*
	 * 
	 */
}

public void queryStateByHttp(){
	queryPd=ProgressDialog.show(WaiXiuApplyTask.this, "加载中", "请稍后");
	AsyncHttpClient queryClient=new AsyncHttpClient();
	queryClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	queryClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	String dUrl=MHttpParams.IP;
	String mUrl=WaixiuTaskSP.getString("IP", dUrl);
	String dPort=MHttpParams.DEFAULT_PORT;
	String mPort=WaixiuTaskSP.getString("Port", dPort);
	String queryUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.queryUrl;
	RequestParams params=new RequestParams();
	//int case_id=caseidStr;
	params.put("case_id", case_idStr);
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
					int repair_state=mData.getInt("repair_state");
					/*int audit_state=mData.getInt("audit_state");
					int loss_state=mData.getInt("loss_state");*/
					if(repair_state==0){
						Toast.makeText(WaiXiuApplyTask.this, "外修任务尚未完成", Toast.LENGTH_SHORT).show();
					}else{
						applyWaixiuByHttp();
					}
				}else{
					Toast.makeText(WaiXiuApplyTask.this, "连接异常", Toast.LENGTH_SHORT).show();
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


public String toTimeStamp(String timeStr){
	String timeStamp="";
	SimpleDateFormat mFormat=new SimpleDateFormat("yyyy-MM-dd");
	Date d;
	Long l;
	try {
		d=mFormat.parse(timeStr);
		l=d.getTime();
		timeStamp=String.valueOf(l);
	} catch (ParseException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
	return timeStamp;
}
}
