package com.android.launcher.airsystem.airflow.allocation;

/**
* @description: (s300类型机器) 主驾气流分配类型
 * 主驾和副驾用两个字符表示气流分配，主驾类型是第二个字符表示， 副驾第一个字符表示，比如66, 第一个6是副驾的气流类型，第二个6是主驾的气流类型
 *      s300
 *      *   6 吹脚
 *      *   8 脚和胸
 *      *   9 胸
 *      *   C 挡风
 *      *   1 挡风和胸部
 *      *   3 挡风和胸部和脚部
 *      *   5 挡风和脚部
* @createDate: 2023/4/30
*/
public class AllocationTypeS300 {

    public static final String FOOT = "6";

    public static final String FOOT_CHEST = "8";

    public static final String CHEST = "9";

    public static final String SHIELD_WIND = "C";

    public static final String SHIELD_WIND_CHEST = "1";

    public static final String SHIELD_WIND_CHEST_FOOT = "3";

    public static final String SHIELD_WIND_FOOT = "5";


}
