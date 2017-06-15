package com.cx.myobject;

import java.io.Serializable;

public class MyTaskObject implements Serializable{
private static final long serialVersionUID=1L;//???
private String case_id,case_No,car_No,brand_name,is_target,target_No,isvip,parter_id,parters_name,
parter_manager,parter_mobile,delivery_name,delivery_mobile,loss_price,yard_time,case_state, repair_state,
audit_state,loss_state;



public MyTaskObject(String case_id, String case_No, String car_No,
		String brand_name, String is_target, String target_No, String isvip,
		String parter_id, String parters_name, String parter_manager,
		String parter_mobile, String delivery_name, String delivery_mobile,
		String loss_price, String yard_time, String case_state,
		String repair_state, String audit_state, String loss_state) {
	super();
	this.case_id = case_id;
	this.case_No = case_No;
	this.car_No = car_No;
	this.brand_name = brand_name;
	this.is_target = is_target;
	this.target_No = target_No;
	this.isvip = isvip;
	this.parter_id = parter_id;
	this.parters_name = parters_name;
	this.parter_manager = parter_manager;
	this.parter_mobile = parter_mobile;
	this.delivery_name = delivery_name;
	this.delivery_mobile = delivery_mobile;
	this.loss_price = loss_price;
	this.yard_time = yard_time;
	this.case_state = case_state;
	this.repair_state = repair_state;
	this.audit_state = audit_state;
	this.loss_state = loss_state;
}
public String getCase_No() {
	return case_No;
}
public void setCase_No(String case_No) {
	this.case_No = case_No;
}
public String getCar_No() {
	return car_No;
}
public void setCar_No(String car_No) {
	this.car_No = car_No;
}
public String getBrand_name() {
	return brand_name;
}
public void setBrand_name(String brand_name) {
	this.brand_name = brand_name;
}
public String getIs_target() {
	return is_target;
}
public void setIs_target(String is_target) {
	this.is_target = is_target;
}
public String getTarget_No() {
	return target_No;
}
public void setTarget_No(String target_No) {
	this.target_No = target_No;
}
public String getIsvip() {
	return isvip;
}
public void setIsvip(String isvip) {
	this.isvip = isvip;
}
public String getParter_id() {
	return parter_id;
}
public void setParter_id(String parter_id) {
	this.parter_id = parter_id;
}
public String getParters_name() {
	return parters_name;
}
public void setParters_name(String parters_name) {
	this.parters_name = parters_name;
}
public String getParter_manager() {
	return parter_manager;
}
public void setParter_manager(String parter_manager) {
	this.parter_manager = parter_manager;
}
public String getParter_mobile() {
	return parter_mobile;
}
public void setParter_mobile(String parter_mobile) {
	this.parter_mobile = parter_mobile;
}
public String getDelivery_name() {
	return delivery_name;
}
public void setDelivery_name(String delivery_name) {
	this.delivery_name = delivery_name;
}
public String getDelivery_mobile() {
	return delivery_mobile;
}
public void setDelivery_mobile(String delivery_mobile) {
	this.delivery_mobile = delivery_mobile;
}
public String getLoss_price() {
	return loss_price;
}
public void setLoss_price(String loss_price) {
	this.loss_price = loss_price;
}
public String getYard_time() {
	return yard_time;
}
public void setYard_time(String yard_time) {
	this.yard_time = yard_time;
}

public String getCase_state(){
	return case_state;
}

public void setCase_state(String case_state){
	this.case_state=case_state;
}

public String getRepair_state() {
	return repair_state;
}
public void setRepair_state(String repair_state) {
	this.repair_state = repair_state;
}
public String getAudit_state() {
	return audit_state;
}
public void setAudit_state(String audit_state) {
	this.audit_state = audit_state;
}
public String getLoss_state() {
	return loss_state;
}
public void setLoss_state(String loss_state) {
	this.loss_state = loss_state;
}
public static long getSerialversionuid() {
	return serialVersionUID;
}
public String getCase_id() {
	return case_id;
}
public void setCase_id(String case_id) {
	this.case_id = case_id;
}




}
