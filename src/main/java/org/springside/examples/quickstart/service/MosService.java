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
import org.springside.examples.quickstart.domain.OilStationBean;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.OsParam;
import org.springside.examples.quickstart.domain.ResultList;
import org.springside.examples.quickstart.repository.MosDao;

@Component
@Transactional
public class MosService {
	@Autowired
	private MosDao mosDao;
	
	 @PersistenceContext  
	 private EntityManager em; 
	
	public ResultList getMakeOrderList(Integer page, Integer pageSize){
		List<OrderBean> result = new ArrayList<OrderBean>();
		ResultList rl = new ResultList();
		int start = (page - 1) * pageSize;
		List<Object[]> list = mosDao.queryMakeOrderList(start, pageSize);
		int total = mosDao.queryMakeOrderTotal();
		
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
	
	public int uStatus(String orderId, String status, String oldStatus) {
		return mosDao.updateStatus(Integer.valueOf(status), Integer.valueOf(oldStatus), orderId);
	}

	public DataGrid<OilStationBean> getOsList(OsParam param) {
		DataGrid<OilStationBean> dg = new DataGrid<OilStationBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and o.name like '%" + param.getOsName() + "%'");
		}
		
		if(!StringUtils.isEmpty(param.getStatus()) && param.getStatus() >=0){
			whereParam.append(" and o.status="+param.getStatus());
		}
		
		if(!StringUtils.isEmpty(param.getCityId()) && param.getStatus() >=0){
			whereParam.append(" and o.city_id="+param.getCityId());
		}
		
		if(!StringUtils.isEmpty(param.getCityName()) && param.getStatus() >=0){
			whereParam.append(" and c.name like '"+param.getCityName() + "'");
		}
		
		String sql = "select o.id, o.name,o.address,c.name cname from t_oil_station o, t_city c where o.city_id=c.id " + whereParam.toString()+" "
				+ "order by o.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<OilStationBean> result = new ArrayList<OilStationBean>();
		String totalSql = "select count(1) from t_oil_station o, t_city c where o.city_id=c.id " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			OilStationBean ob = new OilStationBean();
			ob.setId(Integer.valueOf(o[0]+""));
			ob.setName(o[1] + "");
			ob.setAddress(o[2] + "");
			ob.setCityName(o[3] + "");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}
}
