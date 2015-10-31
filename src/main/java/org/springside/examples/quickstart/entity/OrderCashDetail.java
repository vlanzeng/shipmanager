package org.springside.examples.quickstart.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name="t_order_cash_detail")
public class OrderCashDetail extends  IdEntity{
	

	private Order historyOrder;

	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="order_id")  
	public Order getHistoryOrder() {
		return historyOrder;
	}


	public void setHistoryOrder(Order historyOrder) {
		this.historyOrder = historyOrder;
	}
	
	

}
