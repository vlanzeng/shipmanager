package org.springside.examples.quickstart.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springside.examples.quickstart.entity.Coupon;

/**
 * 
 * @author lyhc
 * 
 */
public interface McouponDao extends CrudRepository<Coupon, Long> {
	@Modifying
	@Query(value="select o.id, o.name,o.`desc`, o.face_value, o.limit_value,o.os_id,o.type,o.status,o.effective_day,o.start_time,o.end_time "
			+ "from t_coupon o where o.status=1 and (o.os_id=?1 or o.os_id=0) order by id desc limit ?2,?3", nativeQuery=true)
	public List<Object[]> queryCouponList(Long osId, Integer start, Integer size);
	
	@Query(value="select count(1) from t_coupon o where o.status=1 and (o.os_id=?1 or o.os_id=0)", nativeQuery=true)
	public int queryCouponTotalList(Long osId);
	
	@Modifying
	@Query(value="select o.id, o.name,o.`desc`, o.face_value, o.limit_value,o.os_id,o.type,o.status,o.effective_day,o.start_time,o.end_time "
			+ "from t_coupon o where o.status=1 and o.os_id=0 order by o.id desc limit ?1, ?2", nativeQuery=true)
	public List<Object[]> queryCouponAll(Integer start, Integer size);
	
	@Query(value="select count(1) from t_coupon o where o.status=1 and o.os_id=0", nativeQuery=true)
	public int queryCouponAllTotal();

	@Modifying
	@Query(value="insert into t_coupon(name,limit_value,face_value,os_id,type,status,start_time,end_time,create_time) "
			+ "values(?1,?2,?3,0,?4,1,?5,?6,now())", nativeQuery=true)
	public int add(String name, Integer face, Integer limit, Integer type,
			Date startTime, Date endTime);

	@Modifying
	@Query(value="update t_coupon set status=?1 where id=?2", nativeQuery=true)
	public int updateStatus(Integer status, Long id);
}
