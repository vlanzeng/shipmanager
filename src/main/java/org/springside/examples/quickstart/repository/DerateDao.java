package org.springside.examples.quickstart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springside.examples.quickstart.entity.Derate;

public interface DerateDao  extends CrudRepository<Derate, Long> {

}
