package com.android.launcher.music.usb;

/**
 * 按钮快速点击事件处理
 * @date： 2023/11/15
 * @author: 78495
*/
class ButtonValidClick {

    private static final String TAG = ButtonValidClick.class.getSimpleName();

    //全局定义
    private long lastNexClickTime = 0L;
    private long lastPreviousClickTime = 0L;
    private long lastPlayClickTime = 0L;
    private long lastItemSelectedClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000; // 快速点击间隔


    public boolean nextIsValid(){
        long currentClickTime = System.currentTimeMillis();
        if(lastNexClickTime == 0L){
            lastNexClickTime = currentClickTime;
            return true;
        }
        long interval = currentClickTime - lastNexClickTime;
        if(interval >= FAST_CLICK_DELAY_TIME){
            lastNexClickTime = currentClickTime;
            return true;
        }
        return false;
    }


    public boolean previousIsValid(){
        long currentClickTime = System.currentTimeMillis();
        if(lastPreviousClickTime == 0L){
            lastPreviousClickTime = currentClickTime;
            return true;
        }
        long interval = currentClickTime - lastPreviousClickTime;
        if(interval >= FAST_CLICK_DELAY_TIME){
            lastPreviousClickTime = currentClickTime;
            return true;
        }
        return false;
    }


    public boolean itemSelectedIsValid(){
        long currentClickTime = System.currentTimeMillis();
        if(lastItemSelectedClickTime == 0L){
            lastItemSelectedClickTime = currentClickTime;
            return true;
        }
        long interval = currentClickTime - lastItemSelectedClickTime;
        if(interval >= FAST_CLICK_DELAY_TIME){
            lastItemSelectedClickTime = currentClickTime;
            return true;
        }
        return false;
    }

    public boolean playIsValid(){
        long currentClickTime = System.currentTimeMillis();
        if(lastPlayClickTime == 0L){
            lastPlayClickTime = currentClickTime;
            return true;
        }
        long interval = currentClickTime - lastPlayClickTime;
        if(interval >= FAST_CLICK_DELAY_TIME){
            lastPlayClickTime = currentClickTime;
            return true;
        }
        return false;
    }
}
