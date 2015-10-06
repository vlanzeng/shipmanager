package org.springside.examples.quickstart.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springside.examples.quickstart.contants.HybConstants;
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
	
	public int uStatus(String orderId, String status, String oldStatus) {
		return mosDao.updateStatus(Integer.valueOf(status), Integer.valueOf(oldStatus), orderId);
	}

	public DataGrid<OilStationBean> getOsList(OsParam param) {
		DataGrid<OilStationBean> dg = new DataGrid<OilStationBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and o.name ='" + param.getOsName() + "'");
		}
		
		if(!StringUtils.isEmpty(param.getStatus()) && param.getStatus() >=0){
			whereParam.append(" and o.status="+param.getStatus());
		}
		
		if(!StringUtils.isEmpty(param.getCityId()) && param.getStatus() >=0){
			whereParam.append(" and o.city_id="+param.getCityId());
		}
		
		if(!StringUtils.isEmpty(param.getCityName()) && param.getStatus() >=0){
			whereParam.append(" and c.name ='"+param.getCityName() + "'");
		}
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.create_time<'"+param.getEndTime()+"'");
		}
		String sql = "select o.id, o.name,o.address,o.phone,o.`status`,o.pic_url,o.create_time,c.name cname from t_oil_station o, t_city c where o.city_id=c.id " + whereParam.toString()+" "
				+ "order by o.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<OilStationBean> result = new ArrayList<OilStationBean>();
		String totalSql = "select count(1) from t_oil_station o, t_city c where o.city_id=c.id " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		
		int total = Integer.valueOf(q1.getSingleResult()+"");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Object[] o : infoList){
			OilStationBean ob = new OilStationBean();
			ob.setId(Integer.valueOf(o[0]+""));
			ob.setName(o[1] + "");
			ob.setAddress(o[2] + "");
			ob.setPhone(o[3] + "");
			ob.setStatus(Integer.valueOf(o[4] + ""));
			ob.setPicUrl(o[5] + "");
			ob.setCreateTime(o[6] + "");
			ob.setCityName(o[7] + "");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}

	public int updatePicUrl(MultipartFile file,OsParam param, HttpServletRequest request) {
	    String fileName = file.getOriginalFilename();
	    String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
	    String newFileName = "JYZ_" + String.valueOf(System.currentTimeMillis()) + "." + extensionName;
	 // 根据配置文件获取服务器图片存放路径
 		String saveFilePath = request.getRealPath("") + "/osImage";
 		/* 构建文件目录 */
 		File fileDir = new File(saveFilePath);
 		if (!fileDir.exists()) {
 			fileDir.mkdirs();
 		}
 		try {
 			
 			FileOutputStream out = new FileOutputStream(new File(saveFilePath,newFileName));
 			// 写入文件
 			out.write(file.getBytes());
 			out.flush();
 			out.close();
 			String PICUrl = "http://" + HybConstants.FW_ADDRESS + request.getContextPath() + "/osImage/" + newFileName;
 			//保存信息
 			int r = mosDao.insertOs(param.osName, param.addr, param.latitude, param.longitude,PICUrl,param.phone,Long.valueOf(param.cityId));
 			return r;
 		} catch (Exception e) {
 			e.printStackTrace();
 			return 0;
 		}
	}
}
