package org.springside.examples.quickstart.web;

import java.io.IOException;
import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.contants.ErrorConstants;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.BaseParam;
import org.springside.examples.quickstart.domain.ChargeLogBean;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.domain.OrderParam;
import org.springside.examples.quickstart.entity.AjaxResult;
import org.springside.examples.quickstart.service.MorderService;
import org.springside.examples.quickstart.service.account.AccountService;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.utils.CommonUtils;

import antlr.collections.List;

@Controller
@RequestMapping(value="/m/order")
public class MorderController extends BaseController implements HybConstants{
	Logger logger = Logger.getLogger(MorderController.class);
	
	@Autowired
    private MorderService morderService;
	
	@Autowired
	private AccountService accountService;
	
	
	
	/**
	 *  订单首页
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/index";
	}
	
	/**
	 *  收入首页
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/income/index", method=RequestMethod.GET)
	public String indexIncome(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/income/index";
	}
	
	/**
	 *  采购支出首页
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/purchase/index", method=RequestMethod.GET)
	public String purchaseIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/purchase/index";
	}
	
	
	
	/**
	 *  预约首页
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/subscribe/index", method=RequestMethod.GET)
	public String indexSubscribe(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/subscribe/index";
	}
	
	
	
	/**
	 *  预约首页
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/subscribe/logout", method=RequestMethod.GET)
	public String subscribeLogout(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "order/subscribe/logout";
	}
	
	
	/**
	 *  查询收入接口
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/query/income", method=RequestMethod.GET)
	@ResponseBody
	public String queryIncomeOrder(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		if(!hasJyzAdmin()){
			ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			 Long osId =  accountService.getUser( u.getId()).getOsId();
			 if(null!=osId){
				 param.setOsId(osId+"");
			 }else{
				 System.out.println("当前用户未关联出加油站！");
			 }
		}else{
			param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		}
		Integer[] pageInfo = getPageInfo(request);
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setOrderNo(request.getParameter("orderNo"));
		param.setUserName(CommonUtils.decode(request.getParameter("userName")));
		if(!StringUtils.isEmpty(request.getParameter("ojStatus"))){
			param.setOjStatus(Integer.valueOf(request.getParameter("ojStatus"))); // 结算状态
		}
		
		if(!StringUtils.isEmpty(request.getParameter("priceRegion"))){
			param.setRegionId(request.getParameter("priceRegion"));
			
		}
		
	/*	if(!StringUtils.isEmpty(request.getParameter("owStatus"))){ // 完成状态
			param.setOwStatus(Integer.valueOf(request.getParameter("owStatus")));
		}*/
		
/*		if(!StringUtils.isEmpty(request.getParameter("type"))){
			param.setType(Integer.valueOf(request.getParameter("type")));
		}*/
	//	param.setArea(CommonUtils.decode2(request.getParameter("area")));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		DataGrid<OrderBean> gb = morderService.getOrderListIncome(param);
		return CommonUtils.printObjStr2(gb);
	}

	
	
	/**
	 *  查询收入接口
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/query", method=RequestMethod.GET)
	@ResponseBody
	public String queryIndex( HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		if(!hasJyzAdmin()){
			ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			 Long osId =  accountService.getUser( u.getId()).getOsId();
			 if(null!=osId){
				 param.setOsId(osId+"");
			 }else{
				 System.out.println("当前用户未关联出加油站！");
			 }
		}else{
			param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		}
		Integer[] pageInfo = getPageInfo(request);
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setOrderNo(request.getParameter("orderNo"));
		param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		param.setUserName(CommonUtils.decode(request.getParameter("userName")));
		if(!StringUtils.isEmpty(request.getParameter("ojStatus"))){
			param.setOjStatus(Integer.valueOf(request.getParameter("ojStatus"))); // 结算状态
		}
		
		if(!StringUtils.isEmpty(request.getParameter("priceRegion"))){
			param.setRegionId(request.getParameter("priceRegion"));
			
		}
		
		if(!StringUtils.isEmpty(request.getParameter("owStatus"))){ // 完成状态
			param.setOwStatus(Integer.valueOf(request.getParameter("owStatus")));
		}
		
		if(!StringUtils.isEmpty(request.getParameter("type"))){
			param.setType(Integer.valueOf(request.getParameter("type")));
		}
		String otherStatus =  request.getParameter("otherStatus");
		if(!StringUtils.isEmpty(otherStatus)){
			String[] oss = otherStatus.split(",");
			param.setOtherStatus(oss);
		}
		param.setArea(CommonUtils.decode2(request.getParameter("area")));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		DataGrid<OrderBean> gb = morderService.getOrderListIncome(param);
		return CommonUtils.printObjStr2(gb);
	}
	
	private boolean hasJyzAdmin() {
		java.util.List<String> roles = new ArrayList<String>();
		roles.add("jyzAdmin");
		roles.add("jyzcwqx");
		boolean[] hasRole = SecurityUtils.getSubject().hasRoles(roles);
		for (boolean b : hasRole) {
			if(b){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 *  查询支出接口
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="query/purchase", method=RequestMethod.GET)
	@ResponseBody
	public String queryPurchase(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		OrderParam param = new OrderParam();
		if(!hasJyzAdmin()){
			ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			 Long osId =  accountService.getUser( u.getId()).getOsId();
			 if(null!=osId){
				 param.setOsId(osId+"");
			 }else{
				 System.out.println("当前用户未关联出加油站！");
			 }
		}else{
			param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		}
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setOrderNo(request.getParameter("orderNo"));
		param.setUserName(CommonUtils.decode(request.getParameter("userName")));
	//	param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		if(!StringUtils.isEmpty(request.getParameter("ojStatus"))){
			param.setOjStatus(Integer.valueOf(request.getParameter("ojStatus"))); // 结算状态
		}
		
		if(!StringUtils.isEmpty(request.getParameter("priceRegion"))){
			param.setRegionId(request.getParameter("priceRegion"));
			
		}
		
		if(!StringUtils.isEmpty(request.getParameter("owStatus"))){ // 完成状态
			param.setOwStatus(Integer.valueOf(request.getParameter("owStatus")));
		}
		
/*		if(!StringUtils.isEmpty(request.getParameter("type"))){
			param.setType(Integer.valueOf(request.getParameter("type")));
		}*/
	//	param.setArea(CommonUtils.decode2(request.getParameter("area")));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		DataGrid<OrderBean> gb = morderService.getOrderListPurchase(param);
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
	 *  删除订单
	 * @param id 订单ID
	 * @return
	 */
	@RequestMapping(value="delOrder", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delOrder(Long id){
		AjaxResult result = new AjaxResult();
		try {
			this.morderService.delOrder(id);
			 result.setStatus(200);
			 return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(500);
			result.setResult("应用异常");
		}
		return result;
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
		BaseParam param = new BaseParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setPhone(request.getParameter("phone"));
		DataGrid<ChargeLogBean> gb = morderService.getRechargeList(param);
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
}
