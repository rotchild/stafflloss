package com.cx.staffloss;

import java.io.Serializable;

public class TaskRecord implements Serializable {
	private static final long serialVersionUID=1L;//???
	private String reportNo,receiveTime,carNo,vipTag,taskState,repairDeport,taskType;
	public TaskRecord(String reportNo, String receiveTime, String carNo,
			String vipTag, String taskState, String repairDeport,
			String taskType) {
		super();
		this.reportNo = reportNo;
		this.receiveTime = receiveTime;
		this.carNo = carNo;
		this.vipTag = vipTag;
		this.taskState = taskState;
		this.repairDeport = repairDeport;
		this.taskType = taskType;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getVipTag() {
		return vipTag;
	}
	public void setVipTag(String vipTag) {
		this.vipTag = vipTag;
	}
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	public String getRepairDeport() {
		return repairDeport;
	}
	public void setRepairDeport(String repairDeport) {
		this.repairDeport = repairDeport;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
