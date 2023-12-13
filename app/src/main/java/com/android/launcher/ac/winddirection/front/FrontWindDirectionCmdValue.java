package com.android.launcher.ac.winddirection.front;

/**
 * 前端空调风向指令
 *
 * s500
 * 主驾和副驾分别用两个字符表示气流分配，主驾类型是第二个字符表示， 副驾第一一个字符表示，比如66, 第一个6是副驾的气流类型，第二个6是主驾的气流类型
 *    s500
 *    C  吹顶部
 *    A  吹头部
 *    9  吹胸部
 *    8  吹膝盖
 *    6  吹脚部
 *    4  吹胸膝脚 （胸部的风大）
 *    3  吹胸膝脚（膝盖的风大）
 *    2  吹胸膝脚（脚部的风大）
 *
 *      s300
 *      *   6 吹脚
 *      *   8 脚和胸
 *      *   9 胸
 *      *   C 挡风
 *      *   1 挡风和胸部
 *      *   3 挡风和胸部和脚部
 *      *   5 挡风和脚部
 * @date： 2023/11/3
 * @author: 78495
*/
public enum  FrontWindDirectionCmdValue {

    AUTO("0"),
    //吹顶部
    S500_TOP("C"),
    //吹头部
    S500_HEAD("A"),
    //吹胸部
    S500_CHEST("9"),
    //吹膝盖
    S500_KNEE("8"),
    //吹脚部
    S500_FOOT("6"),
    //吹胸膝脚 （胸部的风大）
    S500_CHEST_MAX("4"),
    //吹胸膝脚（膝盖的风大）
    S500_KNEE_MAX("3"),
    //吹胸膝脚（脚部的风大）
    S500_FOOT_MAX("2"),

    //吹脚
    S300_FOOT("6"),
    //脚和胸
    S300_FOOT_CHEST("8"),
    //胸
    S300_CHEST("9"),
    //挡风
    S300_WINDBREAK("C"),
    //挡风和胸部
    S300_WINDBREAK_CHEST("1"),
    //挡风和胸部和脚部
    S300_WINDBREAK_CHEST_FOOT("3"),
    //挡风脚部
    S300_WINDBREAK_FOOT("5");


    private String value;


    FrontWindDirectionCmdValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
