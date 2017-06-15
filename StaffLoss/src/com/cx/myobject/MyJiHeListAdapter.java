package com.cx.myobject;

import java.util.ArrayList;
import java.util.List;

import com.cx.staffloss.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Interpolator.Result;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class MyJiHeListAdapter extends ArrayAdapter<JiheRenObject> implements OnClickListener,Filterable{
	private int mResource;
	private Context mContext;
	private List<JiheRenObject> mList;
	int mSelect=0;
	private int selectID=-1;
	
	private final Object mLock=new Object();
	private ArrayList<JiheRenObject> mOriginalValues;
	private ArrayFilter mFilter;
	
	private Callback mCallback;
	
	public interface Callback{
		public void click(View v);
	}
	
	public MyJiHeListAdapter(Context context, int resource,
			List<JiheRenObject> objects,Callback callback) {
		super(context, resource, objects);
		// TODO 自动生成的构造函数存根
		mContext=context;
		mResource=resource;
		mList=objects;
		mCallback=callback;
	}
	
	
	public int setSelectID(int position){
		selectID=position;
		return position;
	}
	
	public void changeSelected(int position){
		if(position!=mSelect){
			mSelect=position;
			notifyDataSetChanged();
		}
	}
	

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if(null!=mList){
			return mList.size();
		}
		Log.e("getView", "mList is null");
		return 0;
	}

	@Override
	public JiheRenObject getItem(int position) {
		// TODO 自动生成的方法存根
		if(null!=mList&&position<getCount()){
			return mList.get(position);
		}
		return null;
	}

	@Override
	public int getPosition(JiheRenObject item) {
		// TODO 自动生成的方法存根
		return super.getPosition(item);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		final JiheRenObject mJiheren=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
		view=LayoutInflater.from(mContext).inflate(mResource, null);
		viewHolder=new ViewHolder();
		viewHolder.chooseRaido=(RadioButton) view.findViewById(R.id.jihe_radio);
		viewHolder.jiheRenName=(TextView)view.findViewById(R.id.jiheren_name);
		viewHolder.gonghao=(TextView)view.findViewById(R.id.gonghao_tv);
		viewHolder.tasknum=(TextView)view.findViewById(R.id.task_num);
		viewHolder.jiherenTel=(ImageButton)view.findViewById(R.id.calljihe_btn);
		view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}

		viewHolder.jiheRenName.setText(mJiheren.getJiherenName());
		viewHolder.gonghao.setText(mJiheren.getGonghao());
		viewHolder.tasknum.setText(mJiheren.getTasknum());
		viewHolder.jiherenTel.setOnClickListener(this);
		viewHolder.jiherenTel.setTag(position);
        if(selectID==position){
        	viewHolder.chooseRaido.setChecked(true);
        }else{
        	viewHolder.chooseRaido.setChecked(false);
        }
		return view;
	}

	class ViewHolder{
		RadioButton chooseRaido;
		TextView jiheRenName,gonghao,tasknum;
		ImageButton jiherenTel;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		mCallback.click(v);
	}
	
	
	
	
	@Override
	public Filter getFilter() {
		// TODO 自动生成的方法存根
		if(mFilter==null){
			mFilter=new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			// TODO 自动生成的方法存根
			FilterResults results=new FilterResults();
			if(mOriginalValues==null){
				synchronized(mLock){
					mOriginalValues=new ArrayList<JiheRenObject>(mList);
				}
				
			}
			if(prefix==null||prefix.length()==0){
				ArrayList<JiheRenObject> list;
				synchronized(mLock){
					list=new ArrayList<JiheRenObject>(mOriginalValues);
				}
				results.values=list;
				results.count=list.size();
			}else{
				String prefixString=prefix.toString().toLowerCase();
				ArrayList<JiheRenObject> values;
				synchronized(mLock){
					values=new ArrayList<JiheRenObject>(mOriginalValues);
				}
				final int count=values.size();
				final ArrayList<JiheRenObject> newValues=new ArrayList<JiheRenObject>();
				for(int i=0;i<count;i++){
					final JiheRenObject value=values.get(i);
					final String valueTextAll=value.getJiherenName()+value.getGonghao()+value.getTasknum();//所有属性做关键字
					final String valueText=valueTextAll.toLowerCase();
					//final String valueText=value.getJiherenName().toLowerCase();
					if(valueText.indexOf(prefixString)!=-1){
						newValues.add(value);
					}
				}
				results.values=newValues;
				results.count=newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// TODO 自动生成的方法存根
			mList=(List<JiheRenObject>) results.values;	
			if(results.count>0){
				notifyDataSetChanged();
			}else{
				notifyDataSetInvalidated();
			}
		}
		
	}
	
	
	
}
