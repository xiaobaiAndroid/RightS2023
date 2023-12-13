package com.android.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.android.launcher.MainActivity;
import com.android.launcher.MyApp;
import com.android.launcher.type.ThirdAppType;
import com.android.launcher.usbdriver.HandlerCanData;
import com.android.launcher.usbdriver.UsbReadIml;

import module.common.MessageEvent;
import module.common.utils.BluetoothMusicHelper;
import com.android.launcher.music.usb.MusicPlayService;
import com.android.launcher.service.LivingService;

import module.common.utils.FMHelper;
import com.android.launcher.util.FuncUtil;
import module.common.utils.GaodeCarMapHelper;
import module.common.utils.LogUtils;
import com.android.launcher.util.LogcatHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * @description: 监听钥匙启动
 * @createDate: 2023/4/28
 */
public class AccReceiver extends BroadcastReceiver {

    private static final String TAG = AccReceiver.class.getSimpleName();

    public volatile static boolean accOn;
    public volatile static boolean accOff;

    private AccReceiver() {
    }

    private static class LazyHolder {
        private static final AccReceiver INSTANCE = new AccReceiver();
    }

    public static final AccReceiver getInstance() {
        return LazyHolder.INSTANCE;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("LauncherAcc", "AccReceiver----onReceive---" + intent.getAction().toString() + "==============");

        //USB断开时
        if (intent.getAction().equals(UsbManager.ACTION_USB_ACCESSORY_DETACHED)) {
//            UsbReadIml_1.getInstance().disconnect();
//            if(UsbManagerBenz.usbDevices.size()>1){
//                UsbReadIml_2.getInstance().disconnect();
//            }
//            if(UsbManagerBenz.usbDevices.size()>2){
//                UsbReadIml_3.getInstance().disconnect();
//            }
//            LivingService.stopLivingService(App.getGlobalContext());

            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.USB_ACCESSORY_DETACHED));
            LogUtils.printI(TAG, "USB 分离------");
        }
//        //USB连接时
        if (intent.getAction().equals(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)) {
//            USBBroadCastReceiver.usbRegisterReceiver();
//            UsbManagerBenz.init();
//            LivingService.startLivingService(App.getGlobalContext());
            LogUtils.printI(TAG, "USB 挂载上------");

            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.USB_ACCESSORY_ATTACHED));
        }

        if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.USB_ACCESSORY_DETACHED));
            LogUtils.printI(TAG, "USB断开时------");
        }

        if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.USB_ACCESSORY_ATTACHED));
            LogUtils.printI(TAG, "USB连接时------");
        }
//
//        USBBroadCastReceiver.usbRegisterReceiver();
//        UsbManagerBenz.init();

        if (intent.getAction().equals("xy.android.acc.on")) {
//
            if (!accOn) {
                accOn = true;
                Log.i("LauncherAcc", "======================进入到开机");
                MyApp.INITFLG = false;

                HandlerCanData.cdCommandSendFinish = true;

                FMHelper.finishFM(MyApp.getGlobalContext());

                LogcatHelper.getInstance(MyApp.getGlobalContext()).stop();
                LogcatHelper.getInstance(MyApp.getGlobalContext()).start();
                android.provider.Settings.System.putString(MyApp.getGlobalContext().getContentResolver(), "boot_xyapk", "");

                new Thread(() -> {
                    LogUtils.printI(TAG, "isAppStart="+ MyApp.isAppStart);
                    if (MyApp.isAppStart) {
                        MyApp.isAppStart = false;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("LauncherAcc", "======================isInNavActivity="+ MyApp.isInNavActivity);
                    if(!MyApp.isInNavActivity){
                        Intent navIntent = new Intent(MyApp.getGlobalContext(), MainActivity.class);
                        navIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.getGlobalContext().startActivity(navIntent);
                    }

                    LivingService.startLivingService(context);
                }).start();

            }
        }
//
        if (intent.getAction().equals("xy.android.acc.off")) {
            MyApp.INITFLG = true;
            MainActivity.mCurrentThirdAppType = ThirdAppType.NONE;
            FMHelper.finishFM(MyApp.getGlobalContext());
            try {
                GaodeCarMapHelper.finish(MyApp.getGlobalContext());

            } catch (Exception e) {
                e.printStackTrace();
            }
            BluetoothMusicHelper.pause(MyApp.getGlobalContext());
            MusicPlayService.stopMusicService(MyApp.getGlobalContext());
        }
//
        if (intent.getAction().equals("autochips.intent.action.PREQB_POWEROFF")) { //电源关闭通知
//            SendcanCD.handler("AA000004000000FD1000000000000000");
            Log.i("LauncherAcc", intent.getAction() + "---------进入--");

            Log.i("LauncherAcc", "================1======" + accOff);
            MyApp.INITFLG = true;
            accOn = false;

            MyApp.mKeyboardConnectState = false;
            MyApp.mMouseConnectState = false;

            HandlerCanData.cdCommandSendFinish = true;

            BluetoothBroadCastReceiver.lastPlaybackState = 0;

            if (FuncUtil.timer != null) {
                FuncUtil.timer.cancel();
                FuncUtil.timer = null;
            }
            Log.i("LauncherAcc", "======================关闭时间");
            FuncUtil.sendCDFLG = false;

            UsbReadIml.getInstance().connected = false;

            FuncUtil.resetCanData();

            LivingService.stopLivingService(MyApp.getGlobalContext());
            Log.i("LauncherAcc", "======================关闭usb");

//            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.TO_MAIN));
        }
    }
}
