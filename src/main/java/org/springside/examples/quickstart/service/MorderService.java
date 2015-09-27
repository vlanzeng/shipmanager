package org.springside.examples.quickstart.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.OrderParam;
import org.springside.examples.quickstart.domain.ResultList;
import org.springside.examples.quickstart.repository.MorderDao;

@Component
@Transactional
public class MorderService {
	@Autowired
	private MorderDao morderDao;
	
	 @PersistenceContext  
	 private EntityManager em; 
	
	public ResultList getMakeOrderList(Integer page, Integer pageSize){
		List<OrderBean> result = new ArrayList<OrderBean>();
		ResultList rl = new ResultList();
		int start = (page - 1) * pageSize;
		List<Object[]> list = morderDao.queryMakeOrderList(start, pageSize);
		int total = morderDao.queryMakeOrderTotal();
		
		//o.id,o.product_name,o.num,o.status,o.order_no,o.book_time,o.book_addr,o.create_time,u.phone,u.user_name,u.ship_name
		for(int i = 0 ; (list != null && i < list.size()); i++){
			Object[] o = list.get(i);
			OrderBean bean = new OrderBean();
			bean.setId(Long.valueOf(o[0] + ""));
			bean.setProductName(o[1] + "");
			bean.setNum(Integer.parseInt(o[2] + ""));
			bean.setStatus(Integer.valueOf(o[3] + ""));
			bean.setOrderNo(o[4] + "");
			bean.setBookTime(o[5] + "");
			bean.setBookAddr(o[6] + "");
			bean.setCreateTime(o[7] + "");
			bean.setPhone(o[8] + "");
			bean.setUserName(o[9] + "");
			bean.setShipName(o[10] + "");
			result.add(bean);
		}
		rl.setDataList(result);
		rl.setPage(page);
		rl.setTotal(total);
		return rl;
	}
	
	public DataGrid<OrderBean> getOrderList(OrderParam param){
		DataGrid<OrderBean> dg = new DataGrid<OrderBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		if(!StringUtils.isEmpty(param.getType())){
			whereParam.append(" and o.type="+param.getType());
		}
		
		if(!StringUtils.isEmpty(param.getOrderNo())){
			whereParam.append(" and o.order_no='"+param.getOrderNo()+"'");
		}
		
		if(!StringUtils.isEmpty(param.getUserName())){
			whereParam.append(" and u.user_name='"+param.getUserName() + "'");
		}
		
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and s.name like '%" + param.getOsName() + "%'");
		}
		
		if(!StringUtils.isEmpty(param.getStatus()) && param.getStatus() >=0){
			whereParam.append(" and o.status="+param.getStatus());
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.order_no >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.order_no<'"+param.getEndTime()+"'");
		}
		String sql = "select o.id,o.order_no,o.product_name,o.price,o.num,o.status,o.money,"
				+ "u.user_name,s.name,o.update_time,o.create_time,o.book_time, o.book_addr "
				+ "from t_order o left join t_user u on o.user_id=u.id left join t_oil_station s on o.os_id=s.id where o.type in (1,3) "+whereParam.toString()+" "
				+ "order by o.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<OrderBean> result = new ArrayList<OrderBean>();
		
		String totalSql = "select count(1) from t_order o left join t_user u on o.user_id=u.id left join t_oil_station s on o.os_id=s.id where o.type in (1,3) " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			OrderBean ob = new OrderBean();
			ob.setId(Long.valueOf(o[0]+""));
			ob.setOrderNo(o[1] + "");
			ob.setProductName(o[2] + "");
			ob.setPrice(o[3]==null ? "" : o[3] + "");
			ob.setNum(Integer.valueOf(o[4]+""));
			ob.setStatus(Integer.valueOf(o[5]+""));
			ob.setAmount(o[6]==null ? "" :o[6] + "");
			ob.setUserName(o[7]==null ? "" :o[7] + "");
			ob.setOsName(o[8]==null ? "" :o[8] + "");
			ob.setUpdateTime(o[9]== null ?  "" : o[9]+"");
			ob.setCreateTime(o[10]==null ? "" :o[10] + "");
			
			ob.setBookTime(o[11]==null ? "" :o[11] + "");
			ob.setBookAddr(o[12]==null ? "" :o[12] + "");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}

	public int uStatus(String orderId, String status, String oldStatus) {
		return morderDao.updateStatus(Integer.valueOf(status), Integer.valueOf(oldStatus), orderId);
	}

	public int uOs(String orderId, String osId, String productName) {
		//根据osId获取油品信息 更新订单表
		List<Object[]> oils = morderDao.queryOilInfo(osId, productName);
		if(oils == null || oils.size() <= 0){
			return 0;
		}
		//获取减免信息 t_derate
		//List<Object[]> derates = morderDao.queryDerateInfo(osId);
		//获取优惠券信息
		//设置加油站
		Integer productId = Integer.valueOf(oils.get(0)[0]+"");
		return morderDao.updateOs(Integer.valueOf(osId), productId, orderId);
	}
}
