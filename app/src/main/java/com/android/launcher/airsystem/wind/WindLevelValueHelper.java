package com.android.launcher.airsystem.wind;

/**
 * @description: 风量等级控制值
 * @createDate: 2023/5/1
 */
public class WindLevelValueHelper {

    public static String getValue(int index) {
        switch (index) {
            case 0:
                return WindLevelType.LEVEL_7;
            case 1:
                return WindLevelType.LEVEL_6;
            case 2:
                return WindLevelType.LEVEL_5;
            case 3:
                return WindLevelType.LEVEL_4;
            case 4:
                return WindLevelType.LEVEL_3;
            case 5:
                return WindLevelType.LEVEL_2;
            default:
                return WindLevelType.LEVEL_1;
        }
    }
}
