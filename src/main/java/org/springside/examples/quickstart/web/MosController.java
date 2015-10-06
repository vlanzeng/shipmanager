package org.springside.examples.quickstart.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springside.examples.quickstart.contants.ErrorConstants;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OilStationBean;
import org.springside.examples.quickstart.domain.OsParam;
import org.springside.examples.quickstart.service.MosService;
import org.springside.examples.quickstart.utils.CommonUtils;

@Controller
@RequestMapping(value="/m/os")
public class MosController extends BaseController implements HybConstants{
	Logger logger  = Logger.getLogger(MosController.class);
	
	@Autowired
    private MosService mosService;
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "os/index";
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="query", method=RequestMethod.GET)
	@ResponseBody
	public String query(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		OsParam param = new OsParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setOsName(request.getParameter("osName"));
		param.setCityName(request.getParameter("cityName"));
		param.setStatus(Integer.valueOf(request.getParameter("status")));
		DataGrid<OilStationBean> gb = mosService.getOsList(param);
		return CommonUtils.printObjStr2(gb);
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="add", method=RequestMethod.POST)
	@ResponseBody
	public String rAdd(@RequestParam("file") MultipartFile file,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OsParam param = new OsParam();
		param.osName = request.getParameter("name");
		param.addr = request.getParameter("addr");
		param.phone = request.getParameter("phone");
		param.latitude = request.getParameter("latitude");
		param.longitude = request.getParameter("langitude");
		param.cityId = request.getParameter("cityId");
		try {
			int res = mosService.updatePicUrl(file, param, request);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
}
