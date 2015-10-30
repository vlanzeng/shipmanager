package org.springside.examples.quickstart.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springside.examples.quickstart.domain.OrderCashVO;
import org.springside.examples.quickstart.domain.OrderParam;
import org.springside.examples.quickstart.domain.OrderVO;
import org.springside.examples.quickstart.domain.PushOsBean;
import org.springside.examples.quickstart.domain.UserOrderBean;
import org.springside.examples.quickstart.entity.AjaxResult;
import org.springside.examples.quickstart.entity.Muser;
import org.springside.examples.quickstart.entity.Order;
import org.springside.examples.quickstart.entity.OrderCash;
import org.springside.examples.quickstart.entity.OrderCashDetail;
import org.springside.examples.quickstart.entity.OrderStatus;
import org.springside.examples.quickstart.service.McouponService;
import org.springside.examples.quickstart.service.MorderService;
import org.springside.examples.quickstart.service.MosService;
import org.springside.examples.quickstart.service.MuserService;
import org.springside.examples.quickstart.service.OrderCashService;
import org.springside.examples.quickstart.service.account.AccountService;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.utils.CommonUtils;
import org.springside.examples.quickstart.utils.ExcelUtil;
import org.springside.examples.quickstart.utils.JPushUtil;

@Controller
@RequestMapping(value="/m/order")
public class MorderController extends BaseController implements HybConstants{
	Logger logger = Logger.getLogger(MorderController.class);
	
	@Autowired
    private MorderService morderService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MosService mosService;
	
	@Autowired
	private McouponService mcouponService;
	
