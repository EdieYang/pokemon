package com.pokepet.algorithm;

import java.math.BigDecimal;

public class PetAlgorithm {

	/**
	 * 1~2级 , 距离(米) : 经验(exp) = 1 : 1
	 */
	static final double WALK_EXP_RATE_FOR_LEVEL_1_2 = 1;

	/**
	 * 1~2级 , 距离(米) : 经验(exp) = 1 : 1
	 */
	static final double WALK_EXP_RATE_FOR_LEVEL_3_10 = 0.8;

	/**
	 * 1~2级 , 距离(米) : 经验(exp) = 1 : 1
	 */
	static final double WALK_EXP_RATE_FOR_LEVEL_11_15 = 0.6;

	/**
	 * 1~2级 , 距离(米) : 经验(exp) = 1 : 1
	 */
	static final double WALK_EXP_RATE_FOR_LEVEL_16_20 = 0.5;

	/**
	 * 1~2级 , 距离(米) : 经验(exp) = 1 : 1
	 */
	static final double WALK_EXP_RATE_FOR_LEVEL_21_30 = 0.4;

	/**
	 * 
	 * @Description: 根据不同pet等级获取相应的行走经验
	 * @param @param petLevel
	 * @param @param distance
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年6月19日
	 */
	public static int getWalkExp(int petLevel, int distance) {
		int exp = 0;
		if (1 <= petLevel && petLevel <= 2) {
			exp = new BigDecimal(distance).multiply(new BigDecimal(WALK_EXP_RATE_FOR_LEVEL_1_2)).intValue();
		} else if (3 <= petLevel && petLevel <= 10) {
			exp = new BigDecimal(distance).multiply(new BigDecimal(WALK_EXP_RATE_FOR_LEVEL_3_10)).intValue();
		} else if (11 <= petLevel && petLevel <= 15) {
			exp = new BigDecimal(distance).multiply(new BigDecimal(WALK_EXP_RATE_FOR_LEVEL_11_15)).intValue();
		} else if (16 <= petLevel && petLevel <= 20) {
			exp = new BigDecimal(distance).multiply(new BigDecimal(WALK_EXP_RATE_FOR_LEVEL_16_20)).intValue();
		} else if (21 <= petLevel && petLevel <= 30) {
			exp = new BigDecimal(distance).multiply(new BigDecimal(WALK_EXP_RATE_FOR_LEVEL_21_30)).intValue();
		}
		return exp;
	}

}
