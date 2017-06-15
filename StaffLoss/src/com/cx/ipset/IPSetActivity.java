package com.cx.ipset;


import com.cx.netset.MHttpParams;
import com.cx.staffloss.R;
import com.cx.utils.MRegex;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class IPSetActivity extends Activity {
EditText ipSetEt,portSetEt;
Button okBtn;
ImageView ipbackIV;
SharedPreferences ipSetSP;

public static final String SPName="StaffLossSP";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipset_layout);
		initView();
	}
public void initView(){
	ipbackIV=(ImageView)findViewById(R.id.ipback_iv);
	ipbackIV.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			IPSetActivity.this.finish();
		}
		
	});
	
	/*finishBtn=(Button)findViewById(R.id.ipsefinish_btn);
	finishBtn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			IPSetActivity.this.finish();
		}
		
	});*/
	
	ipSetSP=getSharedPreferences(SPName,0);
	ipSetEt=(EditText)findViewById(R.id.ipset_et);
	portSetEt=(EditText)findViewById(R.id.portset_et);
	String ipPre=ipSetSP.getString("IP", MHttpParams.IP);
	String portPre=ipSetSP.getString("Port", MHttpParams.DEFAULT_PORT);
	ipSetEt.setText(ipPre);
	portSetEt.setText(portPre);
	okBtn=(Button)findViewById(R.id.ipsetOk_btn);
	okBtn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			if(ipSetEt==null||ipSetEt.length()<=0||portSetEt==null||portSetEt.length()<=0){
				Toast.makeText(IPSetActivity.this, "IP��˿����ò���Ϊ��", Toast.LENGTH_SHORT).show();
			}else{
				String ipsetStr=ipSetEt.getText().toString();
				String portsetStr=portSetEt.getText().toString();

				if(MRegex.isIPV4(ipsetStr)){
					if(MRegex.isPort(portsetStr)){
						SharedPreferences.Editor ipsetEditor=ipSetSP.edit();
						ipsetEditor.putString("IP", ipsetStr);
						ipsetEditor.putString("Port", portsetStr);
						boolean result=ipsetEditor.commit();
						if(result){
							Toast.makeText(IPSetActivity.this, "���óɹ�", Toast.LENGTH_SHORT).show();
							IPSetActivity.this.finish();
						}
					}else{
						Toast.makeText(IPSetActivity.this, "�˿ڸ�ʽ����", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(IPSetActivity.this, "IP��ʽ����", Toast.LENGTH_SHORT).show();
				}
	
			}

		}
		
	});
}
}
