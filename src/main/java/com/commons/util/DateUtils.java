package com.commons.util;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtils {

	public final static String YYYY = "yyyy";
	public final static String MM = "MM";
	public final static String DD = "dd";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYY_MM = "yyyy-MM";
	public final static String HH_MM_SS = "HH:mm:ss";
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static String formatStr_yyyyMMddHHmmssS = "yyyy-MM-dd HH:mm:ss.S";
	public static String formatStr_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	public static String formatStr_yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
	public static String formatStr_yyyyMMddHH = "yyyy-MM-dd HH";
	public static String formatStr_yyyyMMdd = "yyyy-MM-dd";
	public static String[] formatStr = { formatStr_yyyyMMddHHmmss,formatStr_yyyyMMddHHmm, formatStr_yyyyMMddHH, formatStr_yyyyMMdd };

	/**
	 * 构造函数
	 */
	public DateUtils() {
	}

	/**
	 * 日期格式化－将<code>Date</code>类型的日期格式化为<code>String</code>型
	 * @param date 待格式化的日期
	 * @param pattern 时间样式
	 * @return 一个被格式化了的<code>String</code>日期
	 */
	public static String format(Date date, String pattern) {
		if (date == null)
			return "";
		else
			return getFormatter(pattern).format(date);
	}

	/**
	 * 默认把日期格式化成yyyy-mm-dd格式
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		if (date == null)
			return "";
		else
			return getFormatter(YYYY_MM_DD).format(date);
	}
	
	/**
	 * 默认把日期格式化成yyyy-MM-dd HH:mm:ss格式
	 * @param date
	 * @return
	 */
	public static String formatSs(Date date) {
		if (date == null)
			return "";
		else
			return getFormatter(YYYY_MM_DD_HH_MM_SS).format(date);
	}

	/**
	 * 把字符串日期默认转换为yyyy-mm-dd格式的Data对象
	 * @param strDate
	 * @return Date
	 */
	public static Date format(String strDate) {
		Date d = null;
		if (strDate == "")
			return null;
		else
			try {
				d = getFormatter(YYYY_MM_DD).parse(strDate);
			} catch (ParseException pex) {
				return null;
			}
		return d;
	}

	/**
	 * 把字符串日期转换为f指定格式的Data对象
	 * 
	 * @param strDate,f
	 * @return
	 */
	public static Date format(String strDate, String f) {
		Date d = null;
		if (strDate == "")
			return null;
		else
			try {
				d = getFormatter(f).parse(strDate);
			} catch (ParseException pex) {
				return null;
			}
		return d;
	}

	/**
	 * 日期解析－将<code>String</code>类型的日期解析为<code>Date</code>型
	 * 
	 * @param date
	 *            待格式化的日期
	 * @param pattern
	 *            日期样式
	 * @exception ParseException
	 *                如果所给的字符串不能被解析成一个日期
	 * @return 一个被格式化了的<code>Date</code>日期
	 */
	public static Date parse(String strDate, String pattern)
			throws ParseException {
		try {
			return getFormatter(pattern).parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException("Method parse in Class DateUtils err: parse strDate fail.",pe.getErrorOffset());
		}
	}

	/**
	 * 获取当前日期
	 * 
	 * @return 一个包含年月日的<code>Date</code>型日期
	 */
	public static synchronized Date getCurrDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	
	/** 
     * 获得指定日期的前一天 
     * @param specifiedDay 
     * @return 
     * @throws Exception 
     */  
    public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数  
    	 Calendar c = Calendar.getInstance();  
         Date date = null;  
         try {  
             date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
         } catch (ParseException e) {  
             e.printStackTrace();  
         }  
         c.setTime(date);  
         int day = c.get(Calendar.DATE);  
         c.set(Calendar.DATE, day - 1);  
   
         String dayAfter = new SimpleDateFormat("yyyy-MM-dd")  
                 .format(c.getTime());  
         return dayAfter;  
    }  
    
    
    /** 
     * 获得指定日期的前两个小时
     * @param specifiedDay 
     * @return 
     * @throws Exception 
     */  
    public static String getSpecifiedHourBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.HOUR);  
        c.set(Calendar.HOUR, day -4);  
  
        String dayBefore = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(c  
                .getTime());  
        return dayBefore;  
    }  
    
  
    /** 
     * 获得指定日期的后一天 
     * @param specifiedDay 
     * @return 
     */  
    public static String getSpecifiedDayAfter(String specifiedDay) {  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day + 1);  
  
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")  
                .format(c.getTime());  
        return dayAfter;  
    }  

	/**
	 * 获取当前日期
	 * 
	 * @return 一个包含年月日的<code>String</code>型日期，但不包含时分秒。yyyy-mm-dd
	 */
	public static String getCurrDateStr() {
		return format(getCurrDate(), YYYY_MM_DD);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 一个包含年月日时分秒的<code>String</code>型日期。hh:mm:ss
	 */
	public static String getCurrTimeStr() {
		return format(getCurrDate(), HH_MM_SS);
	}

	/**
	 * 获取当前完整时间,样式: yyyy－MM－dd hh:mm:ss
	 * 
	 * @return 一个包含年月日时分秒的<code>String</code>型日期。yyyy-MM-dd hh:mm:ss
	 */
	public static String getCurrDateTimeStr() {
		return format(getCurrDate(), YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 获取当前年分 样式：yyyy
	 * 
	 * @return 当前年分
	 */
	public static String getYear() {
		return format(getCurrDate(), YYYY);
	}

	/**
	 * 获取当前月分 样式：MM
	 * @return 当前月分
	 */
	public static String getMonth() {
		return format(getCurrDate(), MM);
	}

	/**
	 * 获取当前日期号 样式：dd
	 * @return 当前日期号
	 */
	public static String getDay() {
		return format(getCurrDate(), DD);
	}

	/**
	 * 按给定日期样式判断给定字符串是否为合法日期数据
	 * @param strDate
	 *            0 * 要判断的日期
	 * @param pattern
	 *            2 * 日期样式
	 * @return true 如果是，否则返回false
	 */
	public static boolean isDate(String strDate, String pattern) {
		try {
			parse(strDate, pattern);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式年份（格式：yyyy）数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @return true 如果是，否则返回false
	 */
	public static boolean isYYYY(String strDate) {
		try {
			parse(strDate, YYYY);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	public static boolean isYYYY_MM(String strDate) {
		try {
			parse(strDate, YYYY_MM);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式的年月日（格式：yyyy-MM-dd）数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @return true 如果是，否则返回false
	 */
	public static boolean isYYYY_MM_DD(String strDate) {
		try {
			parse(strDate, YYYY_MM_DD);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式年月日时分秒（格式：yyyy-MM-dd HH:mm:ss）数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @return true 如果是，否则返回false
	 */
	public static boolean isYYYY_MM_DD_HH_MM_SS(String strDate) {
		try {
			parse(strDate, YYYY_MM_DD_HH_MM_SS);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式时分秒（格式：HH:mm:ss）数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @return true 如果是，否则返回false
	 */
	public static boolean isHH_MM_SS(String strDate) {
		try {
			parse(strDate, HH_MM_SS);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 获取一个简单的日期格式化对象
	 * @return 一个简单的日期格式化对象
	 */
	private static SimpleDateFormat getFormatter(String parttern) {
		return new SimpleDateFormat(parttern);
	}

	/**
	 * 获取给定日前的后intevalDay天的日期
	 * @param refenceDate
	 *            给定日期（格式为：yyyy-MM-dd）
	 * @param intevalDays
	 *            间隔天数
	 * @return 计算后的日期
	 */
	public static String getNextDate(String refenceDate, int intevalDays) {
		try {
			return getNextDate(parse(refenceDate, YYYY_MM_DD), intevalDays);
		} catch (Exception ee) {
			return "";
		}
	}

	/**
	 * 获取给定日前的后intevalDay天的日期
	 * @param refenceDate
	 *            Date 给定日期
	 * @param intevalDays
	 *            int 间隔天数
	 * @return String 计算后的日期
	 */
	public static String getNextDate(Date refenceDate, int intevalDays) {
		try {
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			calendar.setTime(refenceDate);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)
					+ intevalDays);
			return format(calendar.getTime(), YYYY_MM_DD);
		} catch (Exception ee) {
			return "";
		}
	}

	public static long getIntevalDays(String startDate, String endDate) {
		try {
			return getIntevalDays(parse(startDate, YYYY_MM_DD), parse(endDate,
					YYYY_MM_DD));
		} catch (Exception ee) {
			return 0l;
		}
	}

	public static long getIntevalDays(Date startDate, Date endDate) {
		try {
			java.util.Calendar startCalendar = java.util.Calendar.getInstance();
			java.util.Calendar endCalendar = java.util.Calendar.getInstance();

			startCalendar.setTime(startDate);
			endCalendar.setTime(endDate);
			long diff = endCalendar.getTimeInMillis()
					- startCalendar.getTimeInMillis();

			return (diff / (000 * 0 * 0 * 2));
		} catch (Exception ee) {
			return 0l;
		}
	}

	/**
	 * 求当前日期和指定字符串日期的相差天数
	 * 
	 * @param startDate
	 * @return
	 */
	public static long getTodayIntevalDays(String startDate) {
		try {
			// 当前时间
			Date currentDate = new Date();
			// 指定日期
			SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date theDate = myFormatter.parse(startDate);
			// 两个时间之间的天数
			long days = (currentDate.getTime() - theDate.getTime())
					/ (2 * 0 * 0 * 000);

			return days;
		} catch (Exception ee) {
			return 0l;
		}
	}

	public static Date parseToDate(String dateTimeStr) {
		if (dateTimeStr == null)
			return null;
		Date d = null;
		int formatStrLength = formatStr.length;
		for (int i = 0; i < formatStrLength; i++) {
			d = parseToDate2(dateTimeStr, formatStr[i]);
			if (d != null) {
				break;
			}
		}
		return d;
	}

	private static Date parseToDate2(String dateTimeStr, String formatString) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try {
			d = sdf.parse(dateTimeStr);
		} catch (ParseException pe) {

		}
		return d;
	}

	public static String dateTimeToString(Date datetime) {

		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(datetime);
		String dateTime = calendar.get(Calendar.YEAR) + ""
				+ (calendar.get(Calendar.MONTH) + 1 > 9 ? "" : "0")
				+ (calendar.get(Calendar.MONTH) + 1) + ""
				+ (calendar.get(Calendar.DATE) > 9 ? "" : "0")
				+ calendar.get(Calendar.DATE) + ""
				+ (calendar.get(Calendar.HOUR_OF_DAY) > 9 ? "" : "0")
				+ calendar.get(Calendar.HOUR_OF_DAY) + ""
				+ (calendar.get(Calendar.MINUTE) > 9 ? "" : "0")
				+ calendar.get(Calendar.MINUTE) + ""
				+ (calendar.get(Calendar.SECOND) > 9 ? "" : "0")
				+ calendar.get(Calendar.SECOND);
		return dateTime;
	}

	/**
	 * 由年、月份，获得当前月的最后一天
	 * 
	 * @param year
	 *            month 月份 01 02 11 12
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDayOfMonth(String year, String month)
			throws ParseException {
		String LastDay = "";
		Calendar cal = Calendar.getInstance();
		Date date_;
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month
				+ "-1");
		cal.setTime(date);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		date_ = cal.getTime();
		LastDay = new SimpleDateFormat("yyyy-MM-dd").format(date_);
		return LastDay;
	}


	/** 根据传人的日期，获取改日期所在月份的所有天数
	 *  返回值为2维列表，6行7列
	 *  firstWeek 为日期的偏移量
	 *  	当firstWeek=0     日 一 二 三 四 五 六
	 *  	当firstWeek=-1  一 二 三 四 五 六 日
	 *  	当firstWeek=1     六 日一 二 三 四 五
	 *  上月下月可通过传人上月的一日或下月的一日来获取整月的数据
	 *  
	 *  add by Leo on 2014-12-30
	 *  **/
	public static List<List<Map<String,Object>>> getMonthDayByDate(Date date,int firstWeek) {
//		System.out.println("当前日期:"+date);
//		String[][] months = new String[5][7];
		List<List<Map<String,Object>>> months = new ArrayList<List<Map<String,Object>>>();
		Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);// 得到年
		int month = calendar.get(Calendar.MONTH);// 得到月，因为从0开始的，所以要加1
		int day = calendar.get(Calendar.DAY_OF_MONTH);// 得到天
//		System.out.println("年:"+year+" 月："+month+" 日："+day);
		
		//取得当月第一天.
		int firstDate = calendar.getActualMinimum(Calendar.DATE);
		calendar.set(Calendar.DATE, firstDate);	
//		System.out.println("当月第一天:"+firstDate);
		int firstDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
//		System.out.println("第一天星期:"+firstDay);
		
		//取得当月最后一天。
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
//		System.out.println("当月最后一天:"+lastDate);
		
		//calendar设定日期为当月最后一天，通过Calendar.DAY_OF_WEEK取得周几
		calendar.set(Calendar.DATE, lastDate);	
		int lastDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
//		System.out.println("星期几:"+lastDay);
		
		int j=0;
		int daynum = 1;
		int d = 0;
		int k=firstDay+firstWeek;
		List<Map<String,Object>> weekDay = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<42;i++){
			
			if(i<k){
				map = new HashMap<String,Object>();
				weekDay.add(map);
			}else{
				d++;
				if(k>6){
					k=0;
					j++;
					months.add(weekDay);
					weekDay = new ArrayList<Map<String,Object>>();
				}
				map = new HashMap<String,Object>();
				if(d>lastDate){
					weekDay.add(map);
				}else{
					String ds = (daynum++)+"";
					if(ds.length()<2)
						ds = "0"+ds;
					String m = (month+1)+"";
					if(m.length()<2)
						m = "0"+m;
					map.put("day", ds);
					map.put("ymd", year+"-"+m+"-"+ds);
					weekDay.add(map);
				}
				k++;
			}
		}
		months.add(weekDay);
		
//		System.out.println(months);
		
		return months;
	}
	
	/** 根据传入的日期，获取改日期所在月份的所有天数
	 *  返回值为2维列表，6行7列
	 *  firstWeek 为日期的偏移量
	 *  	当firstWeek=0     日 一 二 三 四 五 六
	 *  	当firstWeek=-1  一 二 三 四 五 六 日
	 *  	当firstWeek=1     六 日一 二 三 四 五
	 *  上月下月可通过传人上月的一日或下月的一日来获取整月的数据
	 *  
	 *  add by Leo on 2014-12-30
	 *  **/
	public static List<List<Map<String,Object>>> getMonthDayByDate2(Date date,int firstWeek) {
//		System.out.println("当前日期:"+date);
//		String[][] months = new String[5][7];
		List<List<Map<String,Object>>> months = new ArrayList<List<Map<String,Object>>>();
		Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);// 得到年
		int month = calendar.get(Calendar.MONTH);// 得到月，因为从0开始的，所以要加1
		int day = calendar.get(Calendar.DAY_OF_MONTH);// 得到天
		//System.out.println("年:"+year+" 月："+month+" 日："+day);
		
		//取得当月第一天.
		int firstDate = calendar.getActualMinimum(Calendar.DATE);
		calendar.set(Calendar.DATE, firstDate);	
		//System.out.println("当月第一天:"+firstDate);
		int firstDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
//		System.out.println("第一天星期:"+firstDay);
		
		//取得当月最后一天。
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		//System.out.println("当月最后一天:"+lastDate);
		
		//calendar设定日期为当月最后一天，通过Calendar.DAY_OF_WEEK取得周几
		calendar.set(Calendar.DATE, lastDate);	
		int lastDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
//		System.out.println("星期几:"+lastDay);
		
		int j=0;
		int daynum = 1;
		int d = 0;
		int k=firstDay+firstWeek;
		List<Map<String,Object>> weekDay = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<42;i++){
			
			if(i<k){
				map = new HashMap<String,Object>();
				weekDay.add(map);
			}else{
				d++;
				if(k>6){
					k=0;
					j++;
					months.add(weekDay);
					weekDay = new ArrayList<Map<String,Object>>();
				}
				map = new HashMap<String,Object>();
				if(d>lastDate){
					weekDay.add(map);
				}else{
					String ds = (daynum++)+"";
					if(ds.length()<2)
						ds = "0"+ds;
					String m = (month+1)+"";
					if(m.length()<2)
						m = "0"+m;
					
					SimpleDateFormat format = new  SimpleDateFormat("yyyy-MM-dd" );  
					 Calendar c = Calendar.getInstance();  
					 try {
						c.setTime(format.parse(year+"-"+m+"-"+ds));
					} catch (ParseException e) {
						e.printStackTrace();
					}  
					 int  dayForWeek = 0 ;  
					 if (c.get(Calendar.DAY_OF_WEEK) == 1 ){  
					  dayForWeek = 7 ;  
					 }else {  
					  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1 ;  
					 } 
					
					map.put("day", ds);
					map.put("week", dayForWeek);
					map.put("ymd", year+"-"+m+"-"+ds);
					weekDay.add(map);
				}
				k++;
			}
		}
		months.add(weekDay);
		
		//System.out.println(months);
		
		return months;
	}
	

	public static List<String> getDayListByStartDateAndEndDate(Date start,Date end,boolean[] weekflag) {

		List<String> dateList = new ArrayList<String>();
		
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        
		calendarStart.setTime(start);
		calendarEnd.setTime(end);
		while(calendarStart.before(calendarEnd)){
			int weekvalue = calendarStart.get(Calendar.DAY_OF_WEEK)-1;
			if(weekflag[weekvalue]){
				String year = calendarStart.get(Calendar.YEAR)+"";// 得到年
				String month = (calendarStart.get(Calendar.MONTH)+1)+"";// 得到月，因为从0开始的，所以要加1
				String day = calendarStart.get(Calendar.DAY_OF_MONTH)+"";// 得到天
				
				if(day.length()<2)
					day = "0"+day;
				if(month.length()<2)
					month = "0"+month;
				dateList.add(year+"-"+month+"-"+day);
			}
			calendarStart.add(Calendar.DAY_OF_MONTH, 1); //日期加一天
		}
		
//		System.out.println(dateList);
		
		return dateList;
	}
	
	/**
	 * 获取上个月的日期 add by Leo on 2014-12-31
	 * @param date
	 * @return
	 */
	public static Date getPreMonthDate(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.add(Calendar.MONTH, -1); 
		return cal.getTime(); 
	}  
	
	
	/**
	 * 获取上个月的日期 add by Leo on 2014-12-31
	 * @param date
	 * @return
	 */
	public static Date getPreDayDate(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.add(Calendar.DATE, -1); 
		return cal.getTime(); 
	}  
	
	/**
	 * 获取下个月的日期 add by Leo on 2014-12-31
	 * @param date
	 * @return
	 */
	public static Date getNextMonthDate(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.add(Calendar.MONTH, 1); 
		return cal.getTime(); 
	}  
	
	//根据日期获取当月的所有天数
	public static List<List<Map<String,Object>>> getMonthDateByDate(Date date){
		List<List<Map<String,Object>>> desclist = new ArrayList<List<Map<String,Object>>>();
		List<List<Map<String,Object>>> list = getMonthDayByDate(date,0);
		for(int i=0;i<list.size();i++){
			List<Map<String,Object>> sublist = list.get(i);
			if(sublist!=null && sublist.size()>0){
				boolean flag = false;
				for(int j=0;j<sublist.size();j++){
					Map<String,Object> map = sublist.get(j);
					if(map!=null && map.size()>0){
						flag = true;
						break;
					}
				}
				if(flag){
					desclist.add(sublist);
				}
			}
		}
		return desclist;
	}
	
	//根据日期获取当月的所有天数 
	public static List<List<Map<String,Object>>> getMonthDateByDate2(Date date){
		List<List<Map<String,Object>>> desclist = new ArrayList<List<Map<String,Object>>>();
		List<List<Map<String,Object>>> list = getMonthDayByDate2(date,0);
		for(int i=0;i<list.size();i++){
			List<Map<String,Object>> sublist = list.get(i);
			if(sublist!=null && sublist.size()>0){
				boolean flag = false;
				for(int j=0;j<sublist.size();j++){
					Map<String,Object> map = sublist.get(j);
					if(map!=null && map.size()>0){
						flag = true;
						break;
					}
				}
				if(flag){
					desclist.add(sublist);
				}
			}
		}
		return desclist;
	}
	
	/**
	 * 获得指定日期的后30天  
	 * @param specifiedDay 制定的日期
	 * @param dayNum 指定的天数
	 * @return
	 */
    public static String getSpecifiedDayMonth(String specifiedDay,int dayNum) {//可以用new Date().toLocalString()传递参数  
    	 Calendar c = Calendar.getInstance();  
         Date date = null;  
         try {  
             date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
         } catch (ParseException e) {  
             e.printStackTrace();  
         }  
         c.setTime(date);  
         int day = c.get(Calendar.DATE);  
         c.set(Calendar.DATE, day + dayNum);  
   
         String dayAfter = new SimpleDateFormat("yyyy-MM-dd")  
                 .format(c.getTime());  
         return dayAfter;  
    }  
	
    /**
	 * 获取结算时间
	 * @param payDay 结算日
	 * @param cycle 结算周期
	 * @param baseDate 基准日期
	 * @return 结算时间
	 */
	public static Date getSettlemnet_time(String payDay,String cycle,Date baseDate){
		//结算日
		int day_of_pay = payDay==null||payDay.equals("")?0:Integer.parseInt(payDay);
		Calendar d = Calendar.getInstance();
		d.setTime(baseDate);
		if("2".equals(cycle)||"5".equals(cycle)){//按消费日期结算 或 按预定日期结算
			d.add(Calendar.DAY_OF_MONTH,day_of_pay ) ;
			Date cd = d.getTime();
			return cd ;
		}
		else if("3".equals(cycle)){//按周结
			//每周几结算
			int weekday = day_of_pay%7+1;
			//给定日期的星期
			int t = d.get(Calendar.DAY_OF_WEEK);
			if(t>weekday){ 
				d.add(Calendar.WEEK_OF_MONTH, 1);
				d.set(Calendar.DAY_OF_WEEK, weekday);
			}else{
				d.set(Calendar.DAY_OF_WEEK, weekday);
			}
			return d.getTime() ;
		}
		else if("4".equals(cycle)){//按月结
			//获取给定日期是几号
			int now_day = d.get(Calendar.DAY_OF_MONTH);
			if(now_day>day_of_pay){
				int m = d.get(Calendar.MONTH);
				d.set(Calendar.MONTH, m+1);
				int maxDay = d.getActualMaximum(Calendar.DAY_OF_MONTH); //一个月的最后一天
				day_of_pay = day_of_pay>maxDay?maxDay:day_of_pay;
				d.set(Calendar.DAY_OF_MONTH, day_of_pay);
			}else{
				int maxDay = d.getActualMaximum(Calendar.DAY_OF_MONTH); //一个月的最后一天
				day_of_pay = day_of_pay>maxDay?maxDay:day_of_pay;
				d.set(Calendar.DAY_OF_MONTH, day_of_pay);
			}
			Date cd = d.getTime();
			return cd;
		}
		else {//按日结
			int hour = d.get(Calendar.HOUR_OF_DAY);
			int maxHour = d.getActualMaximum(Calendar.HOUR_OF_DAY); //一天的最后时间
			day_of_pay = day_of_pay>maxHour?0:day_of_pay;
			if(hour>=day_of_pay){
				int dd = d.get(Calendar.DAY_OF_MONTH);
				d.set(Calendar.DAY_OF_MONTH, dd+1);
			}
			d.set(Calendar.HOUR_OF_DAY, day_of_pay);
			d.set(Calendar.MINUTE, 0);
			d.set(Calendar.SECOND, 0);
			Date cd = d.getTime();
			return cd;
		}
	}
}
