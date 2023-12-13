package com.android.launcher.airsystem;

/**
* @description: 空调系统协议发送的标识
* @createDate: 2023/5/1
*/
public class AirSystemProtocolFlags {

    //主驾的气流分配数据标志
    public static final String AIRFLOW_MAIN_DRIVER = "04";
    //副驾的气流分配数据标志
    public static final String AIRFLOW_COPILOT = "05";

    //风量标志
    public static final String WIND_MAIN_DRIVER = "06";
    public static final String WIND_COPILOT = "18";

    //温度控制标志
    public static String TEMP_MAIN_DRIVER = "09";
    public static String TEMP_COPILOT = "19";

    //空调开关

    //气流模式
    public static String AIRFLOW_MODE_MAIN_DRIVER = "20";
//    public static String AIRFLOW_MODE_COPILOT = "21";
}
