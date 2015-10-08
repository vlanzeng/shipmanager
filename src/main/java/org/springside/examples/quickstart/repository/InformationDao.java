package org.springside.examples.quickstart.repository;


import org.springframework.data.repository.CrudRepository;
import org.springside.examples.quickstart.entity.Information;

public interface InformationDao extends CrudRepository<Information, Long> {
	//List<Information> findInfoByParam(String param,Integer page,Integer pageSize);
}
