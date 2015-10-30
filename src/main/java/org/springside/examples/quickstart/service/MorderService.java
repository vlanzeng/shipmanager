package org.springside.examples.quickstart.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springside.examples.quickstart.domain.BaseParam;
import org.springside.examples.quickstart.domain.ChargeLogBean;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.OrderParam;
import org.springside.examples.quickstart.domain.PushOsBean;
import org.springside.examples.quickstart.domain.ResultList;
import org.springside.examples.quickstart.domain.UserOrderBean;
import org.springside.examples.quickstart.entity.AjaxResult;
import org.springside.examples.quickstart.entity.Muser;
import org.springside.examples.quickstart.entity.Order;
import org.springside.examples.quickstart.entity.OrderCash;
import org.springside.examples.quickstart.entity.OrderCashDetail;
import org.springside.examples.quickstart.entity.OrderStatus;
import org.springside.examples.quickstart.repository.MorderDao;
import org.springside.examples.quickstart.repository.MosDao;
import org.springside.examples.quickstart.service.account.AccountService;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.utils.CommonUtils;
import org.springside.examples.quickstart.utils.JPushUtil;

@Component
@Transactional
public class MorderService {
	@Autowired
	private MorderDao morderDao;
	
	@Autowired
	private MuserService muserService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private OrderCashService orderCashService;
	
	@Autowired
	private MosDao mosDao;
	
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
			bean.setStatus(OrderStatus.status.get(o[3].toString()));
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
	
	public DataGrid<OrderBean> getOrderListIncome(OrderParam param){
		DataGrid<OrderBean> dg = new DataGrid<OrderBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		//订单类型
		if(!StringUtils.isEmpty(param.getType()) && param.getType() >=0){
			whereParam.append(" and o.type="+param.getType());
		}
		
		//订单状态，自己添加的
		if(  null!=param.getOtherStatus()&& param.getOtherStatus().length>0){
			whereParam.append(" and o.status in ( "  );
			for (int i = 0; i < param.getOtherStatus().length; i++) {
					if(i==0){
						whereParam.append(param.getOtherStatus()[i]);
					}else{
						whereParam.append(","+param.getOtherStatus()[i]);
					}
			}
			whereParam.append(" ) "  );
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
			whereParam.append(" and s.name like '%" + param.getOsName().trim() + "%'");
		}
		
		//区域
		if(!StringUtils.isEmpty(param.getArea())){
			whereParam.append(" and s.name='" + param.getArea().trim() + "'");
		}
		
		//订单结算状态
		if(!StringUtils.isEmpty(param.getOjStatus()) && param.getOjStatus() >=0){
			//订单状态  等待付款中-0 付款成功-1 付款失败-2 过期-3 撤销成功-4 退款中-5 
			//退款成功-6 退款失败-7 部分退款成功-8  11-新建预约订单 12-后台加油站以确定 99-删除
			String s = "1,88,9";
			if(param.getOjStatus() == 1){
				whereParam.append(" and o.status in ("+s+")");
			}else{
				whereParam.append(" and o.status not in ("+s+")");
			}
		}
		
		if(param.isHasCash()){
			StringBuilder notIds =  new StringBuilder();
			List<OrderCash> oss =  orderCashService.findByOsId(Long.valueOf(param.getOsId()));
			if(null!=oss && !oss.isEmpty()  ){
				
				for (OrderCash orderCash : oss) {
					for (OrderCashDetail ocd : orderCash.getOrderCashDetails()) {
						notIds.append(ocd.getHistoryOrder().getId()+",");
					}
				}
				whereParam.append("  and o.id  not in ("+notIds.substring(0, notIds.lastIndexOf(","))+" )"); //  排除上次提现的订单
			}
		}
		
		whereParam.append(" and o.status not in (99)"); //  删除状态
		
		//订单完成状态
		if(!StringUtils.isEmpty(param.getOwStatus()) && param.getOwStatus() >=0){
			//订单状态  等待付款中-0 付款成功-1 付款失败-2 过期-3 撤销成功-4 退款中-5 退款成功-6 退款失败-7 部分退款成功-8  11-新建预约订单 12-后台加油站以确定 99-删除
			String s = "1,9,88"; //完成
			if(param.getOwStatus()== 1){
				//s = "0,1,4,6,7,8,11,12";
				whereParam.append(" and o.status in ("+s+")");
			}else{
				whereParam.append(" and o.status not  in ("+s+")");
			}
		}
		
		
		
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and  DATE_FORMAT(o.create_time,'%Y-%m-%d')  >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and    DATE_FORMAT(o.create_time,'%Y-%m-%d')  <='"+param.getEndTime()+"'");
		}
		
		if(!StringUtils.isEmpty(param.getOsId())){
			whereParam.append(" and o.os_id =  '"+param.getOsId()+"'");
		}
		
		
		// 价格区间
		if( null!= param.getRegion()){
			if(param.getRegion().length==2){
				if( null != param.getRegion()[0]){
					whereParam.append(" and o.money >=  " + param.getRegion()[0]);
				}
				if( null != param.getRegion()[1]){
					whereParam.append(" and o.money <  " + param.getRegion()[1]);
				}
			}else{
				if( null != param.getRegion()[0]){
					whereParam.append(" and o.money >=  " + param.getRegion()[0]);
				}
			}
		}
		
