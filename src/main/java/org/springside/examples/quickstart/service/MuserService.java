package org.springside.examples.quickstart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.MuserBean;
import org.springside.examples.quickstart.domain.MuserParam;
import org.springside.examples.quickstart.domain.NUserParam;
import org.springside.examples.quickstart.domain.UserBean;
import org.springside.examples.quickstart.domain.UserParam;
import org.springside.examples.quickstart.entity.Muser;
import org.springside.examples.quickstart.repository.AppUserDao;
import org.springside.examples.quickstart.repository.MUserDao;
import org.springside.examples.quickstart.repository.UserDao;
import org.springside.examples.quickstart.utils.RoleUtil;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;
//github.com/zhaohfup/shipmanager.git
import org.springside.examples.quickstart.entity.User;

@Component
@Transactional
public class MuserService {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	
	@Autowired
	private MUserDao muserDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AppUserDao auserDao; 
	
	 @PersistenceContext  
	 private EntityManager em; 
	 
	 
	 public  Muser  findOne(Long id){
		 return muserDao.findOne(id);
	 }
	 
	 public  List<Muser> findByPhone(String phone){
		 return muserDao.findByPhone(phone);
	 }

	public DataGrid<MuserBean> getMuserList(String loginName, MuserParam param) {
		DataGrid<MuserBean> dg = new DataGrid<MuserBean>();
		StringBuffer whereParam = new StringBuffer();
		//获取登录用户角色
		User user = userDao.findByLoginName(loginName, 1);
		if(StringUtils.isEmpty(user.getRoles()) || ( !RoleUtil.isRole(user.getRoles(), HybConstants.ADMIN)
				&&  !RoleUtil.isRole(user.getRoles(), HybConstants.JYZADMIN) )  ){
			return dg;
			
		}
		
		//jyzadmin只操作自己加油站的用户
		if( !RoleUtil.isRole(user.getRoles(), HybConstants.ADMIN) && RoleUtil.isRole(user.getRoles(), HybConstants.JYZADMIN)){
			whereParam.append(" and u.os_id="+user.getOsId());
		}
		
		int start = (param.getPage() - 1) * param.getRows();
		//订单类型
		if(!StringUtils.isEmpty(param.getStatus()) && param.getStatus()>= 0){
			whereParam.append(" and u.status="+param.getStatus());
		}
		//用户名或手机号
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and s.name='"+param.getOsName().trim() + "'");
		}
		//用户名或手机号
		if(!StringUtils.isEmpty(param.getUserName())){
			whereParam.append(" and u.login_name='"+param.getUserName().trim() + "'");
		}
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and u.register_date >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and u.register_date<'"+param.getEndTime()+"'");
		}
		
		if( !RoleUtil.isRole(user.getRoles(), HybConstants.ADMIN) && RoleUtil.isRole(user.getRoles(), HybConstants.JYZADMIN)){
			whereParam.append(" and u.roles in ('jyzAdmin','jyzcwqx','jyzjygqx') and u.os_id="+user.getOsId());
		}
		
		String sql = "SELECT u.id,u.login_name,u.os_id,u.roles,u.register_date,u.status, s.name as osName "
				+ "from ss_user u LEFT JOIN t_oil_station s on u.os_id=s.id where 1=1 "+whereParam.toString()+" "
				+ "order by u.register_date desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<MuserBean> result = new ArrayList<MuserBean>();
		
		String totalSql = "select count(1) from ss_user u LEFT JOIN t_oil_station s on u.os_id=s.id where 1=1 " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			MuserBean ob = new MuserBean();
			ob.setId(o[0]==null?null:Long.valueOf(o[0] + ""));
			ob.setUserName(o[1]+"");
			ob.setOsId(o[2]==null?-1:Long.valueOf(o[2]+""));
			ob.setRole(o[3]+"");
			ob.setCreateTime(o[4]+"");
			ob.setStatus(o[5]==null?null:Integer.valueOf(o[5]+""));
			ob.setOsName(o[6]==null?"":o[6]+"");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}

	//普通用户与加油站没有关联，也就只有admin能管理，不作权限判断
	public DataGrid<UserBean> getNuserList(UserParam param){
		DataGrid<UserBean> dg = new DataGrid<UserBean>();
		StringBuffer whereParam = new StringBuffer();
		StringBuffer where1 = new StringBuffer();
		StringBuffer where2 = new StringBuffer();
		//jyzadmin只操作自己加油站的用户
		int start = (param.getPage() - 1) * param.getRows();
		//用户名或手机号
		if(!StringUtils.isEmpty(param.getPhone())){
			where2.append(" and exists (select * from t_user tu where tu.id=trinima.user_id and tu.phone='"+param.getPhone().trim()+"')");
			where1.append(" and phone = '"+param.getPhone().trim() + "'");
			whereParam.append(" and u.phone='"+param.getPhone().trim() + "'");
		}
		//用户名或手机号
		if(!StringUtils.isEmpty(param.getUserName())){
			whereParam.append(" and u.user_name='"+param.getUserName().trim() + "'");
		}
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and u.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and u.create_time<'"+param.getEndTime()+"'");
		}
		
