package com.android.launcher.usbdriver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.android.launcher.MyApp;

import module.common.utils.LogUtils;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

/**
 * @date： 2023/10/19
 * @author: 78495
*/
public class MUsbReceiver extends BroadcastReceiver {

    public static final String TAG = MUsbReceiver.class.getSimpleName();

    public static UsbManager mUsbManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        LogUtils.printI(MUsbReceiver.class.getSimpleName(), "action=" + action);
        if ("com.car.right.usb".equals(action)) {
            synchronized (this) {
                context.unregisterReceiver(this);

                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    LogUtils.printI("MUsbReceiver", "usb授权=============" + action + "==========================" + device.getDeviceName() + device.getProductId());
                    UsbReadIml.getInstance().connect(115200, 0, device.getDeviceId());
                }else{
                    LogUtils.printI(TAG,"未获得USB授权");
                }

            }
        }
    }

    public static void initUsbAuth(UsbDevice usbDevice) {
        mUsbManager = (UsbManager) MyApp.getGlobalContext().getSystemService(Context.USB_SERVICE);
        PendingIntent var3 = PendingIntent.getBroadcast(MyApp.getGlobalContext(), 0, new Intent("com.car.right.usb"), FLAG_CANCEL_CURRENT);
        LogUtils.printI(TAG, "initUsbAuth----var3=" + var3);
        mUsbManager.requestPermission(usbDevice, var3);
    }

    public static void write(byte[] bytes) {
        try {
            UsbReadIml.getInstance().write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
