package com.android.launcher.type;

/**
 * 串口数据标志
 * @date： 2023/10/17
 * @author: 78495
*/
public enum SerialPortDataFlag {

    //汽车类型
    CAR_TYPE("35"),
    //锁车时自动合拢
    AUTOMATIC_CLOSING("23"),
    //中央门锁反馈
    DOOR_LOCK("24"),
    //头枕放倒
    HEADREST_RECLINE("31"),
    //安全带辅助
    SEAT_BELT_ASSIST("34"),
    //上下车辅助
    IN_AND_OUT_ASSIST("25"),
    //原车公里数
    CAR_MILEAGE("32"),
    //外部照明延迟
    EXTERIOR_LIGHTING("28"),
    //内部照明延迟
    INTERIOR_LIGHTING("27"),
    //环境照明延迟
    AMBIENT_LIGHTING("29"),
    //警报音量设置
    ALARM_SET("30"),
    //单位类型
    UNIT_TYPE("36"),
    //语言类型
    LANGUAGE_TYPE("37"),
    //后空调控制
    REAR_AC_CONTROL("14"),
    //前排空调，驾驶位风向
    FRONT_LEFT_WINDDIRE("04"),
    //前排空调，副驾风向
    FRONT_RIGHT_WINDDIRE("05"),
    //压缩机开关
    COMPRESSOR_OFF("16"),
    //行李箱开启限制
//    SUITCASE_OPENING_LIMIT("49"),

    CAN_1DC("50"),

    AC_OFF("51"),
    //主驾自动
    DRIVER_AUTO("52"),
    //副驾自动
    FRONT_SEAT_AUTO("53"),

    //自动空调
    SET_AUTO_AC("54"),

    //空调压缩机状态
    SET_AC_COMPRESSOR_STATUS("55"),

    //驾驶员风向自动
    SET_DRIVER_WINDDIR_AUTO("56"),
    //副驾驶风向自动状态
    SET_FRONT_SEAT_WINDDIR_AUTO("57"),

    //主驾风速自动
    SET_DRIVER_WIND_AUTO("58"),
    //副驾风速自动
    SET_FRONT_SEAT_WIND_AUTO("59"),
    //初始化1E5配置
    INIT_CAN1E5_CONFIG("60"),
    //设置气流模式
    SET_AIRFLOW_PATTERN("61"),

    //音乐暂停
    MUSIC_PAUSE("62"),
    //地图导航
    MAP_NAV("63"),
    STOP_MAP_NAV("64"),
    //下载离线地图 市
    DOWNLOAD_MAP_CITY("65"),
    //下载离线地图 省
    DOWNLOAD_MAP_PROVINCE("66"),

    ////下载离线地图 省的城市数量
    DOWNLOAD_MAP_PROVINCE_CITY_SIZE("67"),
    //取消下载地图
    CANCEL_DOWNLOAD_MAP("68");

    private String typeValue;

    SerialPortDataFlag(String typeValue) {
        this.typeValue = typeValue;
    }



    public String getTypeValue() {
        return typeValue;
    }

    public static final String START_FLAG = "AABB";
    public static final String END_FLAG = "CCDD";
}
