package com.android.launcher.util;

import android.content.Context;

/**
 * @date： 2023/10/15
 * @author: 78495
*/
public class StatusBarUtils {

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

}
