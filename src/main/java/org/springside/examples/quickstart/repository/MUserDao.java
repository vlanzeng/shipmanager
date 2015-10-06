/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.springside.examples.quickstart.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.Muser;

public interface MUserDao extends PagingAndSortingRepository<Muser, Long> {
	@Query(value="select roles "
			+ "from ss_user o where o.login_name=?1 and o.status=1", nativeQuery=true)
	String queryMuserRole(String loginName);
	
	
	@Modifying
	@Query(value="insert into ss_user(login_name,name,password,salt,roles,os_id,status,register_date) "
			+ "values(?1,?1,?2,?3,?4,?5,1,now())", nativeQuery=true)
	int add(String userNmae,String pwd, String salt, String role, Long osId);
}
