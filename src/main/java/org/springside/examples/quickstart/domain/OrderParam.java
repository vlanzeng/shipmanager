package org.springside.examples.quickstart.domain;

public class OrderParam extends BaseParam {
	public String orderNo;
	public String osId;
	public String osName;
	public Integer ojStatus;
	public Integer owStatus;
	public String area;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOsId() {
		return osId;
	}
	public void setOsId(String osId) {
		this.osId = osId;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public Integer getOjStatus() {
		return ojStatus;
	}
	public void setOjStatus(Integer ojStatus) {
		this.ojStatus = ojStatus;
	}
	public Integer getOwStatus() {
		return owStatus;
	}
	public void setOwStatus(Integer owStatus) {
		this.owStatus = owStatus;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
}
