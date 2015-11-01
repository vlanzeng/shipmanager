package org.springside.examples.quickstart.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.DerateParam;
import org.springside.examples.quickstart.domain.OilStationBean;
import org.springside.examples.quickstart.domain.OsComment;
import org.springside.examples.quickstart.domain.OsOilBean;
import org.springside.examples.quickstart.domain.OsOilParam;
import org.springside.examples.quickstart.domain.OsParam;
import org.springside.examples.quickstart.entity.Derate;
import org.springside.examples.quickstart.entity.OilStation;
import org.springside.examples.quickstart.entity.OilStation_1;
import org.springside.examples.quickstart.entity.User;
import org.springside.examples.quickstart.repository.DerateDao;
import org.springside.examples.quickstart.repository.MUserDao;
import org.springside.examples.quickstart.repository.MosDao;
import org.springside.examples.quickstart.repository.UserDao;
import org.springside.examples.quickstart.utils.RoleUtil;

@Component
@Transactional
public class MosService {
	@Autowired
	private MosDao mosDao;
	
	@Autowired
	private MUserDao muserDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DerateDao derateDao;
	
	 @PersistenceContext  
	 private EntityManager em; 
	
	public int uStatus(String orderId, String status, String oldStatus) {
		return mosDao.updateStatus(Integer.valueOf(status), Integer.valueOf(oldStatus),Long.valueOf(orderId));
	}
	
	public int updateStaus(String sosId, String sstatus){
		Integer status = Integer.valueOf(sstatus);
		Long id = Long.valueOf(sosId);
		
		return mosDao.updateOSStatus(status, id);
	}
	
	public int deleteOS(String osid){
		Long id = Long.valueOf(osid);
		return mosDao.deleteOSStatus(id);
	}
	
