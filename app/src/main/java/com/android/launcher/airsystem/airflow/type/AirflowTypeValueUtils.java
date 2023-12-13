package com.android.launcher.airsystem.airflow.type;

import com.android.launcher.type.CarType;

/**
* @description: 气流类型值
* @createDate: 2023/4/30
*/
public class AirflowTypeValueUtils {

    public static String getValue(CarType airDeviceType, int index){
        if(airDeviceType == CarType.S500){
            return getValueS500(index);
        }else if(airDeviceType == CarType.S300){
            return getValueS500(index);
        }else if(airDeviceType == CarType.S65){
            return getValueS65(index);
        }else {
            return getValueS500(index);
        }
    }

    private static String getValueS65(int index) {
        switch (index){
            case 0:
                return AirflowTypeS65.ASSEMBLE;
            case 1:
                return AirflowTypeS65.MEDIUM_SPEED;
            case 2:
                return AirflowTypeS65.DISPERSE;
        }
        return "";
    }

    private static String getValueS500(int index){
        switch (index){
            case 0:
                return AirflowTypeS500.ASSEMBLE;
            case 1:
                return AirflowTypeS500.MEDIUM_SPEED;
            case 2:
                return AirflowTypeS500.DISPERSE;
        }
        return "";
    }
}
