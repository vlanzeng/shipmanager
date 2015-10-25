package org.springside.examples.quickstart.domain;

import java.util.Date;

public class CouponBean {
	private Long id;
	private String name;
	private String desc;
	private Integer faceValue;
	private Integer limitValue;
	private Integer type;
	private Long osId;
	private Integer effectiveDay;
	private String startTime;
	private String endTime;
	private String createTime;
	private Integer status;
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getFaceValue() {
		return faceValue;
	}
	public void setFaceValue(Integer faceValue) {
		this.faceValue = faceValue;
	}
	public Integer getLimitValue() {
		return limitValue;
	}
	public void setLimitValue(Integer limitValue) {
		this.limitValue = limitValue;
	}
	public Integer getType() {
		return type;
	}
	public Long getOsId() {
		return osId;
	}
	public void setOsId(Long osId) {
		this.osId = osId;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getEffectiveDay() {
		return effectiveDay;
	}
	public void setEffectiveDay(Integer effectiveDay) {
		this.effectiveDay = effectiveDay;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
