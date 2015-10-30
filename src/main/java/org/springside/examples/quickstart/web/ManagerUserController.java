package org.springside.examples.quickstart.web;

import java.io.IOException;
import java.net.URLEncoder;

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
import org.springside.examples.quickstart.domain.DataGrid;
import org.springside.examples.quickstart.domain.MuserBean;
import org.springside.examples.quickstart.domain.MuserParam;
import org.springside.examples.quickstart.domain.NUserParam;
import org.springside.examples.quickstart.domain.UserBean;
import org.springside.examples.quickstart.domain.UserParam;
import org.springside.examples.quickstart.repository.MUserDao;
import org.springside.examples.quickstart.service.MuserService;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.utils.CommonUtils;
import org.springside.examples.quickstart.utils.ExportExcel;

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
	 * 普通用户管理
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="nindex", method=RequestMethod.GET)
	public String nindex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();	
		String role = muserDao.queryMuserRole(user.loginName);
		request.setAttribute("userRole", role);
		return "account/nIndex";
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
	 * 普通用户查询
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="nquery", method=RequestMethod.GET)
	@ResponseBody
	public String nquery(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();	
		Integer[] pageInfo = getPageInfo(request);
		UserParam param = new UserParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setUserName(CommonUtils.decode(request.getParameter("name")));
		param.setPhone(CommonUtils.decode(request.getParameter("phone")));
		DataGrid<UserBean> gb = muserService.getNuserList( param);
		return CommonUtils.printObjStr2(gb);
	}
	
	
	@RequestMapping(value="exportNuser", method=RequestMethod.GET)
	public String exportNuser(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();	
		Integer[] pageInfo = getPageInfo(request);
		UserParam param = new UserParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setUserName(CommonUtils.decode(request.getParameter("name")));
		param.setPhone(CommonUtils.decode(request.getParameter("phone")));
		DataGrid<UserBean> gb = muserService.getNuserAllList( param);
		

		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", new String("attachment; filename=清单.xls".getBytes(),"ISO8859-1"));
		
		ExportExcel<UserBean> excel = new ExportExcel<UserBean>();
		excel.exportExcel(new String[]{"用户ID","用户名","手机号","船舶名","船舶编号","帐户余额","支出","创建时间"}, gb.getRows(), response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		
		return null;
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
		return CommonUtils.printStr(ErrorConstants.MUSER_ALREADY_EXISTS_ERROR);
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="nadd", method=RequestMethod.POST)
	@ResponseBody
	public String nadd(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		Integer[] pageInfo = getPageInfo(request);
		NUserParam param = new NUserParam();
		param.setPage(pageInfo[0]);
		param.setRows(pageInfo[1]);
		param.setUserName(request.getParameter("userName"));
		param.setPwd(request.getParameter("pwd"));
		param.setPhone(request.getParameter("phone"));
		
		if(!StringUtils.isEmpty(request.getParameter("c_no"))){
			param.setC_no(request.getParameter("c_no").trim());
		}
		if(!StringUtils.isEmpty(request.getParameter("c_name"))){
			param.setC_name(request.getParameter("c_name").trim());
		}
		try {
			int res = muserService.nadd(param);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.MUSER_PHONEALREADY_EXISTS_ERROR);
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
	
	@RequestMapping(value="delete", method=RequestMethod.POST)
	@ResponseBody
	public String delete(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		String userId = request.getParameter("id");
		try {
			int res = muserService.delete(userId);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
	
	@RequestMapping(value="updateMuser", method=RequestMethod.POST)
	@ResponseBody
	public String updateMuser(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		try {
			String username = "";
			String pwd = "";
			String id = "";
			
			if(!StringUtils.isEmpty(request.getParameter("userName"))){
				username = request.getParameter("userName").trim();
			}
			
			if(!StringUtils.isEmpty(request.getParameter("pwd"))){
				pwd = request.getParameter("pwd").trim();
			}
			
			if(StringUtils.isEmpty(request.getParameter("id"))){
				
				return CommonUtils.printStr(ErrorConstants.PARAM_ERRO);
			}
			id = request.getParameter("id");
			int res = muserService.updateMUserInfo(username, pwd, id);
			if(res > 0){
				return CommonUtils.printObjStr(res);
			}
		} catch (Exception e) {
			logger.error("uStatus error.", e);
		}
		return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
	}
}
