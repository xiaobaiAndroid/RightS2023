package com.android.launcher.airsystem.airflow.allocation;

/**
* @description: 气流的自动模式类型
* @createDate: 2023/5/7
*/
public enum  AirflowAutoType {
    ALL_AUTO("F"), //全部自动
    ALL_MANUAL_OPERATION("0"), //全部手动
    COPILOT_AUTO("A"), // 主家手动， 副驾自动
    MAIN_DRIVER_AUTO("5"); //主驾自动，副驾手动

    private String value;

    AirflowAutoType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
