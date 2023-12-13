package com.android.launcher.ac.wind;

/**
 * 风速指令值
 *   2E   1
 *   38   2
 *   42   3
 *   4C   4
 *   60   5
 *   78   6
 *   C8   7
 * @date： 2023/10/31
 * @author: 78495
*/
public enum  WindCmdValue {

    WIND1("2E"),
    WIND2("38"),
    WIND3("42"),
    WIND4("4C"),
    WIND5("60"),
    WIND6("78"),
    WIND7("C8");

    private String value;


    WindCmdValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
