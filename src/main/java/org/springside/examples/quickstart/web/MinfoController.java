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
import org.springside.examples.quickstart.domain.CouponBean;
import org.springside.examples.quickstart.domain.CouponParam;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.utils.CommonUtils;
@Controller
@RequestMapping(value="/m/info")
public class MinfoController extends BaseController implements HybConstants{
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		return "information/index";
	}
	
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
}
