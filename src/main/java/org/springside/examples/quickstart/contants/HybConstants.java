package org.springside.examples.quickstart.contants;

public interface HybConstants {
	static String SESSION_USER = "current_session_user";
	static String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIN1qcu/T8RecFDvkiZENbYjIFN3sg0opRyjcGKr7RXD/+/gSB1gzxmYU+2eqMz7/aUQyIE5LVLx76cd4x7s5yaHF5IbVnVOT7DaZsN7QlNuw7SOf4j/AxKU4vfaWSbyfw+iWxQy/JP7ca04tKAl1xdVbHWqs5eBWQ9ZhD9VMT9tAgMBAAECgYBaDVo5Rl6AKClMaDB3ojYhBML6Kn5/4R3qFlPAZAHO7Sr/yWXdKhn0cJLdrdf4Q46nam3Ph7lynmUK18BjyOk2Sujj/nSKHMVDkkFLsGiCOAGIAPLWTks9lUKOR4d2o63JVra0iEYF0Fnjtgqu+zSwlIMJAARb7lQvA2by6AD7QQJBAOhM3soQpkh+0B8ZRV1UeuScsQQEe4fn3Kv2rbmYMhpCHXGNdNmWqzhIekfg2iBCzaCF2K0Ylut4wkGYUOKhOFUCQQCQ3xNNe7oCBZC7F6ypM5H809b4B2QWpf6PbhO3Rg8dTVn+KpYDvv2KGu5x4UJmxYAK/DpStef4ZnUr9bQVo2K5AkATyShYsZ5YImT7oR1lW6K0AhyZqQAUjU0gYHxfiHUMA4GagF/pgInI23N+18n8YXQ+LehMpgGEwN5a0+xfwTeZAkBbSTWQjXYX8AnS2ogh1i0XcyTyBTY8TzyohGRICA1eGW+riIoesipw66d0esyslaapRWY62ATjsSwEFz7yo5GBAkEA1bPUT2KrAzzw6wzExXtv58zbrn840B+ms+x7IfXaHg1WM4GIJVBxShHjRfIywlFZdmRCi5peu6MyrX9LgBUlwQ==";
	static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDdanLv0/EXnBQ75ImRDW2IyBTd7INKKUco3Biq+0Vw//v4EgdYM8ZmFPtnqjM+/2lEMiBOS1S8e+nHeMe7OcmhxeSG1Z1Tk+w2mbDe0JTbsO0jn+I/wMSlOL32lkm8n8PolsUMvyT+3GtOLSgJdcXVWx1qrOXgVkPWYQ/VTE/bQIDAQAB";

	static Integer MINAMOUNT  = 100;
	
	/**支付常量 开始----------------------------------------------*/
	static final String MERCHANTNO = "245888";
	static final String PRINCIPALID = "103100000000000111637220";
	static final String SUCCESS = "SUCCESS";
	
	static final String PAY_SUCCESS = "";
	
	static final boolean TEST = false;
	
	static final String OUTMEMBERID = "outMemberId";
	static final String OUTMEMBERREGISTTIME = "20110707112233";
	static final String OUTMEMBERREGISTIP = "127.0.0.1";
	static final String OUTMEMBERVERIFYSTATUS = "1";
	static final String OUTMEMBERNAME = "aa";
	static final String OUTMEMBERMOBILE = "15521366554";
	/**支付常量结束-----------------------------------------------*/
	
	
	static final String NOTIFYURL = "http://121.40.91.82/shipService/api/v1/order/notify";
	
	static final String MERCHANTMY = "abcdefg";
	
	static final String USERID = "userId";
	
	//admin-系统管理员      cwgly-财务管理员     kfqx客服权限  jygly进油管理员
	//jyzAdmin-系统管理员     jyzcwqx-加油站财务权限     jyzjygqx加油工权限
	static final String ADMIN = "admin";
	static final String CWGLY = "cwgly";
	static final String KFQX = "kfqx";
	static final String JYGLY = "jygly";
	
	static final String JYZADMIN = "jyzAdmin";
	static final String JYZCWQX = "jyzcwqx";
	static final String JYZJYGQX = "jyzjygqx";
	
	static final String FW_ADDRESS = "43.254.55.158";
	
}
