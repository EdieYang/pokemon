package com.pokepet.enums;

import java.util.Calendar;
import java.util.Date;

public enum StarSignEnum {

	ARIES("白羊","03-21","04-20"),//白羊（3月21日～4月20日）
	TAURUS("金牛座","04-21","05-21"),//金牛座（4月21～5月21日）
	GEMINI("双子座","05-22","06-21"),// 双子座（5月22日～6月21日）
	CANCER("巨蟹座","06-22","07-22"),// 巨蟹座（6月22日～7月22日）
	LEO("狮子座","07-23","08-23"),// 狮子座（7月23日～8月23日）
	VIRGO("处女座","08-24","09-23"),// 处女座（8月24日～9月23日）
	LIBRA("天秤座","09-24","10-23"),// 天秤座（9月24日～10月23日）
	SCORPIO("天蝎座","10-24","11-22"),// 天蝎座（10月24日～11月22日）
	SAGITTARIUS("射手座","11-23","12-21"),// 射手座（11月23日～12月21日）
	CAPRICORN("摩羯座","12-22","01-20"),// 摩羯座（12月22日～1月20日）
	AQUARIUS("水瓶座","01-21","02-19"),// 水瓶座（1月21日～2月19日）
	PISCES("双鱼座","02-20","03-20");// 双鱼座（2月20日～3月20日）
	
	// 成员变量
	private String name;
	private String dateStart;
	private String dateEnd;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	// 构造方法  
	private StarSignEnum(String name ,String dateStart ,String dateEnd){
		this.name = name;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}
	
	/*// 成员变量
		private String name;
		private int mStart;
		private int dStart;
		private int mEnd;
		private int dEnd;
		
		

		// 构造方法  
		private StarSignEnum(String name ,int mStart ,int dStart ,int mEnd,int dEnd){
			this.name = name;
			this.mStart = mStart;
			this.dStart = dStart;
			this.mEnd = mEnd;
			this.mEnd = mEnd;
		}*/
	
	// 普通方法  
    public static String getName(Date date) {  
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	int m = cal.get(Calendar.MONTH);
    	int d = cal.get(Calendar.DAY_OF_MONTH);
    	
        for (StarSignEnum c : StarSignEnum.values()) {  
        	String dateStart = c.getDateStart();
        	String dateEnd = c.getDateEnd();
        	int mStart = Integer.parseInt(dateStart.split("-")[0])-1;
        	int dStart = Integer.parseInt(dateStart.split("-")[1])-1;
        	int mEnd = Integer.parseInt(dateEnd.split("-")[0])-1;
        	int dEnd = Integer.parseInt(dateEnd.split("-")[1])-1;
            if ( (mStart == m && dStart <= d) || (m == mEnd && d <= dEnd)) {  
                return c.name;  
            }  
        }  
        return null;  
    }  
}
