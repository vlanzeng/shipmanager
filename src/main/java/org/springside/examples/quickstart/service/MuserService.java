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
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.MuserBean;
import org.springside.examples.quickstart.domain.MuserParam;
import org.springside.examples.quickstart.entity.User;
import org.springside.examples.quickstart.repository.MUserDao;
import org.springside.examples.quickstart.repository.UserDao;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

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
	
	 @PersistenceContext  
	 private EntityManager em; 

	public DataGrid<MuserBean> getMuserList(String loginName, MuserParam param) {
		DataGrid<MuserBean> dg = new DataGrid<MuserBean>();
		StringBuffer whereParam = new StringBuffer();
		//获取登录用户角色
		User user = userDao.findByLoginName(loginName, 1);
		if(StringUtils.isEmpty(user.getRoles()) || (!HybConstants.ADMIN.equalsIgnoreCase(user.getRoles()) 
				&& !HybConstants.JYZADMIN.equalsIgnoreCase(user.getRoles()))){
			return dg;
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
			whereParam.append(" and s.login_name='"+param.getUserName().trim() + "'");
		}
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.register_date >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.register_date<'"+param.getEndTime()+"'");
		}
		
		if(HybConstants.JYZADMIN.equalsIgnoreCase(user.getRoles())){
			whereParam.append(" and u.roles in ('jyzAdmin','jyzcwqx','jyzjygqx') and o.os_id="+user.getOsId());
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
			ob.setId(Long.valueOf(o[0] + ""));
			ob.setUserName(o[1]+"");
			ob.setOsId(o[2]==null?-1:Long.valueOf(o[2]+""));
			ob.setRole(o[3]+"");
			ob.setCreateTime(o[4]+"");
			ob.setStatus(Integer.valueOf(o[5]+""));
			ob.setOsName(o[6]==null?"":o[6]+"");
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

	public int uStatus(String userId, String status) {
		// TODO Auto-generated method stub
		return 0;
	}

}
