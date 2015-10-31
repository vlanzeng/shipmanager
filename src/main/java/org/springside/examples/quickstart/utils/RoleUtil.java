package org.springside.examples.quickstart.utils;

public class RoleUtil {
	
	public static boolean isRole( String roles, String role  ){
		if(roles == null || role == null){
			return false;
		}
		String[] roleArr = roles.split(",");
		for(int i=0; i<roleArr.length; i++){
			if(roleArr[i].equalsIgnoreCase(role)){
				return true;
			}
		}
		
		return false;
	}

}