//		if(HybConstants.JYZADMIN.equalsIgnoreCase(user.getRoles())){
//			whereParam.append(" and u.roles in ('jyzAdmin','jyzcwqx','jyzjygqx') and u.os_id="+user.getOsId());
//		}
		String sql = "SELECT u.user_name, u.phone,u.ship_name, u.ship_no, u.create_time, o.money money, r.amount recharge, b.fund  "
			+" from t_user u left join ( select user_id, sum(money) money from t_order trinima where 1=1 "
			+where2.toString()	
			+" group by 1 ) o on u.id=o.user_id left join  t_user_bank  b on u.id=b.user_id left join ( select phone, sum(amount) amount from t_recharge_log where 1=1 "
			+where1.toString()
			+" group by 1 ) r on u.phone=r.phone where 1=1 "
			+whereParam.toString()+" order by create_time desc limit "+start+","+param.getRows()+" ";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<UserBean> result = new ArrayList<UserBean>();
		
		String totalSql = "select count(*) from t_user u where 1=1 "
		+ whereParam.toString() +" ";
		Query q1 = em.createNativeQuery(totalSql);
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			UserBean ob = new UserBean();
			ob.setUserName(o[0]+"");
			ob.setPhone(o[1]+"");
			ob.setShipname(o[2]+"");
			ob.setShipno(o[3]+"");
			ob.setCreatetime(o[4]+"");
			ob.setFee(o[5]==null?null: new BigDecimal(Double.valueOf(o[5]+"")).setScale(2	, BigDecimal.ROUND_HALF_UP).doubleValue() );
			ob.setRechargeamount(o[6]==null?null:new BigDecimal(Double.valueOf(o[6]+"")).setScale(2	, BigDecimal.ROUND_HALF_UP).doubleValue() );
			ob.setLeft(o[7]==null?null:new BigDecimal(Double.valueOf(o[7]+"")).setScale(2	, BigDecimal.ROUND_HALF_UP).doubleValue() );
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}
	
	//普通用户与加油站没有关联，也就只有admin能管理，不作权限判断
	public DataGrid<UserBean> getNuserAllList(UserParam param){
		DataGrid<UserBean> dg = new DataGrid<UserBean>();
		StringBuffer whereParam = new StringBuffer();
		//jyzadmin只操作自己加油站的用户
		int start = (param.getPage() - 1) * param.getRows();
		//用户名或手机号
		if(!StringUtils.isEmpty(param.getPhone())){
			whereParam.append(" and u.phone='"+param.getPhone().trim() + "'");
		}
		//用户名或手机号
		if(!StringUtils.isEmpty(param.getUserName())){
			whereParam.append(" and u.user_name='"+param.getUserName().trim() + "'");
		}
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and u.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and u.create_time<'"+param.getEndTime()+"'");
		}
		
//		if(HybConstants.JYZADMIN.equalsIgnoreCase(user.getRoles())){
//			whereParam.append(" and u.roles in ('jyzAdmin','jyzcwqx','jyzjygqx') and u.os_id="+user.getOsId());
//		}
		String sql = "SELECT u.user_name, u.phone,u.ship_name, u.ship_no, u.create_time, SUM(o.money) money,b.fund recharge "
			+" from t_user u left join t_order o on u.id=o.user_id left join t_user_bank b on u.id=b.user_id where 1=1 "
			+whereParam.toString()+" group by 1,2,3,4,5,7 order by create_time desc  ";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<UserBean> result = new ArrayList<UserBean>();
		
		String totalSql = "select count(*) from ( select  u.user_name, u.phone,u.ship_name, u.ship_no, u.create_time from t_user u left join t_order o on u.id=o.user_id left join t_user_bank b on u.id=b.user_id where 1=1 "
		+ whereParam.toString() +" group by 1,2,3,4,5 ) a";
		Query q1 = em.createNativeQuery(totalSql);
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			UserBean ob = new UserBean();
			ob.setUserName(o[0]+"");
			ob.setPhone(o[1]+"");
			ob.setShipname(o[2]+"");
			ob.setShipno(o[3]+"");
			ob.setCreatetime(o[4]+"");
			ob.setFee(o[5]==null?null:Double.valueOf(o[5]+""));
			ob.setRechargeamount(o[6]==null?null:Double.valueOf(o[6]+""));
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}
	
	public int add(MuserParam param) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(param.getPwd().getBytes(), salt, HASH_INTERATIONS);
		int r = 0;
		Long osId = -1L;
		try {
			if(!StringUtils.isEmpty(param.getOsId())){
				osId = Long.valueOf(param.getOsId());
			}
			r = muserDao.add(param.getUserName(), Encodes.encodeHex(hashPassword), Encodes.encodeHex(salt), param.getRole(), osId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public int delete( String id){
		int r = 0;
		try{
		r = muserDao.deleteMuser(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return r;
	}
	
	public int updateMUserInfo(String username, String pwd, String id){
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(pwd.getBytes(), salt, HASH_INTERATIONS);
		int r = 0;
		try{
			r = muserDao.updateMuser(username, Encodes.encodeHex(hashPassword), Encodes.encodeHex(salt), id);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return r;
	}
	
	public int nadd(NUserParam param) {
//		byte[] salt = Digests.generateSalt(SALT_SIZE);
//		byte[] hashPassword = Digests.sha1(param.getPwd().getBytes(), salt, HASH_INTERATIONS);
		int r = 0;
		//Long osId = -1L;
		try {
//			if(!StringUtils.isEmpty(param.getOsId())){
//				osId = Long.valueOf(param.getOsId());
//			}
			r = muserDao.nadd(param.getUserName(), param.getPwd(), param.getPhone(), param.getC_name(), param.getC_no());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	public int uStatus(String userId, String status) {
		// TODO Auto-generated method stub
		int res= muserDao.updateStatus(status, userId);
		return res;
	}
	
	public  List<Object[]>  selectAppUserByPhone(String phone) {
		return  this.auserDao.selectAppUserByPhone(phone);
	}
	
	public  Iterable<Muser> selectByIds(List<Long> ids){
		return	muserDao.findAll(ids);
		
	}

}
