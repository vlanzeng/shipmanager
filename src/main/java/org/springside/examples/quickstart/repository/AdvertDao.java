package org.springside.examples.quickstart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springside.examples.quickstart.entity.Advert;

public interface AdvertDao extends CrudRepository<Advert,  Long>  {

}
