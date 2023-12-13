package com.android.launcher.ac.winddirection.rear;

/**
 * 后空调风向指令值
 * @date： 2023/10/31
 * @author: 78495
*/
public enum RearWindDirectionCmdValue {
    AUTO("0"),
    //吹膝盖
    KNEE("9"),
    //吹腿
    LEG("8"),
    //吹脚
    FOOT("6");

    private String value;


    RearWindDirectionCmdValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
