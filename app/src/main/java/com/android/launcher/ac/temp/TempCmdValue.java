package com.android.launcher.ac.temp;

/**
 * 温度指令值
 28	Lo
 32	16
 3C	17
 46	18
 50	19
 5A	20
 64	21
 6E	22
 78	23
 82	24
 8C	25
 96	26
 A0	27
 AA	28
 B4	Hi
 * @date： 2023/10/31
 * @author: 78495
*/
public enum TempCmdValue {

    TEMP_LO("28"),
    TEMP_16("32"),
    TEMP_17("3C"),
    TEMP_18("46"),
    TEMP_19("50"),
    TEMP_20("5A"),
    TEMP_21("64"),
    TEMP_22("6E"),
    TEMP_23("78"),
    TEMP_24("82"),
    TEMP_25("8C"),
    TEMP_26("96"),
    TEMP_27("A0"),
    TEMP_28("AA"),
    TEMP_HI("B4");

    private String value;


    TempCmdValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
