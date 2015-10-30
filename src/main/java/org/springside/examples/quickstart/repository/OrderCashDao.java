/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.springside.examples.quickstart.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.OrderCash;

public interface OrderCashDao extends PagingAndSortingRepository<OrderCash, Long> {
	
	public  List<OrderCash> findAll();
	
	
	public  List<OrderCash> findByOsid(Long osid);
	
	
	
}
