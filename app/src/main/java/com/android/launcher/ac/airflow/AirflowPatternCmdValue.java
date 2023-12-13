package com.android.launcher.ac.airflow;

/**
 * 风速指令值
 *  集中	7A
 *  中等	6E
 *  扩散	62
 * @date： 2023/10/31
 * @author: 78495
*/
public enum AirflowPatternCmdValue {

    CONCENTRATE("7A"),
    MEDIUM("6E"),
    DIFFUSE("62");

    private String value;


    AirflowPatternCmdValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
