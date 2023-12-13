package com.android.launcher.type;


import com.android.launcher.util.LanguageUtils;

/**
* @description:
* @createDate: 2023/9/12
*/
public enum  UnitType {
    //公里
    KM,
    //英里
    MI;

    public static int getDefaultType() {
        if(LanguageUtils.isCN()){
            return KM.ordinal();
        }else{
            return MI.ordinal();
        }

    }
}
