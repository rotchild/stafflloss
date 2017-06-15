package com.cx.myobject;

public class JiheRenObject {
private String jiherenId,jiherenName,gonghao,tasknum,jiherenTel;

public JiheRenObject(String jiherenId,String jiherenName, String gonghao, String tasknum,
		String jiherenTel) {
	super();
	this.jiherenId=jiherenId;
	this.jiherenName = jiherenName;
	this.gonghao = gonghao;
	this.tasknum = tasknum;
	this.jiherenTel = jiherenTel;
}

public String getJiherenName() {
	return jiherenName;
}

public void setJiherenName(String jiherenName) {
	this.jiherenName = jiherenName;
}

public String getGonghao() {
	return gonghao;
}

public void setGonghao(String gonghao) {
	this.gonghao = gonghao;
}

public String getTasknum() {
	return tasknum;
}

public void setTasknum(String tasknum) {
	this.tasknum = tasknum;
}

public String getJiherenTel() {
	return jiherenTel;
}

public void setJiherenTel(String jiherenTel) {
	this.jiherenTel = jiherenTel;
}

public String getJiherenId() {
	return jiherenId;
}

public void setJiherenId(String jiherenId) {
	this.jiherenId = jiherenId;
}

}
