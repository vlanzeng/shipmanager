package org.springside.examples.quickstart.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.contants.ErrorConstants;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.OrderParam;
import org.springside.examples.quickstart.domain.ResultList;
import org.springside.examples.quickstart.service.MorderService;
import org.springside.examples.quickstart.utils.CommonUtils;

@Controller
@RequestMapping(value="/m/order")
public class MorderController extends BaseController implements HybConstants{
	Logger logger = Logger.getLogger(MorderController.class);
	
	@Autowired
    private MorderService morderService;
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/index";
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
		OrderParam param = new OrderParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setOrderNo(request.getParameter("orderNo"));
		param.setUserName(request.getParameter("userName"));
		param.setOsName(request.getParameter("osName"));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		param.setStatus(Integer.valueOf(request.getParameter("status")));
		DataGrid<OrderBean> gb = morderService.getOrderList(param);
		return CommonUtils.printObjStr2(gb);
	}
	
	@RequestMapping(value="uStatus", method=RequestMethod.POST)
	@ResponseBody
	public String uStatus(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		String orderId = request.getParameter("orderId");
		String status = request.getParameter("status");
		String oldStatus = request.getParameter("oldStatus");
		try {
			int res = morderService.uStatus(orderId, status, oldStatus);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	@RequestMapping(value="uos", method=RequestMethod.POST)
	@ResponseBody
	public String uOs(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		String orderId = request.getParameter("orderId");
		String osId = request.getParameter("osId");
		String productName = request.getParameter("productName");
		try {
			int res = morderService.uOs(orderId, osId, productName);
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
	@RequestMapping(value="mIndex", method=RequestMethod.GET)
	public String mIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/mIndex";
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="mQuery", method=RequestMethod.GET)
	@ResponseBody
	public String mQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		OrderParam param = new OrderParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setStatus(11);
		DataGrid<OrderBean> gb = morderService.getOrderList(param);
		return CommonUtils.printObjStr2(gb);
	}
}
