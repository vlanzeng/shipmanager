package org.springside.examples.quickstart.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.contants.ErrorConstants;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.MuserBean;
import org.springside.examples.quickstart.domain.MuserParam;
import org.springside.examples.quickstart.repository.MUserDao;
import org.springside.examples.quickstart.service.MuserService;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.utils.CommonUtils;

@Controller
@RequestMapping(value="/m/muser")
public class ManagerUserController extends BaseController implements HybConstants{
	Logger logger = Logger.getLogger(Logger.class);
	
	//admin-系统管理员      cwgly-财务管理员     kfqx客服权限  jygly进油管理员
	//jyzAdmin-系统管理员     jyzcwqx-加油站财务权限     jyzjygqx加油工权限
	
	@Autowired
    private MuserService muserService;
	
	@Autowired
	private MUserDao muserDao;
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();	
		String role = muserDao.queryMuserRole(user.loginName);
		request.setAttribute("userRole", role);
		return "account/mIndex";
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
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();	
		Integer[] pageInfo = getPageInfo(request);
		MuserParam param = new MuserParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setUserName(CommonUtils.decode(request.getParameter("name")));
		param.setOsName(CommonUtils.decode(request.getParameter("osName")));
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		DataGrid<MuserBean> gb = muserService.getMuserList(user.loginName, param);
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
		MuserParam param = new MuserParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setUserName(request.getParameter("userName"));
		param.setPwd(request.getParameter("pwd"));
		param.setRole(request.getParameter("role"));
		param.setOsId(request.getParameter("osId"));
		try {
			int res = muserService.add(param);
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
		String userId = request.getParameter("id");
		String status = request.getParameter("status");
		try {
			int res = muserService.uStatus(userId, status);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
}
