package com.cx.myobject;

import java.util.List;

import com.cx.staffloss.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MydepartListAdapter extends ArrayAdapter<MydepartmentObject>{

	private int mResource;
	private Context mContext;
	private List<MydepartmentObject> mList;
	public MydepartListAdapter(Context context, int resource,
			List<MydepartmentObject> objects) {
		super(context, resource, objects);
		// TODO �Զ����ɵĹ��캯�����
		mContext=context;
		mResource=resource;
		mList=objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �Զ����ɵķ������
		//return super.getView(position, convertView, parent);
		MydepartmentObject departSelect=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(mContext).inflate(mResource, null);
			viewHolder=new ViewHolder();
			viewHolder.departTv=(TextView)view.findViewById(R.id.depart_item);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
			
		}
		viewHolder.departTv.setText(departSelect.getRepair_factoryname());
		return view;
	}
	
	
	
@Override
	public int getCount() {
		// TODO �Զ����ɵķ������
	if(null!=mList){
		return mList.size();
	}
	Log.e("getView", "mList is null");
	return 0;
	}




@Override
public MydepartmentObject getItem(int position) {
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




class ViewHolder{
	TextView departTv;
}
}
