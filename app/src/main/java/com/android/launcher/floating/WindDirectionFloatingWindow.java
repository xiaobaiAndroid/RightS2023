package com.android.launcher.floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.android.launcher.ac.winddirection.WindDirectionHomeView;

/**
 * 风向悬浮窗
 * @date： 2023/11/12
 * @author: 78495
 */
public class WindDirectionFloatingWindow {

    private Context context;
    private int height;
    private View mFloatingView;

    public static volatile boolean isShow = false;

    public WindDirectionFloatingWindow(Context context, int height) {
        this.context = context;
        this.height = height;
        mFloatingView = new WindDirectionHomeView(context);
    }

    public void show() {
        try {
            if (isShow) {
                return;
            }

            WindowManager.LayoutParams params;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);


            int sdkVersion = Build.VERSION.SDK_INT;
            int type;
            if (sdkVersion >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    height,
                    type,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP;
            params.y = 0;
            windowManager.addView(mFloatingView, params);
            isShow = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (!isShow) {
            return;
        }
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.removeView(mFloatingView);

            isShow = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShow() {
        return isShow;
    }
}
