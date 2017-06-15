package com.cx.myobject;

import java.io.Serializable;

public class MydepartmentObject implements Serializable{
	private static final long serialVersionUID=1L;//???
	private String repair_factoryname;
	private int repair_id;
	public MydepartmentObject(String repair_factoryname, int repair_id) {
		super();
		this.repair_factoryname = repair_factoryname;
		this.repair_id = repair_id;
	}
	public String getRepair_factoryname() {
		return repair_factoryname;
	}
	public void setRepair_factoryname(String repair_factoryname) {
		this.repair_factoryname = repair_factoryname;
	}
	public int getRepair_id() {
		return repair_id;
	}
	public void setRepair_id(int repair_id) {
		this.repair_id = repair_id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
