/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.springside.examples.quickstart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.Muser;

public interface AppUserDao extends PagingAndSortingRepository<Muser, Long> {
	
	@Query(value="SELECT * FROM  t_user WHERE phone =  ?1 ", nativeQuery=true)
	List<Object[]> selectAppUserByPhone(String phone);
	
	
	
}
