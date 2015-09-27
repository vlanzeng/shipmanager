package org.springside.examples.quickstart.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class BaseController {
	protected Integer[] getPageInfo(HttpServletRequest request){
		String pageStr = request.getParameter("page");
		String rowsStr = request.getParameter("rows");
		Integer page = 1;
		Integer rows = 15;
		if(!StringUtils.isEmpty(pageStr)){
			try {
				page = Integer.valueOf(pageStr);
			} catch (NumberFormatException e) {
			}
		}
		
		if(!StringUtils.isEmpty(rowsStr)){
			try {
				rows = Integer.valueOf(rowsStr);
			} catch (NumberFormatException e) {
			}
		}
		
		return new Integer[]{page, rows};
	}
}
