package com.commons.util;

import java.util.Map;

public class Constants {
	
	//验证码名称
    public static String VALIDATE_CODE = "monitor_validate";
    
    //验证码过期时间
    public static int VALIDATE_CODE_TIME = 60;

    //session、cookie名称
    public static String SMS_ADMIN_SESSION = "baiwu_sms_monitor";
    
    //cookie名称
    public static String SMS_ADMIN_COOKIE = "smsMonitorId";

    //防止表单重复提交令牌名称
    public static String AVOID_TOKEN = "avoid_token";

    //通道信息
    public static Map<String, String> td_info_list = null;

    //数据库测试连接的时间
    public static int DB_TIME_OUT = 5 * 1000;

    //告警信息未配置模板时 的模板
    public static final String NOTICE_TEMPLATE = "【${level}告警】${ip}（${server_id}）${content}，${time}";
    
    //用户登录有效时间，7天
    public static final int USER_OUT_TIME_7DAYS = 604800;
    
    //登录有效时间，短时间，2小时
    public static final int USER_OUT_TIME_2HOURS = 7200;
    
    //sessionRedis
    public static String SESSION_REDIS_NAME = "sms_moniotr_session:";
    
    
}
