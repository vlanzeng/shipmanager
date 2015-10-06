/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.springside.examples.quickstart.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
	
	@Query("select o from User o where o.loginName=?1 and o.status=?2")
	User findByLoginName(String loginName, Integer status);
	
}
