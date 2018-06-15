package cpm.pokepet.util;

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
	 * @date 2018年5月25日
	 */
	public static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			return -1;
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return age;
	}

}
