package org.springside.examples.quickstart.contants;

public class ErrorConstants {
	
	/**成功*/
	public static final String SUCCESS = "200";
	
	public static final String PARAM_ERRO = "10010001";
	
	/**优惠券领取异常*/
	public static final String COUPON_GET_ERROR = "10020001";
	/**优惠券已经领取*/
	public static final String COUPON_GET_EXIST_DAY_ERROR = "10020002";
	/**优惠券已过期*/
	public static final String COUPON_GET_OVERTIME_ERROR = "10020003";
	
	public static final String BANK_GET_INFO_ERROR = "10030001";
	public static final String PRECHECK_FOR_SIGN_ERROR = "10030002";
	
	/**充值失败**/
	public static final String RECHARGE_PHONE_NOT_EXISTS_ERROR = "10040001";
	public static final String RECHARGE_GENERAL_ERROR = "10040002";
	/**添加用户类错误**/
	public static final String MUSER_ALREADY_EXISTS_ERROR = "10050001";
	public static final String MUSER_PHONEALREADY_EXISTS_ERROR = "10050002";
	
	public static String getErrorMsg(String code){
		if(SUCCESS.equalsIgnoreCase(code)){
			return "请求完成";
		}else if(COUPON_GET_EXIST_DAY_ERROR.equalsIgnoreCase(code)){
			return "今天已经领取过该优惠券";
		}else if(COUPON_GET_OVERTIME_ERROR.equalsIgnoreCase(code)){
			return "优惠券已过期";
		}else if(COUPON_GET_ERROR.equalsIgnoreCase(code)){
			return "优惠券领取异常";
		}else if(BANK_GET_INFO_ERROR.equalsIgnoreCase(code)){
			return "获取卡信息失败";
		}else if(RECHARGE_PHONE_NOT_EXISTS_ERROR.equalsIgnoreCase(code)){
			return "充值失败:电话号码无对应用户";
		}else if(RECHARGE_GENERAL_ERROR.equalsIgnoreCase(code)){
			return "充值失败";
		}else if(MUSER_ALREADY_EXISTS_ERROR.equalsIgnoreCase(code)){
			return "用户已存在";
		}else if(MUSER_PHONEALREADY_EXISTS_ERROR.equalsIgnoreCase(code)){
			return "该手机已注册";
		}
		return "";
	}
	
}
