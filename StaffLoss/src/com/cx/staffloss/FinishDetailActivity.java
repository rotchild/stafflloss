package com.cx.staffloss;

import com.cx.myobject.MyFinishObject;
import com.cx.myobject.MyNoteShowDialog;
import com.cx.myobject.MyTaskObject;
import com.cx.utils.MUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FinishDetailActivity extends Activity {
	MyFinishObject selectTaskObject;
	TextView reportNo_tv,carNo_tv,carType_tv,isTheCar_tv,theCarNo_tv,reportDeport_tv,
	deportConnector_tv,deportConnectorphone_tv,reporterName_tv,theReporterPhone_tv,thefenpeiTime_tv,theAboutMoney_tv,dingsun_money,
	finishDetailBack_tv;
	
	TextView repairDate_tv,repairFactory_tv,repairParts_tv,partsPrice_tv,repairPrice_tv;
	//TextView repairRemark_tv;
	
	TextView lossStartTime_tv;
	//TextView lossRemark_tv;
	TextView auditStartTime_tv;
	//TextView auditRemark_tv;
	ImageView finishDetailBack_iv;
	
	Button waixiuLookBtn,tuidingLookBtn,jiheLookBtn;//查看备注
	MyNoteShowDialog mNoteShowDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finishdetail_layout);
		selectTaskObject=(MyFinishObject) this.getIntent().getSerializableExtra("selectFtask");
		
		initView();
	}
