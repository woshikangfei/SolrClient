package com.fei.base.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 * 
 */
public class DateUtils {

	/** 取得服务器时间 */
	public static Date serverTime() {
		return new Date();
	}

	
	/**
	 * @param date
	 *            格式必须为yyyy-MM-dd HH:mm:ss
	 * @throws PubException
	 *             如果参数错误或解析错误，抛出此异常
	 */
	public static Date parse(final String date) {
		try {
			return parse(date, "yyyy-MM-dd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date parseStartDate(final String date) throws Exception {
		return parse(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseEndDate(final String date) throws Exception {
		return parse(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
	}

	public static void main(String[] args) {
		String dt = getNowDateString("yyyy-MM-dd");
		try {
			System.out.println(parseEndDate(dt));
			System.out.println(parseStartDate(dt));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param date
	 *            日期字符串
	 * @param format
	 *            日期解析格式
	 * @throws PubException
	 *             如果参数错误或解析错误，抛出此异常
	 * @return 转换后的日期类
	 */
	public static Date parse(final String date, final String format)
			throws Exception {
		if (date == null) {
			throw new Exception("参数date不能为空！");
		}
		if (format == null) {
			throw new Exception("参数format不能为空！");
		}
		Map<String, SimpleDateFormat> map = cache.get();
		if (map == null) {
			map = new HashMap<String, SimpleDateFormat>();
			cache.set(map);
		}
		SimpleDateFormat dateFormat = map.get(format);
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(format);
			map.put(format, dateFormat);
		}
		try {
			return dateFormat.parse(date);
		} catch (final ParseException e) {
			throw new Exception("日期格式解析错误。日期字符串：" + date + "；日期解析格式：" + format);
		}
	}
	/**
	 *  @param int
	 *            数字类型时间
	 * @throws PubException
	 *             如果参数错误或解析错误，抛出此异常
	 * @return 返回格式为m分s秒格式
	 */
	public static String IntToDate(final int usetime) throws Exception {
		String relUsetime=null;
		if((int)usetime/60000>=1){
			relUsetime = (int)usetime/60000+"分"+(int)(usetime/1000)%60+"秒";
		}else{
			relUsetime = usetime/1000 +"秒";
		}
		return relUsetime;
	}
	

	/**
	 * @throws PubException
	 *             如果参数错误或解析错误，抛出此异常
	 * @return 返回格式为yyyy-MM-dd HH:mm:ss的日期字符串
	 */
	public static String format(final Date date) {
		try {
			return format(date, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param format
	 *            "example:yyyy/MM/dd"
	 * @throws PubException
	 *             如果参数错误或解析错误，抛出此异常
	 */
	public static String format(final Date date, final String format)
			throws Exception {
		if (date == null) {
			throw new Exception("参数date不能为空！");
		}
		if (format == null) {
			throw new Exception("参数format不能为空！");
		}
		Map<String, SimpleDateFormat> map = cache.get();
		if (map == null) {
			map = new HashMap<String, SimpleDateFormat>();
			cache.set(map);
		}
		SimpleDateFormat dateFormat = map.get(format);
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(format);
			map.put(format, dateFormat);
		}
		return dateFormat.format(date);
	}

	/** 为减小创建SimpleDateFormat的开销，设立缓存。 (Map的key为日期转换格式) */
	private static final ThreadLocal<Map<String, SimpleDateFormat>> cache = new ThreadLocal<Map<String, SimpleDateFormat>>();

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 获取现在时间字符串
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDateString() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 获取现在时间字符串
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDateString(String fmt) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(fmt);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	public static Date getTodayBeginDate()
	{
		Date now = new Date();
		Date rs=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			rs=sdf.parse(sdf.format(now));
		} catch (ParseException e)
		{
			
		}
		
		return rs;
	}
	
	public static String addMonth(String s, int n) {   
        try {   
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
  
                 Calendar cd = Calendar.getInstance();   
                 cd.setTime(sdf.parse(s));   
                 //cd.add(Calendar.DATE, n);//增加一天   
                 cd.add(Calendar.MONTH, n);//增加一个月   
  
                 return sdf.format(cd.getTime());   
       
             } catch (Exception e) {   
                 return null;   
             }   
     }  

}
