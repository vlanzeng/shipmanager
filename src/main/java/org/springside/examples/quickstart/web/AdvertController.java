package org.springside.examples.quickstart.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.examples.quickstart.contants.ErrorConstants;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.AdvertBean;
import org.springside.examples.quickstart.domain.AdvertType;
import org.springside.examples.quickstart.domain.BaseParam;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.service.MAdvertService;
import org.springside.examples.quickstart.service.MosService;
import org.springside.examples.quickstart.utils.CommonUtils;

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
		if(request.getParameter("type") != null)
			param.setType(Integer.valueOf(request.getParameter("type")));
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
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	//@ResponseBody
	public String addAdvert( 
			@RequestParam(required=false) MultipartFile file,
			@RequestParam(required=false) String url,
			@RequestParam(required=false) String type,
			@RequestParam(required=false) String title,
			@RequestParam(required=false) String os,
			HttpServletRequest request, HttpServletResponse response
			){
		int r = 0; 
		try{
		r = mAdvertService.insertAdvert(file, url, type, title, os, request);
		if(r >= 0){
			return "redirect:/advert/index";
		}
		}catch(Exception e){
			return "<html><body>失败</br><a href='javascript:history.go(-1);'>返回</a></body></html>";
		}
		
		return "<html><body>失败</br><a href='javascript:history.go(-1);'>返回</a></body></html>";
	}
	
	@RequestMapping(value="delete", method=RequestMethod.POST)
	@ResponseBody
	public String delete( 
			@RequestParam(required=true) Long id,
			HttpServletRequest request, HttpServletResponse response){
		int r = 0;
		
		r = mAdvertService.delete(id);
		
		if(r == 0){
			return CommonUtils.printStr(ErrorConstants.SUCCESS);
		}else
			return CommonUtils.printStr(ErrorConstants.ADVERT_ERROR);
	}
}
