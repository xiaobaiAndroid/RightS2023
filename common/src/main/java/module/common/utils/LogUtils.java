package module.common.utils;

import android.text.TextUtils;
import android.util.Log;

/**
* @description:
* @createDate: 2023/4/29
*/
public class LogUtils {

    public static final boolean isOpen = true;

    public static void printI(String tag, String message){
        if(isOpen){
            if(!TextUtils.isEmpty(message)){
                Log.i(tag,message);
            }
        }
    }

    public static void printE(String tag, String message){
        if(isOpen){
            if(!TextUtils.isEmpty(message)){
                Log.i(tag,message);
            }
        }
    }
}
