package com.cx.myobject;

import java.util.ArrayList;
import java.util.List;

public class MyUndealList{
public static List<MyTaskObject> mList=new ArrayList<MyTaskObject>();
public static void add(MyTaskObject taskObject){
	mList.add(taskObject);
}
public static void remove(MyTaskObject taskObject){
	if(mList.contains(taskObject)){
		mList.remove(taskObject);
	}
}

public static void clear(){
mList.clear();
}

}
