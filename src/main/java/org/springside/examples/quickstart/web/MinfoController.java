package org.springside.examples.quickstart.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.CouponParam;
import org.springside.examples.quickstart.utils.CommonUtils;
@Controller
@RequestMapping(value="/m/info")
public class MinfoController extends BaseController implements HybConstants{
	/**
	 * 大宗商品页
	 */
	@RequestMapping(value="goods", method=RequestMethod.GET)
	public String goods(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "information/goods";
	}
	
	/**
	 * 船舶服务页
	 */
	@RequestMapping(value="ship", method=RequestMethod.GET)
	public String ship(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "information/ship";
	}
	
	/**
	 * 配货信息页
	 */
	@RequestMapping(value="some", method=RequestMethod.GET)
	public String some(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "information/some";
	}
	
	/**
	 * 海事服务页
	 */
	@RequestMapping(value="maritime", method=RequestMethod.GET)
	public String maritime(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "information/maritime";
	}
	
	/**
	 * 资讯列表
	 */
	@RequestMapping(value="list", method=RequestMethod.GET)
	@ResponseBody
	public String list(HttpServletRequest request,
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
		//DataGrid<CouponBean> gb = mcouponService.getCouponList(param);
		return CommonUtils.printObjStr2(null);
	}
	/**
	 * 条件查询资讯
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
		//DataGrid<CouponBean> gb = mcouponService.getCouponList(param);
		return CommonUtils.printObjStr2(null);
	}
	/**
	 * 新增资讯
	 */
	@RequestMapping(value="add", method=RequestMethod.GET)
	@ResponseBody
	public String add(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return CommonUtils.printObjStr2(null);
	}
	/**
	 * 修改资讯
	 */
	@RequestMapping(value="update", method=RequestMethod.GET)
	@ResponseBody
	public String update(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return CommonUtils.printObjStr2(null);
	}
	/**
	 * 删除资讯
	 */
	@RequestMapping(value="delete", method=RequestMethod.GET)
	@ResponseBody
	public String delete(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return CommonUtils.printObjStr2(null);
	}
}
