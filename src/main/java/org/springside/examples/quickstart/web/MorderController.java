package org.springside.examples.quickstart.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.contants.ErrorConstants;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.BaseParam;
import org.springside.examples.quickstart.domain.ChargeLogBean;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.OrderParam;
import org.springside.examples.quickstart.domain.RechargeParam;
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
		param.setUserName(CommonUtils.decode(request.getParameter("userName")));
		param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		if(!StringUtils.isEmpty(request.getParameter("ojStatus"))){
			param.setOjStatus(Integer.valueOf(request.getParameter("ojStatus")));
		}
		
		if(!StringUtils.isEmpty(request.getParameter("owStatus"))){
			param.setOwStatus(Integer.valueOf(request.getParameter("owStatus")));
		}
		
		if(!StringUtils.isEmpty(request.getParameter("type"))){
			param.setType(Integer.valueOf(request.getParameter("type")));
		}
		param.setArea(CommonUtils.decode2(request.getParameter("area")));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
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
	@RequestMapping(value="rIndex", method=RequestMethod.GET)
	public String rIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/rIndex";
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="rQuery", method=RequestMethod.GET)
	@ResponseBody
	public String rQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		RechargeParam param = new RechargeParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setPhone(request.getParameter("phone"));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		param.setCname(request.getParameter("cname"));
		String amountS = request.getParameter("amountStart");
		if(!StringUtils.isEmpty(amountS)){
			param.setAmountStart(Float.valueOf(amountS));
		}
		String amountE = request.getParameter("amountEnd");
		if(!StringUtils.isEmpty(amountE)){
			param.setAmountEnd(Float.valueOf(amountE));
		}
		DataGrid<ChargeLogBean> gb = morderService.getRechargeList(param);
		return CommonUtils.printObjStr2(gb);
	}
	
	@RequestMapping(value="rQuerySum", method=RequestMethod.GET)
	@ResponseBody
	public String rQuerySum(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		RechargeParam param = new RechargeParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setPhone(request.getParameter("phone"));
		
		
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		param.setCname(request.getParameter("cname"));
		String amountS = request.getParameter("amountStart");
		if(!StringUtils.isEmpty(amountS)){
			param.setAmountStart(Float.valueOf(amountS));
		}
		String amountE = request.getParameter("amountEnd");
		if(!StringUtils.isEmpty(amountE)){
			param.setAmountEnd(Float.valueOf(amountE));
		}
		DataGrid<ChargeLogBean> gb = morderService.getRechargeSumList(param);
		return CommonUtils.printObjStr2(gb);
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="radd", method=RequestMethod.POST)
	@ResponseBody
	public String rAdd(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		String amount = request.getParameter("amount");
		String phone = request.getParameter("phone");
		try {
			int res = morderService.recharge(phone, amount);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}else if(res==-1){
				return CommonUtils.printStr(ErrorConstants.RECHARGE_PHONE_NOT_EXISTS_ERROR);
			}else{
				return  CommonUtils.printStr(ErrorConstants.RECHARGE_GENERAL_ERROR);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			//logger.error("uStatus error.", e);
		}
		return  CommonUtils.printStr(ErrorConstants.RECHARGE_GENERAL_ERROR);
	}
}
