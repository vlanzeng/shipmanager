package org.springside.examples.quickstart.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
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
import org.springside.examples.quickstart.domain.OsOilBean;
import org.springside.examples.quickstart.domain.OsOilParam;
import org.springside.examples.quickstart.domain.OsParam;
import org.springside.examples.quickstart.entity.User;
import org.springside.examples.quickstart.service.MosService;
import org.springside.examples.quickstart.service.MuserService;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.utils.CommonUtils;

@Controller
@RequestMapping(value="/m/os")
public class MosController extends BaseController implements HybConstants{
	Logger logger  = Logger.getLogger(MosController.class);
	
	@Autowired
    private MosService mosService;
	@Autowired
	private MuserService muserService;
	
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
		param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		param.setCityName(CommonUtils.decode(request.getParameter("cityName")));
		param.setStatus(Integer.valueOf(request.getParameter("status")));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
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
	public String rAdd(@RequestParam(required=false) MultipartFile file,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OsParam param = new OsParam();
//		if(request.getParameter("add_os_name")!=null){
//			param.setOsName(CommonUtils.decode(request.getParameter("add_os_name")));
//		}
//		if(request.getParameter("add_os_addr")!=null){
//			param.setAddr(CommonUtils.decode(request.getParameter("add_os_addr")));
//		}
		param.osName = request.getParameter("add_os_name");
		param.addr = request.getParameter("add_os_addr");
		param.phone = request.getParameter("add_os_phone");
		param.latitude = request.getParameter("add_os_latitude");
		param.longitude = request.getParameter("add_os_longitude");
		param.cityId = request.getParameter("add_os_city");
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
	
	@RequestMapping(value="addnima", method=RequestMethod.POST)
	@ResponseBody
	public String rAddnima(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OsParam param = new OsParam();
		if(request.getParameter("add_os_name")!=null){
			param.setOsName(CommonUtils.decode(request.getParameter("add_os_name")));
		}
		if(request.getParameter("add_os_addr")!=null){
			param.setAddr(CommonUtils.decode(request.getParameter("add_os_addr")));
		}
		//param.osName = request.getParameter("add_os_name");
		//param.addr = request.getParameter("add_os_addr");
		param.phone = request.getParameter("add_os_phone");
		param.latitude = request.getParameter("add_os_latitude");
		param.longitude = request.getParameter("add_os_longitude");
		param.cityId = request.getParameter("add_os_city");
		try {
			//int res = mosService.updatePicUrl(file, param, request);
			int res=0;
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	@RequestMapping(value="updateStatus", method=RequestMethod.POST)
	@ResponseBody
	public String osUpdateStatus(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		//OsParam param = new OsParam();
//		param.osName = request.getParameter("name");
//		param.addr = request.getParameter("addr");
//		param.phone = request.getParameter("phone");
//		param.latitude = request.getParameter("latitude");
//		param.longitude = request.getParameter("langitude");
//		param.cityId = request.getParameter("cityId");
		//param.setId(request.getParameter("osId")==null?null:Integer.valueOf(request.getParameter("osId")));
		//param.setStatus(request.getParameter("status")==null?null:Integer.valueOf(request.getParameter("status")));
		String osId = request.getParameter("osId");
		String status = request.getParameter("status");
		try {
			//int res = mosService.updatePicUrl(file, param, request);
			int res = mosService.updateStaus(osId, status);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	@RequestMapping(value="deleteOS", method=RequestMethod.POST)
	@ResponseBody
	public String osDelete(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		//OsParam param = new OsParam();
//		param.osName = request.getParameter("name");
//		param.addr = request.getParameter("addr");
//		param.phone = request.getParameter("phone");
//		param.latitude = request.getParameter("latitude");
//		param.longitude = request.getParameter("langitude");
//		param.cityId = request.getParameter("cityId");
		//param.setId(request.getParameter("osId")==null?null:Integer.valueOf(request.getParameter("osId")));
		//param.setStatus(request.getParameter("status")==null?null:Integer.valueOf(request.getParameter("status")));
		String osId = request.getParameter("osId");
		//String status = request.getParameter("status");
		try {
			//int res = mosService.updatePicUrl(file, param, request);
			int res = mosService.deleteOS(osId);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="jyzOIndex", method=RequestMethod.GET)
	public String jyzOIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "os/jyzOIndex";
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="jyzOQuery", method=RequestMethod.GET)
	@ResponseBody
	public String jyzOQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		OsOilParam param = new OsOilParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setStatus(Integer.valueOf(request.getParameter("status")));
		param.setOrderNo(request.getParameter("orderNo"));
		param.setOsName(request.getParameter("osName"));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		DataGrid<OsOilBean> gb = mosService.getOsBuyOilList(param);
		return CommonUtils.printObjStr2(gb);
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="addOsOil", method=RequestMethod.POST)
	@ResponseBody
	public String addOsOilOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		OsOilParam param = new OsOilParam();
		param.oilId = Long.valueOf(request.getParameter("oilId"));
		param.num = Integer.valueOf(request.getParameter("num"));
		param.status = Integer.valueOf(request.getParameter("status"));
		param.price = request.getParameter("price");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();	
		param.userName = user.loginName;
		param.orderNo = CommonUtils.getMerchantOrderNo("12");
		param.amount = param.num*param.num+"";
		try {
			int res = mosService.addOsOilOrder(param);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	
	
	@RequestMapping(value="upOsOilstatus", method=RequestMethod.POST)
	@ResponseBody
	public String upOsOilstatus(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		String oldStatus = request.getParameter("oldStatus");
		try {
			int res = mosService.uStatus(id, status, oldStatus);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="adminOIndex", method=RequestMethod.GET)
	public String adminOIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "os/adminOIndex";
	}
}
