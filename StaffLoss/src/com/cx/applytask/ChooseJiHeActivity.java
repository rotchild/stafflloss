package com.cx.applytask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.myobject.JiheRenObject;
import com.cx.myobject.MyJiHeListAdapter;
import com.cx.myobject.MyJiHeListAdapter.Callback;
import com.cx.myobject.MyTaskObject;
import com.cx.netset.MHttpParams;
import com.cx.netset.MyhttpStorage;
import com.cx.staffloss.DealingTaskDetail;
import com.cx.staffloss.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChooseJiHeActivity extends Activity implements Callback{
	MyTaskObject chooseTaskObject;//just for chuandi
	Button chooseOkBtn,chooseCancelBtn;
	ListView jiheRenList;
	List<JiheRenObject> jiheRenListData;
	EditText searchEt;
	Button searchBtn;
	public static final String TAG="chooseJiHe";
	int SelectPosition=-1;
	public static ChooseJiHeActivity chooseJiheActiviy=null;
	public static String SPName="StaffLossSP";
	SharedPreferences chooseJiheSP;
	ProgressDialog pd;
	MyJiHeListAdapter mJiHeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosejihe_layout);
		chooseTaskObject=(MyTaskObject) this.getIntent().getSerializableExtra("jiselectTaskObject");
		chooseJiheActiviy=this;
		initView();
	}
public void initView(){
	
	chooseJiheSP=getSharedPreferences(SPName,0);
	
	searchEt=(EditText)findViewById(R.id.search_text);
	searchBtn=(Button)findViewById(R.id.search_btn);
	

	
	jiheRenList=(ListView)findViewById(R.id.jiherenlist);
	/*JiheRenObject(String jiherenName, String gonghao, String tasknum,
			String jiherenTel)
	*/

	jiheRenListData=new ArrayList<JiheRenObject>();
	/*JiheRenObject mJiHeren=new JiheRenObject("0","王松","20112222","3","12222222");
	
	jiheRenListData.add(mJiHeren);*/
	pd=ProgressDialog.show(ChooseJiHeActivity.this, "请稍候", "加载中...");
	getJiheUserList();//获取稽核人列表
    mJiHeAdapter=new MyJiHeListAdapter(ChooseJiHeActivity.this,R.layout.jiheren_item_layout,jiheRenListData,this);
	jiheRenList.setAdapter(mJiHeAdapter);
	//有了adpater之后
	searchBtn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			String keyword=searchEt.getText().toString();
			mJiHeAdapter.getFilter().filter(keyword);
		}
		
	});
	searchEt.addTextChangedListener(new TextWatcher(){

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO 自动生成的方法存根
			if(s.toString().length()<=0){
				mJiHeAdapter.getFilter().filter("");//不作筛选
			}
		}
		
	});
	
	
	jiheRenList.setOnItemClickListener(new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO 自动生成的方法存根
			//mJiHeAdapter.changeSelected(position);//refresh
			//view.setBackgroundColor(Color.parseColor("#AEAEAE"));
			Log.d(TAG, "postion"+position);
			SelectPosition=mJiHeAdapter.setSelectID(position);
			mJiHeAdapter.notifyDataSetChanged();
			JiheRenObject selectedObject=jiheRenListData.get(position);//start position 0
			String jiherenId=selectedObject.getJiherenId();
			Log.d("ChooseActivity", "position"+position);
			MyhttpStorage.jiherenId=jiherenId;
			/*Bundle bundle=new Bundle();
			bundle.putSerializable("jichooseTaskObject", chooseTaskObject);
			Intent toJiheApply=new Intent(ChooseJiHeActivity.this,JiHeApplyTask.class);
			toJiheApply.putExtras(bundle);
			startActivity(toJiheApply);*/
			
		}
		
	});
	

	
	
	chooseOkBtn=(Button)findViewById(R.id.choose_ok_btn);
	chooseCancelBtn=(Button)findViewById(R.id.choose_cancel_btn);
	
	chooseOkBtn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			if(SelectPosition==-1){
				Toast.makeText(ChooseJiHeActivity.this, "请选择一个稽核人员", Toast.LENGTH_SHORT).show();
			}else{
				Bundle bundle=new Bundle();
				bundle.putSerializable("jichooseTaskObject", chooseTaskObject);
				//Intent toApplyJiHe=new Intent(DealingTaskDetail.this,JiHeApplyTask.class);
				Intent toApplyJiHe=new Intent(ChooseJiHeActivity.this,JiHeApplyTask.class);
				toApplyJiHe.putExtras(bundle);
				startActivity(toApplyJiHe);
			}

		}
		
	});
	
	chooseCancelBtn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			ChooseJiHeActivity.this.finish();
		}
		
	});
	
	
	
}
@Override
public void click(View v) {
	// TODO 自动生成的方法存根
	String telStr=jiheRenListData.get((Integer)v.getTag()).getJiherenTel();
	Intent toTel=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+telStr));
	startActivity(toTel);
}	
public void getJiheUserList(){
	AsyncHttpClient getJiheUserClient=new AsyncHttpClient();
	getJiheUserClient.addHeader("Charset", MHttpParams.DEFAULT_CHARSET);
	getJiheUserClient.setTimeout(MHttpParams.DEFAULT_TIME_OUT);
	String dUrl=MHttpParams.IP;
    String mUrl=chooseJiheSP.getString("IP", dUrl);
    String dPort=MHttpParams.DEFAULT_PORT;
    String mPort=chooseJiheSP.getString("Port", dPort);
	String getJiheUserUrl="http://"+mUrl+":"+mPort+"/"+MHttpParams.getJiheUrl;
	RequestParams params=new RequestParams();
	//int user_id=MyhttpStorage.id;
	int user_id=chooseJiheSP.getInt("id", -1);
	getJiheUserClient.post(getJiheUserUrl, params, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, JSONObject response) {
			// TODO 自动生成的方法存根
			super.onSuccess(statusCode, response);
			boolean success=false;
			if(pd!=null&&pd.isShowing()){
				pd.dismiss();
			}
			try{
				success=response.getBoolean("success");
				if(success){
					jiheRenListData.clear();
					JSONArray data=response.getJSONArray("data");
					if(data.length()>0){
						for(int i=0;i<data.length();i++){
							JSONObject mdata=data.getJSONObject(i);
							int jiherenIdInt=mdata.getInt("user_id");
							String jiherenId=String.valueOf(jiherenIdInt);
							String jiherenName=mdata.getString("real_name");
							String gonghao=mdata.getString("user_name");//工号
							int tasknumInt=mdata.getInt("audit_count");
							String tasknum=String.valueOf(tasknumInt);
							String jiheTel=mdata.getString("telephone");
							JiheRenObject mjiheRen=new JiheRenObject(jiherenId,jiherenName,gonghao,tasknum,jiheTel);
							jiheRenListData.add(mjiheRen);
						}
						
					}
				mJiHeAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(ChooseJiHeActivity.this, "加载失败，请重新加载", Toast.LENGTH_SHORT).show();
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
			if(pd!=null&&pd.isShowing()){
				pd.dismiss();
			}
		}
		
	});
}	
}
