package com.cx.staffloss;

import java.util.List;

import com.cx.myobject.MyFinishObject;
import com.cx.myobject.MyTaskObject;
import com.cx.utils.MUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FinishTaskRecordAdapter extends ArrayAdapter<MyFinishObject>{
	private int resourceId;
	private Context mContext;
	private List<MyFinishObject> mList;
	private final String WAI="1";
	private final String TUI="2";
	private final String JI="3";
	public FinishTaskRecordAdapter(Context context, int resource,
			List<MyFinishObject> objects) {
		super(context, resource, objects);
		// TODO 自动生成的构造函数存根
		resourceId=resource;
		mContext=context;
		mList=objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		MyFinishObject taskRecord=getItem(position);
		View view=null;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(mContext).inflate(R.layout.task_item2_layout, null);
			viewHolder=new ViewHolder();
			viewHolder.reportNoTv=(TextView)view.findViewById(R.id.item2_reportNoTV);
			viewHolder.cardNoTv=(TextView)view.findViewById(R.id.item2_carNo);
			viewHolder.reportDeportTv=(TextView)view.findViewById(R.id.item2_reportDeport);
			viewHolder.receiveTimeTv=(TextView)view.findViewById(R.id.item2_receiveTimeTv);
			
			viewHolder.taskStateTv=(TextView)view.findViewById(R.id.item2_taskState);
			viewHolder.waixiuTagTv=(TextView)view.findViewById(R.id.tag2_waixiu);
			viewHolder.tuidingTagTv=(TextView)view.findViewById(R.id.tag2_tuiding);
			viewHolder.jiheTagTv=(TextView)view.findViewById(R.id.tag2_jihe);
/*			viewHolder.taskStateTv=(TextView)view.findViewById(R.id.taskState);
			viewHolder.taskTypeWTv=(TextView)view.findViewById(R.id.tag_waixiu);
			viewHolder.taskTypeTTv=(TextView)view.findViewById(R.id.tag_tuiding);
			viewHolder.taskTypeJTv=(TextView)view.findViewById(R.id.tag_jihe);*/
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.reportNoTv.setText(taskRecord.getCase_No());
		viewHolder.cardNoTv.setText(taskRecord.getCar_No());
		viewHolder.reportDeportTv.setText(taskRecord.getParters_name());
		String receiveTimeStamp=taskRecord.getYard_time();
		String receiveTimeStr=MUtil.getDetailTime(receiveTimeStamp);
		viewHolder.receiveTimeTv.setText(receiveTimeStr);
		
		viewHolder.taskStateTv.setText("定损完成");
		String waixiuState=taskRecord.getWaixiu_state();
		String auditState=taskRecord.getAudit_state();
		String tuidingState=taskRecord.getLoss_state();
		viewHolder.waixiuTagTv.setBackgroundResource(R.drawable.tag_shape);
		viewHolder.tuidingTagTv.setBackgroundResource(R.drawable.tag_shape);
		viewHolder.jiheTagTv.setBackgroundResource(R.drawable.tag_shape);
		if(waixiuState.equals("0")){
			viewHolder.waixiuTagTv.setBackgroundResource(R.drawable.tagred_shape);
			viewHolder.tuidingTagTv.setBackgroundResource(R.drawable.tagred_shape);
			viewHolder.jiheTagTv.setBackgroundResource(R.drawable.tagred_shape);
		}else if(waixiuState.equals("1")){
			viewHolder.waixiuTagTv.setBackgroundResource(R.drawable.taggreen_shape);
			viewHolder.tuidingTagTv.setBackgroundResource(R.drawable.taggreen_shape);
			viewHolder.jiheTagTv.setBackgroundResource(R.drawable.taggreen_shape);
		}
		
		

		//viewHolder.taskStateTv.setText(taskRecord.getCase_state());
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
	public MyFinishObject getItem(int position) {
		// TODO 自动生成的方法存根
		if(null!=mList&&position<getCount()){
			return mList.get(position);
		}
		return null;
	}
	
	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if(null!=mList){
			return mList.size();
		}
		return 0;
	}



	class ViewHolder{
	TextView reportNoTv,cardNoTv,vipTagTv,reportDeportTv,receiveTimeTv;
	TextView taskStateTv,waixiuTagTv,tuidingTagTv,jiheTagTv;
	}
}
