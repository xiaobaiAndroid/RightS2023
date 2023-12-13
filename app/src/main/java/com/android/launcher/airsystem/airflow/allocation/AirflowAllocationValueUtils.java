package com.android.launcher.airsystem.airflow.allocation;

import com.android.launcher.type.CarType;

/**
* @description: 气流类型值
 * index的值：
 * 0: 吹脚
 * 1： 吹胸和脚
 * 2：吹胸
 * 3：吹脚和胸
 * 4：吹脚膝盖胸，胸的风最大
 * 5：吹脚膝盖头，膝盖的风最大
 * 6：吹脚膝盖头，脚的风最大
* @createDate: 2023/4/30
*/
public class AirflowAllocationValueUtils {

    private static String getS300Value(int index){
        String value = "";
        switch (index) {
            case 0:
                value = AllocationTypeS300.FOOT;
                break;
            case 1:
                value = AllocationTypeS300.FOOT_CHEST;
                break;
            case 2:
                value = AllocationTypeS300.CHEST;
                break;
            case 3:
                value = AllocationTypeS300.SHIELD_WIND;
                break;
            case 4:
                value = AllocationTypeS300.SHIELD_WIND_CHEST;
                break;
            case 5:
                value = AllocationTypeS300.SHIELD_WIND_CHEST_FOOT;
                break;
            case 6:
                value = AllocationTypeS300.SHIELD_WIND_FOOT;
                break;
        }
        return value;
    }

    private static String getS500Value(int index){
        String value = "";
        switch (index) {
            case 0:
                value = AllocationTypeS500.TOP;
                break;
            case 1:
                value = AllocationTypeS500.HEAD;
                break;
            case 2:
                value = AllocationTypeS500.CHEST;
                break;
            case 3:
                value = AllocationTypeS500.KNEE;
                break;
            case 4:
                value = AllocationTypeS500.FOOT;
                break;
            case 5:
                value = AllocationTypeS500.CKF_CHEST_MAX;
                break;
            case 6:
                value = AllocationTypeS500.CKF_KNESS_MAX;
                break;
            case 7:
                value = AllocationTypeS500.CKF_FOOT_MAX;
                break;
        }
        return value;
    }

    private static String getS65Value(int index){
        String value = "";
        switch (index) {
            case 0:
                value = AllocationTypeS65.TOP;
                break;
            case 1:
                value = AllocationTypeS65.HEAD;
                break;
            case 2:
                value = AllocationTypeS65.CHEST;
                break;
            case 3:
                value = AllocationTypeS65.KNEE;
                break;
            case 4:
                value = AllocationTypeS65.FOOT;
                break;
            case 5:
                value = AllocationTypeS65.CKF_CHEST_MAX;
                break;
            case 6:
                value = AllocationTypeS65.CKF_ALL_MAX;
                break;
            case 7:
                value = AllocationTypeS65.CKF_FOOT_MAX;
                break;
        }
        return value;
    }

    public static String getDriverValue(CarType carType, int index){
        if(carType == CarType.S65){
            return getS65Value(index);
        }else if(carType == CarType.S300){
            return getS300Value(index);
        }else if(carType == CarType.S500){
            return getS500Value(index);
        }else{
            return getS500Value(index);
        }
    }

}
