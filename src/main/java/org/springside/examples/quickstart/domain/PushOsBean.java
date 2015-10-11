package org.springside.examples.quickstart.domain;

public class PushOsBean {
	public Long osId;
	public Long orderId;
	public String orderNo;
	public Long productId;
    public String productName;
    public String price;
    public Integer num;
    public String bookTime;
    public String addr;
	
	public String action="cmo_order";
	
	public Long getOsId() {
		return osId;
	}
	public void setOsId(Long osId) {
		this.osId = osId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getBookTime() {
		return bookTime;
	}
	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
}
