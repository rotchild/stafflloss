package com.cx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class MUtil {
public static String SPName="StaffLossSP";
public static String getStrTime(String timeStamp){//timeStampTotimeStr
	String timeStr=null;
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	long timeStampLong=Long.valueOf(timeStamp);
	timeStr=sdf.format(new Date(timeStampLong));
	return timeStr;
}
public static String getDetailTime(String timeStamp){
	String timeStr=null;
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  HH:mm");
	long timeStampLong=Long.valueOf(timeStamp);
	timeStr=sdf.format(new Date(timeStampLong));
	return timeStr;
}

public static String stateChange(int case_code){
	String case_stateResult="";
	switch(case_code){
	case 0:
		case_stateResult="未分配";
		break;
	case 1:
		case_stateResult="待定损";
		break;
	case 2:
		case_stateResult="定损中";
		break;
	case 3:
		case_stateResult="已完成";
		break;
	default:
		break;
	}
	return case_stateResult;
}

public static void saveMap(Context context,Map<String,String>mMap){
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

public static Map<String,String> getMap(Context context,String mapObjStr){
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
