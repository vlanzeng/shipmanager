package org.springside.examples.quickstart.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.repository.InformationDao;

/**
 * @author zhf
 *
 */
@Component
@Transactional
public class InformationService {
	private InformationDao infoDao;
	
	public InformationDao getInfoDao() {
		return infoDao;
	}
	@Autowired
	public void setInfoDao(InformationDao infoDao) {
		this.infoDao = infoDao;
	}
	
	
} 
