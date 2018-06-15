package com.pokepet.enums;

/**
 * 
 * ClassName: PetLevelEnum 
 * @Description: 宠物等级、经验枚举类
 * @author Bean Zhou
 * @date 2018年5月25日
 */
public enum PetLevelEnum {
	
	LEVEL_0(0,0),
	LEVEL_1(1,620),
	LEVEL_2(2,650),
	LEVEL_3(3,700),
	LEVEL_4(4,780),
	LEVEL_5(5,890),
	LEVEL_6(6,1030),
	LEVEL_7(7,1200),
	LEVEL_8(8,1410),
	LEVEL_9(9,1650),
	LEVEL_10(10,1930),
	LEVEL_11(11,2250),
	LEVEL_12(12,2600),
	LEVEL_13(13,3000),
	LEVEL_14(14,3440),
	LEVEL_15(15,3930),
	LEVEL_16(16,4460),
	LEVEL_17(17,5040),
	LEVEL_18(18,5670),
	LEVEL_19(19,6350),
	LEVEL_20(20,7080),
	LEVEL_21(21,7870),
	LEVEL_22(22,8710),
	LEVEL_23(23,9610),
	LEVEL_24(24,10560),
	LEVEL_25(25,11580),
	LEVEL_26(26,12650),
	LEVEL_27(27,13790),
	LEVEL_28(28,14990),
	LEVEL_29(29,16260),
	LEVEL_30(30,17590),
	LEVEL_31(31,99999999),//19000
	LEVEL_32(32,20470),
	LEVEL_33(33,22010),
	LEVEL_34(34,23630),
	LEVEL_35(35,25320),
	LEVEL_36(36,27090),
	LEVEL_37(37,28930),
	LEVEL_38(38,30860),
	LEVEL_39(39,32860),
	LEVEL_40(40,34950),
	LEVEL_41(41,37120),
	LEVEL_42(42,39380),
	LEVEL_43(43,41720),
	LEVEL_44(44,44150),
	LEVEL_45(45,46670),
	LEVEL_46(46,49280),
	LEVEL_47(47,51980),
	LEVEL_48(48,54780),
	LEVEL_49(49,57670),
	LEVEL_50(50,60660),
	LEVEL_51(51,63750),
	LEVEL_52(52,66930),
	LEVEL_53(53,70220),
	LEVEL_54(54,73610),
	LEVEL_55(55,77110),
	LEVEL_56(56,80710),
	LEVEL_57(57,84420),
	LEVEL_58(58,88240),
	LEVEL_59(59,92170),
	LEVEL_60(60,96210),
	LEVEL_61(61,100370),
	LEVEL_62(62,104640),
	LEVEL_63(63,109030),
	LEVEL_64(64,113530),
	LEVEL_65(65,118160),
	LEVEL_66(66,122900),
	LEVEL_67(67,127770),
	LEVEL_68(68,132760),
	LEVEL_69(69,137880),
	LEVEL_70(70,143120),
	LEVEL_71(71,148500),
	LEVEL_72(72,154000),
	LEVEL_73(73,159630),
	LEVEL_74(74,165400),
	LEVEL_75(75,171300),
	LEVEL_76(76,177340),
	LEVEL_77(77,183510),
	LEVEL_78(78,189830),
	LEVEL_79(79,196280),
	LEVEL_80(80,202880),
	LEVEL_81(81,209620),
	LEVEL_82(82,216510),
	LEVEL_83(83,223540),
	LEVEL_84(84,230720),
	LEVEL_85(85,238050),
	LEVEL_86(86,245530),
	LEVEL_87(87,253160),
	LEVEL_88(88,260950),
	LEVEL_89(89,268890),
	LEVEL_90(90,276990),
	LEVEL_91(91,285250),
	LEVEL_92(92,293660),
	LEVEL_93(93,302240),
	LEVEL_94(94,310980),
	LEVEL_95(95,319890),
	LEVEL_96(96,328960),
	LEVEL_97(97,338200),
	LEVEL_98(98,347610),
	LEVEL_99(99,357190),
	LEVEL_100(100,366940);
	
	private int level ;
	private int exp_value;
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getEmpirical_value() {
		return exp_value;
	}

	public void setEmpirical_value(int empirical_value) {
		this.exp_value = empirical_value;
	}

	private PetLevelEnum(int level, int empirical_value) {  
        this.level = level;  
        this.exp_value = empirical_value;  
    } 
	
	// 普通方法  
    public static int getValue(int level) {  
        for (PetLevelEnum c : PetLevelEnum.values()) {  
            if (c.getLevel() == level) {  
                return c.exp_value;  
            }  
        }  
        return -1;  
    } 

}
