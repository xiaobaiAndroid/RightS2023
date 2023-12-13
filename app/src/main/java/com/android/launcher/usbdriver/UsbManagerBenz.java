package com.android.launcher.usbdriver;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.android.launcher.MyApp;

import module.common.utils.LogUtils;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class    UsbManagerBenz {
    public static UsbManager mUsbManager;

    public static List<UsbDevice> usbDevices;
    public static List<UsbSerialDriver> availableDrivers;

    public static int messageUsbId = 0;

    private static int position6790 = 0;

    public static void init() {
        messageUsbId = 0;
        position6790 = 0;

        mUsbManager = (UsbManager) MyApp.getGlobalContext().getSystemService(Context.USB_SERVICE);
        usbDevices = getAllUsbDevice(mUsbManager);
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
        int size = usbDevices.size();
        Log.i("MUsbReceiversize", size + "--多少个usb--");

        try {
            if(size >= 1){
                MUsbReceiver.initUsbAuth(usbDevices.get(position6790));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized List<UsbDevice> getAllUsbDevice(UsbManager mUsbManager) {

        List<UsbDevice> usbDevices = new ArrayList<>();
        HashMap<String, UsbDevice> allusb = mUsbManager.getDeviceList();

        for (Map.Entry<String, UsbDevice> map : allusb.entrySet()) {
            UsbDevice usbDevice = map.getValue();
            if(usbDevice.getVendorId() == 6790){
                position6790 = usbDevices.size();
            }
            usbDevices.add(usbDevice);
            LogUtils.printI(UsbManagerBenz.class.getSimpleName(), "getAllUsbDevice----key=" + map.getKey() + ", value=" + map.getValue());
        }
        return usbDevices;
    }
}
