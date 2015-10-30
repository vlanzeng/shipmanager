package org.springside.examples.quickstart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Order;
import org.springside.examples.quickstart.entity.OrderCash;
import org.springside.examples.quickstart.repository.OrderCashDao;

@Component
@Transactional
public class OrderCashService {
	
	
	@Autowired
	private OrderCashDao orderCashDao;
	
	
	public  List<OrderCash>  findAll(){
		return orderCashDao.findAll();
	}
	
	public  List<OrderCash> findByOsId(Long osId){
		return orderCashDao.findByOsid(osId);
	}
	
	public  OrderCash findById(Long id){
		return orderCashDao.findOne(id);
	}
	
	public  OrderCash save(OrderCash oc){
		return  orderCashDao.save(oc);
	}
	
}
