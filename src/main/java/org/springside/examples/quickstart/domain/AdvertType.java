package org.springside.examples.quickstart.domain;

import java.util.HashMap;
import java.util.Map;

public class AdvertType {
	
	
	public static Map<String, String>  TYPES = new HashMap<String, String>();
	
	static{
		 TYPES.put("1", "加油站");
		 TYPES.put("2", "网站");
		 TYPES.put("3", "加油站发放优惠券");
	}

}
