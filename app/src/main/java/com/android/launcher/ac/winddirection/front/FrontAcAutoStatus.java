package com.android.launcher.ac.winddirection.front;

/**
 * 前排空调自动状态
 * @date： 2023/11/3
 * @author: 78495
*/
public enum FrontAcAutoStatus {
    AUTO("F"),
    LEFT_AUTO("5"),
    RIGHT_AUTO("A"),
    NOT_AUTO("0");

    private String value;

    FrontAcAutoStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
