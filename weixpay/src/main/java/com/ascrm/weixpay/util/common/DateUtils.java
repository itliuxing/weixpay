package com.ascrm.weixpay.util.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/****
 * *
 * 类名称：		DateUtils   
 * 类描述：   		日期工具类
 * 创建人：		
 * 创建时间：		2014-11-25 下午3:34:22   
 * 修改人：		liuxing   编撰
 * 修改时间：		2014-11-25 下午3:34:22   
 * 修改备注：   
 * @version
 */
public class DateUtils {

	/**
	 * 定义常见的时间格式
	 */
	private DateUtils() {
	}

	private static String[] dateFormat = { "yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd HH:mm:ss", "yyyy年MM月dd日HH时mm分ss秒", "yyyy-MM-dd",
			"yyyy/MM/dd", "yy-MM-dd", "yy/MM/dd", "yyyy年MM月dd日", "HH:mm:ss",
			"yyyyMMddHHmmss", "yyyyMMdd", "SSS","yyyy-MM" };

	/***
	 * 根据日期和指定的格式转换为String 字符串
	 * @param date
	 * @param style
	 * 		0：yyyy-MM-dd HH:mm:ss;
	 * 		1：yyyy/MM/dd HH:mm:ss;
	 * 		2：yyyy年MM月dd日HH时mm分ss秒;
	 * 		3：yyyy-MM-dd;
	 * 		4：yyyy/MM/dd;
	 * 		5：yy-MM-dd;
	 * 		6：yy/MM/dd;
	 * 		7：yyyy年MM月dd日;
	 * 		8：HH:mm:ss;
	 * 		9：yyyyMMddHHmmss;
	 * 		10：yyyyMMdd;
	 * 		11：SSS;
	 * 		12：yyyy-MM
	 * @return
	 */
	public static String simpleDateFormat( Date date ,int style ){
		if( date == null || style >= dateFormat.length || style < 0 ){
			return null ;
		}else{
			return new SimpleDateFormat( dateFormat[style] ).format(date) ;
		}
	}
	
	/***
	 * 根据String日期和指定的格式转换为 Date
	 * @param date
	 * @param style
	 * 		0：yyyy-MM-dd HH:mm:ss;
	 * 		1：yyyy/MM/dd HH:mm:ss;
	 * 		2：yyyy年MM月dd日HH时mm分ss秒;
	 * 		3：yyyy-MM-dd;
	 * 		4：yyyy/MM/dd;
	 * 		5：yy-MM-dd;
	 * 		6：yy/MM/dd;
	 * 		7：yyyy年MM月dd日;
	 * 		8：HH:mm:ss;
	 * 		9：yyyyMMddHHmmss;
	 * 		10：yyyyMMdd;
	 * 		11：SSS;
	 * 		12：yyyy-MM
	 * @return Date
	 * @throws ParseException 
	 */
	public static Date simpleDateFormat( String date ,int style ) throws ParseException{
		if( date == null || style >= dateFormat.length || style < 0 ){
			return null ;
		}else{
			return new SimpleDateFormat( dateFormat[style] ).parse(date) ;
		}
	}
	
	/**
	 * 将日期格式从 java.util.Calendar 转到 java.sql.Timestamp 格式
	 * 
	 * @param date
	 *            java.util.Calendar 格式表示的日期
	 * @return java.sql.Timestamp 格式表示的日期
	 */
	public static Timestamp convUtilCalendarToSqlTimestamp(Calendar date) {
		if (date == null)
			return null;
		else
			return new Timestamp(date.getTimeInMillis());
	}

	/**
	 * 将日期格式从 java.util.Timestamp 转到 java.util.Calendar 格式
	 * 
	 * @param date
	 *            java.sql.Timestamp 格式表示的日期
	 * @return java.util.Calendar 格式表示的日期
	 */
	public static Calendar convSqlTimestampToUtilCalendar(Timestamp date) {
		if (date == null)
			return null;
		else {
			java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
			gc.setTimeInMillis(date.getTime());
			return gc;
		}
	}

