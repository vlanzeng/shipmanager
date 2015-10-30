package org.springside.examples.quickstart.entity;

import java.util.HashMap;
import java.util.Map;

public class OrderStatus {

	/**
	 * 
	 * 等待付款中-0 付款成功-1 付款失败-2 过期-3 撤销成功-4 退款中-5 
	 * 退款成功-6 退款失败-7 部分退款成功-8 
	 * 11-新建预约订单 12-后台加油站以确定 88-订单完成 99-删除
	 */
	public  static  Map<String, String> status = new HashMap<String, String>();
	static{
		status.put("0", "等待付款中");
		status.put("1", "付款成功");
		status.put("2", "付款失败");
		status.put("3", "过期");
		status.put("4", "撤销成功");
		status.put("5", "退款中");
		status.put("6", "退款成功");
		status.put("7", "退款失败");
		status.put("8", "部分退款成功");
		status.put("11", "新建预约订单");
		status.put("12", "后台加油站以确定");
		status.put("88", "订单完成");
		status.put("99", "删除");
		
		
	}
	
	
}
