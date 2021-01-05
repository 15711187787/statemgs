package Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	/***
	 * 
	 * @param oldDate 传入需要对比的时间戳
	 * @param day 如果对比今天传入0，对比昨天传入-1
	 * @return
	 */
	public static Boolean equalsDate(long oldDate,int day) {
		System.out.println("配置day:"+day);
		Date date = new Date();
		date.setTime(oldDate); //注意此处毫秒值应该加上l，表示long型数据，否则报错
		//补充：获取当前系统时间的毫秒值：
		SimpleDateFormat fm  =new SimpleDateFormat("yyyy-MM-dd");
		String olddate= fm.format(date);
		Calendar  cal = Calendar.getInstance();
		cal.add(Calendar.DATE,day);
		String newdate = fm.format(cal.getTime());
		System.out.println("告警生成日期"+olddate);
		System.out.println("告警过滤日期"+newdate);
		return olddate.equals(newdate);
	}
	/***
	 * 
	 * @param time 传入时间戳
	 * @return Strng date
	 */
	public static String getStringDate(long time) {
		Date date = new Date();
		date.setTime(time); //注意此处毫秒值应该加上l，表示long型数据，否则报错
		//补充：获取当前系统时间的毫秒值：
		SimpleDateFormat fm  =new SimpleDateFormat("yyyy-MM-dd");
		return fm.format(date);
	}
}
