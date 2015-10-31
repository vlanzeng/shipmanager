package org.springside.examples.quickstart.domain;

public class RechargeParam extends BaseParam {
	public String cname;  //船舶名
	public Float amountStart;
	public Float amountEnd;
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Float getAmountStart() {
		return amountStart;
	}
	public void setAmountStart(Float amountStart) {
		this.amountStart = amountStart;
	}
	public Float getAmountEnd() {
		return amountEnd;
	}
	public void setAmountEnd(Float amountEnd) {
		this.amountEnd = amountEnd;
	}
	
	
}
