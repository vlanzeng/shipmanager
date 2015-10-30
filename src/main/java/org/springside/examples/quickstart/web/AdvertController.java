package org.springside.examples.quickstart.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.AdvertBean;
import org.springside.examples.quickstart.domain.AdvertType;
import org.springside.examples.quickstart.domain.BaseParam;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.service.MAdvertService;
import org.springside.examples.quickstart.service.MosService;

@Controller
@RequestMapping(value="/advert")
public class AdvertController extends BaseController implements HybConstants{
	Logger logger = Logger.getLogger(Logger.class);

	
	
	
	@Autowired
	private MAdvertService mAdvertService ;
	
	@Autowired
	private MosService mosService;
	
	@RequestMapping("/index")
	public String  index(){
		return "/advert/index";
	}
	
	
	@RequestMapping("/query")
	@ResponseBody
	public  DataGrid<AdvertBean>  query(HttpServletRequest request){
		BaseParam param = new BaseParam();
		Integer[] pageInfo = getPageInfo(request);
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		DataGrid<AdvertBean> adverts =  mAdvertService.getAdvertsList(param);
		List<AdvertBean> data =  adverts.getRows();	
		for (AdvertBean advertBean : data) {
			String type = advertBean.getType();
			String[] arrayType =  type.split(":");
			if(arrayType!=null && arrayType.length>0){
				advertBean.setType(AdvertType.TYPES.get(arrayType[0]));
			}
		}
		return adverts;
	}
	
	
}