		String sql = "select o.id,o.order_no,o.product_name,o.price,o.num,o.status,o.money,"
				+ "u.user_name,s.name,o.update_time,o.create_time,o.book_time, o.book_addr,o.consume_code "
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
			ob.setStatus(OrderStatus.status.get(o[5].toString()));
			ob.setStatusId(o[5].toString());
			ob.setAmount(o[6]==null ? "" :o[6] + "");
			ob.setUserName(o[7]==null ? "" :o[7] + "");
			ob.setOsName(o[8]==null ? "" :o[8] + "");
			ob.setUpdateTime(o[9]== null ?  "" : o[9]+"");
			ob.setCreateTime(o[10]==null ? "" :o[10] + "");
			
			ob.setBookTime(o[11]==null ? "" :o[11] + "");
			ob.setBookAddr(o[12]==null ? "" :o[12] + "");
			ob.setCode(o[13]==null ? "" :o[13] + "");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}

	
	public DataGrid<OrderBean> getOrderListPurchase(OrderParam param){
		DataGrid<OrderBean> dg = new DataGrid<OrderBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		
		//订单号
		if(!StringUtils.isEmpty(param.getOrderNo())){
			whereParam.append(" and o.order_no='"+param.getOrderNo().trim()+"'");
		}
		
		
		//加油站
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and os.name like '%" + param.getOsName().trim() + "%'");
		}
		
		
		//订单结算状态
		if(!StringUtils.isEmpty(param.getOjStatus()) && param.getOjStatus() >=0){
			// 1 未付款  2 已付款  3 已交货
			whereParam.append(" and o.status in ("+param.getOjStatus() +")");
		}
		
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.create_time<'"+param.getEndTime()+"'");
		}
		
		// 价格区间
		if( null!= param.getRegion()){
			if(param.getRegion().length==2){
				if( null != param.getRegion()[0]){
					whereParam.append(" and o.amount >=  " + param.getRegion()[0]);
				}
				if( null != param.getRegion()[1]){
					whereParam.append(" and o.amount <  " + param.getRegion()[1]);
				}
			}else{
				if( null != param.getRegion()[0]){
					whereParam.append(" and o.amount >=  " + param.getRegion()[0]);
				}
			}
		}
		
		String sql = "  SELECT o.*,os.name FROM  t_os_buy o  LEFT JOIN t_oil_station  os ON  o.os_id = os.id where 1=1   "+whereParam.toString()
				+ " order by o.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<OrderBean> result = new ArrayList<OrderBean>();
		
		String totalSql = " SELECT count(*) FROM  t_os_buy o  LEFT JOIN t_oil_station  os ON  o.os_id = os.id where 1=1 " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			OrderBean ob = new OrderBean();
			ob.setId(Long.valueOf(o[0]+""));
			ob.setOrderNo(o[3] + "");
			//ob.setProductName(o[2] + "");
			ob.setPrice(o[6]==null ? "" : o[6] + "");
			ob.setNum(Integer.valueOf(o[7]+""));
			ob.setStatus(OrderStatus.status.get(o[9].toString()));
			ob.setStatusId(o[9].toString());
			ob.setAmount(o[8]==null ? "" :o[8] + "");
			ob.setUserName(o[1]==null ? "" :o[1] + "");
			ob.setOsName(o[12]==null ? "" :o[12] + "");
			ob.setUpdateTime(o[11]== null ?  "" : o[11]+"");
			ob.setCreateTime(o[10]==null ? "" :o[10] + "");
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

	public int recharge(String phone, String amount) {
		//更新用户月
		//插入充值记录表
		int r = morderDao.insertLog(phone,amount);
		if(r > 0){
			//根据手机号查询ID
				Long userId = morderDao.queryUserIdForPhone(phone);
				if(userId <= 0){
					throw new RuntimeException("充值失败");
				}
				int rr = morderDao.updateRecharge(userId, amount);
				if(rr <= 0){
					throw new RuntimeException("充值失败");
				}
		}
		return r;
	}
	
	public DataGrid<ChargeLogBean> getRechargeList(BaseParam param){
		DataGrid<ChargeLogBean> dg = new DataGrid<ChargeLogBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		if(!StringUtils.isEmpty(param.phone)){
			whereParam.append(" and r.phone ='"+param.phone+"'");
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.create_time<'"+param.getEndTime()+"'");
		}
		String sql = "SELECT r.id,r.phone,r.`status`,r.amount,r.create_time from t_recharge_log r where 1=1 " + 
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
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}
	
	public void  delOrder(Long id) throws Exception{
		morderDao.deleteOrder(id);
	}
	
	public  Order  saveOrder(Order order){
		return  morderDao.save(order);
	}
	
	
	public AjaxResult  addUserOrder(UserOrderBean order) throws Exception{
		
		AjaxResult ar = new AjaxResult();
		if(StringUtils.isEmpty(order.getPhone())){
			ar.setStatus(300);
			ar.setResult("电话号码为空");
			return ar;
		}
		List<Muser> user = muserService.findByPhone(order.getPhone());
		if(null==user || user.isEmpty()){
			ar.setStatus(300);
			ar.setResult("系统无为此手机号用户");
			return ar;
		}
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Order o = new Order();
		o.setBookTime(null);
		o.setCreateTime(new java.util.Date());
		o.setPrice(new BigDecimal(order.getPrice()));
		o.setNum(order.getNum());
		Double money =  new BigDecimal(order.getPrice()).multiply(new BigDecimal(order.getNum())).doubleValue();
		o.setMoney( money);
		o.setOrderNo(CommonUtils.getMerchantOrderNo("22"));
		o.setOsId(accountService.getUser(u.getId()).getOsId());
		o.setProductId(order.getProId());
		o.setProductName(order.getProName());
		o.setStatus(0);
		o.setType(4); //  后台用户订单
		o.setUserId(user.get(0).getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		o.setBookTime(StringUtils.isEmpty(order.getBookTime())?null:sdf.parse(order.getBookTime()));
		Order newOrder =  this.saveOrder(o);
		List<Object[]>  stations  = mosDao.findOStationById(newOrder.getOsId());
		if(null==stations  || stations.isEmpty()){
			ar.setStatus(300);
			ar.setResult("加油站ID错误");
			return ar;
		}
		 Object[] station = stations.get(0);
		  PushOsBean bean = new PushOsBean();
		   	 bean.setOsId(newOrder.getOsId());
		   	 bean.setOrderId(newOrder.getId());
		   	 bean.setOrderNo(newOrder.getOrderNo());
		   	 bean.setAddr((null!=station && null!= station[3])?station[3].toString():"");
		   	 bean.setBookTime(order.getBookTime());
		   	 bean.setNum(newOrder.getNum());
		   	 bean.setPrice(newOrder.getPrice().toString());
		     bean.setProductId(newOrder.getProductId());
		   	 bean.setProductName(newOrder.getProductName());
		  if(!JPushUtil.pushOrderOs(order.getPhone(), bean)){
			 throw new Exception("推送失败");
		  }
		ar.setStatus(200);
		return ar;
	}
	
	
	public AjaxResult  addBookOrder(UserOrderBean order) throws Exception{
		
		AjaxResult ar = new AjaxResult();
		if(StringUtils.isEmpty(order.getPhone())){
			ar.setStatus(300);
			ar.setResult("电话号码为空");
			return ar;
		}
		List<Muser> user = muserService.findByPhone(order.getPhone());
		if(null==user || user.isEmpty()){
			ar.setStatus(300);
			ar.setResult("系统无为此手机号用户");
			return ar;
		}
		Order o = new Order();
		o.setBookTime(null);
		o.setCreateTime(new java.util.Date());
		o.setPrice(new BigDecimal(order.getPrice()));
		o.setNum(order.getNum());
		Double money =  new BigDecimal(order.getPrice()).multiply(new BigDecimal(order.getNum())).doubleValue();
		o.setMoney( money);
		o.setConsumeCode(((new  Random().nextInt(800000)+100000))+"");
		o.setOrderNo("H"+System.currentTimeMillis());
		o.setOsId(order.getStationId());
		o.setProductId(order.getProId());
		o.setProductName(order.getProName());
		o.setStatus(11);
		o.setType(3); //  预约单
		o.setUserId(user.get(0).getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		o.setBookTime(StringUtils.isEmpty(order.getBookTime())?null:sdf.parse(order.getBookTime()));
		Order newOrder =  this.saveOrder(o);
		List<Object[]>  stations  = mosDao.findOStationById(newOrder.getOsId());
		if(null==stations  || stations.isEmpty()){
			ar.setStatus(300);
			ar.setResult("加油站ID错误");
			return ar;
		}
		 Object[] station = stations.get(0);
		  PushOsBean bean = new PushOsBean();
		   	 bean.setOsId(newOrder.getOsId());
		   	 bean.setOrderId(newOrder.getId());
		   	 bean.setOrderNo(newOrder.getOrderNo());
		   	 bean.setAddr((null!=station && null!= station[3])?station[3].toString():"");
		   	 bean.setBookTime(order.getBookTime());
		   	 bean.setNum(newOrder.getNum());
		   	 bean.setPrice(newOrder.getPrice().toString());
		     bean.setProductId(newOrder.getProductId());
		   	 bean.setProductName(newOrder.getProductName());
		  if(!JPushUtil.pushOrderOs(order.getPhone(), bean)){
			 throw new Exception("推送失败");
		  }
		ar.setStatus(200);
		return ar;
	}
	
	public  Order findOne(Long id){
		return morderDao.findOne(id);
	}
	
}
