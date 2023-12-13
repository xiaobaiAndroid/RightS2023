package com.android.launcher.music.usb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import module.common.MessageEvent;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

/**
* @description: 外部存储设备广播
* @createDate: 2023/6/13
*/
public class OtgBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "OtgBroadcastReceiver";

    private String usbPath;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // 外部存储设备插入时会发出 MEDIA_MOUNTED 广播
        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
            Uri uri = intent.getData();
            String volumePath = uri.getPath();
            LogUtils.printI(TAG, "外部存储设备插入----volumePath="+volumePath);

            SPUtils.putString(context, SPUtils.SP_USB_PATH, volumePath);
            usbPath = volumePath;
            // 对插入的存储设备进行一些操作，如获取可读取的文件列表等
            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.READ_OTG_MUSIC);
            messageEvent.data = usbPath;
            EventBus.getDefault().post(messageEvent);

        }
        // 外部存储设备卸载时会发出 MEDIA_EJECT 广播
        else if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
            Uri uri = intent.getData();
            String volumePath = uri.getPath();
            LogUtils.printI(TAG, "外部存储设备卸载----volumePath="+volumePath);
            // 对卸载的存储设备进行一些操作，如释放资源等
            SPUtils.putString(context, SPUtils.SP_USB_PATH, "");
            usbPath = volumePath;
        }
        // 开始扫描外部存储设备的文件时会发出 MEDIA_CHECKING 广播
        // 文件扫描完成后会发出 MEDIA_SCANNER_FINISHED 广播
        else if (Intent.ACTION_MEDIA_CHECKING.equals(action) || Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
            LogUtils.printI(TAG, "外部存储设备扫描完成----volumePath="+usbPath);
        }
    }
}