public void initView(){
	
	finishDetailBack_iv=(ImageView)findViewById(R.id.finishdetailback_iv);
	finishDetailBack_iv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			FinishDetailActivity.this.finish();
		}
		
	});
	
	/*finishDetailBack_tv=(TextView)findViewById(R.id.finishdetailback_tv);
	finishDetailBack_tv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			FinishDetailActivity.this.finish();
		}
		
	});*/
	
	
	reportNo_tv=(TextView)findViewById(R.id.finishdetail_reportNo_tv);
	carNo_tv=(TextView)findViewById(R.id.finishdetail_carNo_tv);
	carType_tv=(TextView)findViewById(R.id.finishdetail_carType_tv);
	isTheCar_tv=(TextView)findViewById(R.id.finishdetail_isTheCar_tv);
	theCarNo_tv=(TextView)findViewById(R.id.finishdetail_theCarNo_tv);
	reportDeport_tv=(TextView)findViewById(R.id.finishdetail_reportDeport_tv);
	deportConnector_tv=(TextView)findViewById(R.id.finishdetail_deportConnector_tv);
	deportConnectorphone_tv=(TextView)findViewById(R.id.finishdetail_deportConnectorphone_tv);
	reporterName_tv=(TextView)findViewById(R.id.finishdetail_reporterName_tv);
	theReporterPhone_tv=(TextView)findViewById(R.id.finishdetail_theReporterPhone_tv);
	thefenpeiTime_tv=(TextView)findViewById(R.id.finishdetail_thefenpeitime_tv);
	theAboutMoney_tv=(TextView)findViewById(R.id.finishdetail_theAboutMoney_tv);
	dingsun_money=(TextView)findViewById(R.id.finishdetail_dingsun_money_tv);
	
	
	
	
	String reportStr=selectTaskObject.getCase_No();
	String carNoStr=selectTaskObject.getCar_No();
	String carTypeStr=selectTaskObject.getBrand_name();
	String isTheCarStr=selectTaskObject.getIs_target();
	String theCarNoStr=selectTaskObject.getTarget_No();
	String reportDeportStr=selectTaskObject.getParters_name();
	String deportConnectorStr=selectTaskObject.getParter_manager();
	String deportConnectorPhoneStr=selectTaskObject.getParter_mobile();
	String reporterNameStr=selectTaskObject.getDelivery_name();
	String reporterPhone=selectTaskObject.getDelivery_mobile();
	String theFenpeiStr=selectTaskObject.getYard_time();
	String theAboutMoneyStr=selectTaskObject.getLoss_price();
	String dingsunMoneyStr=selectTaskObject.getDingsun_price();
	
	reportNo_tv.setText(reportStr);
	carNo_tv.setText(carNoStr);
	carType_tv.setText(carTypeStr);
	isTheCar_tv.setText(isTheCarStr);
	theCarNo_tv.setText(theCarNoStr);
	reportDeport_tv.setText(reportDeportStr);
	deportConnector_tv.setText(deportConnectorStr);
	deportConnectorphone_tv.setText(deportConnectorPhoneStr);
	reporterName_tv.setText(reporterNameStr);
	theReporterPhone_tv.setText(reporterPhone);
	String theFenpeiSDFStr=MUtil.getStrTime(theFenpeiStr);
	thefenpeiTime_tv.setText(theFenpeiSDFStr);
	theAboutMoney_tv.setText(theAboutMoneyStr);
	dingsun_money.setText(dingsunMoneyStr);
	//--------------------isVisible?-----------------
	String repairState=selectTaskObject.getWaixiu_state();
	String lossState=selectTaskObject.getLoss_state();
	String auditState=selectTaskObject.getAudit_state();


	//repairDate_tv,repairFactory_tv,repairParts_tv,partsPrice_tv,repairPrice_tv,repairRemark_tv
	if(repairState.equals("1")){
		repairDate_tv=(TextView)findViewById(R.id.finish_waixiu_date_tv);
		repairFactory_tv=(TextView)findViewById(R.id.waixiu_danwei_tv);
		repairParts_tv=(TextView)findViewById(R.id.waixiu_peijian_tv);
		partsPrice_tv=(TextView)findViewById(R.id.waixiu_peijianprice_tv);
		repairPrice_tv=(TextView)findViewById(R.id.waixiu_repairprice_tv);
		//repairRemark_tv=(TextView)findViewById(R.id.waixiu_note_tv);
		waixiuLookBtn=(Button)findViewById(R.id.waixiu_look_btn);

		String repairDateStamp=selectTaskObject.getRepair_time();
		String repairTime=MUtil.getStrTime(repairDateStamp);
		String repairFactory=selectTaskObject.getRepair_factory();
		String repairParts=selectTaskObject.getRepair_parts();
		String partsPrice=selectTaskObject.getParts_price();
		String repairPrice=selectTaskObject.getRepair_price();
		final String repairRemark=selectTaskObject.getRepair_remark();
		
		repairDate_tv.setText(repairTime);
		repairFactory_tv.setText(repairFactory);
		repairParts_tv.setText(repairParts);
		partsPrice_tv.setText(partsPrice);
		repairPrice_tv.setText(repairPrice);
		//repairRemark_tv.setText(repairRemark);
		waixiuLookBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showNoteDialog(repairRemark);
			}
			
		});
		
		LinearLayout waixiupartLinear=(LinearLayout)findViewById(R.id.waixiu_part_linear);
		RelativeLayout repairTimeLayout=(RelativeLayout)findViewById(R.id.finish_repair_date_rl);
		RelativeLayout repairFactoryLayout=(RelativeLayout)findViewById(R.id.repair_factory_rl);
		RelativeLayout repairPartsLayout=(RelativeLayout)findViewById(R.id.repair_parts_rl);
		RelativeLayout repairPartsPriceLayout=(RelativeLayout)findViewById(R.id.repair_partsprice_rl);
		RelativeLayout repairPriceLayout=(RelativeLayout)findViewById(R.id.repair_price_rl);
		RelativeLayout repairRemarkLayout=(RelativeLayout)findViewById(R.id.repair_remark_rl);
		
		LinearLayout linear1=(LinearLayout)findViewById(R.id.linear1);
		LinearLayout linear2=(LinearLayout)findViewById(R.id.linear2);
		LinearLayout linear3=(LinearLayout)findViewById(R.id.linear3);
		LinearLayout linear4=(LinearLayout)findViewById(R.id.linear4);
		LinearLayout linear5=(LinearLayout)findViewById(R.id.linear5);
		
		LinearLayout waixiuDividerLayout=(LinearLayout)findViewById(R.id.waixiudivider_linear);
		
		waixiupartLinear.setVisibility(View.VISIBLE);//全部的方式无效
		waixiuDividerLayout.setVisibility(View.VISIBLE);
		repairTimeLayout.setVisibility(View.VISIBLE);
		repairFactoryLayout.setVisibility(View.VISIBLE);
		repairPartsLayout.setVisibility(View.VISIBLE);
		repairPartsPriceLayout.setVisibility(View.VISIBLE);
		repairPriceLayout.setVisibility(View.VISIBLE);
		repairRemarkLayout.setVisibility(View.VISIBLE);
		
		linear1.setVisibility(View.VISIBLE);
		linear2.setVisibility(View.VISIBLE);
		linear3.setVisibility(View.VISIBLE);
		linear4.setVisibility(View.VISIBLE);
		linear5.setVisibility(View.VISIBLE);
	}
	
	if(lossState.equals("1")){
		lossStartTime_tv=(TextView)findViewById(R.id.loss_startTime_tv);
		//lossRemark_tv=(TextView)findViewById(R.id.loss_remark_tv);
		tuidingLookBtn=(Button)findViewById(R.id.loss_look_btn);
		String lossStartTimeStamp=selectTaskObject.getLossStartTimeStamp();
		final String lossRemark=selectTaskObject.getLossRemark();
		String lossStartTime=MUtil.getStrTime(lossStartTimeStamp);
		lossStartTime_tv.setText(lossStartTime);
		//lossRemark_tv.setText(lossRemark);
		tuidingLookBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showNoteDialog(lossRemark);
			}
			
		});
		RelativeLayout loss_starttimeRelative=(RelativeLayout)findViewById(R.id.loss_starttime_rl);
		RelativeLayout loss_remarkRelative=(RelativeLayout)findViewById(R.id.loss_remark_rl);
		
		LinearLayout linear6=(LinearLayout)findViewById(R.id.linear6);
		LinearLayout linear7=(LinearLayout)findViewById(R.id.linear7);
		
		
		loss_starttimeRelative.setVisibility(View.VISIBLE);
		loss_remarkRelative.setVisibility(View.VISIBLE);
		
		linear6.setVisibility(View.VISIBLE);
		linear7.setVisibility(View.VISIBLE);
	}
	
	if(auditState.equals("1")){
		auditStartTime_tv=(TextView)findViewById(R.id.audit_starttime_tv);
		//auditRemark_tv=(TextView)findViewById(R.id.audit_remark_tv);
		jiheLookBtn=(Button)findViewById(R.id.audit_look_btn);
		String auditStartTimeStamp=selectTaskObject.getAuditStartTimeStamp();
		final String auditRemark=selectTaskObject.getAuditRemark();
		String auditStartTime=MUtil.getStrTime(auditStartTimeStamp);
		auditStartTime_tv.setText(auditStartTime);
		//auditRemark_tv.setText(auditRemark);
		jiheLookBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showNoteDialog(auditRemark);
			}
			
		});
		
		RelativeLayout audit_starttimeRelative=(RelativeLayout)findViewById(R.id.audit_starttime_rl);
		RelativeLayout audit_RemarkRelative=(RelativeLayout)findViewById(R.id.audit_remark_rl);
		
		LinearLayout linear8=(LinearLayout)findViewById(R.id.linear8);
		LinearLayout linear9=(LinearLayout)findViewById(R.id.linear9);
		
		audit_starttimeRelative.setVisibility(View.VISIBLE);
		audit_RemarkRelative.setVisibility(View.VISIBLE);
		
		linear8.setVisibility(View.VISIBLE);
		linear9.setVisibility(View.VISIBLE);
	}
	
}

public void showNoteDialog(String remark){
	TextView noteTv;
	mNoteShowDialog=new MyNoteShowDialog(FinishDetailActivity.this,R.style.Dialog);
	mNoteShowDialog.setMyNoteShowDialog();
	String noteTitleStr="备注";
	mNoteShowDialog.setTitle(noteTitleStr);
	noteTv=(TextView)mNoteShowDialog.getNoteText();
	noteTv.setText(remark);
	mNoteShowDialog.setCloseBtn(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			if(mNoteShowDialog!=null&&mNoteShowDialog.isShowing()){
				mNoteShowDialog.dismiss();
				mNoteShowDialog=null;
			}
		}
		
	});
	mNoteShowDialog.show();
}
}
