package org.springside.examples.quickstart.domain;

public class UserBean {
	public Long userId;
	public String userName;
	public String phone;
	public String shipname;
	public String shipno;
	public Double rechargeamount;
	public Double fee;	//支出
	public String createtime;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getShipname() {
		return shipname;
	}
	public void setShipname(String shipname) {
		this.shipname = shipname;
	}
	public String getShipno() {
		return shipno;
	}
	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
	public Double getRechargeamount() {
		return rechargeamount;
	}
	public void setRechargeamount(Double rechargeamount) {
		this.rechargeamount = rechargeamount;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
}
