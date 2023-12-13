package com.android.launcher.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import module.common.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: 蓝牙设备管理
 * @createDate: 2023/5/27
 */
public class BluetoothDeviceHelper {

    private static final String TAG = BluetoothDeviceHelper.class.getSimpleName();

    /**
     * @description: 删除设备
     * @createDate: 2023/5/29
     */
    public static void deleteDevice(BluetoothDevice bluetoothDevice) {
        try {
            if (bluetoothDevice == null) {
                return;
            }
            LogUtils.printI(TAG, "deleteDevice---删除的设备 " + bluetoothDevice.getName());
            Method m = bluetoothDevice.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(bluetoothDevice, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasDevice(Context context) {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
            LogUtils.printI(TAG, "hasDevice---pairedDevices="+pairedDevices.size());
            if (!pairedDevices.isEmpty()) {
                Iterator<BluetoothDevice> deviceIterator = pairedDevices.iterator();

                while (deviceIterator.hasNext()) {
                    BluetoothDevice bluetoothDevice = deviceIterator.next();
                    if(bluetoothDevice!=null){
                        LogUtils.printI(TAG, "device=" + bluetoothDevice.getName() + ", address=" + bluetoothDevice.getAddress() + ", bondState=" + bluetoothDevice.getBondState());
                        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @description: 获取连接的蓝牙设备
     * @createDate: 2023/5/29
     */
    public static BluetoothDevice getConnectDevice() {
        BluetoothDevice connectDevice = null;
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
            LogUtils.printI(TAG, "getConnectDevice---pairedDevices="+pairedDevices.size());


            connectDevice = null;
            if (!pairedDevices.isEmpty()) {
                Iterator<BluetoothDevice> deviceIterator = pairedDevices.iterator();

                while (deviceIterator.hasNext()) {
                    BluetoothDevice bluetoothDevice = deviceIterator.next();
                    if(bluetoothDevice!=null){
                        //使用反射调用获取设备连接状态方法
                        Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                        isConnectedMethod.setAccessible(true);
                        boolean isConnected = (boolean) isConnectedMethod.invoke(bluetoothDevice, (Object[]) null);

                        LogUtils.printI(TAG, "device=" + bluetoothDevice.getName() + ", address=" + bluetoothDevice.getAddress() + ", bondState=" + bluetoothDevice.getBondState()+", isConnected="+isConnected);

                        if(isConnected){
                            return bluetoothDevice;
                        }

//                        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
//                            connectDevice = bluetoothDevice;
//                            break;
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(connectDevice == null){
            LogUtils.printI(TAG, "没有获取到配对的蓝牙设备");
        }
        return connectDevice;
    }
}
