package com.android.launcher.ac.temp;

/**
 * 温度显示的值
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
public enum TempDisplayValue {

    TEMP_LO("Lo"),
    TEMP_16("16"),
    TEMP_17("17"),
    TEMP_18("18"),
    TEMP_19("19"),
    TEMP_20("20"),
    TEMP_21("21"),
    TEMP_22("22"),
    TEMP_23("23"),
    TEMP_24("24"),
    TEMP_25("25"),
    TEMP_26("26"),
    TEMP_27("27"),
    TEMP_28("28"),
    TEMP_HI("Hi");

    private String value;


    TempDisplayValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
