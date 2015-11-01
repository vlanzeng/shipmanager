package org.springside.examples.quickstart.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springside.examples.quickstart.domain.DerateParam;
import org.springside.examples.quickstart.entity.Derate;
import org.springside.examples.quickstart.repository.DerateDao;

@Component
@Transactional
public class MderateService {
	@Autowired
	private DerateDao derateDao;
	
	public  int insert( DerateParam dParam ){
		
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
}
