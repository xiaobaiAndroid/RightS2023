package com.android.launcher.airsystem.airflow.allocation;

/**
* @description: (s500类型机器) 主驾气流分配类型
 * 主驾和副驾分别用两个字符表示气流分配，主驾类型是第二个字符表示， 副驾第一一个字符表示，比如66, 第一个6是副驾的气流类型，第二个6是主驾的气流类型
 *    s500
 *    C  吹顶部
 *    A  吹头部
 *    9  吹胸部
 *    8  吹膝盖
 *    6  吹脚部
 *    4  吹胸膝脚 （胸部的风大）
 *    3  吹胸膝脚（膝盖的风大）
 *    2  吹胸膝脚（脚部的风大）
* @createDate: 2023/4/30
*/
public class AllocationTypeS500 {

    public static final String TOP = "C";

    public static final String HEAD = "A";

    public static final String CHEST = "9";

    public static final String KNEE = "8";

    public static final String FOOT = "6";

    public static final String CKF_CHEST_MAX = "4";

    public static final String CKF_KNESS_MAX = "3";
    public static final String CKF_FOOT_MAX = "2";

}
