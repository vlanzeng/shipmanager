package org.springside.examples.quickstart.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.contants.ErrorConstants;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.CouponBean;
import org.springside.examples.quickstart.domain.CouponParam;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.OrderBean;
import org.springside.examples.quickstart.service.McouponService;
import org.springside.examples.quickstart.utils.CommonUtils;

@Controller
@RequestMapping(value="/m/coupon")
public class McouponController extends BaseController implements HybConstants{
	Logger logger = Logger.getLogger(Logger.class);
	
	@Autowired
    private McouponService mcouponService;
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "coupon/index";
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="uIndex", method=RequestMethod.GET)
	public String useIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "coupon/useIndex";
	}
	
	/**
	 * 优惠券查询
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
		CouponParam param = new CouponParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setName(CommonUtils.decode(request.getParameter("name")));
		param.setFaceLimit(request.getParameter("faceLimit"));
		if(!StringUtils.isEmpty(request.getParameter("type"))){
			param.setType(Integer.valueOf(request.getParameter("type")));
		}
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		DataGrid<CouponBean> gb = mcouponService.getCouponList(param);
		return CommonUtils.printObjStr2(gb);
	}
	
	
	
	/**
	 *  优惠券使用查询
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="/use/query", method=RequestMethod.GET)
	@ResponseBody
	public String queryUse(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		CouponParam param = new CouponParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setName(CommonUtils.decode(request.getParameter("name")));
		param.setFaceLimit(request.getParameter("faceLimit"));
		if(!StringUtils.isEmpty(request.getParameter("type"))){
			param.setType(Integer.valueOf(request.getParameter("type")));
		}
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		DataGrid<CouponBean> gb = mcouponService.getMUseList(param);
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
	public String add(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		CouponParam param = new CouponParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setName(request.getParameter("name"));
		param.setFaceLimit(request.getParameter("faceLimit"));
		param.setType(Integer.valueOf(request.getParameter("type")));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		try {
			int res = mcouponService.add(param);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	@RequestMapping(value="status", method=RequestMethod.POST)
	@ResponseBody
	public String status(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		String couponId = request.getParameter("id");
		String status = request.getParameter("status");
		try {
			int res = mcouponService.uStatus(couponId, status);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
}
