package org.springside.examples.quickstart.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="t_order_cash")
public class OrderCash extends  IdEntity{
	

	private Date createTime;

	private String orderName  ;
	

	private Long operationId;
	
	private Long osid;
	

	private Set<OrderCashDetail> orderCashDetails;
	
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="order_name")
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	@Column(name="operation_id")
	public Long getOperationId() {
		return operationId;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="cash_order_id")
	public Set<OrderCashDetail> getOrderCashDetails() {
		return orderCashDetails;
	}

	public void setOrderCashDetails(Set<OrderCashDetail> orderCashDetails) {
		this.orderCashDetails = orderCashDetails;
	}

	@Column(name="os_id")
	public Long getOsid() {
		return osid;
	}

	public void setOsid(Long osid) {
		this.osid = osid;
	}


}
