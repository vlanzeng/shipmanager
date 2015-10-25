package org.springside.examples.quickstart.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springside.examples.quickstart.domain.CouponBean;
import org.springside.examples.quickstart.domain.CouponParam;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.ResultList;
import org.springside.examples.quickstart.repository.McouponDao;

@Component
@Transactional
public class McouponService {
	@Autowired
	private McouponDao mcouponDao;
	
	 @PersistenceContext  
	 private EntityManager em; 
	
	/**
	 * 获取优惠券列表
	 * @param param
	 * @return
	 */
	public DataGrid<CouponBean> getCouponList(CouponParam param){
		DataGrid<CouponBean> dg = new DataGrid<CouponBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		if(!StringUtils.isEmpty(param.getName())){
			whereParam.append(" and c.name='"+param.getName()+"'");
		}
		
		if(!StringUtils.isEmpty(param.getType()) && param.getType() > 0){
			whereParam.append(" and c.type="+param.getType());
		}
		
		if(!StringUtils.isEmpty(param.getFaceLimit())){
			whereParam.append(" and c.face_value="+param.getFaceLimit());
		}
		
		if(!StringUtils.isEmpty(param.getStatus()) && param.getStatus() >=0){
			whereParam.append(" and c.status="+param.getStatus());
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and  DATE_FORMAT(c.create_time,'%Y-%m-%d')   >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and  DATE_FORMAT(c.create_time,'%Y-%m-%d')  <='"+param.getEndTime()+"'");
		}
		
		String sql = "select id,name,face_value,limit_value,os_id,type,status,start_time,end_time,create_time"
				+ " from t_coupon c   where 1=1 " + whereParam.toString()
				+ " order by c.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<CouponBean> result = new ArrayList<CouponBean>();
		String totalSql = "select count(1) from t_coupon c where 1=1 " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		int total = Integer.valueOf(q1.getSingleResult()+"");
		SimpleDateFormat fromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0 ; (infoList != null && i < infoList.size()); i++){
			Object[] o = infoList.get(i);
			CouponBean bean = new CouponBean();
			bean.setId(Long.valueOf(o[0] + ""));
			bean.setName(o[1] + "");
			bean.setFaceValue(Integer.valueOf(o[2] + ""));
			bean.setLimitValue(Integer.valueOf(o[3] + ""));
			bean.setOsId(Long.valueOf(o[4] + ""));
			bean.setType(Integer.valueOf(o[5] + ""));
			bean.setStatus(Integer.valueOf(o[6] + ""));
			bean.setStartTime(o[7] + "");
			bean.setEndTime(o[8] + "");
			try {
				bean.setCreateTime(fromat.parse(o[9].toString()).toLocaleString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result.add(bean);
		}
		dg.setRows(result);
		dg.setTotal(total);
		return dg;
	}

	public int add(CouponParam param) {
		String faceLimit = param.getFaceLimit();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = format.parse(param.getStartTime());
			endDate = format.parse(param.getEndTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mcouponDao.add(param.getName(), Integer.valueOf(faceLimit.split("-")[0]), Integer.valueOf(faceLimit.split("-")[1]), 
				param.getType(),startDate, endDate);
	}

	public int uStatus(String couponId, String status) {
		return mcouponDao.updateStatus(Integer.valueOf(status), Long.valueOf(couponId));
	}
}
