package com.android.launcher.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import module.common.utils.LogUtils;

/**
 * @description:
 * @createDate: 2023/6/19
 */
public class BluetoothScanModeReceiver extends BroadcastReceiver {

    private static final String TAG = BluetoothScanModeReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {


        try {
            String action = intent.getAction();
            LogUtils.printI(TAG, "action="+action);

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {


                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);
                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    // 当前蓝牙设备处于可发现状态
                    LogUtils.printI(TAG, "-----当前蓝牙设备处于可发现状态");
                } else {
                    // 当前蓝牙设备不处于可发现状态
                    LogUtils.printI(TAG, "-----当前蓝牙设备不处于可发现状态---scanMode="+scanMode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BluetoothScanModeReceiver register(Context context){
        try {
            BluetoothScanModeReceiver bluetoothScanModeReceiver = new BluetoothScanModeReceiver();

            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            context.registerReceiver(bluetoothScanModeReceiver, filter);
            return bluetoothScanModeReceiver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void unregister(Context context,  BluetoothScanModeReceiver receiver){
        try {
            if(context!=null){
                context.unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

