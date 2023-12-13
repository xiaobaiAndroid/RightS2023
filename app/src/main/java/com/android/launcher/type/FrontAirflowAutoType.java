package com.android.launcher.type;

/**
 * 前排空调气流自动类型
 * @date： 2023/10/20
 * @author: 78495
*/
@Deprecated
public enum FrontAirflowAutoType {
    //主副驾都是手动
    NOT_AUTO("0"),
    //主驾自动
    LEFT_AUTO("1"),
    //副驾自动
    RIGHT_AUTO("2"),
    ALL_AUTO("3");

    private String value;

    private FrontAirflowAutoType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
