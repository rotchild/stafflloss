package com.cx.staffloss;

import java.util.List;

import com.cx.myobject.MyTaskObject;
import com.cx.utils.MUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskRecordAdapter extends ArrayAdapter<MyTaskObject>{
	private int resourceId;
	private Context mContext;
	private List<MyTaskObject> mList;
	private final String WAI="1";
	private final String TUI="2";
	private final String JI="3";
	public TaskRecordAdapter(Context context, int resource,
			List<MyTaskObject> objects) {
		super(context, resource, objects);
		// TODO �Զ����ɵĹ��캯�����
		resourceId=resource;
		mContext=context;
		mList=objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �Զ����ɵķ������
		MyTaskObject taskRecord=getItem(position);
		View view=null;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(mContext).inflate(R.layout.task_item2_layout, null);
			viewHolder=new ViewHolder();
			viewHolder.reportNoTv=(TextView)view.findViewById(R.id.item2_reportNoTV);
			viewHolder.cardNoTv=(TextView)view.findViewById(R.id.item2_carNo);
			viewHolder.vipTagTv=(TextView)view.findViewById(R.id.item2_vipTag);
			
			viewHolder.reportDeportTv=(TextView)view.findViewById(R.id.item2_reportDeport);
			viewHolder.receiveTimeTv=(TextView)view.findViewById(R.id.item2_receiveTimeTv);
			viewHolder.taskStateTv=(TextView)view.findViewById(R.id.item2_taskState);
			viewHolder.taskTypeWTv=(TextView)view.findViewById(R.id.tag2_waixiu);
			viewHolder.taskTypeTTv=(TextView)view.findViewById(R.id.tag2_tuiding);
			viewHolder.taskTypeJTv=(TextView)view.findViewById(R.id.tag2_jihe);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.reportNoTv.setText(taskRecord.getCase_No());
		viewHolder.cardNoTv.setText(taskRecord.getCar_No());
		viewHolder.vipTagTv.setText(taskRecord.getIsvip());//vip���
		viewHolder.reportDeportTv.setText(taskRecord.getParters_name());
		//String timeStampStr=taskRecord.getYard_time();
		//String yardTimeStr=MUtil.getDetailTime(timeStampStr);
		viewHolder.receiveTimeTv.setText(taskRecord.getYard_time());
		viewHolder.taskStateTv.setText(taskRecord.getCase_state());
		String isRepair=taskRecord.getRepair_state();
		String isLoss=taskRecord.getLoss_state();
		String isAudit=taskRecord.getAudit_state();
		
		if(isRepair.equals("-1")){
			viewHolder.taskTypeWTv.setBackgroundResource(R.drawable.tag_shape);
		}else if(isRepair.equals("0")){
			viewHolder.taskTypeWTv.setBackgroundResource(R.drawable.tagred_shape);
			//viewHolder.taskStateTv.setText("��ʼ����");
		}else if(isRepair.equals("1")){
			viewHolder.taskTypeWTv.setBackgroundResource(R.drawable.taggreen_shape);
			//viewHolder.taskStateTv.setText("�������");
		}
		/*else if(isRepair.equals("2")){
			viewHolder.taskTypeWTv.setTextColor(Color.YELLOW);
			viewHolder.taskStateTv.setText("�������");
		}*/
		
		if(isLoss.equals("-1")){
			viewHolder.taskTypeTTv.setBackgroundResource(R.drawable.tag_shape);
		}else if(isLoss.equals("0")){
			viewHolder.taskTypeTTv.setBackgroundResource(R.drawable.tagred_shape);
			//viewHolder.taskStateTv.setText("��ʼ�ƶ�");
		}else if(isLoss.equals("1")){
			viewHolder.taskTypeTTv.setBackgroundResource(R.drawable.taggreen_shape);
			//viewHolder.taskStateTv.setText("�ƶ����");
		}
/*		else if(isLoss.equals("2")){
			viewHolder.taskTypeWTv.setTextColor(Color.YELLOW);
			viewHolder.taskStateTv.setText("�ƶ����");
		}*/
		
		if(isAudit.equals("-1")){
			viewHolder.taskTypeJTv.setBackgroundResource(R.drawable.tag_shape);
		}else if(isAudit.equals("0")){
			viewHolder.taskTypeJTv.setBackgroundResource(R.drawable.tagred_shape);
			//viewHolder.taskStateTv.setText("��ʼ����");
		}else if(isAudit.equals("1")){
			viewHolder.taskTypeJTv.setBackgroundResource(R.drawable.taggreen_shape);
			//viewHolder.taskStateTv.setText("������");
		}
/*		else if(isAudit.equals("2")){
			viewHolder.taskTypeWTv.setTextColor(Color.YELLOW);
			viewHolder.taskStateTv.setText("�������");
		}*/
		/*String taskType=taskRecord.getTaskType();
		
		if(taskType.equals(WAI)){
			viewHolder.taskTypeWTv.setBackgroundColor(Color.YELLOW);
		}else if(taskType.equals(TUI)){
			viewHolder.taskTypeTTv.setBackgroundColor(Color.YELLOW);
		}else if(taskType.equals(JI)){
			viewHolder.taskTypeJTv.setBackgroundColor(Color.YELLOW);
		}*/
		return view;
	}
	
	@Override
	public MyTaskObject getItem(int position) {
		// TODO �Զ����ɵķ������
		if(null!=mList&&position<getCount()){
			return mList.get(position);
		}
		return null;
	}
	
	@Override
	public long getItemId(int position) {
		// TODO �Զ����ɵķ������
		return position;
	}

	
	@Override
	public int getCount() {
		// TODO �Զ����ɵķ������
		if(null!=mList){
			return mList.size();
		}
		return 0;
	}



	class ViewHolder{
	TextView reportNoTv,cardNoTv,vipTagTv,reportDeportTv,receiveTimeTv,taskStateTv,taskTypeWTv,taskTypeTTv,taskTypeJTv,
	vipTgTv;
	}
}
