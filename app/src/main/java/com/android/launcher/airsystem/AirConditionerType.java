package com.android.launcher.airsystem;

/**
* @description: 空调类型
* @createDate: 2023/5/5
*/
public enum  AirConditionerType {

    FRONT("00"), //前空调
    BACK("02"); //后空调

    private String value;

    private AirConditionerType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
