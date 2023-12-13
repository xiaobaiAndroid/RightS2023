package com.android.launcher.floating;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.launcher.R;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class KeyBackFloating {

    private Context context;
    private View mFloatingView;

    private boolean isShow = false;


    public KeyBackFloating(Context context) {
        this.context = context;
    }


    public void show(){
        try {
            WindowManager.LayoutParams params;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            mFloatingView = LayoutInflater.from(context).inflate(R.layout.layout_status_bar_window, null);
            ImageView backIV = mFloatingView.findViewById(R.id.backIV);
            backIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    simulateBackKey();
                }
            });

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

            params.gravity = Gravity.TOP;
            windowManager.addView(mFloatingView, params);
            isShow = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
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

    // 创建一个方法来模拟系统返回键的操作
    public void simulateBackKey() {
        new Thread(() -> {
            // 创建一个 Instrumentation 对象
            Instrumentation inst = new Instrumentation();
            // 发送一个按键事件，模拟按下和松开返回键
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
        }).start();
    }

    public boolean isShow() {
        return isShow;
    }
}
