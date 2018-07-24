package cpm.pokepet.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * ClassName: CommonUtil 
 * @Description: 工具类
 * @author Bean Zhou
 * @date 2018年5月25日
 */
public class CommonUtil {
	
	/**
	 * 
	 * @Description: 获取年龄
	 * @param @param birthday
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Bean Zhou
	 * @Modified By lily
	 * @date 2018年5月25日
	 */
	public static String getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			return "";
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		//获取
		int year = yearNow - yearBirth;
		int month=monthNow-monthBirth;
		int day=dayOfMonthNow-dayOfMonthBirth;

		if(day<0){
			month -= 1;
			cal.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
			day = day + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		if(month<0){
			month = (month+12)%12;
			year--;
		}
		System.out.println("年龄："+year+"年"+month+"月"+day+"天");
		double ageD=year+month/12.0+	day/30.0*0.1;
		DecimalFormat df = new DecimalFormat("0.00");
		String age=df.format(ageD);
		System.out.println(age);

		return age;
	}





	public static void main(String[] args) {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2018,6,3);
		System.out.println(getAgeByBirthday(calendar.getTime()));
	}

}
