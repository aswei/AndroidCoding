/**
 * DateTool.java
 * 版权所有(C) 2011 cuiran2001@163.com
 * 创建:CuiRan  2011-6-10 上午10:47:15
 */

package cn.com.coding.androidcodinglibrary.utils.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author CuiRan
 * @version 1.0.0
 * @desc
 */
public class DateUtil {
    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 1s中的毫秒数
     */
    private static final int MILLIS = 1000;

    /**
     * 一年当中的月份数
     */
    private static final int MONTH_PER_YEAR = 12;

    /**
     * 私有构造方法，禁止对该类进行实例化
     */
    private DateUtil() {

    }

    /**
     * 得到系统当前日期时间
     *
     * @return 当前日期时间
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 得到用缺省方式格式化的当前日期
     *
     * @return 当前日期
     */
    public static String getDate() {
        return getDateTime(DEFAULT_DATE_FORMAT);
    }

    /**
     * 得到用缺省方式格式化的当前日期及时间
     *
     * @return 当前日期及时间
     */
    public static String getDateTime() {
        return getDateTime(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 得到系统当前日期及时间，并用指定的方式格式化
     *
     * @param pattern 显示格式
     * @return 当前日期及时间
     */
    public static String getDateTime(String pattern) {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, pattern);
    }


    /**
     * 得到用指定方式格式化的日期
     *
     * @param date    需要进行格式化的日期
     * @param pattern 显示格式
     * @return 日期时间字符串
     */
    public static String getDateTime(Date date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 得到当前年份
     *
     * @return 当前年份
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);

    }

    /**
     * 得到当前月份
     *
     * @return 当前月份
     */
    public static int getCurrentMonth() {
        //用get得到的月份数比实际的小1，需要加上
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前日
     *
     * @return 当前日
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     *
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(int days) {
        return add(getNow(), days, Calendar.DATE);
    }

    /**
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date 基准日期
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }

    /**
     * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(int months) {
        return add(getNow(), months, Calendar.MONTH);
    }

    /**
     * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     * 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
     *
     * @param date   基准日期
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    /**
     * 内部方法。为指定日期增加相应的天数或月数
     *
     * @param date   基准日期
     * @param amount 增加的数量
     * @param field  增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    private static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(field, amount);

        return calendar.getTime();
    }

    /**
     * 通过date对象取得格式为小时:分钟的实符串
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getHourMin(Date date) {
        StringBuffer sf = new StringBuffer();
        sf.append(date.getHours());
        sf.append(":");
        sf.append(date.getMinutes());
        return sf.toString();
    }

    /**
     * 获取增加多少月的时间
     *
     * @return addMonth - 增加多少月
     */
    public static Date getAddMonthDate(int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, addMonth);
        return calendar.getTime();
    }

    /**
     * 获取增加多少天的时间
     *
     * @return addDay - 增加多少天
     */
    public static Date getAddDayDate(int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, addDay);
        return calendar.getTime();
    }

    /**
     * 获取增加多少小时的时间
     *
     * @return addDay - 增加多少消失
     */
    public static Date getAddHourDate(int addHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, addHour);
        return calendar.getTime();
    }


    /**
     * 是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(long date1, long date2) {
        long days1 = date1 / (1000 * 60 * 60 * 24);
        long days2 = date2 / (1000 * 60 * 60 * 24);
        return days1 == days2;
    }


    /**
     * 获取某一个日期的月份
     *
     * @param d
     * @return
     */
    public static int getMonth(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某一个日期的天
     *
     * @param d
     * @return
     */
    public static int getDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.DATE);
    }


    /**
     * 获取某一个日期的年份
     *
     * @param d
     * @return
     */
    public static int getYear(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.YEAR);
    }


    /**
     * 将一个字符串用给定的格式转换为日期类型。 <br>
     * 注意：如果返回null，则表示解析失败
     *
     * @param datestr 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为"yyyy-MM-dd"的形式
     * @return 解析后的日期
     */
    public static Date parse(String datestr, String pattern) {
        Date date = null;

        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATE_FORMAT;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 返回本月的最后一天
     *
     * @return 本月最后一天的日期
     */
    public static Date getMonthLastDay() {
        return getMonthLastDay(getNow());
    }

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @param date 基准日期
     * @return 该月最后一天的日期
     */
    public static Date getMonthLastDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //将日期设置为下一月第一天
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);

        //减去1天，得到的即本月的最后一天
        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }

    /**
     * 计算两个具体日期之间的秒差，第一个日期-第二个日期
     *
     * @param date1
     * @param date2
     * @param onlyTime 是否只计算2个日期的时间差异，忽略日期，true代表只计算时间差
     * @return
     */
    public static long diffSeconds(Date date1, Date date2, boolean onlyTime) {
        if (onlyTime) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            //calendar.set(1984, 5, 24);
            long t1 = calendar.getTimeInMillis();
            calendar.setTime(date2);
            //calendar.set(1984, 5, 24);
            long t2 = calendar.getTimeInMillis();
            return (t1 - t2) / MILLIS;
        } else {
            return (date1.getTime() - date2.getTime()) / MILLIS;
        }
    }

    /**
     * long 转 日期格式
     * @param dateFormat
     * @param millSec
     * @return
     */
    public static String longToDateFormart(String dateFormat, Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }

    /**
     * @param date1
     * @param date2
     * @return
     */
    public static long diffSeconds(Date date1, Date date2) {
        return diffSeconds(date1, date2, false);
    }

    /**
     * 根据日期确定星期几:1-星期日，2-星期一.....s
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int mydate = cd.get(Calendar.DAY_OF_WEEK);
        return mydate;
    }

//    /**
//     * 将2010-06-01转换为20100601格式
//     * @param date
//     * @return
//     */
//    public static String toVODate(String date) {
//    	if (StringUtil.isEmpty(date)) {
//    		//return DateFormatUtils.format(new Date(), "yyyy-MM-dd");
//    		return "";
//    	}
//    	Date tdate;
//		try {
//			tdate = new SimpleDateFormat("yyyyMMdd").parse(date);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			throw new SmsException("日期转换异常!");
//		}
//    	return DateFormatUtils.format(tdate, "yyyy-MM-dd");
//    }
//    
//    /**
//     * 将20100601转换为2010-06-01格式
//     * @param date
//     * @return
//     */
//    public static String toDomainDate(String date) {
//    	if (StringUtil.isEmpty(date)) {
//    		return "";
//    	}
//    	Date tdate;
//		try {
//			tdate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
//		} catch (ParseException e) {
//			throw new BusinessException("上收时间或者启用时间格式不正确!");
//		}
//    	return DateFormatUtils.format(tdate, "yyyyMMdd");
//    }

    /**
     * 验证用密码是否在有效期内(跟当前日期比较)
     *
     * @param format    "yyyyMMdd"
     * @param validDate
     * @return
     */
    public static boolean isValidDate(String validDate, String format) {
        Date valid = parse(validDate, format);
        Date now = new Date();
        String nowStr = new SimpleDateFormat(format).format(now);
        try {
            now = new SimpleDateFormat(format).parse(nowStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valid.after(now);
    }


}
