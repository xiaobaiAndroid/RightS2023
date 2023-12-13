package com.android.launcher.floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.android.launcher.R;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class ControlCenterFloating {

    private Context context;
    private View mFloatingView;

    private boolean isShow = false;


    public ControlCenterFloating(Context context) {
        this.context = context;
    }

    /**
     * @description: 显示悬浮窗
     * @createDate: 2023/6/8
     */
    public void show() {
        try {
            WindowManager.LayoutParams params;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            mFloatingView = LayoutInflater.from(context).inflate(R.layout.layout_floating_window, null);

            int sdkVersion = Build.VERSION.SDK_INT;
            int type;
            if (sdkVersion >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else{
                type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    type,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.BOTTOM;
            windowManager.addView(mFloatingView, params);
            isShow = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 关闭悬浮窗
     * @createDate: 2023/6/8
     */
    public void close() {
        if (mFloatingView != null) {
            try {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                windowManager.removeView(mFloatingView);
                mFloatingView = null;
                isShow = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isShow() {
        return isShow;
    }
}