	@Autowired
	private MuserService muserService;
	
	
	@Autowired
	private OrderCashService ordreCashService;
	
	
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
		request.setAttribute("oss", mosService.findAllOsation());
		request.setAttribute("conpons", mcouponService.findAllValidCoupons());
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
		request.setAttribute("oss", mosService.findAllOsation());
		return "order/income/index";
	}
	
	
	
	/**
	 *  申请提现首页
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/cash/index", method=RequestMethod.GET)
	public String indexCash(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		request.setAttribute("oss", mosService.findAllOsation());
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		 Long osId =  accountService.getUser( u.getId()).getOsId();
		request.setAttribute("passOsid", osId);
		return "order/cash/index";
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
		request.setAttribute("oss", mosService.findAllOsation());
		return "order/purchase/index";
	}
	
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/subscribe/index", method=RequestMethod.GET)
	public String indexSubscribe(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		request.setAttribute("oss", mosService.findAllOsation());
		return "order/subscribe/index";
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/check/index", method=RequestMethod.GET)
	public String indexCheck(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		request.setAttribute("oss", mosService.findAllOsation());
		return "order/check/index";
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
	 *  查询收入接口 （财务明细）
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
	 *  导出收入接口 （财务明细）
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/excel/income", method=RequestMethod.GET)
	public void excelIncomeOrder(HttpServletRequest request,
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
			param.setOsName(new String (request.getParameter("osName").getBytes("iso8859-1"),"utf-8"));
		}
		Integer[] pageInfo = getPageInfo(request);
		param.setPage(pageInfo[0]);
		param.setRows(1000000);
		param.setOrderNo(request.getParameter("orderNo"));
		if(null!=request.getParameter("userName")){
			param.setUserName(new String (request.getParameter("userName").getBytes("iso8859-1"),"utf-8"));
		}
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
		String[] headers = new String[]{"订单号","油品","数量","状态","价格","用户名","加油站","创建时间"};
		List<OrderVO> ovs = new ArrayList<OrderVO>();
		OrderVO ov = null;
		for (OrderBean ob : gb.getRows()) {
			ov = new OrderVO();
			ov.setCreate(ob.getCreateTime());
			ov.setNum(ob.getNum());
			ov.setOil(ob.getProductName());
			ov.setOname(ob.getOsName());
			ov.setOrderNo(ob.getOrderNo());
			ov.setPrice(ob.getAmount());
			ov.setStatus(ob.getStatus());
			ov.setUserName(ob.getUserName());
			ovs.add(ov);
			
		}
		new ExcelUtil<OrderVO>().exportExcel("加油收入", headers, ovs, response);
		
	}
	
	/**
	 *  查询订单接口 （订单管理）
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
	
	
	
	
	
	
	/**
	 *  查询订单接口 （预约销码）
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/query/check/code", method=RequestMethod.GET)
	@ResponseBody
	public String queryCheckCode( HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long osId =  accountService.getUser( u.getId()).getOsId();
		param.setOsId(osId+"");
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
	
	
	/**
	 *   （提现申请）  申请订单
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/query/cashorder", method=RequestMethod.GET)
	@ResponseBody
	public String queryCashOrder( HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long osId =  accountService.getUser( u.getId()).getOsId();
		if(null!=osId){
			param.setOsId(osId+"");
		}else{
			System.out.println("当前用户未关联出加油站！");
		}
		Integer[] pageInfo = getPageInfo(request);
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setOrderNo(request.getParameter("orderNo"));
		List<OrderCash>  data =  ordreCashService.findByOsId(osId);
		DataGrid<OrderCash> dg = new DataGrid<OrderCash>();
		dg.setRows(data);
		return CommonUtils.printObjStr2(dg);
	}
	
	
	
	@RequestMapping(value="/excel/cashorder", method=RequestMethod.GET)
	public void excelCashOrder( HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long osId =  accountService.getUser( u.getId()).getOsId();
		if(null!=osId){
			param.setOsId(osId+"");
		}else{
			System.out.println("当前用户未关联出加油站！");
		}
		Integer[] pageInfo = getPageInfo(request);
		param.setPage(pageInfo[0]);
		param.setRows(100000);
		param.setOrderNo(request.getParameter("orderNo"));
		List<OrderCash>  data =  ordreCashService.findByOsId(osId);
		List<OrderCashVO> ovs = new ArrayList<OrderCashVO>();
		OrderCashVO ov = null;
		for (OrderCash orderCash : data) {
			 Set<OrderCashDetail>  datas = orderCash.getOrderCashDetails();
			 	for (OrderCashDetail o : datas) {
					ov = new OrderCashVO();
					ov.setOrderName(orderCash.getOrderName());
					ov.setCreate(o.getHistoryOrder().getCreateTime().toLocaleString());
					ov.setNum(o.getHistoryOrder().getNum());
					ov.setOil(o.getHistoryOrder().getProductName());
					ov.setOname("oname");
					ov.setOrderNo(o.getHistoryOrder().getOrderNo());
					ov.setPrice(o.getHistoryOrder().getMoney().toString());
					ov.setStatus(OrderStatus.status.get(o.getHistoryOrder().getStatus()));
					ov.setUserName(o.getHistoryOrder().getUserName());
					ovs.add(ov);
				}
		}
		String[] headers = new String[]{"订单号","油品","数量","状态","价格","用户名","加油站","创建时间"};
		new ExcelUtil<OrderCashVO>().exportExcel("提现申请订单", headers, ovs, response);
	}
	
	
	/**
	 *   （提现申请）  申请订单
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/query/cashorderdetail", method=RequestMethod.GET)
	@ResponseBody
	public String queryCashOrderDetail( Long id) throws IOException, ServletException{
		OrderCash cashOrder =  ordreCashService.findById(id);
		List<OrderCashDetail> ocd = new ArrayList<OrderCashDetail>();
		ocd.addAll(cashOrder.getOrderCashDetails());
		Collections.sort(ocd,new sortOrderCash());
		List<Long> uids = new ArrayList<Long>();
		for (OrderCashDetail orderCashDetail : ocd) {
			uids.add(orderCashDetail.getHistoryOrder().getUserId());
		}
		Map<Long, Muser> users =  getMapUsers(uids);
		for (OrderCashDetail orderCashDetail : ocd) {
			Muser u =  users.get(orderCashDetail.getHistoryOrder().getUserId());
			if(null!=u){
				orderCashDetail.getHistoryOrder().setUserName(u.getUsername());
				orderCashDetail.getHistoryOrder().setPhone(u.getPhone());
			}
		}
		return CommonUtils.printObjStr2(ocd);
	}

	private Map<Long, Muser> getMapUsers(List<Long> uids) {
		Iterable<Muser>  ite =  muserService.selectByIds(uids);
		Iterator<Muser> it =  ite.iterator();
		Map<Long , Muser> users = new HashMap<Long, Muser>();
		while(it.hasNext()){
			Muser u =  it.next();
			users.put(u.getId(), u);
		}
		return users;
	}
	
	private class sortOrderCash implements Comparator<OrderCashDetail>{

		@Override
		public int compare(OrderCashDetail o1, OrderCashDetail o2) {
			int value = (int)(o1.getHistoryOrder().getId() - o2.getHistoryOrder().getId());
			return value;
		}

		
		
	}
	
	/**
	 *   （提现申请）
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/query/cash", method=RequestMethod.GET)
	@ResponseBody
	public String queryCash( HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long osId =  accountService.getUser( u.getId()).getOsId();
		if(null!=osId){
			param.setOsId(osId+"");
		}else{
			System.out.println("当前用户未关联出加油站！");
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
	
	
	
	
	/**
	 *  导出支出接口
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="excel/purchase", method=RequestMethod.GET)
	public void excelPurchase(HttpServletRequest request,
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
			param.setOsName(new String (request.getParameter("osName").getBytes("iso8859-1"),"utf-8"));
		}
		param.setPage(pageInfo[0]);
		param.setRows(1000000);
		param.setOrderNo(request.getParameter("orderNo"));
		if(null!=request.getParameter("userName")){
			param.setUserName(new String (request.getParameter("userName").getBytes("iso8859-1"),"utf-8"));
		}else{
			param.setUserName("");
		}
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
		String[] headers = new String[]{"订单号","油品","数量","状态","价格","用户名","加油站","创建时间"};
		List<OrderVO> ovs = new ArrayList<OrderVO>();
		OrderVO ov = null;
		for (OrderBean ob : gb.getRows()) {
			ov = new OrderVO();
			ov.setCreate(ob.getCreateTime());
			ov.setNum(ob.getNum());
			ov.setOil(ob.getProductName());
			ov.setOname(ob.getOsName());
			ov.setOrderNo(ob.getOrderNo());
			ov.setPrice(ob.getAmount());
			ov.setStatus(ob.getStatus());
			ov.setUserName(ob.getUserName());
			ovs.add(ov);
			
		}
		new ExcelUtil<OrderVO>().exportExcel("采购支出", headers, ovs, response);
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
	
	
	@RequestMapping(value="getAllSation", method=RequestMethod.GET)
	@ResponseBody
	public java.util.List<Object[]> getAllSation() {
		return mosService.findAllOsation();
	}
	
	
	@RequestMapping(value="addUserOrder", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult addUserOrder(UserOrderBean order) {
		try {
			return morderService.addUserOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
			AjaxResult ar = new AjaxResult();
			ar.setStatus(300);
			ar.setResult("推送失败");
			return ar;
		}
	}
	
	
	@RequestMapping(value="addBookOrder", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult addBookOrder(UserOrderBean order) {
		try {
			return morderService.addBookOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
			AjaxResult ar = new AjaxResult();
			ar.setStatus(300);
			ar.setResult("推送失败");
			return ar;
		}
	}
	
	
	
	/**
	 * 申请提现列表 （财务明细）
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/query/apply/cash", method=RequestMethod.GET)
	@ResponseBody
	public String applyCash(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long osId =  accountService.getUser( u.getId()).getOsId();
		param.setOsId(osId+"");
		param.setOjStatus(1);; // 获取支付状态的订单
		param.setRows(1000000);
		param.setHasCash(true);
		DataGrid<OrderBean> gb = morderService.getOrderListIncome(param);
		return CommonUtils.printObjStr2(gb.getRows());
	}
	
	
	/**
	 * 申请提现 （财务明细）
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/apply", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult apply(String orderName) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		AjaxResult ar = new AjaxResult();
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long osId =  accountService.getUser( u.getId()).getOsId();
		param.setOsId(osId+"");
		param.setOjStatus(1);; // 获取支付状态的订单
		param.setRows(1000000);
		param.setHasCash(true);
		DataGrid<OrderBean> gb = morderService.getOrderListIncome(param);
		List<OrderBean> obs =  gb.getRows();
		OrderCash oc = new OrderCash();
		oc.setOperationId(u.getId());
		oc.setCreateTime(new java.util.Date());
		oc.setOsid(osId);
		oc.setOrderName(orderName);
		Set<OrderCashDetail> datas = new HashSet<OrderCashDetail>();
		oc.setOrderCashDetails(datas);
		if(null!=obs && !obs.isEmpty()){
				OrderCashDetail o = null;
			for (OrderBean orderBean : obs) {
				o = new OrderCashDetail();
				o.setHistoryOrder(morderService.findOne(orderBean.getId()));
				datas.add(o);
			}
		}
		ordreCashService.save(oc);
		ar.setStatus(200);
		return ar;
	}
	
}
