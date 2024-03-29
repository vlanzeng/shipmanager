package org.springside.examples.quickstart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springside.examples.quickstart.entity.Coupon;
import org.springside.examples.quickstart.entity.OilStation;
import org.springside.examples.quickstart.entity.OilStation_1;

/**
 * 
 * @author lyhc
 * 
 */
public interface MosDao extends CrudRepository<OilStation_1, Long> {
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
	@Query(value="update t_os_buy set status=?1 where status=?2 and id=?3", nativeQuery=true)
	int updateStatus(Integer status, Integer oldStatus, Long id);
	
	@Modifying
	@Query(value="update t_oil_station set status=?1 where id=?2", nativeQuery=true)
	int updateOSStatus(Integer status, Long id);
	
	@Modifying
	@Query(value="update t_oil_station set `desc`=?1 where id=?2", nativeQuery=true)
	int updateOSDesc( String desc, Long id);
	
	@Modifying
	@Query(value="delete from t_oil_station  where id=?1", nativeQuery=true)
	int deleteOSStatus(Long id);

	@Modifying
	@Query(value="insert into t_oil_station(name,`desc`,address,latitude,longitude,pic_url,phone,city_id,credit,quality,"
			+ "service,coupon_flag,derate_flag,status,create_time) values(?1,?1,?2,?3,?4,?5,?6,?7,5,5,5,1,1,1,now())", nativeQuery=true)
	int insertOs(String osName, String addr, String latitude,String longitude,String picurl, String phone, Long cityId);
	
	@Modifying
	@Query(value="insert into t_os_buy(user_name,os_id,order_no,oil_id,price,num,amount,status,create_time) "
			+ "values(?1,?2,?3,?4,?5,?6,?7,1,now())", nativeQuery=true)
	int addOsOilOrder(String userNmae,Long osId, String orderNo,Long oilId, String price, Integer num,String amount);
	
	@Query(value= " SELECT * FROM   t_oil_station", nativeQuery=true)
	List<Object[]> findAllOStation();
	
	@Query(value= " SELECT * FROM   t_oil_station   WHERE id = ?1  ", nativeQuery=true)
	List<Object[]> findOStationById(Long id);
	
}
