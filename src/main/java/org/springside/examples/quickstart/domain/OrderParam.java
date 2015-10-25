package org.springside.examples.quickstart.domain;

import java.util.HashMap;
import java.util.Map;

public class OrderParam extends BaseParam {
	public String orderNo;
	public String osId;
	public String osName;
	public Integer ojStatus;
	public Integer owStatus;
	public String area;
	public String regionId;
	private Integer[] region;
	private String[] otherStatus;
	public String[] getOtherStatus() {
		return otherStatus;
	}
	public void setOtherStatus(String[] otherStatus) {
		this.otherStatus = otherStatus;
	}
	public Integer[] getRegion() {
		return region;
	}
	
	public final static Map<String, Integer[]> priceRegion = new HashMap<String, Integer[]>();
	
	static{
		priceRegion.put("1", new Integer[]{0,50000});
		priceRegion.put("2", new Integer[]{50000,100000});
		priceRegion.put("3", new Integer[]{100000});
		priceRegion.put("4", new Integer[]{0,500000});
		priceRegion.put("5", new Integer[]{500000,1000000});
		priceRegion.put("6", new Integer[]{1000000});
	}
	
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
	public void setRegionId(String regionId) {
		this.region = priceRegion.get(regionId);
	}
}
