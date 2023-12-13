package com.android.launcher.can.status;

/**
 * 后舱盖高度 开关
 * @date： 2023/11/27
 * @author: 78495
*/
public enum RearHatchCoverStatus {
    STATE_CLOSE("C"),
    STATE_OPEN("D");

    private String value;

    RearHatchCoverStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
