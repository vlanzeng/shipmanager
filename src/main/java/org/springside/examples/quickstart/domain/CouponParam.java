package org.springside.examples.quickstart.domain;

public class CouponParam extends BaseParam{
	private String name;
	private String faceLimit;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFaceLimit() {
		return faceLimit;
	}
	public void setFaceLimit(String faceLimit) {
		this.faceLimit = faceLimit;
	}
}
