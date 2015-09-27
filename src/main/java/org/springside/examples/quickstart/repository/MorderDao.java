package org.springside.examples.quickstart.repository;

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
public interface MorderDao extends CrudRepository<Coupon, Long> {
	@Modifying
	@Query(value="select o.id,o.product_name,o.num,o.status,o.order_no,o.book_time,o.book_addr,o.create_time,u.phone,u.user_name,u.ship_name "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11 "
			+ "order by o.create_time desc limit ?1,?2", nativeQuery=true)
	List<Object[]> queryMakeOrderList(Integer start, Integer size);
	
	@Query(value="select count(1) "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11", nativeQuery=true)
	int queryMakeOrderTotal();
	
	@Modifying
	@Query(value="select o.id,o.product_name,o.num,o.status,o.order_no,o.book_time,o.book_addr,o.create_time,u.phone,u.user_name,u.ship_name "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11 "
			+ "order by o.create_time desc limit ?1,?2", nativeQuery=true)
	List<Object[]> queryOrderList(Integer start, Integer size);
	
	@Query(value="select count(1) "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11", nativeQuery=true)
	int queryOrderTotal();
	
	@Modifying
	@Query(value="update t_order set status=12,os_id=?1,product_id=?2 where id=?3", nativeQuery=true)
	int updateOs(Integer osId, Integer productId, String orderId);
	
	@Modifying
	@Query(value="update t_order set status=?1 where status=?2 and id=?3", nativeQuery=true)
	int updateStatus(Integer status, Integer oldStatus, String orderId);

	@Query(value="select id,name,price from t_oil o where o.os_id=?1 and name=?2 and o.status=1;", nativeQuery=true)
	List<Object[]> queryOilInfo(String osId, String productName);

	@Query(value="select id,info from t_derate o where o.os_id=?1 and o.status=1;", nativeQuery=true)
	List<Object[]> queryDerateInfo(String osId);
}
