package org.springside.examples.quickstart.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

public class HttpUtil {
    public static String httpGet(String url) {  
        try {  
            HttpGet httpGet = new HttpGet(url);  
            HttpClient client = new DefaultHttpClient();  
            HttpResponse resp = client.execute(httpGet);  
            HttpEntity entity = resp.getEntity();  
            String respContent = EntityUtils.toString(entity , "GBK").trim();  
            httpGet.abort();  
            return respContent;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
    
    public static String httpPost(String url, Map<String, String> params) {
    	try {  
            HttpPost httpPost = new HttpPost(url);  
            HttpClient client = new DefaultHttpClient();  
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(params.size());  
            for(Map.Entry<String, String> entry : params.entrySet()){  
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));  
                valuePairs.add(nameValuePair);  
            }  
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(valuePairs, "UTF8");  
            httpPost.setEntity(formEntity);  
            HttpResponse resp = client.execute(httpPost);  
              
            HttpEntity entity = resp.getEntity();  
            String respContent = EntityUtils.toString(entity , "GBK").trim();  
            httpPost.abort();  
            return respContent;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }
    
    public static void main(String[] args) {
    	 JPushClient jpushClient = new JPushClient("94b0506b0bcfcd06d33b6198", "748df3f198144ba82d948e01", 3);
         // For push, all you need do is to build PushPayload object.
         PushPayload payload = buildPushObject_all_all_alert();
         try {
             PushResult result = jpushClient.sendPush(payload);
            System.out.println("Got result - " + result);

         } catch (APIConnectionException e) {
             // Connection error, should retry later
        	 System.out.println("Connection error, should retry later"+e);
         } catch (APIRequestException e) {
             // Should review the error, and fix the request
        	 System.out.println("Should review the error, and fix the request"+e);
        	 System.out.println("HTTP Status: " + e.getStatus());
        	 System.out.println("Error Code: " + e.getErrorCode());
        	 System.out.println("Error Message: " + e.getErrorMessage());
         }
	}
    
    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll("sssssssssssssss");
    }
}