	/**
	 * 解析一个字符串，形成一个Calendar对象，适应各种不同的日期表示法
	 * 
	 * @param dateStr
	 *            期望解析的字符串，注意，不能传null进去，否则出错
	 * @return 返回解析后的Calendar对象 <br>
	 * <br>
	 *         可输入的日期字串格式如下： <br>
	 *         "yyyy-MM-dd HH:mm:ss", <br>
	 *         "yyyy/MM/dd HH:mm:ss", <br>
	 *         "yyyy年MM月dd日HH时mm分ss秒", <br>
	 *         "yyyy-MM-dd", <br>
	 *         "yyyy/MM/dd", <br>
	 *         "yy-MM-dd", <br>
	 *         "yy/MM/dd", <br>
	 *         "yyyy年MM月dd日", <br>
	 *         "HH:mm:ss", <br>
	 *         "yyyyMMddHHmmss", <br>
	 *         "yyyyMMdd", <br>
	 *         "yyyy.MM.dd", <br>
	 *         "yy.MM.dd"
	 */
	public static Calendar parseCalender(String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0)
			return null;
		Date result = parseDate(dateStr, 0);
		// System.out.println("result="+result);
		Calendar cal = Calendar.getInstance();
		cal.setTime(result);
		return cal;
	}

	/**
	 * 查询当天的前n天的具体时间
	 * 
	 * @param 天数
	 * @return
	 */
	public static Calendar getCurrentCalBefore(int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, -n);
		return day;
	}

	/**
	 * 查询当天的后n月的具体时间
	 * 
	 * @param 月数
	 * @return
	 */
	public static Calendar getCurrentCalAfterByMonth(int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.MONTH, n);
		return day;
	}

	/**
	 * 查询当天的前n年的具体时间
	 * 
	 * @param 年数
	 * @return
	 */
	public static Calendar getCurrentCalBeforeYear(int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.YEAR, -n);
		return day;
	}

	/**
	 * 计算两个时间的相差天数
	 * 
	 * @param d1
	 *            开始时间
	 * @param d2
	 *            结束时间
	 * @return
	 */
	public static long calendarminus(Calendar d1, Calendar d2) {
		if (d1 == null || d2 == null) {
			return 0;
		}
		return (d2.getTimeInMillis() - d1.getTimeInMillis())
				/ (3600 * 24 * 1000);
	}

	/**
	 * 内部方法，根据某个索引中的日期格式解析日期
	 * 
	 * @param dateStr
	 *            期望解析的字符串
	 * @param index
	 *            日期格式的索引--此索引指的当前类的静态属性值
	 * @return 返回解析结果
	 */
	public static Date parseDate(String dateStr, int index) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat[index]);
			return df.parse(dateStr);
		} catch (ParseException pe) {
			return parseDate(dateStr, index + 1);
		} catch (ArrayIndexOutOfBoundsException aioe) {
			return null;
		}
	}

	/**
	 * 将日期格式从 java.util.Date 转到 java.sql.Timestamp 格式 convUtilDateToSqlTimestamp <br>
	 * 
	 * @param date
	 *            java.util.Date 格式表示的日期
	 * @return Timestamp java.sql.Timestamp 格式表示的日期
	 */
	public static Timestamp convUtilDateToSqlTimestamp(Date date) {
		if (date == null)
			return null;
		else
			return new Timestamp(date.getTime());
	}

	/***
	 * 日期类转换成--Calendar
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar convUtilDateToUtilCalendar(Date date) {
		if (date == null)
			return null;
		else {
			java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
			gc.setTimeInMillis(date.getTime());
			return gc;
		}
	}

	/**
	 * 内部方法，根据某个索引中的日期格式解析日期
	 * 
	 * @param dateStr
	 *            期望解析的字符串
	 * @param index
	 *            日期格式的索引
	 * @return 返回解析结果
	 */
	public static Timestamp parseTimestamp(String dateStr, int index) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat[index]);

			return new Timestamp(df.parse(dateStr).getTime());
		} catch (ParseException pe) {
			return new Timestamp(parseDate(dateStr, index + 1).getTime());
		} catch (ArrayIndexOutOfBoundsException aioe) {
			return null;
		}
	}

	/***
	 * 得到当前时间的时间戳
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/***
	 * 根据年月得到此年此月的最后一天的年月日格式
	 * @param year	指定年
	 * @param month	指定月
	 * @return
	 */
	public static String getLastDate(int year, int month) {
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			monthDay[1] = 29;
		}
		int monthDayNum = monthDay[month - 1];
		String end = year + "-" + month + "-" + monthDayNum;
		return end;
	}

	/***
	 * 根据年月得到此年此月的最后一天的天数
	 * @param year	指定年
	 * @param month	指定月
	 * @return
	 */
	public static int getmonthDayNum(int year, int month) {
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			monthDay[1] = 29;
		}
		int monthDayNum = monthDay[month - 1];
		return monthDayNum;
	}

	/***
	 * 判断日期的是否为空
	 * @param myDate
	 * @return
	 */
	public static Date isDate(Date myDate) {
		if (myDate == null)
			return new Date();
		return myDate;
	}

	/**
	 * 查询当天的前n天的具体时间
	 * @param 	n
	 * @return	yyyy-MM-dd
	 */
	public static String getCurrentDateBefore(int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, -n);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String result = sdf.format(day.getTime());
		return result;
	}

	/**
	 * 毫秒差(time1 - time2，返回负数，若time1在time2之前)
	 * @param stopTime		结束时间
	 * @param startTime		开始时间
	 * @return
	 */
	public static long getQuot(Date startTime, Date stopTime) {
		long quot = 0;
		try {
			quot = stopTime.getTime() - startTime.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quot;
	}

	/***
	 * 判断一个日期是星期几
	 * @param date	传入的日期		error
	 * @return
	 */
	public static int dayOfWeek( Date date) {
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return x;
	}

	/***
	 * 判断一个日期是星期几
	 * @param date	传入的日期		complete
	 * @return	返回的是一个1,2,3,4,5,6,7
	 */
	public static int dayOfWeek3(Object date1) {
		Date date = (Date) date1;
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return x != 1 ? x - 1 : 7;
	}

	/***
	 * 判断一个日期是星期几
	 * @param date	传入的日期		complete
	 * @return	返回的是一个周一,周二....周日
	 */
	public static String dayOfWeek2(Object date1) {
		Date date = (Date) date1;
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeekByDayNum(x);
	}

	/****
	 * 判断week的中文返回
	 * @param x
	 * @return
	 */
	public static String dayOfWeekByDayNum(int x) {
		String str = "周日";
		if (x == 7) {
			str = "周六";
		} else if (x == 6) {
			str = "周五";
		} else if (x == 5) {
			str = "周四";
		} else if (x == 4) {
			str = "周三";
		} else if (x == 3) {
			str = "周二";
		} else if (x == 2) {
			str = "周一";
		}
		return str;
	}

	// 根据当前一个星期中的第几天得到它的日期
	public static Date getDateOfCurrentWeek(int day) {
		Calendar aCalendar = Calendar.getInstance();
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		aCalendar.add(Calendar.DAY_OF_WEEK, day - (x - 1));
		return aCalendar.getTime();
	}

	/****
	 * 得到当前日期是此月的第几个星期
	 * @param date
	 * @return
	 */
	public static int getWeekOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/***
	 * 将字符串转换成日期 
	 * @param rq
	 * @return
	 */
	public static Date getDateByString(String rq) {

		DateFormat df = new SimpleDateFormat();
		Date d = null;
		try {
			d = df.parse(rq);
		} catch (Exception e) {
		}
		return d;
	}

	/***
	 * 将字符串按内内定格式转换成日期
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date getDateByString(String str, String pattern) {
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat(pattern);
			return sdf.parse(str);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 比较时间是否相同，是否同步
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean equals(Date start, Date end) {
		if (start != null && end != null && start.getTime() == end.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 * 匹配是否在某个时间段中
	 * @param timePeriod
	 *            "00:00-06:00"格式
	 * @param time
	 *            "07:30"
	 * @return
	 */
	public static boolean isInPeriod(String timePeriod, String time) {
		String startTime = timePeriod.substring(0, 5);
		String endTime = timePeriod.substring(6);
		String timeTime = time;
		// 和时间段的开始或者结束刚好相等的时候
		if (startTime.equalsIgnoreCase(timeTime)
				|| endTime.equalsIgnoreCase(timeTime)) {
			return true;
		}
		SimpleDateFormat d = new SimpleDateFormat("HH:mm");
		try {
			Date startDate = d.parse(startTime);
			Date endDate = d.parse(endTime);
			Date timeDate = d.parse(timeTime);
			if (timeDate.after(startDate) && timeDate.before(endDate)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/***
	 * 当前日期增加几个月
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date addMonth(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.MONTH, num);
		return startDT.getTime();
	}

	/****
	 * 当前日期增加或减少天数
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

	/***
	 * 根据格式，获取当前时间
	 * @param str
	 * @return
	 */
	public static String nowDate(String str) {
		SimpleDateFormat fo = new SimpleDateFormat(str);
		Date date = new Date();
		String nowDate = null;
		try {
			nowDate = fo.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowDate;
	}

	/**
	 * 将某个日期转换成业务逻辑上面的星期几 calendar: 周一：2；周二：3；周三：4；周四：5；周五：6；周六：7；周日：1 业务逻辑 ：
	 * 周一：1；周二：2；周三：3；周四：4；周五：5；周六：6；周日：7
	 * 
	 * @param day
	 * @return
	 */
	public static String explainDayOfWeek(Date date) {
		int departDay = dayOfWeek(date);
		if (departDay == 1) {
			return "7";
		} else if (departDay == 2) {
			return "1";
		} else {
			return String.valueOf(departDay - 1);
		}
	}

	/***
	 * 计算两个时间之间的时间距离
	 * @param start
	 * @param stop
	 * @param style
	 * @return	int
	 * @throws ParseException 日期格式化异常则不做处理
	 */
	public static int timeDistance( String start,String stop ,int style ) throws ParseException{
		return (int) ( simpleDateFormat( stop , style).getTime() - simpleDateFormat( start , style).getTime() ) ;
	}
	
	/***
	 * 计算两个时间之间的时间距离
	 * @param start
	 * @param stop
	 * @param style
	 * @return	int
	 * @throws ParseException 日期格式化异常则不做处理
	 */
	public static int timeDistance( Date start,Date stop ,int style ) {
		return (int) ( stop.getTime() - start.getTime() )  ;
	}

}
