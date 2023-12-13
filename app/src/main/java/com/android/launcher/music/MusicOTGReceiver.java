package com.android.launcher.music;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.android.launcher.MyApp;

import module.common.MessageEvent;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

/**
* @description:
* @createDate: 2023/6/9
*/
public class MusicOTGReceiver  extends BroadcastReceiver {

//    public static final String ACTION_USB = "com.car.right.usb";
    public static final String ACTION_USB1 = "com.car.right.usb1";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.unregisterReceiver(this);
        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
            LogUtils.printI(MusicOTGReceiver.class.getSimpleName(), "onReceive----获得U盘权限");
            SPUtils.putBoolean(context.getApplicationContext(),SPUtils.USB_OTG_STATUS, true);

//            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.READ_OTG_MUSIC));
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.START_READ_OTG_MUSIC));
        }

    }


    public static MusicOTGReceiver registerReceiver(Context context){
        SPUtils.getBoolean(context,SPUtils.USB_OTG_STATUS, false);

        LogUtils.printI(MusicOTGReceiver.class.getSimpleName(), "registerReceiver----");

        MusicOTGReceiver musicOTGReceiver = new MusicOTGReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB1);
        //3.注册广播接收者
        context.registerReceiver(musicOTGReceiver, filter);
        return musicOTGReceiver;
    }

    public static void initUsbAuth(UsbDevice usbDevice) {
        try {
            LogUtils.printI(MusicOTGReceiver.class.getSimpleName(), "registerReceiver----");
            Intent intent = new Intent(ACTION_USB1);

            UsbManager  mUsbManager = (UsbManager) MyApp.getGlobalContext().getSystemService(Context.USB_SERVICE);
            PendingIntent var3 = PendingIntent.getBroadcast(MyApp.getGlobalContext(), 0, intent, FLAG_CANCEL_CURRENT);
            LogUtils.printI(MusicOTGReceiver.class.getSimpleName(), "initUsbAuth----var3=" + var3);
            mUsbManager.requestPermission(usbDevice, var3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
