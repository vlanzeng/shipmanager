package org.springside.examples.quickstart.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springside.examples.quickstart.domain.AdvertBean;
import org.springside.examples.quickstart.domain.BaseParam;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OilStationBean;
import org.springside.examples.quickstart.domain.OsParam;
import org.springside.examples.quickstart.entity.Advert;
import org.springside.examples.quickstart.repository.AdvertDao;
import org.springside.examples.quickstart.repository.MUserDao;
import org.springside.examples.quickstart.repository.MosDao;
import org.springside.examples.quickstart.repository.UserDao;

@Component
@Transactional
public class MAdvertService {
	@Autowired
	private MosDao mosDao;
	
	@Autowired
	private MUserDao muserDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdvertDao advertDao;
	
	 @PersistenceContext  
	 private EntityManager em; 
	

	 public int insertAdvert( String url, String purl, String type, String title, String os){
		 Advert advert = new Advert();
		 
		 advert.setUrl(url);
		 advert.setPurl(purl);
		 advert.setTitle(title);
		 advert.setType(type+":"+os);
		 try{
			 Advert ret = advertDao.save(advert);
			 if(ret == null){
				 return -1;
			 }
		 }catch(Exception e){
			 e.printStackTrace();
			 return -2;
		 }
		 
		 return 0;
	 }
	 
	public DataGrid<AdvertBean> getAdvertsList(BaseParam bp) {
		DataGrid<AdvertBean> dg = new DataGrid<AdvertBean>();
		int start = (bp.getPage() - 1) * bp.getRows();
		StringBuilder str = new StringBuilder();
		str.append(" where 1=1 ");
		if(bp.getType() != null){
			str.append(" and type like '"+bp.getType()+"%'");
		}
		String sql = "  SELECT * FROM  t_advert  " +str.toString()+" limit "+start+","+bp.getRows()+"";
		Query q = em.createNativeQuery(sql);
		List<Object[]> infoList = q.getResultList();
		List<AdvertBean> result = new ArrayList<AdvertBean>();
		String totalSql = "SELECT count(*) FROM  t_advert " ;
		Query q1 = em.createNativeQuery(totalSql);
		
		int total = Integer.valueOf(q1.getSingleResult()+"");
		for(Object[] o : infoList){
			AdvertBean ob = new AdvertBean();
			ob.setId(Integer.valueOf(o[0]+""));
			ob.setUrl(o[1] + "");
			ob.setPurl(o[2] + "");
			ob.setTitle(o[3] + "");
			ob.setType(o[4] + "");
			result.add(ob);
		}
		dg.setTotal(total);
		dg.setRows(result);
		return dg;
	}

}
