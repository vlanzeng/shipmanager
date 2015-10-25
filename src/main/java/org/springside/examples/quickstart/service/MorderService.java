package org.springside.examples.quickstart.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springside.examples.quickstart.domain.BaseParam;
import org.springside.examples.quickstart.domain.ChargeLogBean;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.OrderParam;
import org.springside.examples.quickstart.domain.PushOsBean;
import org.springside.examples.quickstart.domain.RechargeParam;
import org.springside.examples.quickstart.domain.ResultList;
import org.springside.examples.quickstart.repository.MorderDao;
import org.springside.examples.quickstart.utils.JPushUtil;

@Component
@EnableTransactionManagement
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
		
		//订单类型
		if(!StringUtils.isEmpty(param.getType()) && param.getType() >=0){
			whereParam.append(" and o.type="+param.getType());
		}
		
		//订单号
		if(!StringUtils.isEmpty(param.getOrderNo())){
			whereParam.append(" and o.order_no='"+param.getOrderNo().trim()+"'");
		}
		
		//用户名或手机号
		if(!StringUtils.isEmpty(param.getUserName())){
			whereParam.append(" and (u.user_name='"+param.getUserName().trim() + "' or u.phone='"+param.getUserName().trim() + "')");
		}
		
		//加油站
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and s.name='" + param.getOsName().trim() + "'");
		}
		
		//区域
		if(!StringUtils.isEmpty(param.getArea())){
			whereParam.append(" and s.name='" + param.getArea().trim() + "'");
		}
		
		//订单结算状态
		if(!StringUtils.isEmpty(param.getOjStatus()) && param.getOjStatus() >=0){
			//订单状态  等待付款中-0 付款成功-1 付款失败-2 过期-3 撤销成功-4 退款中-5 退款成功-6 退款失败-7 部分退款成功-8  11-新建预约订单 12-后台加油站以确定 99-删除
			String s = "1";
			if(param.getOjStatus() == 2){
				s = "0,2,3,4,5,6,7,9,11,12";
			}
			whereParam.append(" and o.status in ("+s+")");
		}
		
		//订单完成状态
		if(!StringUtils.isEmpty(param.getOwStatus()) && param.getOwStatus() >=0){
			//订单状态  等待付款中-0 付款成功-1 付款失败-2 过期-3 撤销成功-4 退款中-5 退款成功-6 退款失败-7 部分退款成功-8  11-新建预约订单 12-后台加油站以确定 99-删除
			String s = "2,3,5,88,99";
			if(param.getOjStatus() == 2){
				s = "0,1,4,6,7,8,11,12";
			}
			whereParam.append(" and o.status in ("+s+")");
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.create_time<'"+param.getEndTime()+"'");
		}
		String sql = "select o.id,o.order_no,o.product_name,o.price,o.num,o.status,o.money,"
				+ "u.user_name,s.name,o.update_time,o.create_time,o.book_time, o.book_addr "
				+ "from t_order o left join t_user u on o.user_id=u.id left join t_oil_station s on o.os_id=s.id left join t_city c on s.city_id=c.id where o.type in (1,3,4) "+whereParam.toString()+" "
				+ "order by o.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<OrderBean> result = new ArrayList<OrderBean>();
		
		String totalSql = "select count(1) from t_order o left join t_user u on o.user_id=u.id left join t_oil_station s on o.os_id=s.id where o.type in (1,3,4) " + whereParam.toString();
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
		int r = morderDao.updateOs(Integer.valueOf(osId), productId, orderId);
		if(r > 0){
			//获取推送消息 推送消息
			List<Object[]> orders = morderDao.queryOrderInfo(orderId);
			if(orders == null || orders.size() <= 0){
				throw new RuntimeException("操作异常。");
			}
			//t.id,t.order_no,t.book_addr,t.book_time,t.num,u.phone,o.price
		    PushOsBean bean = new PushOsBean();
		   	bean.setOsId(Long.valueOf(osId));
		   	bean.setOrderId(Long.valueOf(orderId));
		   	bean.setOrderNo(orders.get(0)[1]+"");
		   	bean.setAddr(orders.get(0)[2]+"");
		   	bean.setBookTime(orders.get(0)[3]+"");
		   	bean.setNum(Integer.valueOf(orders.get(0)[4]+""));
		   	bean.setPrice(orders.get(0)[6]+"");
		    bean.setProductId(Long.valueOf(productId+""));
		   	bean.setProductName(productName);
			JPushUtil.pushOrderOs(orders.get(0)[5]+"", bean);
		}
		return 1;
	}

	@Transactional
	public int recharge(String phone, String amount) {
		//根据手机号查询ID
		Long userId =-1L;
		try{
		userId= morderDao.queryUserIdForPhone(phone);
		}catch(Exception e){
			return -1;
		}
		if(userId <= 0){
			//throw new RuntimeException("充值失败:手机号不存在");
			return -1;
		}
		int rr= -1;
		try{
			rr = morderDao.queryUserBankCount(userId);
			if(rr <=0){
				rr = morderDao.insertUserBank(userId, 0);
				if(rr <= 0){
					return -2;
				}
			}
		}catch(Exception e){
			return -2;
		}
		//更新用户月
		//插入充值记录表
		int r = morderDao.insertLog(phone,amount);
		return r;
	}
	
	public DataGrid<ChargeLogBean> getRechargeList(RechargeParam param){
		DataGrid<ChargeLogBean> dg = new DataGrid<ChargeLogBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		if(!StringUtils.isEmpty(param.phone)){
			whereParam.append(" and r.phone ='"+param.phone+"'");
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and r.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and r.create_time<'"+param.getEndTime()+"'");
		}
		
		if(!StringUtils.isEmpty(param.getCname())){
			whereParam.append(" and exists (select * from t_user c where r.phone=c.phone and c.ship_name like '%"+param.getCname()+"%')");
		}
		
		if(param.getAmountStart() != null){
			whereParam.append(" and r.amount>="+param.getAmountStart());
		}
		
		if(param.getAmountEnd() != null){
			whereParam.append(" and r.amount <= "+param.getAmountEnd());
		}
		
		String sql = "SELECT r.id,r.phone,r.status,r.amount,r.create_time, b.user_name,b.ship_name from t_recharge_log r, t_user b where 1=1 and r.phone=b.phone " + 
		              whereParam.toString() + " " + "order by r.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<ChargeLogBean> result = new ArrayList<ChargeLogBean>();
		
		String totalSql = "select count(1) from t_recharge_log r where 1=1 " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			ChargeLogBean ob = new ChargeLogBean();
			ob.setId(Long.valueOf(o[0]+""));
			ob.setPhone(o[1]+"");
			ob.setStatus(Integer.valueOf(o[2]+""));
			ob.setAmount(o[3]==null ? "" :o[3] + "");
			ob.setCreateTime(o[4]==null ? "" :o[4] + "");
			ob.setUsername(o[5]==null?"":o[5]+"");
			ob.setShipname(o[6]==null?"":o[6]+"");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}
	
	public DataGrid<ChargeLogBean> getRechargeSumList(RechargeParam param){
		DataGrid<ChargeLogBean> dg = new DataGrid<ChargeLogBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		if(!StringUtils.isEmpty(param.phone)){
			whereParam.append(" and r.phone ='"+param.phone+"'");
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and r.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and r.create_time<'"+param.getEndTime()+"'");
		}
		
		if(!StringUtils.isEmpty(param.getCname())){
			whereParam.append(" and exists (select * from t_user c where r.phone=c.phone and c.ship_name like '%"+param.getCname()+"%')");
		}
		
		if(param.getAmountStart() != null){
			whereParam.append(" and r.amount>="+param.getAmountStart());
		}
		
		if(param.getAmountEnd() != null){
			whereParam.append(" and r.amount <= "+param.getAmountEnd());
		}
		
		String sql = "SELECT r.phone,sum(r.amount), b.user_name,b.ship_name from t_recharge_log r, t_user b where 1=1 and r.phone=b.phone " + 
		              whereParam.toString() + " " + "group by 1,3,4 order by r.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<ChargeLogBean> result = new ArrayList<ChargeLogBean>();
		
		String totalSql = "select count(1) from t_recharge_log r where 1=1 " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			ChargeLogBean ob = new ChargeLogBean();
			//ob.setId(Long.valueOf(o[0]+""));
			ob.setPhone(o[0]+"");
			//ob.setStatus(Integer.valueOf(o[2]+""));
			ob.setAmount(o[1]==null ? "" :o[1] + "");
			//ob.setCreateTime(o[4]==null ? "" :o[4] + "");
			ob.setUsername(o[2]==null?"":o[2]+"");
			ob.setShipname(o[3]==null?"":o[3]+"");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}
	
}

