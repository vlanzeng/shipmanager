package org.springside.examples.quickstart.domain;

public class OsOilParam extends BaseParam {
	public String orderNo;
	public String osName;
	public Long osId;
	public Long oilId;
	public String price;
	public Integer num;
	public String amount;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public Long getOsId() {
		return osId;
	}

	public void setOsId(Long osId) {
		this.osId = osId;
	}

	public Long getOilId() {
		return oilId;
	}

	public void setOilId(Long oilId) {
		this.oilId = oilId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