	public DataGrid<OsComment> getOsCommentList( OsParam param ){
		DataGrid<OsComment> dg = new DataGrid<OsComment>();
		int start = (param.getPage() - 1) * param.getRows();
		
		String sql = "select o.name, u.user_name, a.content  from t_appraise a left join t_oil_station o on a.os_id=o.id left join t_user u on a.user_id=u.id where a.os_id= " + param.getOsId()
				+ " limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<OsComment> result = new ArrayList<OsComment>();
		String totalSql = "select count(1) from t_appraise  where os_id= " + param.getOsId();
		Query q1 = em.createNativeQuery(totalSql);
		int total = Integer.valueOf(q1.getSingleResult()+"");
		
		for(Object[] o : infoList){
			OsComment ob = new OsComment();
			ob.setComment(o[2]+"");
			ob.setOsName(o[0]+"");
			ob.setUserName(o[1]+"");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}

	public DataGrid<OilStationBean> getOsList(String login_name, OsParam param) {
		DataGrid<OilStationBean> dg = new DataGrid<OilStationBean>();
		StringBuffer whereParam = new StringBuffer();
		
		//获取登录用户角色
		User user = userDao.findByLoginName(login_name, 1);
//		if(user==null || StringUtils.isEmpty(user.getRoles()) || (!HybConstants.ADMIN.equalsIgnoreCase(user.getRoles()) 
//				&& !HybConstants.JYZADMIN.equalsIgnoreCase(user.getRoles()))){
//			return dg;
//		}
		if( user==null || StringUtils.isEmpty(user.getRoles()) || ( !RoleUtil.isRole(user.getRoles(), HybConstants.ADMIN)
				&&  !RoleUtil.isRole(user.getRoles(), HybConstants.JYZADMIN) )  ){
			return dg;
			
		}
		
		int start = (param.getPage() - 1) * param.getRows();
		
		//jyzadmin只操作自己加油站的用户
		boolean rb = !RoleUtil.isRole(user.getRoles(), HybConstants.ADMIN);
		rb = rb && RoleUtil.isRole(user.getRoles(), HybConstants.JYZADMIN);
		if(rb){
			whereParam.append(" and o.id="+user.getOsId());
		}
		
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and o.name like '%" + param.getOsName() + "%'");
		}
		
		if(!StringUtils.isEmpty(param.getStatus()) && param.getStatus() >=0){
			whereParam.append(" and o.status="+param.getStatus());
		}
		
		if(!StringUtils.isEmpty(param.getCityId()) ){
			whereParam.append(" and o.city_id="+param.getCityId());
		}
		
		if(!StringUtils.isEmpty(param.getCityName()) ){
			whereParam.append(" and c.name ='"+param.getCityName() + "'");
		}
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and o.create_time >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and o.create_time<'"+param.getEndTime()+"'");
		}
		String sql = "select o.id, o.name,o.address,o.phone,o.`status`,o.pic_url,o.create_time,c.name cname, o.latitude, o.longitude, d.info from t_oil_station o left join t_city c on o.city_id=c.id left join t_derate d on o.id=d.os_id where 1=1 " + whereParam.toString()+" "
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
			ob.setLatitude(o[8]+"");
			ob.setLongitude(o[9]+"");
			ob.setDerate(o[10]+"");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}

	private  int insertDeprate( DerateParam dParam ){
		
		Long osId = dParam.getOsId();
		Integer man = dParam.getMan();
		Integer jian = dParam.getJian();
		String status = dParam.getStatus();
		
		Derate en = new Derate();
		String info = man+"-"+jian;
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String time = df.format(new Date());
		Date now = new Date();
		
		en.setOsId(osId);
		en.setCreateTime( now );
		en.setUpdateTime( now );
		en.setInfo(info);
		if(StringUtils.isEmpty(status)){
			en.setStatus("1");
		}else{
			en.setStatus(status);
		}
		
		try{
			derateDao.save(en);
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	public int updatePicUrl(MultipartFile file,OsParam param, 
			DerateParam dParam,
			HttpServletRequest request) {
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
 		
 		OilStation_1 osret = null;
 		try {
 			
 			FileOutputStream out = new FileOutputStream(new File(saveFilePath,newFileName));
 			// 写入文件
 			out.write(file.getBytes());
 			out.flush();
 			out.close();
 			String PICUrl = "http://" + HybConstants.FW_ADDRESS + request.getContextPath() + "/osImage/" + newFileName;
 			//保存信息
 			int r = 0;
 			//mosDao.insertOs(param.osName, param.addr, param.latitude, param.longitude,PICUrl,param.phone,Long.valueOf(param.cityId));
 			
 			OilStation_1 os = new OilStation_1();
 			os.setAddress(param.getAddr());
 			os.setCityId(param.getCityId()==null?null:Integer.valueOf(param.getCityId()));
 			os.setName(param.getOsName());
 			//os.setDesc(param.getOsName());
 			os.setLatitude(param.getLatitude());
 			os.setLongitude(param.getLongitude());
 			os.setPicUrl(PICUrl);
 			os.setPhone(param.getPhone());
 			os.setCredit(5f);
 			os.setQuality(5f);
 			os.setService(5f);
 			os.setCouponFlag(1);
 			os.setDerateFlag(1);
 			os.setStatus(1);
 			os.setCreateTime(new Date());
// 			@Query(value="insert into t_oil_station(name,`desc`,address,latitude,longitude,pic_url,phone,city_id,credit,quality,"
// 					+ "service,coupon_flag,derate_flag,status,create_time) values(?1,?1,?2,?3,?4,?5,?6,?7,5,5,5,1,1,1,now())", nativeQuery=true)
// 			int insertOs(String osName, String addr, String latitude,String longitude,String picurl, String phone, Long cityId);
 			
 			osret = mosDao.save(os);
 			
 			if( osret == null) return -1;
 			dParam.setOsId(osret.getId());
 			if(dParam.getJian() != null && dParam.getMan() != null)
 				r = insertDeprate(dParam);
 			if(r < 0){
 	 			if(osret != null){
 	 				mosDao.delete(osret.getId());
 	 			}
 	 			return -1;
 			}
 			return 0;
 		} catch (Exception e) {
 			e.printStackTrace();
 			if(osret != null){
 				mosDao.delete(osret.getId());
 			}
 			return -1;
 		}
	}

	public int updatePicUrl_stub(OsParam param, 
			DerateParam dParam) {
 		OilStation_1 osret = null;
 		String PICUrl = "www.testurl.com";
 		try {
 			
 			//保存信息
 			int r = 0;
 			//mosDao.insertOs(param.osName, param.addr, param.latitude, param.longitude,PICUrl,param.phone,Long.valueOf(param.cityId));
 			
 			OilStation_1 os = new OilStation_1();
 			os.setAddress(param.getAddr());
 			os.setCityId(param.getCityId()==null?null:Integer.valueOf(param.getCityId()));
 			os.setName(param.getOsName());
 			//os.setDesc(param.getOsName());
 			os.setLatitude(param.getLatitude());
 			os.setLongitude(param.getLongitude());
 			os.setPicUrl(PICUrl);
 			os.setPhone(param.getPhone());
 			os.setCredit(5f);
 			os.setQuality(5f);
 			os.setService(5f);
 			os.setCouponFlag(1);
 			os.setDerateFlag(1);
 			os.setStatus(1);
 			os.setCreateTime(new Date());
// 			@Query(value="insert into t_oil_station(name,`desc`,address,latitude,longitude,pic_url,phone,city_id,credit,quality,"
// 					+ "service,coupon_flag,derate_flag,status,create_time) values(?1,?1,?2,?3,?4,?5,?6,?7,5,5,5,1,1,1,now())", nativeQuery=true)
// 			int insertOs(String osName, String addr, String latitude,String longitude,String picurl, String phone, Long cityId);
 			
 			osret = mosDao.save(os);
 			mosDao.updateOSDesc( osret.getName(), osret.getId() );
 			
 			if( osret == null) return -1;
 			dParam.setOsId(osret.getId());
 			r = insertDeprate(dParam);
 			if(r < 0){
 	 			if(osret != null){
 	 				mosDao.delete(osret.getId());
 	 			}
 	 			return -1;
 			}
 			return 0;
 		} catch (Exception e) {
 			e.printStackTrace();
 			if(osret != null){
 				mosDao.delete(osret.getId());
 			}
 			return -1;
 		}
	}

	public DataGrid<OsOilBean> getOsBuyOilList(OsOilParam param) {
		DataGrid<OsOilBean> dg = new DataGrid<OsOilBean>();
		StringBuffer whereParam = new StringBuffer();
		int start = (param.getPage() - 1) * param.getRows();
		
		if(!StringUtils.isEmpty(param.getStatus()) && param.getStatus() >=0){
			whereParam.append(" and b.status="+param.getStatus());
		}
		
		if(!StringUtils.isEmpty(param.getOrderNo())){
			whereParam.append(" and b.order_no='"+param.getOrderNo()+"'");
		}
		
		if(!StringUtils.isEmpty(param.getOsName())){
			whereParam.append(" and s.name like '%"+param.getOsName().trim()+"%'");
		}
		
		if(!StringUtils.isEmpty(param.startTime)){
			whereParam.append(" and    DATE_FORMAT( b.create_time,'%Y-%m-%d') >='"+param.startTime+"'");
		}
		
		if(!StringUtils.isEmpty(param.getEndTime())){
			whereParam.append(" and DATE_FORMAT( b.create_time,'%Y-%m-%d') <='"+param.getEndTime()+"'");
		}
		String sql = "select b.id,b.user_name,b.os_id,s.name os_name,b.order_no,b.oil_id,o.name oil_name,b.price,b.num,b.amount,"
				+ "b.status,b.create_time "
				+ "from t_os_buy b,t_oil_station s,t_oil o where b.os_id=s.id and b.oil_id=o.id " + whereParam.toString()+" "
				+ "order by o.create_time desc limit "+start+","+param.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<OsOilBean> result = new ArrayList<OsOilBean>();
		String totalSql = "select count(1) from t_os_buy b,t_oil_station s,t_oil o where b.os_id=s.id and b.oil_id=o.id " + whereParam.toString();
		Query q1 = em.createNativeQuery(totalSql);
		int total = Integer.valueOf(q1.getSingleResult()+"");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Object[] o : infoList){
			OsOilBean ob = new OsOilBean();
			ob.setId(Integer.valueOf(o[0]+""));
			ob.userName = o[1]+"";
			ob.osId = Long.valueOf(o[2]+"");
			ob.osName = o[3]+"";
			ob.orderNo = o[4]+"";
			ob.oilId = Long.valueOf(o[5]+"");
			ob.oilName = o[6]+"";
			ob.price = o[7]+"";
			ob.num = Integer.valueOf(o[8]+"");
			ob.amount = o[9]+"";
			ob.status = Integer.valueOf(o[10]+"");
			ob.createTime = o[11]+"";
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}
	
	public int addOsOilOrder(OsOilParam param){
		User user = userDao.findByLoginName(param.userName, 1);
		param.osId = user.getOsId();
		return mosDao.addOsOilOrder(param.userName,param.osId, param.orderNo,param.oilId,param.price, param.num,param.amount);
	}
	
	public  List<Object[]> findAllOsation(){
		return mosDao.findAllOStation();
	}
	
	public  List<Object[]> findById(Long id){
		return mosDao.findOStationById(id);
	}
}
