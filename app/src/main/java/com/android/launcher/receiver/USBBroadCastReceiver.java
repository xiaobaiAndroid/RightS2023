package com.android.launcher.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;

import com.android.launcher.MyApp;
import com.android.launcher.usbdriver.MUsbReceiver;

import module.common.utils.LogUtils;

public class USBBroadCastReceiver {


    public static AccReceiver accReceiver ;

    public static BluetoothBroadCastReceiver bluetoothBroadCastReceiver ;

    public static void registerReceiver() {

        if(accReceiver==null){
            accReceiver = AccReceiver.getInstance();
            //2.创建intent-filter对象
            IntentFilter filter3 = new IntentFilter();

            filter3.addAction("xy.android.acc.off");
            filter3.addAction("xy.android.acc.on");
            filter3.addAction("android.intent.action.SCREEN_OFF");
            filter3.addAction("android.intent.action.SCREEN_ON");
            filter3.addAction("autochips.intent.action.PREQB_POWEROFF");
            filter3.addAction("android.hardware.usb.action.USB_STATE");
            filter3.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
            filter3.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
            filter3.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter3.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

            //3.注册广播接收者
            MyApp.getGlobalContext().registerReceiver(accReceiver, filter3);

        }else{
            //2.创建intent-filter对象
            IntentFilter filter3 = new IntentFilter();
            filter3.addAction("xy.android.acc.off");
            filter3.addAction("xy.android.acc.on");
            filter3.addAction("android.intent.action.SCREEN_OFF");
            filter3.addAction("android.intent.action.SCREEN_ON");
            filter3.addAction("autochips.intent.action.QB_POWERON");
            filter3.addAction("autochips.intent.action.PREQB_POWEROFF");
            //3.注册广播接收者
            MyApp.getGlobalContext().registerReceiver(accReceiver, filter3);
        }

        bluetoothBroadCastReceiver = new BluetoothBroadCastReceiver() ;

        //2.创建intent-filter对象
        IntentFilter filtertooth = new IntentFilter();

        filtertooth.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filtertooth.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filtertooth.addAction("com.android.radio.widget.freq_volue");
        filtertooth.addAction("com.txznet.adapter.recv");
        filtertooth.addAction("com.xyauto.bt.songinfo");
        filtertooth.addAction("com.xyauto.bt.playstate");

        filtertooth.addAction("android.bluetooth.headsetclient.profile.action.AG_CALL_CHANGED");
        filtertooth.addAction("android.bluetooth.avrcp-controller.profile.action.TRACK_EVENT");
        filtertooth.addAction("music_currentplay_datasend");
        //3.注册广播接收者
        MyApp.getGlobalContext().registerReceiver(bluetoothBroadCastReceiver, filtertooth);

    }

//
//    public static void usbRegisterReceiver(){
//
//        mUsb1Receiver = new MUsb1Receiver();
//        //2.创建intent-filter对象
//        IntentFilter filter = new IntentFilter();
//
//        filter.addAction("com.car.right.usb");
//
//        //3.注册广播接收者
//        App.getGlobalContext().registerReceiver(mUsb1Receiver, filter);
//
//        mUsb2Receiver = new MUsb2Receiver();
//        //2.创建intent-filter对象
//        IntentFilter filter1 = new IntentFilter();
//
//        filter1.addAction("com.car.right.usb1");
//        //3.注册广播接收者
//        App.getGlobalContext().registerReceiver(mUsb2Receiver, filter1);
//
//        mUsb3Receiver = new MUsb3Receiver();
//        //2.创建intent-filter对象
//        IntentFilter filter2 = new IntentFilter();
//
//
//        filter2.addAction("com.car.right.usb2");
//        //3.注册广播接收者
//        App.getGlobalContext().registerReceiver(mUsb3Receiver, filter2);
//
//    }

    public static void usbRegisterReceiver(Context context){

        try {
            LogUtils.printI(USBBroadCastReceiver.class.getSimpleName(), "usbRegisterReceiver------");
            MUsbReceiver mUsbReceiver = new MUsbReceiver();
            //2.创建intent-filter对象
            IntentFilter filter = new IntentFilter();

            filter.addAction("com.car.right.usb");

            //3.注册广播接收者
            context.registerReceiver(mUsbReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  void unRegisterReceiver(Context context){
        try {
            if(bluetoothBroadCastReceiver!=null){
                context.unregisterReceiver(bluetoothBroadCastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
