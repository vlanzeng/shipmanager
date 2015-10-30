package org.springside.examples.quickstart.utils;

import org.apache.log4j.Logger;
import org.springside.examples.quickstart.contants.HybConstants;
import org.springside.examples.quickstart.domain.PushOsBean;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.alibaba.fastjson.JSONObject;

public class JPushUtil {
	static Logger logger  = Logger.getLogger(JPushUtil.class);
	
	public static void main(String[] args) {
	     PushOsBean bean = new PushOsBean();
	   	 bean.setOsId(1L);
	   	 bean.setOrderId(143L);
	   	 bean.setOrderNo("H2015101032144443706688805227");
	   	 bean.setAddr("上海哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈");
	   	 bean.setBookTime("2015-10-10 00:55:04");
	   	 bean.setNum(12);
	   	 bean.setPrice("12.4");
	     bean.setProductId(1L);
	   	 bean.setProductName("机油");
	   	 System.out.println(pushOrderOs("18215658695", bean));
	}
	
	public static boolean pushOrderOs(String phone, PushOsBean bean){
		try {
			JPushClient jpushClient = new JPushClient(HybConstants.JPUSH_MASTERSECRET, HybConstants.JPUSH_APPKEY, 3);
			PushPayload payload = buildPushObject_all_alias_alert("HYB_"+phone, HybConstants.JPUSH_ALERT, bean);
	        PushResult result = jpushClient.sendPush(payload);
	      /*  JSONObject obj = new JSONObject();
	        System.out.println(obj.get("dddddddddd"));*/
	        System.out.println(result.toString());
	        return result.isResultOK();
	     } catch (Exception e) {
	    	 System.out.println("Should review the error, and fix the request"+e);
	    	 logger.error("Should review the error, and fix the request",e);
	     }
		return false;
	}
    
    private static PushPayload buildPushObject_all_alias_alert(String alias,String alert,PushOsBean bean) {
        return PushPayload.newBuilder()
            .setPlatform(Platform.android_ios())
            .setAudience(Audience.alias(alias))
            .setNotification(Notification.newBuilder()
                    .addPlatformNotification(AndroidNotification.newBuilder()
                            .setAlert(alert)
                            .addExtra("action", "cmo_order")
                            .addExtra("osId", bean.osId)
                            .addExtra("orderId", bean.orderId)
	                		.addExtra("orderNo", bean.orderNo)
	                		.addExtra("productId", bean.getProductId())
	                		.addExtra("productName", bean.getProductName())
	                		.addExtra("price", bean.getPrice())
	                		.addExtra("num", bean.getNum())
	                		.addExtra("bookTime", bean.getBookTime())
	                		.addExtra("addr", bean.addr)
                            .build())
                    .addPlatformNotification(IosNotification.newBuilder()
                            .setAlert(alert)
                            .addExtra("action", "cmo_order")
                            .addExtra("osId", bean.osId)
                            .addExtra("orderId", bean.orderId)
	                		.addExtra("orderNo", bean.orderNo)
	                		.addExtra("productId", bean.getProductId())
	                		.addExtra("productName", bean.getProductName())
	                		.addExtra("price", bean.getPrice())
	                		.addExtra("num", bean.getNum())
	                		.addExtra("bookTime", bean.getBookTime())
	                		.addExtra("addr", bean.addr)
                            .build())
                    .build())
            .setOptions(Options.newBuilder().setApnsProduction(true).build())
            .build();
    }
}
