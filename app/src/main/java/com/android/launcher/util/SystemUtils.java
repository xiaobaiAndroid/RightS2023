package com.android.launcher.util;

import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class SystemUtils {


    /**
     * 设置系统亮度
     * @date： 2023/10/18
     * @author: 78495
     * @param brightness  0~255
    */
    public static void setBrightness(Context context,int brightness){
        if(context == null){
            return;
        }
        // 设置系统屏幕亮度值
        // 请注意，此方法需要WRITE_SETTINGS权限
        // 并且仅适用于手动模式下的亮度控制
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                brightness);
    }
}
